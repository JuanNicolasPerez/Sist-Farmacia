package Controllers;

import Models.DynamicComboBox;
import static Models.EmployeesDao.id_user;
import static Models.EmployeesDao.rol_user;
import Models.Products;
import Models.ProductsDao;
import Models.Purchases;
import Models.PurchasesDao;
import Views.Print;
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

public class PurchasesController implements ActionListener, MouseListener, KeyListener {

    private Purchases purchase;
    private PurchasesDao purchaseDao;
    private SystemView views;

    Products product = new Products();
    ProductsDao productsDao = new ProductsDao();

    String rol = rol_user;

    private int getIdSupplier = 0;
    private int item = 0;
    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel temp;

    public PurchasesController(Purchases purchase, PurchasesDao purchaseDao, SystemView views) {
        this.purchase = purchase;
        this.purchaseDao = purchaseDao;
        this.views = views;

        //REGISTRAR COMPRA
        this.views.txt_purchase_product_code.addKeyListener(this);
        this.views.txt_purchase_product_price.addKeyListener(this);

        //AGREGAR
        this.views.btn_add_product_to_buy.addActionListener(this);

        //COMPRAR
        this.views.btn_confirm_purchases.addActionListener(this);
        
        //ELIMINAR
        this.views.btn_remove_purchases.addActionListener(this);
        
        //NUEVO
        this.views.btn_new_purchase.addActionListener(this);
        
        //LABEL
        this.views.jLabelPurChases.addMouseListener(this);
        this.views.jLabelReports.addMouseListener(this);
    
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == views.btn_add_product_to_buy) {

            DynamicComboBox supplier_cmb = (DynamicComboBox) views.cmb_purchase_supplier.getSelectedItem();

            int supplier_id = supplier_cmb.getId();

            if (getIdSupplier == 0) {

                getIdSupplier = supplier_id;

            } else {

                if (getIdSupplier != supplier_id) {

                    JOptionPane.showMessageDialog(null, "No se puede realizar una misma compra con varios proveedores.");

                } else {

                    int amount = Integer.parseInt(views.txt_purchase__amount.getText());
                    String product_name = views.txt_purchase_product_name.getText();
                    double price = Double.parseDouble(views.txt_purchase_product_price.getText());
                    int purchase_id = Integer.parseInt(views.txt_purchase_id.getText());
                    String supplier_name = views.cmb_purchase_supplier.getSelectedItem().toString();

                    if (amount > 0) {

                        temp = (DefaultTableModel) views.purcheses_table.getModel();

                        for (int i = 0; i < views.purcheses_table.getRowCount(); i++) {

                            if (views.purcheses_table.getValueAt(i, 1).equals(views.txt_purchase_product_name.getText())) {

                                JOptionPane.showMessageDialog(null, "El producto ya esta registrado en la tabla de compras.");

                                return;

                            }

                        }

                        ArrayList list = new ArrayList();

                        item = 1;
                        list.add(item);
                        list.add(purchase_id);
                        list.add(product_name);
                        list.add(amount);
                        list.add(price);
                        list.add(amount * price);
                        list.add(supplier_name);

                        Object[] obj = new Object[6];
                        obj[0] = list.get(1);
                        obj[1] = list.get(2);
                        obj[2] = list.get(3);
                        obj[3] = list.get(4);
                        obj[4] = list.get(5);
                        obj[5] = list.get(6);

                        temp.addRow(obj);
                        views.purcheses_table.setModel(temp);

                        cleanFieldsPurchases();

                        views.cmb_purchase_supplier.setEditable(false);
                        views.txt_purchase_product_code.requestFocus();

                        calculatePurchase();

                    }

                }

            }

        } else if (e.getSource() == views.btn_confirm_purchases) {

            insertPurchases();

        }else if(e.getSource() == views.btn_remove_purchases){
            
            model = (DefaultTableModel) views.purcheses_table.getModel();
            
            model.removeRow(views.purcheses_table.getSelectedRow());
            
            calculatePurchase();
            
            views.txt_purchase_product_code.requestFocus();
            
        }else if(e.getSource() == views.btn_new_purchase){
        
            cleanTableTemp();
            cleanFieldsPurchases();
            
        }

    }

    public void cleanFieldsPurchases() {

        views.txt_purchase_product_name.setText("");
        views.txt_purchase_product_price.setText("");
        views.txt_purchase__amount.setText("");
        views.txt_purchase_product_code.setText("");
        views.txt_purchase_product_subtotal.setText("");
        views.txt_purchase_id.setText("");
        views.txt_purchase_to_pay.setText("");

    }

    public void listAllPurchases(){
    
        if(rol.equals("Administrador") || rol.equals("Auxiliar")){
        
            List<Purchases> list = purchaseDao.listAllPuchassesQuery();
            
            model = (DefaultTableModel) views.table_all_purches.getModel();
            
            Object[] row = new Object[4];
            
            for (int i = 0; i < list.size(); i++) {
                
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getSupplier_name_product();
                row[2] = list.get(i).getTotal();
                row[3] = list.get(i).getCreated();
                
                model.addRow(row);
                                
            }
            
            views.table_all_purches.setModel(model);
            
        }
        
    }
    
    public void calculatePurchase() {

        double total = 0;

        int numRow = views.purcheses_table.getRowCount();

        for (int i = 0; i < numRow; i++) {

            total = total + Double.parseDouble(String.valueOf(views.purcheses_table.getValueAt(i, 4)));

        }

        views.txt_purchase_to_pay.setText("" + total);
    }

    public void insertPurchases() {

        double total = Double.parseDouble(views.txt_purchase_to_pay.getText());
        int employee_id = id_user;

        if (purchaseDao.registerPurchaseQuery(getIdSupplier, employee_id, total)) {

            int purchase_id = purchaseDao.purchaseId();

            for (int i = 0; i < views.purcheses_table.getRowCount(); i++) {

                int product_id = Integer.parseInt(views.purcheses_table.getValueAt(i, 0).toString());
                int purchase_amount = Integer.parseInt(views.purcheses_table.getValueAt(i, 2).toString());
                double purchase_price = Double.parseDouble(views.purcheses_table.getValueAt(i, 3).toString());
                double purchase_subtotal = purchase_price * purchase_amount;

                purchaseDao.registerPurcheseDetailQuery(purchase_id, purchase_price, purchase_amount, purchase_subtotal, product_id);

                //ACTUALIZAMOS LA CANTIDAD EN STOCK DE PRODUCTOS
                product = productsDao.searchId(product_id);
                int amount = product.getProduct_quantify() + purchase_amount;
                productsDao.updateStockQuery(amount, product_id);
            }

            cleanTableTemp();

            cleanFieldsPurchases();

            JOptionPane.showMessageDialog(null, "Compra generada con exito.");
            
            Print print = new Print(purchase_id);
            print.setVisible(true);
            
        }

    }

    public void cleanTableTemp() {

        for (int i = 0; i < temp.getRowCount(); i++) {
            temp.removeRow(i);

            i = i - 1;

        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
        if(e.getSource() == views.jLabelPurChases){
        
            if(rol.equals("Administrador")){
                
                views.jTabbedPane1.setSelectedIndex(1);

                cleanTable();
            
            }else{
            
                views.jTabbedPane1.setEnabledAt(1, false);
                
                views.jLabelPurChases.setEnabled(false);
                
                JOptionPane.showMessageDialog(null, "No tiene privilegios de administrador");
                
            }
            
        }else if(e.getSource() == views.jLabelReports){
        
            views.jTabbedPane1.setSelectedIndex(7);
            
            cleanTable();
            
            listAllPurchases();
        
        }
        
    }
    
    public void cleanTable(){
    
        for (int i = 0; i < model.getRowCount(); i++) {
            
            model.removeRow(i);
            
            i = i -1;
            
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

        if (e.getSource() == views.txt_purchase_product_code) {

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                if (views.txt_purchase_product_code.getText().equals("")) {

                    JOptionPane.showMessageDialog(null, "Ingrese el codigo del producto");

                } else {

                    int id = Integer.parseInt(views.txt_purchase_product_code.getText());

                    product = productsDao.searchCode(id);

                    views.txt_purchase_product_name.setText(product.getName());
                    views.txt_purchase_id.setText("" + product.getId());

                    views.txt_purchase__amount.requestFocus();

                }

            }

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (e.getSource() == views.txt_purchase_product_price) {

            int quantity;
            double price = 0.0;

            if (views.txt_purchase__amount.getText().equals("")) {

                quantity = 1;

                views.txt_purchase_product_price.setText("" + price);

            } else {

                quantity = Integer.parseInt(views.txt_purchase__amount.getText());
                price = Double.parseDouble(views.txt_purchase_product_price.getText());

                views.txt_purchase_product_subtotal.setText("" + quantity * price);

            }

        }

    }

}
