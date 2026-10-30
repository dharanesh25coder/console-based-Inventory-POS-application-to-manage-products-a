package PosSystem;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private String storeName;
    private List<Product> inventory;
    private List<CartItem> cart;
    private List<String> salesLog;
    private double lowStockThreshold;
    private double dailySales;
    private int billCounter;

    private static class CartItem {
        Product product;
        double qty;

        CartItem(Product product, double qty) {
            this.product = product;
            this.qty = qty;
        }
    }

    public InventoryManager(String storeName, double lowStockThreshold) {
        this.storeName = storeName;
        this.lowStockThreshold = lowStockThreshold;
        this.inventory = new ArrayList<>();
        this.cart = new ArrayList<>();
        this.salesLog = new ArrayList<>();
        this.dailySales = 0;
        this.billCounter = 1000;
    }

    public String getStoreName() {
        return storeName;
    }

    public double getDailySales() {
        return dailySales;
    }

    public double getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setLowStockThreshold(double lowStockThreshold) {
        if (lowStockThreshold >= 0) {
            this.lowStockThreshold = lowStockThreshold;
        }
    }

    // ---------- Product management ----------
    public void addProduct(Product product) {
        inventory.add(product);
        System.out.println("Added: " + product.getName());
    }

    public boolean removeProduct(int id) {
        Product p = findProduct(id);
        if (p == null) {
            System.out.println("Product " + id + " not found.");
            return false;
        }
        inventory.remove(p);
        System.out.println("Removed: " + p.getName());
        return true;
    }

    public Product findProduct(int id) {
        for (Product p : inventory) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public void updateStock(int id, double qty) {
        Product p = findProduct(id);
        if (p == null) {
            System.out.println("Product " + id + " not found.");
            return;
        }
        p.addStock(qty);
        System.out.println("Stock updated: " + p.getName() + " -> " + p.getStockQty());
    }

    // Polymorphism: each Product subclass prints its own details
    public void listProducts() {
        System.out.println("\n----- INVENTORY -----");
        for (Product p : inventory) {
            p.display();
        }
    }

    // Polymorphism: iterate List<Product> to compute total stock value
    public double inventoryValue() {
        double total = 0;
        for (Product p : inventory) {
            total += p.getBasePrice() * p.getStockQty();
        }
        return total;
    }

    // ---------- Cart ----------
    public void addToCart(int id, double qty) {
        Product p = findProduct(id);
        if (p == null) {
            System.out.println("Product " + id + " not found.");
        } else if (qty <= 0 || qty > p.getStockQty()) {
            System.out.println("Cannot add " + qty + " of " + p.getName()
                    + " (available: " + p.getStockQty() + ")");
        } else {
            cart.add(new CartItem(p, qty));
            System.out.println("Cart <- " + p.getName() + " x " + qty);
        }
    }

    public void clearCart() {
        cart.clear();
    }

    // ---------- Invoicing (Method Overloading) ----------
    public void generateInvoice() {
        generateInvoice("");
    }

    public void generateInvoice(String couponCode) {
        double couponPercent = Product.couponPercent(couponCode);
        double subtotal = 0;
        double tax = 0;
        billCounter++;

        System.out.println("\n==================== " + storeName + " ====================");
        System.out.println("Invoice No: " + billCounter);
        if (couponCode != null && !couponCode.isEmpty()) {
            System.out.println("Coupon: " + couponCode.toUpperCase()
                    + (couponPercent > 0 ? " (" + couponPercent + "% off)" : " (invalid)"));
        }
        System.out.println("---------------------------------------------------------------");

        // Polymorphism: each item uses its own tax/discount rules
        for (CartItem item : cart) {
            Product p = item.product;
            if (p.reduceStock(item.qty)) {
                System.out.println(p.generateBill(item.qty, couponPercent));
                subtotal += p.lineSubtotal(item.qty, couponPercent);
                tax += p.lineTax(item.qty, couponPercent);
            }
        }

        double grandTotal = subtotal + tax;
        System.out.println("---------------------------------------------------------------");
        System.out.printf("Subtotal : %10.2f%n", subtotal);
        System.out.printf("Tax      : %10.2f%n", tax);
        System.out.printf("TOTAL    : %10.2f%n", grandTotal);
        System.out.println("===============================================================");

        dailySales += grandTotal;
        salesLog.add("Invoice " + billCounter + " : " + String.format("%.2f", grandTotal));
        cart.clear();
    }

    // ---------- Reports ----------
    public void lowStockReport() {
        System.out.println("\n----- LOW STOCK REPORT (threshold <= " + lowStockThreshold + ") -----");
        boolean found = false;
        for (Product p : inventory) {
            if (p.isLowStock(lowStockThreshold)) {
                System.out.println(p.getName() + " -> " + p.getStockQty() + " left");
                found = true;
            }
        }
        if (!found) {
            System.out.println("All products sufficiently stocked.");
        }
    }

    public void dailySalesReport() {
        System.out.println("\n----- DAILY SALES REPORT -----");
        for (String entry : salesLog) {
            System.out.println(entry);
        }
        System.out.printf("Bills generated : %d%n", salesLog.size());
        System.out.printf("Total sales     : %.2f%n", dailySales);
        System.out.printf("Stock value left: %.2f%n", inventoryValue());
    }
}
