package controllers;

import chazz.c482project.InventoryApplication;
import com.sun.javafx.charts.Legend;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddPartController implements Initializable {
    // FXML fields declaration
    @FXML private Label nameIdLabel;
    @FXML private RadioButton inHouseButton;
    @FXML private ToggleGroup partTypeGroup;
    @FXML private RadioButton outsourcedButton;
    @FXML private TextField idTextField; // Not used for adding new parts (IDs are auto-generated)
    @FXML private TextField nameTextField;
    @FXML private TextField invTextField;
    @FXML private TextField priceTextField;
    @FXML private TextField maxTextField;
    @FXML private TextField machineIdTextField; // Used for InHouse parts
    @FXML private TextField companyNameTextField; // Used for Outsourced parts
    @FXML private TextField minTextField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inHouseButton.setSelected(true);
        idTextField.setText(String.valueOf(Inventory.nextPartId())); // Display next ID without incrementing
        idTextField.setEditable(false); // Make sure the field is not editable
    }

    // Handler for InHouse radio button
    @FXML
    private void inHouseButtonAction(ActionEvent event) {
        nameIdLabel.setText("Machine ID");
        machineIdTextField.setVisible(true);
        companyNameTextField.setVisible(false);
    }

    // Handler for Outsourced radio button
    public void outsourcedButtonAction(ActionEvent actionEvent) {
        nameIdLabel.setText("Company Name");
        machineIdTextField.setVisible(false);
        companyNameTextField.setVisible(true);
    }
    @FXML
    private void savePart(ActionEvent event) {
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

            // Check which type of part is being added
            if (inHouseButton.isSelected()) {
                addInHousePart(name, price, inv, min, max);
            } else if (outsourcedButton.isSelected()) {
                addOutsourcedPart(name, price, inv, min, max);
            }
            System.out.println("Part added successfully");

            // Return to main screen after adding part
            closeCurrentStage(event);

        } catch (Exception e) {
            showAlert(4); // General error
        }
    }

    // Adds an InHouse part
    public void addInHousePart(String name, double price, int inv, int min, int max) {
        try {
            int machineId = Integer.parseInt(machineIdTextField.getText());
            int newId = Inventory.getNewPartId(); // Generate new ID here
            InHouse inHousePart = new InHouse(newId, name, price, inv, min, max, machineId);
            Inventory.addPart(inHousePart);
        } catch (NumberFormatException e) {
            showAlert(3); // Invalid Machine ID
        }
    }

    // Adds an Outsourced part
    private void addOutsourcedPart(String name, double price, int inv, int min, int max) {
        String companyName = companyNameTextField.getText();
        int newId = Inventory.getNewPartId(); // Generate new ID here
        Outsourced outsourcedPart = new Outsourced(newId, name, price, inv, min, max, companyName);
        Inventory.addPart(outsourcedPart);
    }

    // Inventory validation
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

    // Utility method to close the current stage
    private void closeCurrentStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * Method to show an alert with a custom message.
     */
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Method to show different types of alerts based on the error
    private void showAlert(int alertType) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        switch (alertType) {
            case 1:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Inventory value");
                alert.setContentText("Inventory must be between or equal to Min and Max.");
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

    // Handler for cancel button
    @FXML
    private void cancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}