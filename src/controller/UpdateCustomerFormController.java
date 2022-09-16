package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Customer;

import java.sql.SQLException;

public class UpdateCustomerFormController {
    public TextField txtId;
    public TextField txtTitle;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtPostalCode;
    public TextField txtCity;
    public ComboBox<String> cmbProvince;

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

    public void selectCustomerOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String customerId = txtId.getText();

        //search relevant customer
        Customer c1 = new CustomerController().searchCustomer(customerId);
        if (c1 == null) {
            new Alert(Alert.AlertType.WARNING, "Empty Result Set").show();
        } else {
            //if passed data didn't null pass data to setData method
            setData(c1);
        }
    }

    //set customer's details to textfields
    void setData(Customer c) {
        txtId.setText(c.getId());
        txtTitle.setText(c.getTitle());
        txtName.setText(c.getName());
        txtAddress.setText(c.getAddress());
        txtCity.setText(c.getCity());
        cmbProvince.setValue(c.getProvince());
        txtPostalCode.setText(c.getPostalCode());
    }

    public void updateCustomerOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        if (txtId.getText().equals("") || txtName.getText().equals("") || txtAddress.getText().equals("") || txtCity.getText().equals("") || txtTitle.getText().equals("") || txtPostalCode.getText().equals("")){
            new Alert(Alert.AlertType.CONFIRMATION, "All fields are required..").showAndWait();
        }else {
            if (new CustomerController().search(txtId.getText())){
                new Alert(Alert.AlertType.CONFIRMATION, "Already Exists..").showAndWait();
            }else {
                Customer c1 = new Customer(
                        txtId.getText(),
                        txtTitle.getText(),
                        txtName.getText(),
                        txtAddress.getText(),
                        txtCity.getText(),
                        cmbProvince.getValue(),
                        txtPostalCode.getText()
                );

                //data of textFields pass to updateCustomer method to update and show alert if it updated or not
                if (new CustomerController().updateCustomer(c1)) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Updated..").showAndWait();
                    txtId.clear();
                    txtTitle.clear();
                    txtName.clear();
                    txtAddress.clear();
                    txtCity.clear();
                    txtPostalCode.clear();
                }else {
                    new Alert(Alert.AlertType.WARNING, "Try Again").show();
                }
            }
        }
    }
}
