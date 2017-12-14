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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Carlos Herrera
 */
public class Metodo {
    
    public static Object [][] VerStock() {
        int registros = 0;
        String sql = "SELECT CODIGO, DESCRIPCION, STOCK, STOCK_CRITICO FROM PRODUTO WHERE STOCK<STOCK_CRITICO";
        String sql2 = "SELECT count(1) as total FROM PRODUTO WHERE STOCK<STOCK_CRITICO";
        //obtenemos la cantidad de registros existentes en la tabla
        try {
            //PreparedStatement pstm = Conexion.conectar().prepareStatement(sql2);
            //ResultSet res = pstm.executeQuery();
            Statement set = Conexion.conectar().createStatement();
            ResultSet res = set.executeQuery(sql2);
            res.next();
            registros = res.getInt("total");
            res.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        Object[][] data = new String[registros][4];
        //realizamos la consulta sql y llenamos los datos en "Object"
        try {
            Statement set = Conexion.conectar().createStatement();
            ResultSet res = set.executeQuery(sql);
            int i = 0;
            while (res.next()) {
                data[i][0] = res.getString("CODIGO");
                data[i][1] = res.getString("DESCRIPCION");
                data[i][2] = String.valueOf(res.getInt("STOCK"));
                data[i][3] = String.valueOf(res.getInt("STOCK_CRITICO"));
                i++;
            }
            res.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return data;
    }
    
    public static boolean ValidarStock(String CODIGO,int STOCK) {
        boolean registros = false;
        String sql = "SELECT count(1) as total FROM PRODUTO WHERE CODIGO=" 
                + CODIGO + " AND STOCK>"+STOCK;
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
    
    
    public static Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
    
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
    
    public static boolean BuscarJefe(String RUT) {
        boolean registros = false;
        String sql = "SELECT count(1) as total FROM JEFEDEPTO where RUT='" + RUT + "'";
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
    
    public static DefaultTableModel CargarVendedor() {
        String sql = "SELECT V.RUT , V.NOMBRE, V.APELLIDO, V.TELEFONO, V.ESTADO, "
                + "V.FECHA_INGRESO, V.SUELDO_BASE, V.JEFE.FULLNAME() AS JEFAZO FROM VENDEDOR V";
        String[] titulos = {"Rut", "Nombre", "Apellido", "Telefono", "Estado",
            "Ingreso", "Sueldo", "Jefe"};
        DefaultTableModel model = new DefaultTableModel(null, titulos);
        String[] fila = new String[8];
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
                //fila[5] = rs.getDate("FECHA_INGRESO").toString();
                java.sql.Date date = rs.getDate("FECHA_INGRESO");
                // TODO: Specify time zone and locale
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = format.format(date);
                fila[5] = formattedDate;
                fila[6] = String.valueOf(rs.getInt("SUELDO_BASE"));
                fila[7] = rs.getString("JEFAZO");
                model.addRow(fila);
            }
            Conexion.desconectar();
            return model;
        } catch (SQLException ex) {
            Conexion.desconectar();
            return model;
        }
    }
    
    public static boolean CrudVendedor(int OPT, String RUT, String NOMBRE, String APELLIDO,
            String TELEFONO, String FECHA, int SUELDO, String JEFE) {
        try {
            CallableStatement PROCEDURE = Conexion.conectar().prepareCall("{call VENDEDOR_CRUD (?,?,?,?,?,?,?,?)}");
            PROCEDURE.setInt(1, OPT);
            PROCEDURE.setString(2, RUT);
            PROCEDURE.setString(3, NOMBRE);
            PROCEDURE.setString(4, APELLIDO);
            PROCEDURE.setString(5, TELEFONO);
            PROCEDURE.setString(6, FECHA);
            PROCEDURE.setInt(7, SUELDO);
            PROCEDURE.setString(8, JEFE);
            PROCEDURE.execute();
            Conexion.desconectar();
            return true;
        } catch (SQLException ex) {
            System.out.println("EL ERROR ES:" + ex);
            Conexion.desconectar();
            return false;
        }
    }
    
    public static boolean BuscarVendedor(String RUT) {
        boolean registros = false;
        String sql = "SELECT count(1) as total FROM VENDEDOR where RUT='" + RUT + "'";
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
    
    public static Object [][] comboJefe(){
        int registros = 0;
        String sql = "SELECT J.RUT , J.FULLNAME() as NOMBRE FROM JEFEDEPTO J WHERE J.ESTADO=1";
        String sql2 = "SELECT count(1) as total FROM JEFEDEPTO J WHERE J.ESTADO=1";
      //obtenemos la cantidad de registros existentes en la tabla
      try{         
         //PreparedStatement pstm = Conexion.conectar().prepareStatement(sql2);
         //ResultSet res = pstm.executeQuery();
            Statement set = Conexion.conectar().createStatement();
            ResultSet res = set.executeQuery(sql2);
         res.next();
         registros = res.getInt("total");
         res.close();
      }catch(SQLException e){
         System.out.println(e);
      }
        Object[][] data = new String[registros][2];  
        //realizamos la consulta sql y llenamos los datos en "Object"
        try{              
            Statement set = Conexion.conectar().createStatement();
            ResultSet res = set.executeQuery(sql);
            int i = 0;
            while(res.next()){
                String estRut = res.getString("RUT");
                String estNombre = res.getString("NOMBRE");
                data[i][0] = estRut;            
                data[i][1] = estNombre;           
                i++;
            }
            res.close();
        }
        catch(SQLException e){
            System.out.println(e);
        }
        return data;
    }  
}
