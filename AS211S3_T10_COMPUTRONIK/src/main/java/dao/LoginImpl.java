package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Cliente;
import modelo.Ubigeo;

public class LoginImpl extends Conexion implements ICRUD<Cliente> {


//    public void registrar(Login login) throws Exception {
//       
//    }

    
//    public List<Login> listar() throws Exception {
//        List<Login> listaLogin = null;
//        Login log;
//        String sql = "select * from Usuario";
//        listaLogin = new ArrayList<>();
//
//        try (Statement st = dao.Conexion.conectar().createStatement()) {
//            ResultSet rs = st.executeQuery(sql);
//            while (rs.next()) {
//                log = new Login();
//                log.setIdcli(rs.getInt("IDUSU"));
//                log.setUsername(rs.getString("USUCLI"));
//                log.setPassword(rs.getString("CONCLI"));
//                log.setCategoria(rs.getString("NIVUSUCLI"));
//                listaLogin.add(log);
//            }
//            rs.close();
//        } catch (Exception e) {
//            System.out.println("Error al listarLogin: " +e.getMessage());
//        }
//        return listaLogin;
//    }

    public boolean verificar(Cliente cli) {

        String sql = "SELECT * FROM PERSONA WHERE USUPER =? AND CONPER =?";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setString(1, cli.getUsername());
            ps.setString(2, cli.getPassword());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cli.setRolper(rs.getString("ROLPER"));
                cli.setCodigo(rs.getInt("IDPER"));
                cli.setNombre(rs.getString("NOMPER"));
                cli.setApellido(rs.getString("APEPER"));
                return true;
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("Error en LoginImpl " + e.getMessage());
        }
        return false;

    }

    public void cliente(Cliente cli , int idcli) {
        String sql = "SELECT * FROM CLIENTE WHERE IDCLI =?";
        
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setInt(1, idcli);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cli.setCodigo(rs.getInt("IDCLI"));
                cli.setNombre(rs.getString("NOMCLI"));
                cli.setApellido(rs.getString("APECLI"));
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("error en LoginImpl cliente " +e.getMessage());
        }
    }

    @Override
    public void registrar(Cliente cli) throws Exception {
        try {
            String sql = "insert into PERSONA"
                        + " (NOMPER,APEPER,DNIPER,CELPER,DIRPER,ROLPER,ESTPER,USUPER,CONPER,CODUBI)"
                        + " values (?,?,?,?,?,?,?,?,?,?) ";
                PreparedStatement ps = this.conectar().prepareStatement(sql);
                ps.setString(1, cli.getNombre());
                ps.setString(2, cli.getApellido());
                ps.setString(3, cli.getDni());
                ps.setString(4, cli.getCelular());
                ps.setString(5, cli.getDirreccion());
                ps.setString(6, cli.getRolper());
                ps.setString(7, cli.getEstado());
                ps.setString(8, cli.getUsername());
                ps.setString(9, cli.getPassword());
                ps.setString(10, cli.getUbigeo());
                ps.executeUpdate();
                ps.close();
            
        } catch (Exception e) {
            System.out.println("Error al registrar LoginImpl: " + e.getMessage());
        }
    }

    @Override
    public void modificar(Cliente generic) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminar(Cliente generic) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Cliente> listar() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

        public List listarUbi() throws Exception {
        List<Ubigeo> listUbig = null;

        Ubigeo ubi;
        String sql = "select * from UBIGEO";
        try {
            listUbig = new ArrayList();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                ubi = new Ubigeo();
                ubi.setCodigo(rs.getString("CODUBI"));
                ubi.setDepubi(rs.getString("DEPUBI"));
                ubi.setProvubi(rs.getString("PROUBI"));
                ubi.setDisubi(rs.getString("DISUBI"));
                listUbig.add(ubi);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en dao listar Ubigeo" + e.getMessage());
        }
        return listUbig;

    } 
}
