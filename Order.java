package application;

import javafx.beans.property.*;

public class Order {
    private final IntegerProperty orderId;
    private final IntegerProperty customerId;
    private final StringProperty orderDate;
    private final StringProperty couponCode;

    public Order(int orderId, int customerId, String orderDate, String couponCode) {
        this.orderId = new SimpleIntegerProperty(orderId);
        this.customerId = new SimpleIntegerProperty(customerId);
        this.orderDate = new SimpleStringProperty(orderDate);
        this.couponCode = new SimpleStringProperty(couponCode);
    }

    public int getOrderId() {
        return orderId.get();
    }

    public IntegerProperty orderIdProperty() {
        return orderId;
    }

    public int getCustomerId() {
        return customerId.get();
    }

    public IntegerProperty customerIdProperty() {
        return customerId;
    }

    public String getOrderDate() {
        return orderDate.get();
    }

    public StringProperty orderDateProperty() {
        return orderDate;
    }

    public String getCouponCode() {
        return couponCode.get();
    }

    public StringProperty couponCodeProperty() {
        return couponCode;
    }
}

