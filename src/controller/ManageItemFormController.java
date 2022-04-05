package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Details;
import view.tm.DetailsTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ManageItemFormController {

    public AnchorPane manageItemContext;
    public Label lblStock;
    public ComboBox<String> cmbItemCode;
    public Label lblDescription;
    public TextField txtMostItem;
    public TextField txtLessItem;
    public Label lblDate;
    public Label lblTotal;
    public TableView tblTodayIncome;
    public TableColumn colOrderId;
    public TableColumn colCustomerId;
    public TableColumn colOrderTime;
    public TableColumn colCost;

    public void initialize(){
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colOrderTime.setCellValueFactory(new PropertyValueFactory<>("orderTime"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
      try {
          setCodes();
          setDate();

          setData();
          setCost();

      } catch (SQLException throwables) {
          throwables.printStackTrace();
      } catch (ClassNotFoundException e) {
          e.printStackTrace();
      }

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {

                showStock((String) newValue);
                getDescription((String) newValue);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
  }

    private void setCost() throws SQLException, ClassNotFoundException {
        String cost = new OrderController().findCost(lblDate.getText());
        lblTotal.setText(cost+" /=");
    }

    private void setData() throws SQLException, ClassNotFoundException {
        String date = lblDate.getText();
        ArrayList<Details> items =new OrderController().setTodayData(date);
        ObservableList<DetailsTM> obList = FXCollections.observableArrayList();
        items.forEach(e->{
            obList.add(new DetailsTM(e.getOrderId(),e.getCustomerId(),e.getOrderDate(),e.getOrderTime(),e.getCost()));
        });
        tblTodayIncome.setItems(obList);
    }

    private void setDate() {
        //load date
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));
    }

    private void getDescription(String code) throws SQLException, ClassNotFoundException {
        String des = new ItemController().finddes(code);
        lblDescription.setText(des);
    }

    private void setCodes() throws SQLException, ClassNotFoundException {
        List<String> itemIds = new ItemController().getItemIds();
        cmbItemCode.getItems().addAll(itemIds);
    }

    public  void showStock(String code) throws SQLException, ClassNotFoundException {
        int qty = new ItemController().findqty(code);
        if (qty != 0) {
            lblStock.setText("Stock In");
        }else {
            lblStock.setText("Stock Out");
        }
    }

    public void searchMostMovableItem(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String item = new OrderController().mostMovableItems();
        txtMostItem.setText(item);
    }

    public void searchLessMovableItem(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String item = new OrderController().leastMovableItems();
        txtLessItem.setText(item);
    }

    public void openSaveItemFormOnAction(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/SaveItemForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void openSearchItemFormOnAction(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/SearchItemForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void openUpdateItemFormOnAction(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/UpdateItemForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void openDeleteItemFormOnAction(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/DeleteItemForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void openDisplayAllItemFormOnAction(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/DisplayAllItemForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void homePageOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/DashBoardForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) manageItemContext.getScene().getWindow();
        window.setScene(new Scene(load));
    }

    public void openReportsFormOnAction(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/ReportsForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
