package Controllers;

import Models.DynamicComboBox;
import static Models.EmployeesDao.rol_user;
import Models.Suppliers;
import Models.SuppliersDao;
import Views.SystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.MouseInputListener;
import javax.swing.table.DefaultTableModel;

public class SuppliersController implements ActionListener, MouseInputListener, KeyListener {

    //VARIABLES
    private Suppliers suppliers;
    private SuppliersDao suppliersDao;
    private SystemView views;
    String rol = rol_user;
    DefaultTableModel model = new DefaultTableModel();

    public SuppliersController(Suppliers suppliers, SuppliersDao suppliersDao, SystemView views) {
        this.suppliers = suppliers;
        this.suppliersDao = suppliersDao;
        this.views = views;

        /**
         * PONER A LA ESCUCHA LOS BOTONES
         */
        //BOTON PARA REGISTAR
        this.views.btn_register_supplier.addActionListener(this);

        //TABLA DE PROVEEDORES
        this.views.supplier_table.addMouseListener(this);

        //BUSCAR
        this.views.txt_search_supplier.addKeyListener(this);

        //BOTON PARA MODIFICAR
        this.views.btn_update_supplier.addActionListener(this);

        //BOTON PARA ELIMINAR
        this.views.btn_delete_supplier.addActionListener(this);

        //BOTON PARA CANCELAR
        this.views.btn_cancel_supplier.addActionListener(this);
        
        //LABEL PROVEEDORES
        this.views.jLabelSupplimers.addMouseListener(this);
        
         getSupplierName();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == views.btn_register_supplier) {

            if (views.txt_supplier_name.getText().equals("")
                    || views.txt_supplier_description.getText().equals("")
                    || views.txt_supplier_addres.getText().equals("")
                    || views.txt_supplier_telephone.getText().equals("")
                    || views.txt_supplier_email.getText().equals("")
                    || views.cmb_supplier_city.getSelectedItem().toString().equals("")) {

                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");

            } else {

                suppliers.setName(views.txt_supplier_name.getText().trim());
                suppliers.setDescription(views.txt_supplier_description.getText().trim());
                suppliers.setAddress(views.txt_supplier_addres.getText().trim());
                suppliers.setTelephone(views.txt_supplier_telephone.getText().trim());
                suppliers.setEmail(views.txt_supplier_email.getText().trim());
                suppliers.setCity(views.cmb_supplier_city.getSelectedItem().toString());

                if (suppliersDao.registerSuppliersQuery(suppliers)) {

                    cleanTable();

                    listAllSuppliers();

                    JOptionPane.showMessageDialog(null, "Proveedor registrado con exito.");

                } else {

                    JOptionPane.showMessageDialog(null, "Error al registrar al proveedor.");

                }

            }

        } else if (e.getSource() == views.btn_update_supplier) {

            if (views.txt_supplier_id.equals("")) {

                JOptionPane.showMessageDialog(null, "Selecciona un proveedor para continuar");

            } else {

                if (views.txt_supplier_name.getText().equals("")
                        || views.txt_supplier_description.getText().equals("")
                        || views.txt_supplier_addres.getText().equals("")
                        || views.txt_supplier_telephone.getText().equals("")
                        || views.txt_supplier_email.getText().equals("")
                        || views.cmb_supplier_city.getSelectedItem().toString().equals("")) {

                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");

                } else {

                    suppliers.setName(views.txt_supplier_name.getText().trim());
                    suppliers.setDescription(views.txt_supplier_description.getText().trim());
                    suppliers.setAddress(views.txt_supplier_addres.getText().trim());
                    suppliers.setTelephone(views.txt_supplier_telephone.getText().trim());
                    suppliers.setEmail(views.txt_supplier_email.getText().trim());
                    suppliers.setCity(views.cmb_supplier_city.getSelectedItem().toString());

                    suppliers.setId(Integer.parseInt(views.txt_supplier_id.getText().trim()));

                    if (suppliersDao.updateSuppliersQuery(suppliers)) {

                        cleanTable();

                        listAllSuppliers();

                        views.btn_register_supplier.setEnabled(true);

                        JOptionPane.showMessageDialog(null, "Proveedor modificado con exito.");

                        cleanFields();

                    } else {

                        JOptionPane.showMessageDialog(null, "Error al modificar al Proveedor.");

                    }

                }

            }

        } else if (e.getSource() == views.btn_delete_supplier) {

            int row = views.supplier_table.getSelectedRow();

            if (row == -1) {

                JOptionPane.showMessageDialog(null, "Error debes seleccionar el proveedor que deseas eliminar.");

            } else {

                int id = Integer.parseInt(views.supplier_table.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "¿Deseas eliminar este proveedor?.");

                if (question == 0 && suppliersDao.deleteSuppliersQuery(id) != false) {

                    cleanFields();
                    cleanTable();
                    listAllSuppliers();

                    JOptionPane.showMessageDialog(null, "Proveedor eliminado con exito.");

                } else {

                    JOptionPane.showMessageDialog(null, "Error al eliminado al proveedor.");

                }

            }

        } else if (e.getSource() == views.btn_cancel_supplier) {

            cleanFields();
            cleanTable();
            listAllSuppliers();

            views.btn_register_supplier.setEnabled(true);

        }

    }

    //LISTA PROVEEDORES
    public void listAllSuppliers() {

        if (rol.equals("Administrador")) {

            List<Suppliers> list = suppliersDao.listSuppliersQuery(views.txt_search_supplier.getText());

            model = (DefaultTableModel) views.supplier_table.getModel();

            Object[] row = new Object[7];

            for (int i = 0; i < list.size(); i++) {

                row[0] = list.get(i).getId();
                row[1] = list.get(i).getName();
                row[2] = list.get(i).getDescription();
                row[3] = list.get(i).getAddress();
                row[4] = list.get(i).getTelephone();
                row[5] = list.get(i).getEmail();
                row[6] = list.get(i).getCity();

                model.addRow(row);

            }

            //PASAMOS LOS DATOS A LA TABLA
            views.supplier_table.setModel(model);

        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getSource() == views.supplier_table) {

            int row = views.supplier_table.rowAtPoint(e.getPoint());

            views.txt_supplier_id.setText(views.supplier_table.getValueAt(row, 0).toString());
            views.txt_supplier_name.setText(views.supplier_table.getValueAt(row, 1).toString());
            views.txt_supplier_description.setText(views.supplier_table.getValueAt(row, 2).toString());
            views.txt_supplier_addres.setText(views.supplier_table.getValueAt(row, 3).toString());
            views.txt_supplier_telephone.setText(views.supplier_table.getValueAt(row, 4).toString());
            views.txt_supplier_email.setText(views.supplier_table.getValueAt(row, 5).toString());
            views.cmb_supplier_city.setSelectedItem(views.supplier_table.getValueAt(row, 6).toString());

            views.btn_register_supplier.setEnabled(false);
            views.txt_supplier_id.setEditable(false);

        }else if (e.getSource() == views.jLabelSupplimers){
            
            if(rol.equals("Administrador")){
                
                views.jTabbedPane1.setSelectedIndex(5);
                cleanFields();
                cleanTable();
                listAllSuppliers();
                
            }else{

                views.jTabbedPane1.setEnabledAt(5, false);

                views.jLabelSupplimers.setEnabled(false);

                JOptionPane.showMessageDialog(null, "No tienes privilegios de Administrador para acceder a proveedores.");
                
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
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (e.getSource() == views.txt_search_supplier) {

            cleanTable();
            listAllSuppliers();

        }

    }

    //LIMPIAR TABLA
    public void cleanTable() {

        for (int i = 0; i < model.getRowCount(); i++) {

            model.removeRow(i);

            i = i - 1;

        }

    }

    //LIMPIAR CAMPOS
    public void cleanFields() {

        views.txt_supplier_id.setText("");
        views.txt_supplier_id.setEditable(true);

        views.txt_supplier_name.setText("");
        views.txt_supplier_description.setText("");
        views.txt_supplier_addres.setText("");
        views.txt_supplier_telephone.setText("");
        views.txt_supplier_email.setText("");

        views.cmb_supplier_city.setSelectedItem(0);

    }
    
        //MOSTRAR EL NOMBRE DE LOS PROVEEDORES
    public void getSupplierName() {

        List<Suppliers> list = suppliersDao.listSuppliersQuery(views.txt_search_supplier.getText());

        for (int i = 0; i < list.size(); i++) {

            int id = list.get(i).getId();
            String name = list.get(i).getName();

            views.cmb_purchase_supplier.addItem(new DynamicComboBox(id, name));
        }

    }
}
