package PosSystem;

import java.util.HashMap;
import java.util.Map;

public class Product {
    private int id;
    private String name;
    private double basePrice;
    private double stockQty;

    private static int productCount = 0;
    private static final Map<String, Double> COUPONS = new HashMap<>();

    static {
        COUPONS.put("SAVE10", 10.0);
        COUPONS.put("FEST20", 20.0);
        COUPONS.put("NEW5", 5.0);
    }

    public Product(int id, String name, double basePrice, double stockQty) {
        this.id = id;
        this.name = name;
        this.basePrice = basePrice;
        this.stockQty = stockQty;
        productCount++;
    }

    // ---------- Getters ----------
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public double getStockQty() {
        return stockQty;
    }

    public static int getProductCount() {
        return productCount;
    }

    // ---------- Controlled setters (Encapsulation) ----------
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
    }

    public void setBasePrice(double basePrice) {
        if (basePrice > 0) {
            this.basePrice = basePrice;
        } else {
            System.out.println("Invalid price. Price must be positive.");
        }
    }

    // Stock can only change through addStock() / reduceStock()
    public void addStock(double qty) {
        if (qty > 0) {
            stockQty += qty;
        } else {
            System.out.println("Quantity to add must be positive.");
        }
    }

    public boolean reduceStock(double qty) {
        if (qty <= 0) {
            System.out.println("Quantity must be positive.");
            return false;
        }
        if (qty > stockQty) {
            System.out.println("Insufficient stock for " + name
                    + " (available: " + stockQty + ")");
            return false;
        }
        stockQty -= qty;
        return true;
    }

    public boolean isLowStock(double threshold) {
        return stockQty <= threshold;
    }

    // ---------- Method Overloading: discount by % or by coupon ----------
    public double priceAfterDiscount(double percent) {
        if (percent < 0) {
            percent = 0;
        }
        if (percent > 100) {
            percent = 100;
        }
        return basePrice - (basePrice * percent / 100.0);
    }

    public double priceAfterDiscount(String couponCode) {
        return priceAfterDiscount(couponPercent(couponCode));
    }

    public static double couponPercent(String couponCode) {
        if (couponCode == null) {
            return 0;
        }
        Double percent = COUPONS.get(couponCode.trim().toUpperCase());
        return percent == null ? 0 : percent;
    }

    // ---------- Rules overridden by subclasses (Method Overriding) ----------
    public String getCategory() {
        return "General";
    }

    public double getTaxRate() {
        return 0.05;
    }

    public double getCategoryDiscount() {
        return 0.0;
    }

    // ---------- Billing ----------
    private double totalDiscountPercent(double extraPercent) {
        return getCategoryDiscount() * 100.0 + extraPercent;
    }

    public double lineSubtotal(double qty, double extraPercent) {
        return priceAfterDiscount(totalDiscountPercent(extraPercent)) * qty;
    }

    public double lineTax(double qty, double extraPercent) {
        return lineSubtotal(qty, extraPercent) * getTaxRate();
    }

    public double lineTotal(double qty, double extraPercent) {
        return lineSubtotal(qty, extraPercent) + lineTax(qty, extraPercent);
    }

    public String generateBill(double qty, double extraPercent) {
        return String.format("%-14s %7.2f x %9.2f | Disc %5.1f%% | Tax %4.1f%% | %11.2f",
                name, qty, basePrice, totalDiscountPercent(extraPercent),
                getTaxRate() * 100.0, lineTotal(qty, extraPercent));
    }

    public void display() {
        System.out.println(id + " | " + name + " | " + getCategory()
                + " | Price: " + String.format("%.2f", basePrice)
                + " | Stock: " + stockQty);
    }

    @Override
    public String toString() {
        return id + " - " + name;
    }
}
