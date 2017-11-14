/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author carhe
 */
public class Metodo {
    
    public static DefaultTableModel CargarJefe() {
        String sql = "SELECT J.RUT , J.NOMBRE, J.APELLIDO, J.TELEFONO, J.ESTADO, "
                + "J.NOMBREDEPTO, J.SUELDO FROM JEFEDEPTO J";
        String[] titulos = {"Rut","Nombre","Apellido","Telefono","Estado",
            "Departamento","Sueldo"};
        DefaultTableModel model = new DefaultTableModel(null, titulos);
        String[] fila = new String[7];
        try {
            Statement st = Conexion.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                fila[0] = rs.getString("RUT");
                fila[1] = rs.getString("NOMBRE");
                fila[2] = rs.getString("APELLIDO");
                fila[3] = rs.getString("TELEFONO");
                if(rs.getInt("ESTADO")==1){
                    fila[4] = "ACTIVO";
                } else {
                    fila[4] = "INACTIVO";
                }
                fila[5] = rs.getString("NOMBREDEPTO");
                fila[6] = String.valueOf(rs.getInt("SUELDO"));
                model.addRow(fila);
            }
            Conexion.desconectar();
            return model;
        } catch (SQLException ex) {
            //Logger.getLogger(OAD_CHOFER.class.getName()).log(Level.SEVERE, null, ex);
            Conexion.desconectar();
            return model;
        }
    }
    
}
