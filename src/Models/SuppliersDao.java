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

public class SuppliersDao {

    //INSTANCIAMOS LA CONEXION
    ConnectionMySQL cn = new ConnectionMySQL();

    //PREPARAMOS LAS VARIBLES PARA LA CONEXION
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    //REGISTRAR PROVEEDOR
    public boolean registerSuppliersQuery(Suppliers suppliers) {

        String query = "INSERT INTO suppliers (name, description, address, telephone, email, city, created, updated) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Timestamp dateTime = new Timestamp(new Date().getTime());

        try {

            conn = cn.getConnection();
            pst = conn.prepareStatement(query);

            pst.setString(1, suppliers.getName());
            pst.setString(2, suppliers.getDescription());
            pst.setString(3, suppliers.getAddress());
            pst.setString(4, suppliers.getTelephone());
            pst.setString(5, suppliers.getEmail());
            pst.setString(6, suppliers.getCity());

            pst.setTimestamp(7, dateTime);
            pst.setTimestamp(8, dateTime);

            pst.execute();

            return true;

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Error al registrar al proveedor" + e);

            return false;

        }
    }

    //LISTAR PROVEEDOR
    public List listSuppliersQuery(String value) {

        List<Suppliers> list_suppliers = new ArrayList();

        String query = "SELECT * FROM suppliers";
        String query_search_suppliers = "SELECT * FROM suppliers WHERE name LIKE'%" + value + "%'";

        try {

            conn = cn.getConnection();

            if (value.equalsIgnoreCase("")) {
                pst = conn.prepareStatement(query);
                rs = pst.executeQuery();
            } else {
                pst = conn.prepareStatement(query_search_suppliers);
                rs = pst.executeQuery();
            }

            while (rs.next()) {

                Suppliers suppliers = new Suppliers();

                suppliers.setId(rs.getInt("id"));
                suppliers.setName(rs.getString("name"));
                suppliers.setDescription(rs.getString("description"));
                suppliers.setAddress(rs.getString("address"));
                suppliers.setTelephone(rs.getString("telephone"));
                suppliers.setEmail(rs.getString("email"));
                suppliers.setCity(rs.getString("city"));

                list_suppliers.add(suppliers);

            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Error al buscar al proveedor" + e);

        }

        return list_suppliers;

    }

    //MODIFICAR PROVEEDOR
    public boolean updateSuppliersQuery(Suppliers suppliers) {

        String query = "UPDATE suppliers SET name = ?, description = ?, telephone = ?, address = ?,  email = ?, city = ?, updated = ? WHERE id = ?";

        Timestamp dateTime = new Timestamp(new Date().getTime());

        try {

            conn = cn.getConnection();
            pst = conn.prepareStatement(query);

            pst.setString(1, suppliers.getName());
            pst.setString(2, suppliers.getDescription());
            pst.setString(3, suppliers.getTelephone());
            pst.setString(4, suppliers.getAddress());
            pst.setString(5, suppliers.getEmail());
            pst.setString(6, suppliers.getCity());
            pst.setTimestamp(7, dateTime);
            pst.setInt(8, suppliers.getId());

            pst.execute();

            return true;

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Error al modificar al proveedor" + e);

            return false;

        }
    }

    //ELIMINAR PROVEEDOR
    public boolean deleteSuppliersQuery(int id) {

        String query = "DELETE FROM suppliers WHERE id = " + id;

        try {

            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.execute();

            return true;

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Error al eliminar al proveedor, debido a que tiene relacion con otra tabla" + e);

            return false;

        }

    }
}
