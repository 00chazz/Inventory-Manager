package controllers;

import chazz.c482project.InventoryApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private static Part partToModify;
    private static Product productToModify;
    @FXML private AnchorPane mainPane;
    @FXML private TextField searchPartsField;
    @FXML private TableView<Part> partsTableView;
    @FXML private TextField searchProductsField;
    @FXML private TableColumn<Part, Integer> partIdColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partInventoryColumn;
    @FXML private TableColumn<Part, Double> partPriceColumn;
    @FXML private TableView<Product> productsTableView;
    @FXML private TableColumn<Product, Integer> productIdColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, Integer> productInventoryColumn;
    @FXML private TableColumn<Product, Double> productPriceColumn;
    @FXML private Button exitButton;




    @FXML public void initialize(URL location, ResourceBundle resources) {

        //Populate parts table view
        partsTableView.setItems(Inventory.getAllParts());
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        InHouse chair = new InHouse(0, "chair", 2.99, 100, 1, 1000, 13);
        InHouse wheel = new InHouse(1, "wheel", 4.99, 100, 1, 1000, 15);
        chair.setId(Inventory.getNewPartId());
        Inventory.addPart(chair);
        wheel.setId(Inventory.getNewPartId());
        Inventory.addPart(wheel);
        Product car = new Product(0, "car", 5.99, 100, 1, 1000);
        Product truck = new Product(1, "truck", 8.99, 100, 1, 1000);
        car.setId(Inventory.getNewProductId());
        Inventory.addProduct(car);
        truck.setId(Inventory.getNewProductId());
        Inventory.addProduct(truck);

        //Populate products table view
        productsTableView.setItems(Inventory.getAllProducts());
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    @FXML
    private void productSearch(ActionEvent event) {

        ObservableList<Product> allProducts = Inventory.getAllProducts();
        ObservableList<Product> productsFound = FXCollections.observableArrayList();
        String searchString = searchProductsField.getText();

        for (Product product : allProducts) {
            if (String.valueOf(product.getId()).contains(searchString) ||
                    product.getName().contains(searchString)) {
                productsFound.add(product);
            }
        }

        productsTableView.setItems(productsFound);
    }
    @FXML
    private void productSearchTextKeyPressed(KeyEvent event) {

        if (searchProductsField.getText().isEmpty()) {
            productsTableView.setItems(Inventory.getAllProducts());
        } else {
            productSearch(null); // Call the search method
        }
    }
    @FXML
    private void partSearch(ActionEvent event) {
        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> partsFound = FXCollections.observableArrayList();
        String searchString = searchPartsField.getText().toLowerCase();

        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(searchString) ||
                    part.getName().toLowerCase().contains(searchString)) {
                partsFound.add(part);
            }
        }

        partsTableView.setItems(partsFound);
    }

    @FXML
    private void partSearchTextKeyPressed(KeyEvent event) {
        if (searchPartsField.getText().isEmpty()) {
            partsTableView.setItems(Inventory.getAllParts());
        } else {
            partSearch(null); // Call the search method
        }
    }

    public static Part getPartToModify() {
        return partToModify;
    }

    public static Product getProductToModify() {
        return productToModify;
    }

    @FXML
    private void handleAddPartAction() {
        try {

            //Load add part view
            FXMLLoader loader = new FXMLLoader(InventoryApplication.class.getResource("addPartView.fxml"));
            Parent addPartView = loader.load();

            //get add part controller
            AddPartController addPartController = loader.getController();

            //open add part in new window
            Scene scene = new Scene(addPartView);
            Stage stage = new Stage();
            stage.setTitle("Add Part");
            stage.setScene(scene);
            stage.showAndWait();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    @FXML
    private void handleModifyPartAction() {
        partToModify = partsTableView.getSelectionModel().getSelectedItem();
        if(partToModify == null){
            showAlert(1);
            return;
        }

        try {

            partToModify = partsTableView.getSelectionModel().getSelectedItem();
            //Load add part view
            FXMLLoader loader = new FXMLLoader(InventoryApplication.class.getResource("modifyPartView.fxml"));
            Parent modifyPartView = loader.load();

            //get modify part controller
            ModifyPartController modifyPartController = loader.getController();

            //open modify part in new window
            Scene scene = new Scene(modifyPartView);
            Stage stage = new Stage();
            stage.setTitle("Modify Part");
            stage.setScene(scene);
            stage.showAndWait();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeletePartAction() {
        // Deletes Selected Part
        Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();

        Alert alert = null;
        if (selectedPart == null) {
            showAlert(1);
        } else {

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setContentText("Delete selected part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deletePart(selectedPart);
            }
        }
    }


    private void showAlert(int i) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        switch(i){
            case 1:
                alert.setTitle("No selection made");
                alert.setHeaderText("Please select a part or product");
                break;
        }
        alert.showAndWait();
    }


    @FXML
    private void handleAddProductAction() {
        try {

            //Load add product view
            FXMLLoader loader = new FXMLLoader(InventoryApplication.class.getResource("addProductView.fxml"));
            Parent addProductView = loader.load();

            //get add product controller
            AddProductController addProductController = loader.getController();

            //open add product in new window
            Scene scene = new Scene(addProductView);
            Stage stage = new Stage();
            stage.setTitle("Add Product");
            stage.setScene(scene);
            stage.showAndWait();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleModifyProductAction() {
        productToModify = productsTableView.getSelectionModel().getSelectedItem();
        if(productToModify == null){
            showAlert(1);
            return;
        }
        try {
            productToModify = productsTableView.getSelectionModel().getSelectedItem();
            //Load modify product view
            FXMLLoader loader = new FXMLLoader(InventoryApplication.class.getResource("modifyProductView.fxml"));
            Parent modifyProductView = loader.load();


            //get modify product controller
            ModifyProductController modifyProductController = loader.getController();
            modifyProductController.setProduct(productsTableView.getSelectionModel().getSelectedItem());
            modifyProductController.populate();
            //open modify product in new window
            Scene scene = new Scene(modifyProductView);
            Stage stage = new Stage();
            stage.setTitle("Modify Product");
            stage.setScene(scene);
            stage.showAndWait();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteProductAction() {
        // Deletes Selected Product
        Product selectedProduct = productsTableView.getSelectionModel().getSelectedItem();

        Alert alert = null;
        if (selectedProduct == null) {
            showAlert(1);
        } else {

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setContentText("Delete selected product?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deleteProduct(selectedProduct);
            }
        }
    }




    @FXML
    private void handleCloseButtonAction(){
        // get a handle to the stage
        Stage stage = (Stage) exitButton.getScene().getWindow();
        // closes window
        stage.close();
    }


}