/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author carhe
 */
public class Conexion {
    static String NAME="CERTI";
    static String PASS="12345";
    static String URL="@localhost";
    static String PORT=":1521";
    static String TYPE=":XE";

    public static Connection conectar() {
        Connection cn = null;

        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            //cn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "PROYECTO", "12345");
            cn = DriverManager.getConnection("jdbc:oracle:thin:"+URL+PORT+TYPE, NAME, PASS);
            return cn;
        } catch (Exception e) {
            System.out.println("Hay un problema en:" + e);
            return cn;
        }
    }

    public static void desconectar() {
        try {
            DriverManager.getConnection("jdbc:oracle:thin:"+URL+PORT+TYPE, NAME, PASS).close();
        } catch (Exception SQLException) {
        }
    }
    
    public static void commit(){
        String sql = "commit";
        try{
            conectar().createStatement().executeQuery(sql);
            System.out.println("Commit Exitoso");
        }
        catch(SQLException ex){
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE,null,ex);
            System.out.println("Error en Commit");
        }
    }
}
