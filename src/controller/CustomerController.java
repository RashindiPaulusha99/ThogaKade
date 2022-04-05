package controller;

import db.DbConnection;
import model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerController implements CustomerService{

    public List<String> getCustomerIds() throws SQLException, ClassNotFoundException {
        //take customer ids from database
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer").executeQuery();
        List<String> ids = new ArrayList<>();
        while (rst.next()){
            ids.add(rst.getString(1));
        }
        return ids;
    }

    @Override
    public boolean saveCustomer(Customer c) throws SQLException, ClassNotFoundException {
        //save new customer in database
        Connection con = DbConnection.getInstance().getConnection();
        String query="INSERT INTO Customer VALUES(?,?,?,?,?,?,?)";

        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,c.getId());
        stm.setObject(2,c.getTitle());
        stm.setObject(3,c.getName());
        stm.setObject(4,c.getAddress());
        stm.setObject(5,c.getCity());
        stm.setObject(6,c.getProvince());
        stm.setObject(7,c.getPostalCode());

        return stm.executeUpdate()>0;
    }

    @Override
    public Customer searchCustomer(String id) throws SQLException, ClassNotFoundException {
        //search relevant customer
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer WHERE id=?");
        stm.setObject(1, id);
        ResultSet rst = stm.executeQuery();
        if (rst.next()){
            //return relevant customer
            return new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            );
        } else {
            //if data didn't come, return null
            return null;
        }
    }

    @Override
    public boolean updateCustomer(Customer c) throws SQLException, ClassNotFoundException {
        //update customer's details
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Customer SET title=?,name=?,address=?,city=?,province=?,postalCode=? WHERE id=?");
        stm.setObject(1,c.getTitle());
        stm.setObject(2,c.getName());
        stm.setObject(3,c.getAddress());
        stm.setObject(4,c.getCity());
        stm.setObject(5,c.getProvince());
        stm.setObject(6,c.getPostalCode());
        stm.setObject(7,c.getId());

        return stm.executeUpdate()>0;
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        if (DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Customer WHERE id='"+id+"'").executeUpdate()>0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public ArrayList<Customer> getAllCustomers() throws SQLException, ClassNotFoundException {
        //take data that should load to table
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer");
        ResultSet rst = stm.executeQuery();
        ArrayList<Customer> customers = new ArrayList<>();
        while (rst.next()) {
            customers.add(new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            ));
        }
        //return data
        return customers;
    }

    public boolean search(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer WHERE id='"+id+"'");
        ResultSet rst = stm.executeQuery();
        return rst.next();
    }
}
