package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Item;

import java.sql.SQLException;

public class SaveItemFormController {

    public TextField txtItemCode;
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

    //save new Item
    public void saveItemOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        if (txtItemCode.getText().equals("") || txtqtyOnHand.getText().equals("") || txtPackSize.getText().equals("") || txtDiscount.getText().equals("") || txtUnitPrice.getText().equals("")){
            new Alert(Alert.AlertType.WARNING, "All fields are required..").show();
        }else {
            if(new ItemController().search(txtItemCode.getText())) {
                new Alert(Alert.AlertType.WARNING, "Already Exists..").show();
            }else {
                Item i1 = new Item(
                        txtItemCode.getText(),
                        (String) cmbDescription.getValue(),
                        Integer.parseInt(txtqtyOnHand.getText()),
                        txtPackSize.getText(),
                        Double.parseDouble(txtUnitPrice.getText()),
                        Double.parseDouble(txtDiscount.getText())
                );

                if (new ItemController().saveItem(i1)){
                    new Alert(Alert.AlertType.CONFIRMATION, "Saved..").showAndWait();
                    txtItemCode.clear();
                    txtPackSize.clear();
                    txtUnitPrice.clear();
                    txtqtyOnHand.clear();
                    txtDiscount.clear();
                }else {
                    new Alert(Alert.AlertType.WARNING, "Try Again..").show();
                }
            }
        }
    }
}
