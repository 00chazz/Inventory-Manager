package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.net.URL;
import java.util.ResourceBundle;


public class ModifyProductController {

    private Product selectedProduct;
    private int selectedProductId;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    @FXML public TextField minTextField;
    @FXML private TableView<Part> addPartTableView;
    @FXML private TableView<Part> productPartsTableView;
    @FXML public TextField partSearchTextField;
    @FXML public Button addButton;
    @FXML public Button removeButton;
    @FXML public Button saveButton;
    @FXML public Button cancelButton;
    @FXML public TextField idTextField;
    @FXML public TextField nameTextField;
    @FXML public TextField invTextField;
    @FXML public TextField priceTextField;
    @FXML public TextField maxTextField;
    @FXML private TableColumn<Part, Integer> addPartIdColumn;
    @FXML private TableColumn<Part, String> addPartNameColumn;
    @FXML private TableColumn<Part, Integer> addPartInventoryColumn;
    @FXML private TableColumn<Part, Double> addPartPriceColumn;
    @FXML private TableColumn<Part, Integer> associatedPartsIdColumn;
    @FXML private TableColumn<Part, String> associatedPartsNameColumn;
    @FXML private TableColumn<Part, Integer> associatedPartsInventoryColumn;
    @FXML private TableColumn<Part, Double> associatedPartsPriceColumn;

    public void initialize(URL location, ResourceBundle resources) {
        selectedProduct = MainController.getProductToModify();
        setProduct(selectedProduct); // This ensures associatedParts is set up correctly
    }
    public void setProduct(Product product) {
        selectedProduct = product;
        associatedParts.clear();
        if (selectedProduct != null) {
            associatedParts.addAll(selectedProduct.getAllAssociatedParts());

            // Update UI elements with the selected product's details
            idTextField.setText(String.valueOf(selectedProduct.getId()));
            nameTextField.setText(selectedProduct.getName());
            invTextField.setText(String.valueOf(selectedProduct.getStock()));
            priceTextField.setText(String.valueOf(selectedProduct.getPrice()));
            maxTextField.setText(String.valueOf(selectedProduct.getMax()));
            minTextField.setText(String.valueOf(selectedProduct.getMin()));

            // Update the parts table view
            productPartsTableView.refresh();
            productPartsTableView.setItems(associatedParts);

        }
    }
    public void populate() {
        if (selectedProduct != null) {
            // Set the items for the addPartTableView
            addPartTableView.setItems(Inventory.getAllParts());
            addPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            addPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            addPartInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
            addPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

            // Set the cell value factories for the associated parts table
            associatedPartsIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            associatedPartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            associatedPartsInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
            associatedPartsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

            // The associatedParts list should already be updated in setProduct
            productPartsTableView.setItems(associatedParts);
            productPartsTableView.refresh();
        }
    }
    @FXML
    private void addPartToProduct() {
        Part partToAdd = addPartTableView.getSelectionModel().getSelectedItem();
        if(partToAdd == null) {
            showAlert("Part not selected", "Please select a part to add.");
            return;
        }
        associatedParts.add(partToAdd);
    }

    @FXML
    private void removePartFromProduct() {
        Part partToRemove = productPartsTableView.getSelectionModel().getSelectedItem();
        if(partToRemove == null) {
            showAlert("Part not selected", "Please select a part to remove.");
            return;
        }
        associatedParts.remove(partToRemove);
        selectedProduct.deleteAssociatedPart(partToRemove); // Update the model immediately
        productPartsTableView.refresh();
    }

    @FXML
    private void saveModifiedProduct(ActionEvent event) {
        try {
            //Parse input
            String name = nameTextField.getText();
            double price = Double.parseDouble(priceTextField.getText());
            int inv = Integer.parseInt(invTextField.getText());
            int min = Integer.parseInt(minTextField.getText());
            int max = Integer.parseInt(maxTextField.getText());

            // Validation
            if(name.isEmpty() || !minValid(min, max) || !inventoryValid(min, max, inv)) {
                return;
            }

            // Update product details
            selectedProduct.setName(name);
            selectedProduct.setPrice(price);
            selectedProduct.setStock(inv);
            selectedProduct.setMin(min);
            selectedProduct.setMax(max);

            // Update associated parts
            selectedProduct.clearAssociatedParts(); // Clear existing parts
            for (Part part : associatedParts) {
                selectedProduct.addAssociatedPart(part); // Add each associated part
            }

            closeCurrentStage(event);

        } catch (Exception e) {
            showAlert("Error", "Part was not modified. Field was left blank, or invalid character was used.");
        }
    }

    private boolean inventoryValid(int min, int max, int inv) {
        if(inv < min || inv > max ){
            showAlert("Error", "Inventory must be between or equal to Min and Max.");
            return false;
        }
        return true;
    }

    private boolean minValid(int min, int max) {
        if(min <= 0 || min >= max) {
            showAlert("Error", "Min must be greater than 0 and less than Max.");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeCurrentStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancel(ActionEvent event) {
        closeCurrentStage(event);
    }
}
