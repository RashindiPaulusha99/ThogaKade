package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Bill;
import view.tm.BillTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

public class FinalBillFormController {

    public Label lblPayment;
    public AnchorPane billContext;
    public Label lblBillDate;
    public Label lblBillTime;
    public Label lblBillId;
    public TableView<BillTM> tblBillDetails;
    public TableColumn colItemCode;
    public TableColumn colUnitPrice;
    public TableColumn colQTY;
    public TableColumn colAmount;
    public Label lblNetAmount;

    public void initialize(){
        //to display data in table
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colQTY.setCellValueFactory(new PropertyValueFactory<>("sellQty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("discount"));
    }

    public void setData(String oId, String date, String time, double total,String payment) throws SQLException, ClassNotFoundException {
        lblBillId.setText(oId);
        lblBillDate.setText(date);
        lblBillTime.setText(date);
        double amount = total;
        lblNetAmount.setText(String.valueOf(amount)+" /=");
        lblPayment.setText(payment);
        setData(oId);
    }

    private void setData(String oId) throws SQLException, ClassNotFoundException {
        ArrayList<Bill> data = new OrderController().setBill(oId);
        ObservableList<BillTM> obList = FXCollections.observableArrayList();
        data.forEach(e->{
            obList.add(new BillTM(e.getOrderId(),e.getItemCode(),e.getSellQty(),e.getPrice(),(e.getDiscount()*e.getSellQty())));
        });
        tblBillDetails.setItems(obList);
    }

    public void openPlaceOrderForm(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/PlaceOrderForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) billContext.getScene().getWindow();
        window.setScene(new Scene(load));
    }
}
