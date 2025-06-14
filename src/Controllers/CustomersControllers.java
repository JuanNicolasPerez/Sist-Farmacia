package Controllers;

import Models.Customers;
import Models.CustomersDao;
import static Models.EmployeesDao.rol_user;
import Views.SystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class CustomersControllers implements ActionListener, MouseListener, KeyListener {

    //ENCAPSULAR VARIABLES
    private Customers customer;
    private CustomersDao customersDao;
    private SystemView views;
    String rol = rol_user;
    DefaultTableModel model = new DefaultTableModel();

    //CONTRUCTOR
    public CustomersControllers(Customers customer, CustomersDao customersDao, SystemView views) {
        this.customer = customer;
        this.customersDao = customersDao;
        this.views = views;

        //BOTON REGISTRAR CLIENTE A LA "ESCUCHA"
        this.views.btn_register_customer.addActionListener(this);

        //TABLA CLIENTES A LA  "ESCUCHA"
        this.views.customer_table.addMouseListener(this);

        //BUSCADOR A LA  "ESCUCHA"
        this.views.txt_search_customer.addKeyListener(this);

        //BOTON MODIFICAR CLIENTE A LA "ESCUCHA"
        this.views.btn_update_customer.addActionListener(this);

        //BOTON ELIMINAR CLIENTE A LA "ESCUCHA"
        this.views.btn_delete_customer.addActionListener(this);

        //BOTON CANCELAR CLIENTE A LA "ESCUCHA"
        this.views.btn_cancel_customer.addActionListener(this);
        
         //PONER A LA ESCUCHA EL BOTON MENU LATERAL
        this.views.jLabelCustomers.addMouseListener(this);

    }

    //EVENTOS DE LOS BOTONES
    @Override
    public void actionPerformed(ActionEvent e) {

        //BOTON REGISTRAR
        if (e.getSource() == views.btn_register_customer) {

            //Verificamos que los campos no esten vacios
            if (views.txt_customer_id.getText().equals("")
                    || views.txt_customer_fullname.getText().equals("")
                    || views.txt_customer_addres.getText().equals("")
                    || views.txt_customer_telephone.getText().equals("")
                    || views.txt_customer_email.getText().equals("")) {

                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");

            } else {

                //Recupera el texto que el usuario ha ingresado en el campo de texto txt_customer_id en la interfaz gráfica
                customer.setId(Integer.parseInt(views.txt_customer_id.getText().trim()));
                customer.setFull_name(views.txt_customer_fullname.getText().trim());
                customer.setAddress(views.txt_customer_addres.getText().trim());
                customer.setTelephone(views.txt_customer_telephone.getText().trim());
                customer.setEmail(views.txt_customer_email.getText().trim());

                //Solicitamos al método  registerCustomersQuery de la clase customersDao, y devuelve un valor verdadero o falso
                if (customersDao.registerCustomersQuery(customer)) {

                    JOptionPane.showMessageDialog(null, "Cliente registrado con exito");

                    cleanTable();

                    listAllCustomers();

                } else {

                    JOptionPane.showMessageDialog(null, "No se pudo registrar al cliente");

                }

            }

            //BOTON MODIFICAR
        } else if (e.getSource() == views.btn_update_customer) {

            if (views.txt_customer_id.getText().equals("")) {

                JOptionPane.showMessageDialog(null, "Seleccione una fila para continuar.");

            } else {

                //Verificamos que los campos no esten vacios
                if (views.txt_customer_id.getText().equals("")
                        || views.txt_customer_fullname.getText().equals("")
                        || views.txt_customer_addres.getText().equals("")
                        || views.txt_customer_telephone.getText().equals("")
                        || views.txt_customer_email.getText().equals("")) {

                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");

                } else {

                    customer.setId(Integer.parseInt(views.txt_customer_id.getText().trim()));
                    customer.setFull_name(views.txt_customer_fullname.getText().trim());
                    customer.setAddress(views.txt_customer_addres.getText().trim());
                    customer.setTelephone(views.txt_customer_telephone.getText().trim());
                    customer.setEmail(views.txt_customer_email.getText().trim());

                    if (customersDao.updateCustomersQuery(customer)) {

                        cleanTable();

                        listAllCustomers();

                        views.btn_register_customer.setEnabled(true);

                        JOptionPane.showMessageDialog(null, "Datos modificados con exito");

                        cleanFields();

                    } else {

                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar al cliente");

                    }

                }

            }

            //BOTON ELIMINAR
        } else if (e.getSource() == views.btn_delete_customer) {

            int row = views.customer_table.getSelectedRow();

            if (row == -1) {

                JOptionPane.showMessageDialog(null, "Debes seleccionar un cliente.");

            } else {

                int id = Integer.parseInt(views.customer_table.getValueAt(row, 0).toString());

                int question = JOptionPane.showConfirmDialog(null, "En realidad quieres eliminar este cliente.");

                if (question == 0 && customersDao.deleteCustomersQuery(id) != false) {

                    cleanFields();

                    cleanTable();

                    listAllCustomers();

                    views.btn_register_customer.setEnabled(true);

                    JOptionPane.showMessageDialog(null, "Se elimino con exito al cliente.");

                }

            }
            //BOTON CANCELAR    
        } else if (e.getSource() == views.btn_cancel_customer) {
            
            views.btn_register_customer.setEnabled(true);
            
            cleanFields();
            
        }

    }

    //LISTAR CLIENTES
    public void listAllCustomers() {

        List<Customers> list = customersDao.listCustomerQuery(views.txt_search_customer.getText());

        model = (DefaultTableModel) views.customer_table.getModel();

        Object[] row = new Object[5];

        for (int i = 0; i < list.size(); i++) {

            row[0] = list.get(i).getId();
            row[1] = list.get(i).getFull_name();
            row[2] = list.get(i).getAddress();
            row[3] = list.get(i).getTelephone();
            row[4] = list.get(i).getEmail();

            model.addRow(row);

        }

        views.customer_table.setModel(model);

    }

    //CARGA LOS DATOS DE LOS CLIENTES AL HACER CLICK SOBRE LA TABLA
    @Override
    public void mouseClicked(MouseEvent e) {

        //CLICK EN LA TABLA
        if (e.getSource() == views.customer_table) {

            int row = views.customer_table.rowAtPoint(e.getPoint());

            views.txt_customer_id.setText(views.customer_table.getValueAt(row, 0).toString());
            views.txt_customer_fullname.setText(views.customer_table.getValueAt(row, 1).toString());
            views.txt_customer_addres.setText(views.customer_table.getValueAt(row, 2).toString());
            views.txt_customer_telephone.setText(views.customer_table.getValueAt(row, 3).toString());
            views.txt_customer_email.setText(views.customer_table.getValueAt(row, 4).toString());

            views.btn_register_customer.setEnabled(false);
            views.txt_customer_id.setEditable(false);

        }

        //CLICK EN EL MENU LATERAL
        if (e.getSource() == views.jLabelCustomers) {

            if (rol.equals("Administrador")) {

                views.jTabbedPane1.setSelectedIndex(3);

                cleanTable();

                cleanFields();

                listAllCustomers();

            } else {

                views.jTabbedPane1.setEnabledAt(3, false);

                views.jLabelEmployers.setEnabled(false);

                JOptionPane.showMessageDialog(null, "No tienes privilegios de Administrador para acceder a clientes.");

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

    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (e.getSource() == views.txt_search_customer) {

            cleanTable();

            listAllCustomers();

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
    private void cleanFields() {

        views.txt_customer_id.setText("");
        views.txt_customer_id.setEditable(true);

        views.txt_customer_fullname.setText("");
        views.txt_customer_addres.setText("");
        views.txt_customer_telephone.setText("");
        views.txt_customer_email.setText("");

    }

}
