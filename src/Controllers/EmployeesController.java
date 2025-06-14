package Controllers;

import Models.Employees;
import Models.EmployeesDao;
import static Models.EmployeesDao.id_user;
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

public class EmployeesController implements ActionListener, MouseListener, KeyListener {

    private Employees employee;
    private EmployeesDao employeeDao;
    private SystemView views;
    String rol = rol_user;
    DefaultTableModel model = new DefaultTableModel();

    public EmployeesController(Employees employee, EmployeesDao employeeDao, SystemView views) {
        this.employee = employee;
        this.employeeDao = employeeDao;
        this.views = views;

        //BOTON REGISTRAR EMPLEADO
        this.views.btn_register_employer.addActionListener(this);

        //PONER A LA ESCUCHA LA TABLA
        this.views.employer_table.addMouseListener(this);

        //TXT BUSCAR A LA ESCUCHA
        this.views.txt_search_employer.addKeyListener(this);

        //PONER A LA ESCUCHA EL BOTON DE MODIFICAR
        this.views.btn_update_employer.addActionListener(this);

        //PONER A LA ESCUCHA EL BOTON DE ELIMINAR
        this.views.btn_delete_employer.addActionListener(this);

        //PONER A LA ESCUCHA EL BOTON DE CANCELAR
        this.views.btn_cancel_employer.addActionListener(this);

        //PONER A LA ESCUCHA EL BOTON DE MODIFICAR PASSWORD
        this.views.btn_modify_data.addActionListener(this);

        //PONER A LA ESCUCHA EL BOTON DE EMPLEADOS DEL MENU LATERAL
        this.views.jLabelEmployers.addMouseListener(this);

    }

    //ACCIONES CON LOS BOTONES DE REGISTRAR, MODIFICAR, ELIMINAR Y CANCELAR
    @Override
    public void actionPerformed(ActionEvent e) {

        //BOTON REGISTRAR EMPLEADOS
        if (e.getSource() == views.btn_register_employer) {

            if (views.txt_employer_id.getText().equals("") || views.txt_employer_fulname.getText().equals("")
                    || views.txt_employer_username.getText().equals("") || views.txt_employer_addres.getText().equals("")
                    || views.txt_employer_telephone.getText().equals("") || views.txt_employer_email.getText().equals("")
                    || views.cmb_rol.getSelectedItem().toString().equals("") || String.valueOf(views.txt_employer_password.getPassword()).equals("")) {

                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");

            } else {

                employee.setId(Integer.parseInt(views.txt_employer_id.getText().trim()));
                employee.setFull_name(views.txt_employer_fulname.getText().trim());
                employee.setUsername(views.txt_employer_username.getText().trim());
                employee.setAddress(views.txt_employer_addres.getText().trim());
                employee.setTelephone(views.txt_employer_telephone.getText().trim());
                employee.setEmail(views.txt_employer_email.getText().trim());

                employee.setPassword(String.valueOf(views.txt_employer_password.getPassword()));
                employee.setRol(views.cmb_rol.getSelectedItem().toString());

                if (employeeDao.registerEmployeeQuery(employee)) {

                    cleanTable();

                    JOptionPane.showMessageDialog(null, "Empleado registrado con exito");

                    cleanFields();

                    listAllEmployees();

                } else {

                    JOptionPane.showMessageDialog(null, "No se pudo registrar al empleado");

                }

            }

            //BOTON MODIFICAR EMPLEADOS
        } else if (e.getSource() == views.btn_update_employer) {

            if (views.txt_employer_id.equals("")) {

                JOptionPane.showMessageDialog(null, "Selecciona una fila de la tabla para continuar");

            } else {

                if (views.txt_employer_id.getText().equals("") || views.txt_employer_fulname.getText().equals("")
                        || views.txt_employer_username.getText().equals("") || views.txt_employer_addres.getText().equals("")
                        || views.txt_employer_telephone.getText().equals("") || views.txt_employer_email.getText().equals("")
                        || views.cmb_rol.getSelectedItem().toString().equals("")) {

                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");

                } else {

                    employee.setId(Integer.parseInt(views.txt_employer_id.getText().trim()));
                    employee.setFull_name(views.txt_employer_fulname.getText().trim());
                    employee.setUsername(views.txt_employer_username.getText().trim());
                    employee.setAddress(views.txt_employer_addres.getText().trim());
                    employee.setTelephone(views.txt_employer_telephone.getText().trim());
                    employee.setEmail(views.txt_employer_email.getText().trim());

                    employee.setPassword(String.valueOf(views.txt_employer_password.getPassword()));
                    employee.setRol(views.cmb_rol.getSelectedItem().toString());

                    if (employeeDao.updateEmployeeQuery(employee)) {

                        cleanTable();

                        listAllEmployees();

                        views.btn_register_employer.setEnabled(true);

                        JOptionPane.showMessageDialog(null, "Datos modificados con exito");

                        cleanFields();

                    } else {

                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar el empleado");

                    }
                }

            }

            //BOTON ELIMINAR EMPLEADOS
        } else if (e.getSource() == views.btn_delete_employer) {

            int row = views.employer_table.getSelectedRow();

            if (row == -1) {

                JOptionPane.showMessageDialog(null, "Debes seleccionar un empleado");

            } else if (views.employer_table.getValueAt(row, 0).equals(id_user)) {

                JOptionPane.showMessageDialog(null, "No puede eliminar al usuario autenticado.");

            } else {

                int id = Integer.parseInt(views.employer_table.getValueAt(row, 0).toString());

                int question = JOptionPane.showConfirmDialog(null, "En realidad quieres eliminar ese empleado");

                if (question == 0 && employeeDao.deleteEmployeeQuery(id) != false) {

                    cleanTable();

                    cleanFields();

                    views.btn_register_employer.setEnabled(true);

                    views.txt_employer_password.setEnabled(true);

                    listAllEmployees();

                    JOptionPane.showMessageDialog(null, "El usuario fue eliminado con exito");

                }
            }

            //BOTON CANCELAR EMPLEADOS
        } else if (e.getSource() == views.btn_cancel_employer) {

            cleanFields();

            views.btn_register_employer.setEnabled(true);

            views.txt_employer_password.setEnabled(true);

            views.txt_employer_id.setEnabled(true);

            views.txt_employer_id.setEditable(true);

            //BOTON MODIFICAR PASSWORD PERFIL EMPLEADO
        } else if (e.getSource() == views.btn_modify_data) {

            String password = String.valueOf(views.txt_password_modify_confirm.getPassword());
            String password_confirm = String.valueOf(views.txt_password_modify_confirm.getPassword());

            if (!password.equals("") && !password_confirm.equals("")) {

                if (password.equals(password_confirm)) {

                    employee.setPassword(String.valueOf(views.txt_password_modify.getPassword()));

                    if (employeeDao.updateEmployeePassword(employee) != false) {

                        JOptionPane.showMessageDialog(null, "Se modifico la contraseña de manera correcta.");

                    } else {

                        JOptionPane.showMessageDialog(null, "No se pudo modificar la contraseña.");

                    }

                } else {

                    JOptionPane.showMessageDialog(null, "La contraseñas no coinciden.");

                }

            } else {

                JOptionPane.showMessageDialog(null, "La campos no deben ir vacios.");

            }

        }
    }

