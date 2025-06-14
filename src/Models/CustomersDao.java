package Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class CustomersDao {

    //INSTANCIAMOS LA CONEXION
    ConnectionMySQL cn = new ConnectionMySQL();

    //PREPARAMOS LAS VARIBLES PARA LA CONEXION
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    
    //REGISTRAR CLIENTE
    public boolean registerCustomersQuery(Customers customers){
        
        String query = "INSERT INTO customers (id, full_name, address, telephone, email, created, updated) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        Timestamp dateTime = new Timestamp(new Date().getTime());
        
        try {

            conn = cn.getConnection();
            pst = conn.prepareStatement(query);

            pst.setInt(1, customers.getId());
            pst.setString(2, customers.getFull_name());
            pst.setString(3, customers.getAddress());
            pst.setString(4, customers.getTelephone());
            pst.setString(5, customers.getEmail());
            pst.setTimestamp(6, dateTime);
            pst.setTimestamp(7, dateTime);

            pst.execute();

            return true;

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Error al registrar al cliente" + e);

            return false;

        }
    }
    
    //LISTAR CLIENTES
    public List listCustomerQuery(String value){
    
        List<Customers> list_customers = new ArrayList();
        
        String query = "SELECT * FROM customers";
        String query_search_customers =  "SELECT * FROM customers WHERE id LIKE'%" + value + "%'";
        
        try {

            conn = cn.getConnection();

            if (value.equalsIgnoreCase("")) {
                pst = conn.prepareStatement(query);
                rs = pst.executeQuery();
            } else {
                pst = conn.prepareStatement(query_search_customers);
                rs = pst.executeQuery();
            }

            while (rs.next()) {

                Customers customers = new Customers();

                customers.setId(rs.getInt("id"));
                customers.setFull_name(rs.getString("full_name"));
                customers.setAddress(rs.getString("address"));
                customers.setTelephone(rs.getString("telephone"));
                customers.setEmail(rs.getString("email"));

                list_customers.add(customers);

            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Error al buscar al cliente" + e);

        }

        return list_customers;
    }

    //MODIFICAR CLIENTE
    public boolean updateCustomersQuery(Customers customers){
    
        String query = "UPDATE customers SET full_name = ?, "
                + "address = ?, telephone = ?, email = ?, updated = ?"
                + "WHERE id = ?";

        Timestamp dateTime = new Timestamp(new Date().getTime());

        try {

            conn = cn.getConnection();
            pst = conn.prepareStatement(query);

            pst.setString(1, customers.getFull_name());
            pst.setString(2, customers.getAddress());
            pst.setString(3, customers.getTelephone());
            pst.setString(4, customers.getEmail());
            pst.setTimestamp(5, dateTime);
            pst.setInt(6, customers.getId());

            pst.execute();

            return true;

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Error al modificar al cliente" + e);

            return false;

        }
    } 
    
    public Customers searchCustomer(int id){
        String query = "SELECT cu.id, cu.full_name FROM customers cu WHERE cu.id = ?";
        Customers customer = new Customers();
        
        try {
            conn = cn.getConnection();

            pst = conn.prepareStatement(query);

            pst.setInt(1, id);

            rs = pst.executeQuery();
            
            if(rs.next()){
            
                customer.setId(rs.getInt("id"));
                customer.setFull_name(rs.getString("full_name"));
                
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, e.getMessage());
            
        }
        return customer;

    }

     //ELIMINAR CLIENTE
    public boolean deleteCustomersQuery(int id) {

        String query = "DELETE FROM customers WHERE id = " + id;

        try {

            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.execute();

            return true;

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Error al eliminar al cliente, debido a que tiene relacion con otra tabla" + e);

            return false;

        }

    }

}
