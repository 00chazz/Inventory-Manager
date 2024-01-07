package controllers;

import chazz.c482project.InventoryApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.net.URL;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();


    @FXML private TableColumn<Part, Integer> addPartIdColumn;
    @FXML private TableColumn<Part, String> addPartNameColumn;
    @FXML private TableColumn<Part, Integer> addPartInventoryColumn;
    @FXML private TableColumn<Part, Double> addPartPriceColumn;
    @FXML private TableColumn<Part, Integer> associatedPartsIdColumn;
    @FXML private TableColumn<Part, String> associatedPartsNameColumn;
    @FXML private TableColumn<Part, Integer> associatedPartsInventoryColumn;
    @FXML private TableColumn<Part, Double> associatedPartsPriceColumn;
    @FXML private TextField minTextField;
    @FXML private TableView<Part> addPartTableView;
    @FXML private TableView<Part> productPartsTableView;
    @FXML private TextField partSearchTextField;
    @FXML private Button addButton;
    @FXML private Button removeButton;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private TextField maxTextField;
    @FXML private TextField idTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField invTextField;
    @FXML private TextField priceTextField;




    @FXML public void initialize(URL url, ResourceBundle resourceBundle) {
        //Populate add table view
        addPartTableView.setItems(Inventory.getAllParts());
        addPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        addPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addPartInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        idTextField.setText(String.valueOf(Inventory.nextProductId()));

        associatedPartsIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartsInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productPartsTableView.setItems(associatedParts);
        idTextField.setText(String.valueOf(Inventory.nextProductId()));

    }

    @FXML private void addPartToProduct(){
        Part partToAdd = addPartTableView.getSelectionModel().getSelectedItem();
        if(partToAdd == null){
            showAlert(1);
            return;
        }
        associatedParts.add(partToAdd);
    }
    @FXML
    private void saveProduct(ActionEvent event) {
        try {
            //Parse input
            int id = 0;
            String name = nameTextField.getText();
            double price = Double.parseDouble(priceTextField.getText());
            int inv = Integer.parseInt(invTextField.getText());
            int min = Integer.parseInt(minTextField.getText());
            int max = Integer.parseInt(maxTextField.getText());

            // Validation
            if(name.isEmpty() || !minValid(min, max) || !inventoryValid(min, max, inv)) {
                return;
            }

            int newId = Inventory.getNewProductId(); // Generate new ID here
            Product newProduct = new Product(newId, name, price, inv, min, max);
            for (Part part : associatedParts) {
                newProduct.addAssociatedPart(part); // Add each associated part to the product
            }
            Inventory.addProduct(newProduct);
            System.out.println("Product added successfully");

            // Return to main screen after adding part
            closeCurrentStage(event);

        } catch (Exception e) {
            showAlert(4); // General error
        }
    }
    private boolean inventoryValid(int min, int max, int inv) {
        if(inv < min || inv > max ){
            showAlert(1);
            return false;
        }
        return true;
    }

    // Minimum value validation
    private boolean minValid(int min, int max) {
        if(min <= 0 || min >= max) {
            showAlert(2);
            return false;
        }
        return true;
    }
    private void showAlert(int alertType) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        switch (alertType) {
            case 1:
                alert.setTitle("Error");
                alert.setHeaderText("Part not selected");
                alert.setContentText("Please select a part to add.");
                break;
            case 2:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Min value");
                alert.setContentText("Min must be greater than 0 and less than Max.");
                break;
            case 3:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Machine ID");
                alert.setContentText("Machine ID must only be numbers.");
                break;

            case 4:
                alert.setTitle("Error");
                alert.setHeaderText("Part was not added.");
                alert.setContentText("Field was left blank, or invalid character was used.");
                break;

        }
        alert.showAndWait();
    }
    private void closeCurrentStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void cancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

}
