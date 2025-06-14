package Controllers;

import Models.Employees;
import Models.EmployeesDao;
import Views.LoginView;
import Views.SystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class LoginControllers implements ActionListener {

    //ENCAPSULAR VARIABLES
    private Employees employe;
    private EmployeesDao employees_dao;
    private LoginView login_view;

    public LoginControllers(Employees employe, EmployeesDao employees_dao, LoginView login_view) {
        this.employe = employe;
        this.employees_dao = employees_dao;
        this.login_view = login_view;

        //ENTRAR AL SISTEMA
        this.login_view.btn_enter.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //OBTENIENDO LOS DATOS DE LA VISTA
        String user = login_view.txt_userName.getText().trim();
        String pass = String.valueOf(login_view.txt_password.getPassword());

        if (e.getSource() == login_view.btn_enter) {

            if (!user.equals("") || !pass.equals("")) {
                employe = employees_dao.loginQuery(user, pass);

                if (employe.getUsername() != null) {

                    if (employe.getRol().equals("Administrador")) {
                        SystemView admin = new SystemView();
                        admin.setVisible(true);

                    } else {
                        SystemView aux = new SystemView();
                        aux.setVisible(true);
                    }

                    this.login_view.dispose();

                } else {
                    JOptionPane.showMessageDialog(null, "Usuario o password son incorrectos.");
                }

            }else{
                JOptionPane.showMessageDialog(null, "Los campos o uno de los campos estan vacios.");
            }

        }

    }

}
