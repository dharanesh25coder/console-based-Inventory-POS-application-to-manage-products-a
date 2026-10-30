package PosSystem;

import java.time.LocalDate;

public class POSAppMain {

    public static void main(String[] args) {

        System.out.println("INVENTORY & POS SYSTEM");
        System.out.println("----------------------");

        InventoryManager manager = new InventoryManager("SMART MART", 5);

        // 1. Add products (stored in the manager as Product references)
        manager.addProduct(new Electronics(101, "Laptop", 55000, 10,
                "Dell", "Laptop", 24, 0.18));
        manager.addProduct(new Electronics(102, "Smartphone", 25000, 15,
                "Samsung", "Mobile", 12, 0.18));
        manager.addProduct(new Electronics(103, "Earbuds", 2000, 3,
                "boAt", "Accessories", 6, 0.18));
        manager.addProduct(new Grocery(201, "Rice", 60, 100,
                "KG", LocalDate.now().plusDays(90), false, "Dry"));
        manager.addProduct(new Grocery(202, "Milk", 30, 4,
                "PCS", LocalDate.now().plusDays(2), false, "Chilled"));
        manager.addProduct(new Grocery(203, "Tomato", 40, 20,
                "KG", LocalDate.now().plusDays(4), true, "Cool"));
        manager.addProduct(new Grocery(204, "Bread", 35, 10,
                "PCS", LocalDate.now().minusDays(1), false, "Dry"));

        manager.listProducts();
        System.out.printf("%nTotal stock value: %.2f%n", manager.inventoryValue());

        // 2. Update products
        System.out.println("\n----- UPDATING PRODUCTS -----");
        manager.updateStock(103, 10);
        Product laptop = manager.findProduct(101);
        laptop.setBasePrice(52000);
        System.out.println("Laptop new price: " + laptop.getBasePrice());
        manager.removeProduct(999);

        // 3. Overloaded discount methods
        System.out.println("\n----- DISCOUNT DEMO (Smartphone) -----");
        Product phone = manager.findProduct(102);
        System.out.println("Flat 10%        : " + phone.priceAfterDiscount(10.0));
        System.out.println("Coupon FEST20   : " + phone.priceAfterDiscount("FEST20"));

        // 4. Bill 1 - mixed cart with coupon
        System.out.println("\n----- BILL 1 -----");
        manager.addToCart(101, 1);
        manager.addToCart(102, 2);
        manager.addToCart(201, 2.5);
        manager.addToCart(202, 2);
        manager.addToCart(203, 1.5);
        manager.addToCart(204, 1);
        manager.addToCart(103, 50);
        manager.generateInvoice("SAVE10");

        // 5. Bill 2 - no coupon
        System.out.println("\n----- BILL 2 -----");
        manager.addToCart(103, 8);
        manager.addToCart(201, 10);
        manager.generateInvoice();

        // 6. Reports
        manager.lowStockReport();
        manager.dailySalesReport();

        System.out.println("\nTotal products created: " + Product.getProductCount());
        System.out.println("POS Session Completed!");
    }
}
