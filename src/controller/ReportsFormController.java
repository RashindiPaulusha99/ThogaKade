package controller;

import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Details;
import view.tm.DetailsTM;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ReportsFormController {

    public Label lblTotal;
    public TableView<DetailsTM> tblIncomeDetails;
    public TableColumn colOrderId;
    public TableColumn colCustomerId;
    public TableColumn colOrderTime;
    public TableColumn colCost;
    public JFXDatePicker DatePicker;

    public void initialize()  {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colOrderTime.setCellValueFactory(new PropertyValueFactory<>("orderTime"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        DatePicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LocalDate date= DatePicker.getValue();
                try {
                    setCost(date);
                    setData(date);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setCost(LocalDate date) throws SQLException, ClassNotFoundException {
        String cost = new OrderController().findCost(String.valueOf(date));
        lblTotal.setText(cost+" /=");
    }

    private void setData(LocalDate date) throws SQLException, ClassNotFoundException {
       ArrayList<Details> items =new OrderController().setTodayData(String.valueOf(date));
        ObservableList<DetailsTM> obList = FXCollections.observableArrayList();
        items.forEach(e->{
            obList.add(new DetailsTM(e.getOrderId(),e.getCustomerId(),e.getOrderDate(),e.getOrderTime(),e.getCost()));
        });
        tblIncomeDetails.setItems(obList);
    }
}
