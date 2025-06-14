package Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class PurchasesDao {
    //INSTANCIAMOS LA CONEXION

    ConnectionMySQL cn = new ConnectionMySQL();

    //PREPARAMOS LAS VARIBLES PARA LA CONEXION
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    //REGISTRAR COMPRA
    public boolean registerPurchaseQuery(int supplier_id, int employee_id, double total) {

        String query = "INSERT INTO purchases  (supplier_id, employee_id, total, created)"
                + "VALUES (?, ?, ?, ?)";

        Timestamp dateTime = new Timestamp(new Date().getTime());

        try {

            conn = cn.getConnection();
            pst = conn.prepareStatement(query);

            pst.setInt(1, supplier_id);
            pst.setInt(2, employee_id);
            pst.setDouble(3, total);

            pst.setTimestamp(4, dateTime);

            pst.execute();

            return true;

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Error al registrar la compra" + e);

            return false;

        }
    }

    //REGISTRAR DETALLE DE LA COMPRA
    public boolean registerPurcheseDetailQuery(int purchase_id, double purchase_price, int purchase_amount, double purchase_subtotal, int product_id) {

        String query = "INSERT INTO purchase_details (purchase_id, purchase_price, purchase_amount, purchase_subtotal, product_id) "
                + "VALUES (?, ?, ?, ?, ?)";

        Timestamp dateTime = new Timestamp(new Date().getTime());

        try {

            conn = cn.getConnection();
            pst = conn.prepareStatement(query);

            pst.setInt(1, purchase_id);
            pst.setDouble(2, purchase_price);
            pst.setInt(3, purchase_amount);
            pst.setDouble(4, purchase_subtotal);

            pst.setInt(5, product_id);

            pst.execute();

            return true;

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Error al registrar los detalles de la compra" + e);

            return false;

        }

    }

    //OBTENER ID DE LA COMPRA
    public int purchaseId() {

        int id = 0;

        String query = "SELECT MAX(id) AS ID FROM purchases";

        try {

            conn = cn.getConnection();
            pst = conn.prepareStatement(query);

            rs = pst.executeQuery();

            if (rs.next()) {

                id = rs.getInt("id");

            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, e.getMessage());

        }

        return id;
    }

    //LISTAR TODAS LAS COMPRAS COMPRAS REALIZADAS
    public List listAllPuchassesQuery() {

        List<Purchases> list_purchases = new ArrayList();

        String query = "SELECT pu.*, su.name as supplier_name FROM purchases pu,"
                + "suppliers su WHERE pu.supplier_id = su.id ORDER BY pu.id ASC";

        try {

            conn = cn.getConnection();
            pst = conn.prepareStatement(query);

            rs = pst.executeQuery();

            while (rs.next()) {

                Purchases purchase = new Purchases();

                purchase.setId(rs.getInt("id"));
                purchase.setSupplier_name_product(rs.getString("supplier_name"));
                purchase.setTotal(rs.getDouble("total"));
                purchase.setCreated(rs.getString("created"));

                list_purchases.add(purchase);
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, e.getMessage());

        }

        return list_purchases;

    }

    //LISTAR LAS COMPRAS DE LAS FACTURAS
    public List listPurchasesDetailQuery(int id) {

        List<Purchases> list_purchases = new ArrayList();

        String query = "SELECT  pu.created,"
                + "		pude.purchase_price, pude.purchase_amount, pude.purchase_subtotal,"
                + "        su.name as supplier_name, "
                + "        pro.name as product_name, "
                + "        em.full_name"
                + "        FROM purchases pu "
                + "        INNER JOIN purchase_details pude ON pu.id = pude.purchase_id "
                + "        INNER JOIN products pro ON pude.product_id = pro.id "
                + "        INNER JOIN suppliers su ON pu.supplier_id = su.id"
                + "        INNER JOIN employees em ON pu.employee_id = em.id "
                + "        WHERE pu.id = ?";
        
         try {

            conn = cn.getConnection();
            pst = conn.prepareStatement(query);

            pst.setInt(1, id);

            rs = pst.executeQuery();

             while (rs.next()) {
                 
                 Purchases purchases = new Purchases();
                 
                 purchases.setProduct_name(rs.getString("product_name"));
                 purchases.setPurchases_amount(rs.getInt("purchase_amount"));
                 purchases.setPurchase_price(rs.getDouble("purchase_price"));
                 purchases.setPurchase_subtotal(rs.getDouble("purchase_subtotal"));
                 purchases.setSupplier_name_product(rs.getString("supplier_name"));
                 purchases.setCreated(rs.getString("created"));
                 purchases.setPurchase(rs.getString("full_name"));
                 
                 list_purchases.add(purchases);
                 
             }
        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, e.getMessage());

        }

        return list_purchases;
    }

    public DefaultTableModel getModel() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
