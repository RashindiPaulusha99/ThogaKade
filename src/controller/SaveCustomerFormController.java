package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Customer;

import java.sql.SQLException;

public class SaveCustomerFormController {

    public TextField txtCustomerId;
    public TextField txtCustomerTitle;
    public TextField txtCustomerName;
    public TextField txtCustomerAddress;
    public TextField txtPostalCode;
    public ComboBox cmbProvince;
    public TextField txtCity;

    public void initialize(){
        ObservableList<String> obList = FXCollections.observableArrayList(
                        "Western Province",
                        "Central Province",
                        "Southern Province",
                        "Eastern Province",
                        "Uva Province",
                        "Sabaragamuwa Province",
                        "North Western Province",
                        "North Central Province",
                        "Nothern Province"
                );
        cmbProvince.setItems(obList);
    }

    //save new customer
    public void saveCustomerOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        if (txtCustomerId.getText().equals("") || txtCustomerName.getText().equals("") || txtCustomerAddress.getText().equals("") || txtCustomerTitle.getText().equals("") || txtCity.getText().equals("") || txtPostalCode.getText().equals("")){
            new Alert(Alert.AlertType.WARNING, "All fields are required..").show();
        }else {
            String id = txtCustomerId.getText();

            if (new CustomerController().search(id)) {
                new Alert(Alert.AlertType.WARNING, "Already Exists..").show();
            }else {
                Customer c1 = new Customer(
                        txtCustomerId.getText(),
                        txtCustomerTitle.getText(),
                        txtCustomerName.getText(),
                        txtCustomerAddress.getText(),
                        txtCity.getText(),
                        (String) cmbProvince.getValue(),
                        txtPostalCode.getText()
                );

                if(new CustomerController().saveCustomer(c1)) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Saved..").showAndWait();
                    txtCustomerId.clear();
                    txtCustomerTitle.clear();
                    txtCustomerName.clear();
                    txtCustomerAddress.clear();
                    txtCity.clear();
                    txtPostalCode.clear();
                }else {
                    new Alert(Alert.AlertType.WARNING, "Try Again..").show();
                }
            }
        }
    }
}
