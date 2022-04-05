package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import model.*;
import view.tm.CartTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlaceOrderFormController {

    public Label lblOrderId;
    public Label lblOrderDate;
    public Label lblOrderTime;
    public Label lblTotal;
    public AnchorPane placeOrderContext;
    
    public JFXComboBox<String> cmbCustomerId;
    public TextField txtCustomerTitle;
    public TextField txtCustomerName;
    public TextField txtCustomerAddress;
    public TextField txtCity;
    public TextField txtProvince;
    public TextField txtPostalCode;
    
    public JFXComboBox<String> cmbItemCode;
    public TextField txtDescription;
    public TextField txtPackSize;
    public TextField txtUnitPrice;
    public TextField txtDiscount;
    public TextField txtqtyOnHand;
    public TextField txtQTY;
    
    public JFXButton btnAddToCart;
    
    public TableView<CartTM> tblItemDetail;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colQTY;
    public TableColumn colUnitPrice;
    public TableColumn colDiscount;
    public TableColumn colSave;
    public TableColumn colFinalPrice;

    int cartSelectedRowForRemove = -1;

    public void initialize(){
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQTY.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colSave.setCellValueFactory(new PropertyValueFactory<>("save"));
        colFinalPrice.setCellValueFactory(new PropertyValueFactory<>("total"));

        //call date & time
        loadDateAndTime();
        //call method to set orderId
        setOrderId();

        try {
            //call customer ids
            loadCustomerIds();
            //call item ids
            loadItemIds();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //create listeners for customers
       cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {

                setCustomerData(newValue);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        //create listeners for items
       cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {

                setItemData(newValue);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        tblItemDetail.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            cartSelectedRowForRemove= (int) newValue;//selected row put to this variable
        });
    }

    private void loadDateAndTime() {
        //load date
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblOrderDate.setText(f.format(date));

        //load time
        Timeline time  = new Timeline(new KeyFrame(Duration.ZERO, e->{
            LocalTime currentTime = LocalTime.now();
            lblOrderTime.setText(currentTime.getHour()+" : "+currentTime.getMinute()+" : "+currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }

    private void setOrderId() {
        try {
            //set orderId to label
            lblOrderId.setText(new OrderController().getOrderId());
            //lblOrderId1.setText(new OrderController().getOrderId());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //load customerids to combobox
    private void loadCustomerIds() throws SQLException, ClassNotFoundException {
        List<String> customerIds = new CustomerController().getCustomerIds();
        cmbCustomerId.getItems().addAll(customerIds);
    }

    //set relevant customer to textFields
    private void setCustomerData(String customerId) throws SQLException, ClassNotFoundException {
        Customer c1 = new CustomerController().searchCustomer(customerId);
        if (c1 == null){
            new Alert(Alert.AlertType.WARNING,"Empty Result Set.");
        }else {
            txtCustomerTitle.setText(c1.getTitle());
            txtCustomerName.setText(c1.getName());
            txtCustomerAddress.setText(c1.getAddress());
            txtCity.setText(c1.getCity());
            txtProvince.setText(c1.getProvince());
            txtPostalCode.setText(c1.getPostalCode());
        }
    }

    //load itemids to combobox
    private void loadItemIds() throws SQLException, ClassNotFoundException {
        List<String> itemIds = new ItemController().getItemIds();
        cmbItemCode.getItems().addAll(itemIds);
    }

    //set relevant item to textFields
    private void setItemData(String itemCode) throws SQLException, ClassNotFoundException {
        Item i1 = new ItemController().getItem(itemCode);
        if (i1 == null){
            new Alert(Alert.AlertType.WARNING,"Empty Result Set.");
        }else {
            txtDescription.setText(i1.getDescription());
            txtPackSize.setText(i1.getPackSize());
            txtqtyOnHand.setText(String.valueOf(i1.getQtyOnHand()));
            txtUnitPrice.setText(String.valueOf(i1.getUnitPrice()));
            txtDiscount.setText(String.valueOf(i1.getDiscount()));
        }
    }

    private int isExists(CartTM tm){
        for (int i = 0; i < obList.size(); i++) {
            //if selected item code in row equals to next item code,return relevant index
            if (tm.getCode().equals(obList.get(i).getCode())){
                return i;
            }
        }//if else return -1
        return -1;
    }

    //calculate whole cost and discount
    double allsave = 0;
    void calculateCost(){
        double cost = 0;

        for (CartTM tm : obList) {
            cost+=tm.getTotal();
            allsave+=tm.getSave();
        }
        lblTotal.setText(cost+" /=");
    }

    //create observable list to put data to table
    ObservableList<CartTM> obList = FXCollections.observableArrayList();

    public void addToCartOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        //data od added cart put table row
        String description = txtDescription.getText();
        String packSize = txtPackSize.getText();
        int qtyOnHand = Integer.parseInt(txtqtyOnHand.getText());
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        double discount = Double.parseDouble(txtDiscount.getText());
        int qtyForCustomer =  Integer.parseInt(txtQTY.getText());
        double save = (unitPrice / 100 * discount);
        double total = qtyForCustomer * (unitPrice - save);//total of taken amount

        if(qtyOnHand < qtyForCustomer || qtyForCustomer < 0){
            new Alert(Alert.AlertType.WARNING,"Invalid QTY.").show();
            return;
        }

        //add data to CartTM class
        CartTM tm = new CartTM(
                cmbItemCode.getValue(),
                description,
                qtyForCustomer,
                unitPrice,
                discount,
                save * qtyForCustomer,
                total
        );

        int rowNumber = isExists(tm);

        if(rowNumber == -1){//-1 means , no one
            //add new value
            obList.add(tm);
        }else {
            //update
            CartTM temp = obList.get(rowNumber);

            CartTM newTm = new CartTM(
                    temp.getCode(),
                    temp.getDescription(),
                    temp.getQty() + qtyForCustomer,
                    unitPrice,
                    discount,
                    temp.getSave()*qtyForCustomer+temp.getSave(),
                    total + temp.getTotal()
            );

            if ((qtyOnHand - qtyForCustomer) < temp.getQty()){
                new Alert(Alert.AlertType.WARNING,"Invalid QTY.").show();
                return;
            }

            obList.remove(rowNumber);
            obList.add(newTm);
        }

        tblItemDetail.setItems(obList);
        calculateCost();
    }

    public void clearOnAction(ActionEvent actionEvent) {
        if (cartSelectedRowForRemove == -1){
            new Alert(Alert.AlertType.WARNING,"Please Select A Row.").show();
        }else {
            obList.remove(cartSelectedRowForRemove);
            calculateCost();
            tblItemDetail.refresh();
        }
    }

    double ttotal = 0;//whole cost of order
    double allDiscount = 0;//whole discount of order
    Order order = null;

    //save data to itemdetails table and order table in database
    public void placeOrderOnAction(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        setOrderId();

        ArrayList<ItemDetails> items = new ArrayList<>();//create arraylist to put items

        for (CartTM tempTm : obList) {
            ttotal+=tempTm.getTotal();
            allDiscount+=tempTm.getSave();
            //add all table details to arraylist
            items.add(new ItemDetails(
                    tempTm.getCode(),
                    tempTm.getUnitPrice(),
                    tempTm.getDiscount(),
                    tempTm.getQty()
            ));
        }

        //create order object
        order = new Order(
                lblOrderId.getText(),
                cmbCustomerId.getValue(),
                lblOrderDate.getText(),
                lblOrderTime.getText(),
                ttotal,
                items
        );

        if (new OrderController().placeOrder(order)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Success.").showAndWait();
            setOrderId();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/PaymentOptionForm.fxml"));
            Parent parent = loader.load();
            PaymentOptionFormController controller = loader.<PaymentOptionFormController>getController();
            controller.setData(order);
            Stage window = (Stage) placeOrderContext.getScene().getWindow();
            window.setScene(new Scene(parent));
        }else {
            new Alert(Alert.AlertType.WARNING,"Try Again").show();
        }
    }
}
