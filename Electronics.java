package PosSystem;

public class Electronics extends Product {
    private String brand;
    private String category;
    private int warrantyMonths;
    private double gstRate;

    public Electronics(int id, String name, double basePrice, double stockQty,
                       String brand, String category, int warrantyMonths, double gstRate) {
        super(id, name, basePrice, stockQty);
        this.brand = brand;
        this.category = category;
        this.warrantyMonths = warrantyMonths;
        this.gstRate = gstRate;
    }

    public String getBrand() {
        return brand;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    public double getGstRate() {
        return gstRate;
    }

    public void setWarrantyMonths(int warrantyMonths) {
        if (warrantyMonths >= 0) {
            this.warrantyMonths = warrantyMonths;
        }
    }

    public void extendWarranty(int months) {
        if (months > 0) {
            warrantyMonths += months;
        }
    }

    @Override
    public String getCategory() {
        return category;
    }

    // GST rate specific to electronics
    @Override
    public double getTaxRate() {
        return gstRate;
    }

    // Category-specific pricing rule
    @Override
    public double getCategoryDiscount() {
        switch (category.toLowerCase()) {
            case "mobile":
                return 0.05;
            case "accessories":
                return 0.10;
            default:
                return 0.0;
        }
    }

    @Override
    public void display() {
        System.out.println(getId() + " | " + getName() + " | " + brand
                + " | " + category + " | Warranty: " + warrantyMonths + " months"
                + " | GST: " + (gstRate * 100) + "%"
                + " | Price: " + String.format("%.2f", getBasePrice())
                + " | Stock: " + getStockQty());
    }
}
