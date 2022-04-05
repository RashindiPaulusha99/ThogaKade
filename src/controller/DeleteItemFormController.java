package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import model.Item;

import java.sql.SQLException;

public class DeleteItemFormController {

    public TextField txtCode;
    public TextField txtDescription;
    public TextField txtPackSize;
    public TextField txtUnitPrice;
    public TextField txtqtyOnHand;
    public TextField txtDiscount;

    public void selectItemOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String ItemId = txtCode.getText();

        //search relevant item
        Item i1 = new ItemController().searchItem(ItemId);
        if (i1 == null) {
            new Alert(Alert.AlertType.WARNING, "Empty Result Set").show();
        } else {
            //if passed data didn't null ,pass data to setData method
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

    public void deleteItemOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        //delete item
        if (new ItemController().deleteItem(txtCode.getText())){
            new Alert(Alert.AlertType.CONFIRMATION, "Deleted").showAndWait();
            txtCode.clear();
            txtDescription.clear();
            txtPackSize.clear();
            txtUnitPrice.clear();
            txtqtyOnHand.clear();
            txtDiscount.clear();
        }else{
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }
}
