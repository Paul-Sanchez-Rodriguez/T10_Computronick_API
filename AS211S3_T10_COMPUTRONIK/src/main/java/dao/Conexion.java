package dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexion {

    public static Connection cnx = null;

    public static Connection conectar(){
        try {
            String user = "ADMIN22";
            String pwd = "abc123";
            String driver = "oracle.jdbc.OracleDriver";
            String url = "jdbc:oracle:thin:@localhost:1521/XE";
            Class.forName(driver).newInstance();
            cnx = DriverManager.getConnection(url, user, pwd);
//            String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
//            String url = "jdbc:sqlserver://localhost:1433;databaseName=DBCOMPUTRONICK";
//            String user = "sa";
//            String pwd = "thextremen";
//            Class.forName(driver).newInstance();
//            cnx = DriverManager.getConnection(url, user, pwd);
        } catch (Exception e) {
            System.out.println("Error en la conexión " + e.getMessage());
        }
        return cnx;
    }

    public static Connection getCnx() {
       return cnx;
   }

    public static void setCnx(Connection aCnx) {
        cnx = aCnx;
    }

    public void Cerrar() throws Exception {
        if (cnx != null) {
            cnx.close();
        }
    }

    public static void main(String[] args) {
        conectar();
        try {
            if (cnx != null) {
                System.out.println("Conexion exitosa");
            } else {
                System.out.println("Conexion fallida");
            }
        } catch (Exception e) {
            System.out.println("El error es " + e.getMessage());
        }

    }

    public Connection getCn() {
        return cnx;
    }

    public void setCn(Connection cnx) {
        this.cnx = cnx;
    }

}
