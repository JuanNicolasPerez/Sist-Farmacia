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

public class CategoriesDao {

    //INSTANCIAMOS LA CONEXION
    ConnectionMySQL cn = new ConnectionMySQL();

    //PREPARAMOS LAS VARIBLES PARA LA CONEXION
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    //REGISTRAR CATEGORIAS
    public boolean registerCategoriesQuery(Categories categories) {

        String query = "INSERT INTO categories (name, created, updated) "
                + "VALUES (?, ?, ?)";

        Timestamp dateTime = new Timestamp(new Date().getTime());

        try {

            conn = cn.getConnection();
            pst = conn.prepareStatement(query);

            pst.setString(1, categories.getName());
            pst.setTimestamp(2, dateTime);
            pst.setTimestamp(3, dateTime);

            pst.execute();

            return true;

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Error al registrar la categoria" + e);

            return false;

        }
    }

    //LISTAR CATEGORIAS
    public List listCategoriesQuery(String value) {

        List<Categories> list_categories = new ArrayList();

        String query = "SELECT * FROM categories";
        String query_search_categories = "SELECT * FROM categories WHERE name LIKE'%" + value + "%'";

        try {

            conn = cn.getConnection();

            if (value.equalsIgnoreCase("")) {
                pst = conn.prepareStatement(query);
                rs = pst.executeQuery();
            } else {
                pst = conn.prepareStatement(query_search_categories);
                rs = pst.executeQuery();
            }

            while (rs.next()) {

                Categories categories = new Categories();

                categories.setId(rs.getInt("id"));
                categories.setName(rs.getString("name"));

                list_categories.add(categories);

            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Error al buscar la categoria" + e);

        }

        return list_categories;
    }

    //MODIFICAR CATEGORIAS
    public boolean updateCategoriesQuery(Categories categories) {

        String query = "UPDATE categories SET name = ?, updated = ? WHERE id = ?";

        Timestamp dateTime = new Timestamp(new Date().getTime());

        try {

            conn = cn.getConnection();
            pst = conn.prepareStatement(query);

            pst.setString(1, categories.getName());
            pst.setTimestamp(2, dateTime);
            pst.setInt(3, categories.getId());

            pst.execute();

            return true;

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Error al modificar la categoria" + e);

            return false;

        }
    }

    //ELIMINAR CATEGORIAS
    public boolean deleteCategoriesQuery(int id) {

        String query = "DELETE FROM categories WHERE id = " + id;

        try {

            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.execute();

            return true;

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Error al eliminar la categoria debido a que tiene relacion con otra tabla" + e);

            return false;

        }

    }

}
