package controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Table;
import view.tm.TableTM;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchOrderFormController {
    public JFXComboBox<String> cmbOid;
    public TableView tblOrderDetails;
    public TableColumn colCode;
    public TableColumn colPrice;
    public TableColumn colQTY;
    public TableColumn colDiscount;
    public TableColumn colAmount;
    public TableColumn colOId;
    public TableColumn colCId;
    public TableColumn colOTime;
    public TableColumn colODate;

    String id =null;

    public void initialize() throws SQLException, ClassNotFoundException {
        colOId.setCellValueFactory(new PropertyValueFactory<>("OrderId"));
        colCId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colODate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colOTime.setCellValueFactory(new PropertyValueFactory<>("orderTime"));
        colCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colQTY.setCellValueFactory(new PropertyValueFactory<>("sellQty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("UPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("cost"));

        setOrderDetailsToTable(new OrderController().getDetails());

        try {

            loadOrderIds();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        cmbOid.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            try {

                deleteDetails(newValue);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            id=newValue;

        });
    }

   private void deleteDetails(String orderId) throws SQLException, ClassNotFoundException {
        if (new OrderController().deleteOrder(orderId)){
            new Alert(Alert.AlertType.CONFIRMATION, "Success.").show();
        }else {
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }

    private void setOrderDetailsToTable(ArrayList<Table> items) {
        ObservableList<TableTM> obList = FXCollections.observableArrayList();
        items.forEach(e->{
            obList.add(new TableTM(e.getOrderId(),e.getCustomerId(),e.getOrderDate(),e.getOrderTime(),e.getItemCode(),e.getSellQty(),e.getUPrice(),e.getDiscount(),e.getCost()));
        });
        tblOrderDetails.setItems(obList);
    }

    private void loadOrderIds() throws SQLException, ClassNotFoundException {
        List<String> orderId = new OrderController().getOIds();
        cmbOid.getItems().addAll(orderId);
    }

    public void deleteOrder(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (new OrderController().deleteOrder(id)){
            new Alert(Alert.AlertType.CONFIRMATION, "Success.").showAndWait();
            tblOrderDetails.refresh();
        }else {
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }
}
