package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Item;
import view.tm.ItemTM;

import java.sql.SQLException;
import java.util.ArrayList;

public class DisplayAllItemFormController {
    public TableView tblItem;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colPackSize;
    public TableColumn colUnitPrice;
    public TableColumn colqtyOnHand;
    public TableColumn colDiscount;

    public void initialize(){
        try {
            //to display data in table
            colItemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
            colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            colPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
            colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
            colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
            colqtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));

            //call setItemsToTable() method passing data
            setItemsToTable(new ItemController().getAllItem());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //set data to table
    private void setItemsToTable(ArrayList<Item> items) {
        ObservableList<ItemTM> obList = FXCollections.observableArrayList();
        items.forEach(e->{
            obList.add(new ItemTM(e.getCode(),e.getDescription(),e.getPackSize(),e.getUnitPrice(),e.getDiscount(),e.getQtyOnHand()));
        });
        tblItem.setItems(obList);
    }
}
