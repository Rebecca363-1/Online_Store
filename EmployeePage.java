package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;

public class EmployeePage {

    @FXML private TextField item_idTF, item_nameTF, item_priceTF, quantityTF;
    @FXML
    private TableView<Coupon> couponTable;
    @FXML private TableColumn<Coupon, String> coupon_code1;
    @FXML private TableColumn<Coupon, Double> discount_percent;
    @FXML private TableColumn<Coupon, String> expiration_date;
    
    @FXML private TableView<Item> employeeTable;
    @FXML private TableColumn<Item, Integer> item_id;
    @FXML private TableColumn<Item, String> item_name;
    @FXML private TableColumn<Item, Double> item_price;
    @FXML private TableColumn<Item, Integer> quantity_in_stock;

    @FXML private TableView<Customer> customer_table;
    @FXML private TableColumn<Customer, Integer> customer_id;
    @FXML private TableColumn<Customer, String> customer_name;
    @FXML private TableColumn<Customer, String> customer_username;
    @FXML private TableColumn<Customer, String> customer_password;
    @FXML private TableColumn<Customer, String> address;

    @FXML private TableView<Order> order_table;
    @FXML private TableColumn<Order, Integer> order_id;
    @FXML private TableColumn<Order, Integer> customer_id_order;
    @FXML private TableColumn<Order, String> order_date;
    @FXML private TableColumn<Order, String> coupon_code;

    private ObservableList<Item> data = FXCollections.observableArrayList();
    private ObservableList<Customer> customers = FXCollections.observableArrayList();
    private ObservableList<Order> orders = FXCollections.observableArrayList();
    private ObservableList<Coupon> coupons = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize item table
        item_id.setCellValueFactory(cellData -> cellData.getValue().itemIdProperty().asObject());
        item_name.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        item_price.setCellValueFactory(cellData -> cellData.getValue().itemPriceProperty().asObject());
        quantity_in_stock.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        employeeTable.setItems(data);

        // Initialize customer table
        customer_id.setCellValueFactory(cellData -> cellData.getValue().customerIdProperty().asObject());
        customer_name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        customer_username.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        customer_password.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        address.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        customer_table.setItems(customers);

        // Initialize order table
        order_id.setCellValueFactory(cellData -> cellData.getValue().orderIdProperty().asObject());
        customer_id_order.setCellValueFactory(cellData -> cellData.getValue().customerIdProperty().asObject());
        order_date.setCellValueFactory(cellData -> cellData.getValue().orderDateProperty());
        coupon_code1.setCellValueFactory(cellData -> cellData.getValue().couponCodeProperty());
        order_table.setItems(orders);

        // Initialize coupon table
        coupon_code1.setCellValueFactory(cellData -> cellData.getValue().couponCodeProperty());
        discount_percent.setCellValueFactory(cellData -> cellData.getValue().discountPercentProperty().asObject());
        expiration_date.setCellValueFactory(cellData -> cellData.getValue().expirationDateProperty());
        couponTable.setItems(coupons);

        // Load data
        loadCoupons();
        loadItems();
        loadCustomers();

        // Show items by default
        showItemsTable();
    }

    @FXML
    public void handleAddItem(ActionEvent event) {
        showItemsTable();
        try {
            int id = Integer.parseInt(item_idTF.getText());
            String name = item_nameTF.getText();
            double price = Double.parseDouble(item_priceTF.getText());
            int quantity = Integer.parseInt(quantityTF.getText());

            Item newItem = new Item(id, name, price, quantity);
            data.add(newItem);

            try (Connection connection = Databaseconnection.getConnection()) {
                String sql = "INSERT INTO Items (item_id, item_name, item_price, quantity_in_stock) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, id);
                stmt.setString(2, name);
                stmt.setDouble(3, price);
                stmt.setInt(4, quantity);
                stmt.executeUpdate();
            }

            item_idTF.clear();
            item_nameTF.clear();
            item_priceTF.clear();
            quantityTF.clear();

        } catch (NumberFormatException e) {
            showAlert("Please enter valid numeric values for ID, Price, and Quantity.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database error: " + e.getMessage());
        }

        loadItems();
    }

    @FXML
    public void handleShowCustomers(ActionEvent event) {
        employeeTable.setVisible(false);
        customer_table.setVisible(true);
        order_table.setVisible(false);
        couponTable.setVisible(false);

        item_idTF.setVisible(false);
        item_nameTF.setVisible(false);
        item_priceTF.setVisible(false);
        quantityTF.setVisible(false);

        loadCustomers();
    }

    @FXML
    public void handleOrders(ActionEvent event) {
        showOrdersTable();
        loadOrders();
    }

    @FXML
    public void handleCoupons(ActionEvent event) {
        couponTable.setVisible(true);
        employeeTable.setVisible(false);
        customer_table.setVisible(false);
        order_table.setVisible(false);

        item_idTF.setVisible(false);
        item_nameTF.setVisible(false);
        item_priceTF.setVisible(false);
        quantityTF.setVisible(false);

        loadCoupons();
    }

    private void showItemsTable() {
        employeeTable.setVisible(true);
        customer_table.setVisible(false);
        order_table.setVisible(false);
        couponTable.setVisible(false);

        item_idTF.setVisible(true);
        item_nameTF.setVisible(true);
        item_priceTF.setVisible(true);
        quantityTF.setVisible(true);
    }

    private void showOrdersTable() {
        employeeTable.setVisible(false);
        customer_table.setVisible(false);
        order_table.setVisible(true);
        couponTable.setVisible(false);

        item_idTF.setVisible(false);
        item_nameTF.setVisible(false);
        item_priceTF.setVisible(false);
        quantityTF.setVisible(false);
    }

    private void loadItems() {
        data.clear();
        try (Connection connection = Databaseconnection.getConnection()) {
            String sql = "SELECT * FROM Items";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Item item = new Item(
                        rs.getInt("item_id"),
                        rs.getString("item_name"),
                        rs.getDouble("item_price"),
                        rs.getInt("quantity_in_stock")
                );
                data.add(item);
            }
        } catch (SQLException e) {
            showAlert("Error loading item data: " + e.getMessage());
        }
    }

    private void loadCustomers() {
        customers.clear();
        try (Connection connection = Databaseconnection.getConnection()) {
            String sql = "SELECT * FROM Customers";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("customer_username"),
                        rs.getString("customer_password"),
                        rs.getString("address")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            showAlert("Error loading customer data: " + e.getMessage());
        }
    }

    private void loadOrders() {
        orders.clear();
        try (Connection connection = Databaseconnection.getConnection()) {
            String sql = "SELECT * FROM Orders";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("order_id"),
                        rs.getInt("customer_id"),
                        rs.getString("order_date"),
                        rs.getString("coupon_code")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            showAlert("Error loading order data: " + e.getMessage());
        }
    }

    private void loadCoupons() {
        coupons.clear();
        try (Connection connection = Databaseconnection.getConnection()) {
            String sql = "SELECT * FROM Coupons";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Coupon coupon = new Coupon(
                    rs.getString("coupon_code"),
                    rs.getDouble("discount_percent"),
                    rs.getString("expiration_date")
                );
                coupons.add(coupon);
            }
        } catch (SQLException e) {
            showAlert("Error loading coupon data: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

