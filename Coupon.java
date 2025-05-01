package application;

import javafx.beans.property.*;

public class Coupon {
    private final StringProperty couponCode;
    private final DoubleProperty discountPercent;
    private final StringProperty expirationDate;

    // Constructor
    public Coupon(String couponCode, double discountPercent, String expirationDate) {
        this.couponCode = new SimpleStringProperty(couponCode);
        this.discountPercent = new SimpleDoubleProperty(discountPercent);
        this.expirationDate = new SimpleStringProperty(expirationDate);
    }

    // Getters and Property Methods
    public String getCouponCode() {
        return couponCode.get();
    }

    public StringProperty couponCodeProperty() {
        return couponCode;
    }

    public double getDiscountPercent() {
        return discountPercent.get();
    }

    public DoubleProperty discountPercentProperty() {
        return discountPercent;
    }

    public String getExpirationDate() {
        return expirationDate.get();
    }

    public StringProperty expirationDateProperty() {
        return expirationDate;
    }
}

