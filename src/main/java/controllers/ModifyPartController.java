package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.net.URL;
import java.util.ResourceBundle;

public class ModifyPartController implements Initializable {

    private Part selectedPart;
    private int selectedPartId;
    @FXML public RadioButton inHouseButton;
    @FXML public ToggleGroup partTypeGroup;
    @FXML public RadioButton outsourcedButton;
    @FXML public TextField idTextField;
    @FXML public TextField nameTextField;
    @FXML public TextField invTextField;
    @FXML public TextField priceTextField;
    @FXML public TextField maxTextField;
    @FXML private TextField nameIdTextField; // Used for InHouse parts
    @FXML public TextField minTextField;
    @FXML public Button saveButton;
    @FXML public Button cancelButton;
    @FXML private Label nameIdLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedPart = MainController.getPartToModify();


        if (selectedPart instanceof InHouse) {
            inHouseButton.setSelected(true);
            nameIdLabel.setText("Machine ID");
            nameIdTextField.setText(String.valueOf(((InHouse) selectedPart).getMachineId()));
        }

        if (selectedPart instanceof Outsourced){
            outsourcedButton.setSelected(true);
            nameIdLabel.setText("Company Name");
            nameIdTextField.setText(((Outsourced) selectedPart).getCompanyName());
        }

        idTextField.setText(String.valueOf(selectedPart.getId()));
        nameTextField.setText(selectedPart.getName());
        invTextField.setText(String.valueOf(selectedPart.getStock()));
        priceTextField.setText(String.valueOf(selectedPart.getPrice()));
        maxTextField.setText(String.valueOf(selectedPart.getMax()));
        minTextField.setText(String.valueOf(selectedPart.getMin()));
    }

    // Handler for InHouse radio button
    @FXML
    private void inHouseButtonAction(ActionEvent event) {
        nameIdLabel.setText("Machine ID");
    }
    public void outsourcedButtonAction(ActionEvent actionEvent) {
        nameIdLabel.setText("Company Name");
    }
    @FXML
    private void modifyPart(ActionEvent event) {
        try {
            //Parse input

            String name = nameTextField.getText();
            double price = Double.parseDouble(priceTextField.getText());
            int inv = Integer.parseInt(invTextField.getText());
            int min = Integer.parseInt(minTextField.getText());
            int max = Integer.parseInt(maxTextField.getText());

            // Validation
            if(name.isEmpty() || !minValid(min, max) || !inventoryValid(min, max, inv)) {
                return; // Validation failed, exit method
            }

            // Check which type of part is being added
            if (inHouseButton.isSelected()) {
                addInHousePart(name, price, inv, min, max);
            } else if (outsourcedButton.isSelected()) {
                addOutsourcedPart(name, price, inv, min, max);
            }

            // Return to main screen after modifying part
            Inventory.deletePart(selectedPart);
            closeCurrentStage(event);
        } catch (Exception e) {
            System.out.println("Error in modify: " + e.getMessage()); // Debugging line
            showAlert(1); // General error
        }
    }

    // Adds an InHouse part
    private void addInHousePart(String name, double price, int inv, int min, int max) {
        try {
            int machineId = Integer.parseInt(nameIdTextField.getText());
            selectedPartId = Integer.parseInt(idTextField.getText());
            InHouse inHousePart = new InHouse(selectedPartId, name, price, inv, min, max, machineId);
            Inventory.addPart(inHousePart);
        } catch (NumberFormatException e) {
            showAlert(3); // Invalid Machine ID
        }
    }

    // Adds an Outsourced part
    private void addOutsourcedPart(String name, double price, int inv, int min, int max) {
        selectedPartId = Integer.parseInt(idTextField.getText());
        String companyName = nameIdTextField.getText();
        Outsourced outsourcedPart = new Outsourced(selectedPartId, name, price, inv, min, max, companyName);
        Inventory.addPart(outsourcedPart);
    }


    private boolean inventoryValid(int min, int max, int inv) {
        if(inv < min || inv > max ){
            showAlert(1);
            return false;
        }
        return true;
    }

    private void closeCurrentStage(ActionEvent event) {
        // Close the stage
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private boolean minValid(int min, int max) {
        if(min <= 0 || min >= max) {
            showAlert(2);
            return false;
        }
        return true;
    }


    @FXML
    private void cancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

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
                alert.setHeaderText("Part was not modified.");
                alert.setContentText("Field was left blank, or invalid character was used.");
                break;

        }
        alert.showAndWait();
    }



}