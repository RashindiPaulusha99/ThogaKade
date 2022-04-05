package controller;

import db.DbConnection;
import model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemController implements ItemService{

    public List<String> getItemIds() throws SQLException, ClassNotFoundException {
        //take item ids from database
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Item").executeQuery();
        List<String> ids = new ArrayList<>();
        while (rst.next()){
            ids.add(rst.getString(1));
        }
        return ids;
    }

    public Item getItem(String code) throws SQLException, ClassNotFoundException {
        //take item details from database
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Item WHERE code ='" + code + "'").executeQuery();
        if (rst.next()){
            return new Item(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(6),
                    rst.getString(3),
                    rst.getDouble(4),
                    rst.getDouble(5)
            );
        }else {
            return null;
        }
    }

    @Override
    public boolean saveItem(Item i) throws SQLException, ClassNotFoundException {
        //save new item in database
        Connection con = DbConnection.getInstance().getConnection();
        String query="INSERT INTO Item VALUES(?,?,?,?,?,?)";

        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,i.getCode());
        stm.setObject(2,i.getDescription());
        stm.setObject(3,i.getPackSize());
        stm.setObject(4,i.getUnitPrice());
        stm.setObject(5,i.getDiscount());
        stm.setObject(6,i.getQtyOnHand());

        return stm.executeUpdate()>0;
    }

    @Override
    public Item searchItem(String code) throws SQLException, ClassNotFoundException {
        //search relevant item
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Item WHERE code=?");
        stm.setObject(1, code);
        ResultSet rst = stm.executeQuery();
        if (rst.next()){
            //return relevant item
            return new Item(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(6),
                    rst.getString(3),
                    rst.getDouble(4),
                    rst.getDouble(5)
            );
        } else {
            //if data didn't come return null
            return null;
        }
    }

    @Override
    public boolean updateItem(Item i) throws SQLException, ClassNotFoundException {
        //update items's details
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Item SET description=?,packSize=?,unitPrice=?,qtyOnHand=?,discount=? WHERE code=?");
        stm.setObject(1,i.getDescription());
        stm.setObject(2,i.getPackSize());
        stm.setObject(3,i.getUnitPrice());
        stm.setObject(4,i.getQtyOnHand());
        stm.setObject(5,i.getDiscount());
        stm.setObject(6,i.getCode());

        return stm.executeUpdate()>0;
    }

    @Override
    public boolean deleteItem(String code) throws SQLException, ClassNotFoundException {
        if (DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Item WHERE code='"+code+"'").executeUpdate()>0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public ArrayList<Item> getAllItem() throws SQLException, ClassNotFoundException {
        //take data that should load to table
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Item");
        ResultSet rst = stm.executeQuery();
        ArrayList<Item> items = new ArrayList<>();
        while (rst.next()) {
            items.add(new Item(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(6),
                    rst.getString(3),
                    rst.getDouble(4),
                    rst.getDouble(5)
            ));
        }
        //return data
        return items;
    }

    public int findqty(String id) throws SQLException, ClassNotFoundException {
        //search relevant item
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Item WHERE code=?");
        stm.setObject(1, id);
        ResultSet rst = stm.executeQuery();
        if (rst.next()){
            //return relevant index
            return rst.getInt(6);
        } else {
            //if data didn't come return -1
            return -1;
        }
    }

    public String finddes(String id) throws SQLException, ClassNotFoundException {
        //search relevant item
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Item WHERE code=?");
        stm.setObject(1, id);
        ResultSet rst = stm.executeQuery();
        if (rst.next()){
            //return relevant index
            return rst.getString(2);
        } else {
            //if data didn't come return null
            return null;
        }
    }

    public boolean search(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Item WHERE code='"+id+"'");
        ResultSet rst = stm.executeQuery();
        return rst.next();
    }

}
