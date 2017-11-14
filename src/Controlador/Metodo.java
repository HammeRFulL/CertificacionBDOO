/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Carlos Herrera
 */
public class Metodo {

    /**
     * Método que devuelve la tabla de JedeDepto
     *
     * @return Modelo de la tabla completa con registros
     */
    public static DefaultTableModel CargarJefe() {
        String sql = "SELECT J.RUT , J.NOMBRE, J.APELLIDO, J.TELEFONO, J.ESTADO, "
                + "J.NOMBREDEPTO, J.SUELDO FROM JEFEDEPTO J";
        String[] titulos = {"Rut", "Nombre", "Apellido", "Telefono", "Estado",
            "Departamento", "Sueldo"};
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
                if (rs.getInt("ESTADO") == 1) {
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
     *
     * @param OPT Opcion crud (1=insertar, 2=actualizar, 3=eliminar jefe,
     * 4=resaurar jefe)
     * @param RUT Rut Jefe de Departamento
     * @param NOMBRE Nombre Jefe de Departamento
     * @param APELLIDO Apellido Jefe de Departamento
     * @param TELEFONO Telefono Jefe de Departamento
     * @param DEPARTAMENTO Nombre departamento en el cual trabaja
     * @param SUELDO Sueldo Jefe de Departamento
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

    /**
     * Método llama al procedimiento de CRUD de JefeDepto y devuelve Booleano
     *
     * @param RUT
     * @return Boolean
     */
    public boolean BuscarJefe(String RUT) {
        boolean registros = false;
        String sql = "SELECT count(1) as total FROM JEFEDEPTO where RUT='" + RUT + "';";
        try {
            PreparedStatement st = Conexion.conectar().prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            rs.next();
            int dato = rs.getInt("total");
            rs.close();
            if (dato > 0) {
                registros = true;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return registros;
    }
}