    //LISTAR TODOS LOS EMPLEADOS
    public void listAllEmployees() {

        if (rol.equals("Administrador")) {
            List<Employees> list = employeeDao.listEmployeesQuery(views.txt_search_employer.getText());

            DefaultTableModel model = (DefaultTableModel) views.employer_table.getModel();
            
            // Limpiar la tabla antes de agregar nuevas filas
            model.setRowCount(0); 

            for (Employees employee : list) {
                Object[] row = {
                    employee.getId(),
                    employee.getFull_name(),
                    employee.getUsername(),
                    employee.getAddress(),
                    employee.getTelephone(),
                    employee.getEmail(),
                    employee.getRol()
                };
                model.addRow(row);
            }
        }
    }

    //LIMPIAR CAMPOS
    public void cleanFields() {

        views.txt_employer_id.setText("");
        views.txt_employer_id.setEditable(true);

        views.txt_employer_fulname.setText("");
        views.txt_employer_username.setText("");
        views.txt_employer_addres.setText("");
        views.txt_employer_telephone.setText("");
        views.txt_employer_email.setText("");

        views.txt_employer_password.setText("");
        views.txt_employer_password.setEnabled(true);

        views.cmb_rol.setSelectedItem(0);

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        //SELECCION O CLICK PARA LA TABLA DE EMPLEADOS
        if (e.getSource() == views.employer_table) {

            int row = views.employer_table.rowAtPoint(e.getPoint());

            views.txt_employer_id.setText(views.employer_table.getValueAt(row, 0).toString());
            views.txt_employer_fulname.setText(views.employer_table.getValueAt(row, 1).toString());
            views.txt_employer_username.setText(views.employer_table.getValueAt(row, 2).toString());
            views.txt_employer_addres.setText(views.employer_table.getValueAt(row, 3).toString());
            views.txt_employer_telephone.setText(views.employer_table.getValueAt(row, 4).toString());
            views.txt_employer_email.setText(views.employer_table.getValueAt(row, 5).toString());
            views.cmb_rol.setSelectedItem(views.employer_table.getValueAt(row, 6).toString());

            views.txt_employer_id.setEditable(false);
            views.txt_employer_password.setEnabled(false);
            views.btn_register_employer.setEnabled(false);

        }
        
        //SELECCION O CLICK DEL MENU LATERAL - PANEL EMPLEADOS
        if (e.getSource() == views.jLabelEmployers) {

            if (rol.equals("Administrador")) {

                views.jTabbedPane1.setSelectedIndex(4);

                cleanTable();

                cleanFields();

                listAllEmployees();

            } else {

                views.jTabbedPane1.setEnabledAt(4, false);

                views.jLabelEmployers.setEnabled(false);

                JOptionPane.showMessageDialog(null, "No tienes privilegios de Administrador para acceder a empleados.");

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

        if (e.getSource() == views.txt_search_employer) {

            cleanTable();

            listAllEmployees();

        }

    }

    //METODO LIMPIAR TABLA
    public void cleanTable() {

        int rowCount = model.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            model.removeRow(0);
        }

    }
}
