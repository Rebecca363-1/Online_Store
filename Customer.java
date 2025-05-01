package application;

import javafx.beans.property.*;

public class Customer {

    private final IntegerProperty customerId;
    private final StringProperty name;
    private final StringProperty username;
    private final StringProperty password;
    private final StringProperty address;

    public Customer(int customerId, String name, String username, String password, String address) {
        this.customerId = new SimpleIntegerProperty(customerId);
        this.name = new SimpleStringProperty(name);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.address = new SimpleStringProperty(address);
    }

    // Getters
    public int getCustomerId() {
        return customerId.get();
    }

    public String getName() {
        return name.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getPassword() {
        return password.get();
    }

    public String getAddress() {
        return address.get();
    }

    // Property Getters
    public IntegerProperty customerIdProperty() {
        return customerId;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty addressProperty() {
        return address;
    }

    // Setters
    public void setCustomerId(int id) {
        this.customerId.set(id);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public void setAddress(String address) {
        this.address.set(address);
    }
}
