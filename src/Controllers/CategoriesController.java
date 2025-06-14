package Controllers;

import Models.Categories;
import Models.CategoriesDao;
import Models.DynamicComboBox;
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
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class CategoriesController implements ActionListener, KeyListener, MouseListener {

    private Categories category;
    private CategoriesDao categoriesDao;
    private SystemView views;
    String rol = rol_user;
    DefaultTableModel model = new DefaultTableModel();

    public CategoriesController(Categories category, CategoriesDao categoriesDao, SystemView views) {
        this.category = category;
        this.categoriesDao = categoriesDao;
        this.views = views;

        //PONER A LA ESCUCHA LAS ACCIONES
        this.views.btn_register_category.addActionListener(this);

        this.views.txt_search_category.addKeyListener(this);

        this.views.categories_table.addMouseListener(this);

        this.views.btn_update_category.addActionListener(this);

        this.views.btn_delete_category.addActionListener(this);

        this.views.btn_cancel_category.addActionListener(this);

        this.views.jLabelCategories.addMouseListener(this);

        getCategoryName();
        AutoCompleteDecorator.decorate(views.cmd_product_category);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == views.btn_register_category) {

            if (views.txt_category_name.getText().equals("")) {

                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");

            } else {

                category.setName(views.txt_category_name.getText().trim());

                if (categoriesDao.registerCategoriesQuery(category)) {

                    cleanTable();

                    cleanFields();

                    listAllCategories();

                    JOptionPane.showMessageDialog(null, "Se registro la categoria con exito.");

                } else {

                    JOptionPane.showMessageDialog(null, "Error al registrar la categoria.");

                }

            }

        } else if (e.getSource() == views.btn_update_category) {

            if (views.txt_category_id.getText().equals("")) {

                JOptionPane.showMessageDialog(null, "Selecciona una fila para continuar.");

            } else {

                if (views.txt_category_id.getText().equals("")
                        || views.txt_category_name.getText().equals("")) {

                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");

                } else {

                    //CREAMOS EL OBJETO
                    category.setId(Integer.parseInt(views.txt_category_id.getText().trim()));
                    category.setName(views.txt_category_name.getText().trim());

                    //PASAMOS EL OBJETO A LA CLASE CATEGORIESDAO PARA ENVIAR EL OBJETO AL METODO DE ACTUALIZAR
                    if (categoriesDao.updateCategoriesQuery(category)) {

                        cleanTable();

                        listAllCategories();

                        views.btn_register_category.setEnabled(true);

                        JOptionPane.showMessageDialog(null, "Se actualizo la categoria con exito.");

                        cleanFields();

                    } else {

                        JOptionPane.showMessageDialog(null, "Error al actualizo la categoria.");

                    }

                }

            }

        } else if (e.getSource() == views.btn_delete_category) {

            int row = views.categories_table.getSelectedRow();

            if (row == -1) {

                JOptionPane.showMessageDialog(null, "Debes seleccionar una categoria.");

            } else {

                int id = Integer.parseInt(views.categories_table.getValueAt(row, 0).toString());

                int question = JOptionPane.showConfirmDialog(null, "En realidad quieres eliminar esta categoria.");

                if (question == 0 && categoriesDao.deleteCategoriesQuery(id) != false) {

                    cleanFields();

                    cleanTable();

                    listAllCategories();

                    views.btn_register_category.setEnabled(true);

                    JOptionPane.showMessageDialog(null, "Se elimino con exito la categoria.");

                }

            }

        } else if (e.getSource() == views.btn_cancel_category) {

            views.btn_register_category.setEnabled(true);

            cleanFields();

        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (e.getSource() == views.txt_search_category) {

            cleanTable();

            cleanFields();

            listAllCategories();

        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getSource() == views.categories_table) {

            int row = views.categories_table.rowAtPoint(e.getPoint());

            views.txt_category_id.setText(views.categories_table.getValueAt(row, 0).toString());
            views.txt_category_name.setText(views.categories_table.getValueAt(row, 1).toString());

            views.btn_register_category.setEnabled(false);

        } else if (e.getSource() == views.jLabelCategories) {

            if (rol.equals("Administrador")) {

                views.jTabbedPane1.setSelectedIndex(6);

                cleanTable();

                cleanFields();

                listAllCategories();

            } else {

                views.jTabbedPane1.setEnabledAt(6, false);

                views.jLabelCategories.setEnabled(false);

                JOptionPane.showMessageDialog(null, "No tienes privilegios de Administrador para acceder a las categorias.");

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

    public void listAllCategories() {

        if (rol.equals("Administrador")) {

            List<Categories> list = categoriesDao.listCategoriesQuery(views.txt_search_category.getText());

            model = (DefaultTableModel) views.categories_table.getModel();

            Object[] row = new Object[2];

            for (int i = 0; i < list.size(); i++) {

                row[0] = list.get(i).getId();
                row[1] = list.get(i).getName();

                model.addRow(row);

            }

            views.categories_table.setModel(model);

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

        views.txt_category_id.setText("");
        views.txt_category_id.setEditable(false);

        views.txt_category_name.setText("");

    }

    //MOSTRAR EL NOMBRE DE LAS CATEGORIAS
    public void getCategoryName() {

        List<Categories> list = categoriesDao.listCategoriesQuery(views.txt_search_category.getText());

        for (int i = 0; i < list.size(); i++) {

            int id = list.get(i).getId();
            String name = list.get(i).getName();

            views.cmd_product_category.addItem(new DynamicComboBox(id, name));
        }

    }
}
