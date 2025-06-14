package Controllers;

import Models.Customers;
import Models.CustomersDao;
import Models.EmployeesDao;
import static Models.EmployeesDao.rol_user;
import Models.Products;
import Models.ProductsDao;
import Models.Sales;
import Models.SalesDao;
import Views.SystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class SalesController implements ActionListener, MouseListener, KeyListener {

    private Sales sale;
    private SalesDao saleDao;
    private SystemView views;

    Products product = new Products();
    ProductsDao productDao = new ProductsDao();

    Customers customer = new Customers();
    CustomersDao customersDao = new CustomersDao();

    private int item = 0;
    String rol = rol_user;

    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel temp;

    public SalesController(Sales sale, SalesDao saleDao, SystemView views) {
        this.sale = sale;
        this.saleDao = saleDao;
        this.views = views;

        this.views.btn_add_product_save.addActionListener(this);
        this.views.btn_confirm_sale.addActionListener(this);
        this.views.btn_remove_save.addActionListener(this);
        this.views.btn_new_save.addActionListener(this);
        this.views.txt_sale_product_code.addKeyListener(this);
        this.views.txt_sale_price.addKeyListener(this);
        this.views.txt_sale_customer_id.addKeyListener(this);
        this.views.txt_sale_quantity.addKeyListener(this);
        this.views.jLabelSales.addMouseListener(this);
        this.views.jLabelReports.addMouseListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == views.btn_confirm_sale) {

            insertSale();

        } else if (e.getSource() == views.btn_new_save) {

            cleanAllFieldsSales();
            cleanTableTemp();

        } else if (e.getSource() == views.btn_remove_save) {

            model = (DefaultTableModel) views.sales_table.getModel();
            model.removeRow(views.sales_table.getSelectedRow());

            calulateSales();

            views.txt_sale_product_code.requestFocus();

        } else if (!"".equals(e.getSource() == views.btn_add_product_save)) {

            int amount = Integer.parseInt(views.txt_sale_quantity.getText());
            String product_name = views.txt_sale_product_name.getText();
            double price = Double.parseDouble(views.txt_sale_price.getText());
            int sale_id = Integer.parseInt(views.txt_sale_product_id.getText());
            double subtotal = amount * price;
            int stock = Integer.parseInt(views.txt_sale_stock.getText());
            String full_name = views.txt_sale_customer_name.getText();

            if (stock >= amount) {

                item = item + 1;
                temp = (DefaultTableModel) views.sales_table.getModel();

                for (int i = 0; i < views.sales_table.getRowCount(); i++) {

                    if (views.sales_table.getValueAt(i, 1).equals(views.txt_sale_product_name.getText())) {

                        JOptionPane.showMessageDialog(null, "El producto ya esta registrado en la tabla de ventas");
                        return;

                    }

                }

                ArrayList list = new ArrayList();
                list.add(item);
                list.add(sale_id);
                list.add(product_name);
                list.add(amount);
                list.add(price);
                list.add(subtotal);
                list.add(full_name);

                Object[] obj = new Object[6];
                obj[0] = list.get(1);
                obj[1] = list.get(2);
                obj[2] = list.get(3);
                obj[3] = list.get(4);
                obj[4] = list.get(5);
                obj[5] = list.get(6);

                temp.addRow(obj);

                calulateSales();
                cleanFieldsSales();
                views.txt_sale_product_code.requestFocus();

            } else {

                JOptionPane.showMessageDialog(null, "Stock no disponible");

            }

        } else {

            JOptionPane.showMessageDialog(null, "Ingrese una cantidad");

        }

    }

    public void listAllSales() {

        if (rol.equals("Administrador")) {

            List<Sales> list = saleDao.listAllSalesQuery();
            model = (DefaultTableModel) views.table_all_sale.getModel();

            Object[] row = new Object[5];

            for (int i = 0; i < list.size(); i++) {

                row[0] = list.get(i).getId();
                row[1] = list.get(i).getCustomer_name();
                row[2] = list.get(i).getEmployee_name();
                row[3] = list.get(i).getTotal_to_pay();
                row[4] = list.get(i).getSale_date();

                model.addRow(row);

            }

            views.table_all_sale.setModel(model);
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getSource() == views.jLabelSales) {

            views.jTabbedPane1.setSelectedIndex(2);

        } else if (e.getSource() == views.jLabelReports) {

            if (rol.equals("Administrador")) {

                views.jTabbedPane1.setSelectedIndex(7);

                listAllSales();

            } else {

                views.jTabbedPane1.setEnabledAt(7, false);

                views.jLabelReports.setEnabled(false);

                JOptionPane.showMessageDialog(null, "No tienes privilegios de administrador para esta parte");

            }

        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getSource() == views.txt_sale_product_code) {

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                if (!"".equals(views.txt_sale_product_code.getText())) {

                    int code = Integer.parseInt(views.txt_sale_product_code.getText());

                    product = productDao.searchCode(code);

                    if (product.getName() != null) {

                        views.txt_sale_product_name.setText(product.getName());
                        views.txt_sale_product_id.setText("" + product.getId());
                        views.txt_sale_stock.setText("" + product.getProduct_quantify());
                        views.txt_sale_price.setText("" + product.getUnit_price());
                        views.txt_sale_quantity.requestFocus();

                    } else {

                        JOptionPane.showMessageDialog(null, "No existe ningun producto con ese codigo");
                        cleanFieldsSales();
                        views.txt_sale_product_code.requestFocus();

                    }

                } else {

                    JOptionPane.showMessageDialog(null, "Ingrese el codigo del producto a vender.");

                }

            }

        } else if (e.getSource() == views.txt_sale_customer_id) {

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                if (!"".equals(views.txt_sale_customer_id.getText())) {

                    int customer_id = Integer.parseInt(views.txt_sale_customer_id.getText());

                    customer = customersDao.searchCustomer(customer_id);

                    if (customer.getFull_name() != null) {

                        views.txt_sale_customer_name.setText("" + customer.getFull_name());

                    } else {

                        views.txt_sale_customer_id.setText("");

                        JOptionPane.showMessageDialog(null, "El cliente no existe");

                    }

                }

            }

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (e.getSource() == views.txt_sale_quantity) {

            int quantity;
            double price = Double.parseDouble(views.txt_sale_price.getText());

            if (views.txt_sale_quantity.getText().equals("")) {

                quantity = 1;

                views.txt_sale_price.setText("" + price);

            } else {

                quantity = Integer.parseInt(views.txt_sale_quantity.getText());
                price = Double.parseDouble(views.txt_sale_price.getText());
                views.txt_sale_subtotal.setText("" + (quantity * price));

            }

        }

    }

    private void insertSale() {

        int customer_id = Integer.parseInt(views.txt_sale_customer_id.getText());
        int employee_id = EmployeesDao.id_user;
        double total = Double.parseDouble(views.txt_sale_total_to_pay.getText());

        if (saleDao.registerSaleQuery(customer_id, employee_id, total)) {

            int sale_id = saleDao.saleId();

            for (int i = 0; i < views.sales_table.getRowCount(); i++) {

                int product_id = Integer.parseInt(views.sales_table.getValueAt(i, 0).toString());
                int sale_quantity = Integer.parseInt(views.sales_table.getValueAt(i, 2).toString());
                double sale_price = Double.parseDouble(views.sales_table.getValueAt(i, 3).toString());
                double sale_subtotal = sale_quantity * sale_price;

                saleDao.registerSaleDetailsQuery(product_id, sale_id, sale_quantity, sale_price, sale_subtotal);

                product = productDao.searchId(product_id);
                int amount = product.getProduct_quantify() - sale_quantity;
                productDao.updateStockQuery(amount, product_id);

            }

            JOptionPane.showMessageDialog(null, "Venta generada");
            cleanTableTemp();
            cleanAllFieldsSales();

        }

    }

    private void calulateSales() {

        double total = 0;
        int numRow = views.sales_table.getRowCount();

        for (int i = 0; i < numRow; i++) {

            total = total + Double.parseDouble(String.valueOf(views.sales_table.getValueAt(i, 4)));

        }

        views.txt_sale_total_to_pay.setText("" + total);

    }

    public void cleanTable() {

        for (int i = 0; i < model.getRowCount(); i++) {

            model.removeRow(i);

            i = i - 1;

        }

    }

    public void cleanTableTemp() {

        for (int i = 0; i < temp.getRowCount(); i++) {

            temp.removeRow(i);

            i = i - 1;

        }

    }
    
    public void cleanFieldsSales(){
    
        views.txt_sale_product_code.setText("");
        views.txt_sale_product_name.setText("");
        views.txt_sale_quantity.setText("");
        views.txt_product_id.setText("");
        views.txt_sale_price.setText("");
        views.txt_sale_subtotal.setText("");
        views.txt_sale_stock.setText("");
    
    }
    
    public void cleanAllFieldsSales(){
    
        views.txt_sale_product_code.setText("");
        views.txt_sale_product_name.setText("");
        views.txt_sale_quantity.setText("");
        views.txt_product_id.setText("");
        views.txt_sale_price.setText("");
        views.txt_sale_subtotal.setText("");
        views.txt_sale_stock.setText("");
        views.txt_sale_customer_id.setText("");
        views.txt_sale_customer_name.setText("");
        views.txt_sale_total_to_pay.setText("");
        
    }
}
