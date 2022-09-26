
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import modelo.GraficoBarra;


public class GraficoImpl extends Conexion{
     DateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
    
    public int Graficar(){
        int cantidadVentas = 0;
        GraficoBarra gra;
        String sql = "SELECT SUM(canvendet) FROM prueba where fecven =?";
        try {
            gra = new GraficoBarra();
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1,formato.format(gra.getFecha()));
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {                
                cantidadVentas = rs.getInt(1);
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
             System.out.println("ErrorIm" +e.getMessage());
        }
        return cantidadVentas;
        
    }
    
        public int Graficar01(){
        int cantidadVentas = 0;
        String sql = "SELECT SUM(canvendet) FROM prueba where fecven ='2022-06-19'";
        try {
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {                
                cantidadVentas = rs.getInt(1);
            }
            st.close();
            rs.close();
        } catch (Exception e) {
             System.out.println("ErrorIm" +e.getMessage());
        }
        return cantidadVentas;
        
    }
        
        public int Graficar03(){
        int cantidadVentas = 0;
        String sql = "SELECT SUM(canvendet) FROM prueba where fecven ='2022-05-22'";
        try {
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {                
                cantidadVentas = rs.getInt(1);
            }
            st.close();
            rs.close();
        } catch (Exception e) {
             System.out.println("ErrorIm" +e.getMessage());
        }
        return cantidadVentas;
        
    }
}
