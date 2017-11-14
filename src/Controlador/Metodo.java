/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author carhe
 */
public class Metodo {
    
    /**
     * Método que devuelve la tabla de JedeDepto 
     * @return Modelo de la tabla completa con registros
     */
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
            Conexion.desconectar();
            return model;
        }
    }
    
    /**
     * Método llama al procedimiento de CRUD de JefeDepto y devuelve Booleano
     * @OPT 1=insert, 2=update, 3=delete(logical), 4=undelete
     * @return Boolean
     */    
    public static boolean CrudJefe(int OPT, String RUT, String NOMBRE, String APELLIDO, 
            String TELEFONO, String DEPARTAMENTO, int SUELDO) {
        try {
            CallableStatement PROCEDURE = Conexion.conectar().prepareCall("{call JEFE_CRUD (?,?,?,?,?,?,?)}");
            PROCEDURE.setInt(1, OPT);
            PROCEDURE.setString(2, RUT);
            PROCEDURE.setString(3, NOMBRE);
            PROCEDURE.setString(4, APELLIDO);
            PROCEDURE.setString(5, TELEFONO);
            PROCEDURE.setString(6, DEPARTAMENTO);
            PROCEDURE.setInt(7, SUELDO);
            PROCEDURE.execute();
            Conexion.desconectar();
            return true;
        } catch (SQLException ex) {
            System.out.println("EL ERROR ES:" + ex);
            Conexion.desconectar();
            return false;
        }

    }
    
}
