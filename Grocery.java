package PosSystem;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Grocery extends Product {
    private LocalDate expiryDate;
    private String unitType;
    private boolean organic;
    private String storageType;

    public Grocery(int id, String name, double pricePerUnit, double stockQty,
                   String unitType, LocalDate expiryDate, boolean organic, String storageType) {
        super(id, name, pricePerUnit, stockQty);
        this.unitType = unitType;
        this.expiryDate = expiryDate;
        this.organic = organic;
        this.storageType = storageType;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public String getUnitType() {
        return unitType;
    }

    public boolean isOrganic() {
        return organic;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isExpired() {
        return expiryDate.isBefore(LocalDate.now());
    }

    public long daysToExpiry() {
        return ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
    }

    // Loose items sold by weight/volume accept fractional quantities
    public boolean isVariableWeight() {
        return unitType.equalsIgnoreCase("KG") || unitType.equalsIgnoreCase("LITRE");
    }

    @Override
    public String getCategory() {
        return "Grocery";
    }

    // Loose produce is tax-free, packaged goods carry 5% tax
    @Override
    public double getTaxRate() {
        return isVariableWeight() ? 0.0 : 0.05;
    }

    // Near-expiry markdown rule
    @Override
    public double getCategoryDiscount() {
        long days = daysToExpiry();
        if (days <= 2) {
            return 0.30;
        } else if (days <= 5) {
            return 0.10;
        }
        return 0.0;
    }

    // Expired goods can never be sold
    @Override
    public boolean reduceStock(double qty) {
        if (isExpired()) {
            System.out.println("Cannot sell " + getName() + ": product expired on " + expiryDate);
            return false;
        }
        if (!isVariableWeight() && qty != Math.floor(qty)) {
            System.out.println(getName() + " must be sold in whole units.");
            return false;
        }
        return super.reduceStock(qty);
    }

    @Override
    public void display() {
        System.out.println(getId() + " | " + getName() + " | Grocery"
                + " | Price/" + unitType + ": " + String.format("%.2f", getBasePrice())
                + " | Stock: " + getStockQty() + " " + unitType
                + " | Expiry: " + expiryDate
                + (isExpired() ? " (EXPIRED)" : ""));
    }
}
