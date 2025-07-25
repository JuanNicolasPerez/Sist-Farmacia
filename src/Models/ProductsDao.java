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

public class ProductsDao {

    //INSTANCIAMOS LA CONEXION
    ConnectionMySQL cn = new ConnectionMySQL();

    //PREPARAMOS LAS VARIBLES PARA LA CONEXION
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    //REGISTRA PRODUCTOS
    public boolean registrarProductsQuery(Products products) {

        String query = "INSERT INTO products (code, name, description, unit_price, created, updated, category_id)"
                + "VALUES(?, ?, ?, ?, ?, ?, ?)";

        Timestamp dateTime = new Timestamp(new Date().getTime());

        try {

            conn = cn.getConnection();
            pst = conn.prepareStatement(query);

            pst.setInt(1, products.getCode());
            pst.setString(2, products.getName());
            pst.setString(3, products.getDescription());
            pst.setDouble(4, products.getUnit_price());

            pst.setTimestamp(5, dateTime);
            pst.setTimestamp(6, dateTime);

            pst.setInt(7, products.getCategory_id());

            pst.execute();

            return true;

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Error al registrar al proveedor" + e);

            return false;

        }

    }

    //LISTAR PRODUCTOS
    public List listProductsQuery(String value) {

        List<Products> list_products = new ArrayList();

        String query = "SELECT pro.*, ca.name AS category_name "
                + "FROM products pro, categories ca "
                + "WHERE pro.category_id = ca.id";

        String query_search_products = "SELECT pro.*, ca.name AS category_name "
                + "FROM products pro "
                + "INNER JOIN categories ca "
                + "ON  pro.category_id = ca.id "
                + "WHERE pro.name "
                + "LIKE '%" + value + "'";

        try {

            conn = cn.getConnection();

            if (value.equalsIgnoreCase("")) {
                pst = conn.prepareStatement(query);
                rs = pst.executeQuery();
            } else {
                pst = conn.prepareStatement(query_search_products);
                rs = pst.executeQuery();
            }

            while (rs.next()) {

                Products products = new Products();

                products.setId(rs.getInt("id"));
                products.setCode(rs.getInt("code"));
                products.setName(rs.getString("name"));
                products.setDescription(rs.getString("description"));
                products.setUnit_price(rs.getDouble("unit_price"));
                products.setProduct_quantify(rs.getInt("product_quantity"));
                products.setCategory_name(rs.getString("category_name"));

                list_products.add(products);

            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Error al buscar al proveedor" + e);

        }

        return list_products;
    }

    //MODIFICAR PRODUCTOS
    public boolean updateProductQuery(Products product) {

        String query = "UPDATE products SET code = ?, name = ?, description = ?, unit_price = ?, "
                + "updated = ?, category_id = ? WHERE id = ?";

        Timestamp dateTime = new Timestamp(new Date().getTime());

        try {

            conn = cn.getConnection();
            pst = conn.prepareStatement(query);

            pst.setInt(1, product.getCode());
            pst.setString(2, product.getName());
            pst.setString(3, product.getDescription());
            pst.setDouble(4, product.getUnit_price());

            pst.setTimestamp(5, dateTime);

            pst.setInt(6, product.getCategory_id());
            pst.setInt(7, product.getId());

            pst.execute();

            return true;

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Error al modificar al producto" + e);

            return false;

        }

    }

    //ELIMINAR PRODUCTOS
    public boolean deleteProductQuery(int id) {

        String query = "DELETE FROM products WHERE id = " + id;

        try {

            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.execute();

            return true;

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Error al eliminar al producto, debido a que tiene relacion con otra tabla" + e);

            return false;

        }

    }

    //BUSCAR PRODUCTO
    public Products searchProduct(int id) {

        String query = "SELECT pro.*, ca.name as category_name "
                + "FROM products pro "
                + "INNER JOIN categories ca "
                + "ON pro.category_id = ca.id "
                + "WHERE pro.id = ?";

        Products product = new Products();

        try {

            conn = cn.getConnection();
            pst = conn.prepareStatement(query);

            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {

                product.setId(rs.getInt("id"));
                product.setCode(rs.getInt("code"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setUnit_price(rs.getDouble("unit_price"));
                product.setCategory_id(rs.getInt("category_id"));
                product.setCategory_name(rs.getString("category_name"));

            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Error al buscar al producto." + e);

        }

        return product;
    }

    //BUSCAR PRODUCTO POR CODIGO
    public Products searchCode(int code) {

        String query = "SELECT pro.id, pro.name, pro.unit_price, pro.product_quantity "
                + "FROM products pro "
                + "WHERE pro.code = ?";

        Products product = new Products();

        try {

            conn = cn.getConnection();
            pst = conn.prepareStatement(query);

            pst.setInt(1, code);
            rs = pst.executeQuery();

            if (rs.next()) {

                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setUnit_price(rs.getDouble("unit_price"));
                product.setProduct_quantify(rs.getInt("product_quantity"));

            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Error al buscar al producto por codigo." + e);

        }

        return product;

    }

    //TRAER LA CANTIDAD DE PRODUCTOS
    public Products searchId(int id) {

        String query = "SELECT pro.product_quantity "
                + "FROM products pro "
                + "WHERE pro.id = ?";

        Products product = new Products();

        try {

            conn = cn.getConnection();
            pst = conn.prepareStatement(query);

            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {

                product.setProduct_quantify(rs.getInt("product_quantity"));

            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, e.getMessage());

        }

        return product;
    }

    //ACTUALIZAR STOCK
    public boolean updateStockQuery(int amount, int product_id) {

        String query = "UPDATE products "
                + "SET product_quantity = ? "
                + "WHERE id = ?";

        try {

            conn = cn.getConnection();
            pst = conn.prepareStatement(query);

            pst.setInt(1, amount);
            pst.setInt(2, product_id);

            pst.execute();

            return true;

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, e.getMessage());

            return false;

        }

    }

}
