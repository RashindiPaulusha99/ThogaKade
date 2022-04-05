package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import model.Item;

import java.sql.SQLException;

public class SearchItemFormController {
    public TextField txtCode;
    public TextField txtDescription;
    public TextField txtPackSize;
    public TextField txtUnitPrice;
    public TextField txtqtyOnHand;
    public TextField txtDiscount;

    public void searchItemOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String itemId = txtCode.getText();

        //search relevant item
        Item i1 = new ItemController().searchItem(itemId);
        if (i1 == null) {
            new Alert(Alert.AlertType.WARNING, "Empty Result Set").showAndWait();
        } else {
            //if passed data didn't null, pass data to setData method
            setData(i1);
        }
    }

    //set item's details to textfields
    void setData(Item i) {
        txtCode.setText(i.getCode());
        txtDescription.setText(i.getDescription());
        txtPackSize.setText(i.getPackSize());
        txtUnitPrice.setText(String.valueOf(i.getUnitPrice()));
        txtDiscount.setText(String.valueOf(i.getDiscount()));
        txtqtyOnHand.setText(String.valueOf(i.getQtyOnHand()));
    }
}
