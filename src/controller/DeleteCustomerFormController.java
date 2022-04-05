package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import model.Customer;

import java.sql.SQLException;

public class DeleteCustomerFormController {
    public TextField txtId;
    public TextField txtTitle;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtPostalCode;
    public TextField txtCity;
    public TextField txtProvince;

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
        txtProvince.setText(c.getProvince());
        txtPostalCode.setText(c.getPostalCode());
    }

    public void deleteCustomerOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        //delete customer
        if (new CustomerController().deleteCustomer(txtId.getText())){
            new Alert(Alert.AlertType.CONFIRMATION, "Deleted").showAndWait();
            txtId.clear();
            txtTitle.clear();
            txtName.clear();
            txtAddress.clear();
            txtCity.clear();
            txtPostalCode.clear();
        }else{
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }
}
