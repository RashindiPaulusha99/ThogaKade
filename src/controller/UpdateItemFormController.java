package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Item;

import java.sql.SQLException;

public class UpdateItemFormController {
    public TextField txtCode;
    public TextField txtDescription;
    public TextField txtPackSize;
    public TextField txtUnitPrice;
    public TextField txtqtyOnHand;
    public TextField txtDiscount;
    public ComboBox cmbDescription;

    public void initialize(){
        ObservableList<String> obList = FXCollections.observableArrayList(
                "Fruits",
                "Vegitables ",
                "Rice",
                "Grains",
                "Spices",
                "All Flours",
                "Dry Fruits",
                "Edible Oils",
                "Beverages",
                "Snacks",
                "Branded foods",
                "Backery items",
                "Sweet & Desserts",
                "Porsonal care",
                "Cosmetics",
                "Baby care ",
                "Medical Health",
                "Household needs",
                "Office & Stationary",
                "Electrical & Electronics products"
        );
        cmbDescription.setItems(obList);
    }

    public void selectItemOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String ItemId = txtCode.getText();

        //search relevant item
        Item i1 = new ItemController().searchItem(ItemId);
        if (i1 == null) {
            new Alert(Alert.AlertType.WARNING, "Empty Result Set").showAndWait();
        } else {
            //if passed data didn't null ,pass data to setData method
            setData(i1);
        }
    }

    //set item's details to textfields
    void setData(Item i) {
        txtCode.setText(i.getCode());
        cmbDescription.setValue(i.getDescription());
        txtPackSize.setText(i.getPackSize());
        txtUnitPrice.setText(String.valueOf(i.getUnitPrice()));
        txtDiscount.setText(String.valueOf(i.getDiscount()));
        txtqtyOnHand.setText(String.valueOf(i.getQtyOnHand()));
    }

    public void updateItemOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Item i1 = new Item(
                txtCode.getText(),
                (String) cmbDescription.getValue(),
                Integer.parseInt(txtqtyOnHand.getText()),
                txtPackSize.getText(),
                Double.parseDouble(txtUnitPrice.getText()),
                Double.parseDouble(txtDiscount.getText())
        );

        //data of textFields pass to updateItem method to update and show alert if it updated or not
        if (new ItemController().updateItem(i1)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Updated..").showAndWait();
            txtCode.clear();
            txtPackSize.clear();
            txtUnitPrice.clear();
            txtDiscount.clear();
            txtqtyOnHand.clear();
        }else {
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }
}
