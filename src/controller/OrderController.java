package controller;

import db.DbConnection;
import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderController {

    //generate orderId
    public String getOrderId() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT orderId FROM `Order` ORDER BY orderId DESC LIMIT 1").executeQuery();
        if (rst.next()){
            //if data has in database ,split orderId
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            tempId = tempId+1;

            if (tempId <= 9){
                return "O-00"+tempId;
            }else if (tempId <= 99){
                return "O-0"+tempId;
            }else {
                return "O-"+tempId;
            }
        }else {
            //if no data in database
            return "O-001";
        }
    }

    public List<String> getOIds() throws SQLException, ClassNotFoundException {
        //take customer ids from database
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `Order`").executeQuery();
        List<String> oIds = new ArrayList<>();
        while (rst.next()){
            oIds.add(rst.getString(1));
        }
        return oIds;
    }

    //data set to tables in database
    public boolean placeOrder(Order order){
        //save order
        Connection con = null;
        try {
            //transaction-----------
            con = DbConnection.getInstance().getConnection();
            con.setAutoCommit(false);//stop put data to table in little time
            //--------------------------

            PreparedStatement stm = con.prepareStatement("INSERT INTO `Order` VALUES(?,?,?,?,?)");
            stm.setObject(1,order.getOrderId());
            stm.setObject(2,order.getCustomerId());
            stm.setObject(3,order.getOrderDate());
            stm.setObject(4,order.getOrderTime());
            stm.setObject(5,order.getCost());

            if(stm.executeUpdate()>0){//if data save
                if(saveOrderDetails(order.getOrderId(),order.getItems())) {
                    con.commit();//three tables update
                    return true;
                }else {
                    con.rollback();//resend data bundle
                    return false;
                }
            }else {
                con.rollback();
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {

                con.setAutoCommit(true);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return true;
    }

    private boolean saveOrderDetails(String orderId , ArrayList<ItemDetails> items) throws SQLException, ClassNotFoundException {
        //data pass itemDetails table
        for (ItemDetails temp : items) {
            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO `orderdetail` VALUES(?,?,?,?,?)");
            stm.setObject(1,temp.getItemCode());
            stm.setObject(2,(temp.getDiscount()*temp.getQtyForSell()));
            stm.setObject(3,orderId);
            stm.setObject(4,temp.getUnitPrice());
            stm.setObject(5,temp.getQtyForSell());

            if(stm.executeUpdate()>0){

                if (updateQty(temp.getItemCode(),temp.getQtyForSell())){
                    return true;
                }else {
                    return false;
                }
            }else {
                return false;
            }
        }return true;

    }

    //modify item qty
    private  boolean updateQty(String itemCode, int qty) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Item SET qtyOnHand =(qtyOnHand - " + qty + " ) WHERE code='" + itemCode + "'");
        return stm.executeUpdate()>0;
    }

    public List<String> searchCustomerId(String oid) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `Order`  WHERE orderId='" + oid + "'");
        ResultSet rst = stm.executeQuery();
        List<String> order = new ArrayList<>();
            while (rst.next()){
                    order.add(rst.getString(2));
                    order.add(rst.getString(3));
                    order.add(rst.getString(4));
                    order.add(String.valueOf(rst.getDouble(5)));

            }
            return order;
    }

    public List<String> searchOrderDetails(String oid) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `orderdetail`  WHERE orderId='" + oid + "'");
        ResultSet rst = stm.executeQuery();
        List<String> item = new ArrayList<>();
        while (rst.next()){
            item.add(rst.getString(1));
            item.add(rst.getString(3));
            item.add(rst.getString(4));
            item.add(String.valueOf(rst.getDouble(5)));

        }
        return item;
    }

    public List<String> searchOrderDetail(String oid) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `orderdetail`  WHERE orderId='" + oid + "'");
        ResultSet rst = stm.executeQuery();
        List<String> orderDetails = new ArrayList<>();
        while (rst.next()){
            orderDetails.add(rst.getString(2));
            orderDetails.add(String.valueOf(rst.getDouble(3)));
            orderDetails.add(String.valueOf(rst.getDouble(4)));
            orderDetails.add(String.valueOf(rst.getInt(5)));
        }
            return orderDetails;

    }

    public ArrayList<Table> getDetails() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(" SELECT o.orderId,o.cId,o.orderDate,o.ordertime,c.itemCode,c.qty,c.price,c.discount,o.cost FROM `Order` o LEFT JOIN `orderdetail` c ON o.orderId=c.orderId");
        ResultSet rst = stm.executeQuery();
        ArrayList<Table> items = new ArrayList<>();
        while (rst.next()) {
            items.add(new Table(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getInt(6),
                    rst.getDouble(7),
                    rst.getDouble(8),
                    rst.getDouble(9)
            ));
        }
        //return data
        return items;
    }

    public boolean deleteOrder(String id) throws SQLException, ClassNotFoundException {
        if (DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM `Order` WHERE orderId='"+id+"'").executeUpdate()>0){
            return true;
        }else{
            return false;
        }
    }

    public String mostMovableItems() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(" SELECT itemCode ,COUNT(qty) FROM `orderdetail` GROUP BY (itemCode) ORDER BY qty DESC LIMIT 1");
        ResultSet rst = stm.executeQuery();
        if (rst.next()){
            return rst.getString(1);
        }
        return null;
    }

    public String leastMovableItems() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(" SELECT itemCode ,COUNT(qty) FROM `orderdetail` GROUP BY (itemCode) ORDER BY qty ASC LIMIT 1");
        ResultSet rst = stm.executeQuery();
       if (rst.next()){
            return rst.getString(1);
        }
        return null;
    }

    public ArrayList<Details> setTodayData(String date) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `Order` WHERE orderDate=?");
        stm.setObject(1, date);
        ResultSet rst = stm.executeQuery();
        ArrayList<Details> items = new ArrayList<>();
        while (rst.next()) {
            items.add(new Details(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDouble(5)

            ));
        }
        //return data
        return items;
    }

    public String findCost(String date) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT SUM(cost) FROM `Order` WHERE orderDate=?");
        stm.setObject(1, date);
        ResultSet rst = stm.executeQuery();
        if (rst.next()){
            return rst.getString(1);
        }else {
            return null;
        }
    }

    public ArrayList<Bill> setBill(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `orderdetail` WHERE orderId=?");
        stm.setObject(1, id);
        ResultSet rst = stm.executeQuery();
        ArrayList<Bill> items = new ArrayList<>();
        while (rst.next()) {
            items.add(new Bill(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getDouble(4),
                    rst.getDouble(5)

            ));
        }
        //return data
        return items;
    }
}
