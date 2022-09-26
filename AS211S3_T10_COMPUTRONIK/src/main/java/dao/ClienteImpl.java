package dao;

//import com.mysql.jdbc.Statement;
import java.sql.Statement;
import java.util.List;
import modelo.Cliente;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import modelo.Ubigeo;

//import javax.resource.cci.ResultSet;
public class ClienteImpl extends Conexion implements ICRUD<Cliente> {

    @Override
    public void registrar(Cliente cliente) throws Exception {
        try {
                String sql = "insert into PERSONA"
                        + " (NOMPER,APEPER,DNIPER,CELPER,DIRPER,ROLPER,ESTPER,USUPER,CONPER,CODUBI)"
                        + " values (?,?,?,?,?,?,?,?,?,?) ";
                PreparedStatement ps = this.conectar().prepareStatement(sql);
                ps.setString(1, cliente.getNombre());
                ps.setString(2, cliente.getApellido());
                ps.setString(3, cliente.getDni());
                ps.setString(4, cliente.getCelular());
                ps.setString(5, cliente.getDirreccion());
                ps.setString(6, cliente.getRolper());
                ps.setString(7, cliente.getEstado());
                ps.setString(8, cliente.getUsername());
                ps.setString(9, cliente.getPassword());
                ps.setString(10, cliente.getUbigeo());
                ps.executeUpdate();
                ps.close();
            
        } catch (Exception e) {
            System.out.println("Error en registrar Cliente GAAAA: " + e.getMessage());
        }
    }

    @Override
    public void modificar(Cliente cli) throws Exception {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String sql = "update PERSONA set NOMPER=?, APEPER=?,DNIPER=?,CELPER=?,DIRPER=?,ESTPER=?,CODUBI=? where IDPER=?";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, cli.getNombre());
            ps.setString(2, cli.getApellido());
            ps.setString(3, cli.getDni());
            ps.setString(4, cli.getCelular());
            ps.setString(5, cli.getDirreccion());
            ps.setString(6, cli.getEstado());
            ps.setString(7, cli.getUbigeo());
            ps.setInt(8, cli.getCodigo());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error al Modificar Cliente: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(Cliente cli) throws Exception {
        try {
            String sql = "update PERSONA set ESTPER=? where IDPER=?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, cli.getEstado());
            ps.setInt(2, cli.getCodigo());
            ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
            System.out.println("Error en Eliminar Cliente: " + e.getMessage());
        }
    }
    
    public void habilitarCli(Cliente cli)throws Exception{
    String sql = "update PERSONA set ESTPER=? where IDPER=?";
    PreparedStatement ps = this.conectar().prepareStatement(sql);
        try {
            ps.setString(1,cli.getEstado());
            ps.setInt(2,cli.getCodigo());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error en habiliar " +e.getMessage());
        }
    }
    public List listar(int caso) throws Exception {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Cliente cli;
        List<Cliente> listado = null;
        String sql = "";
        switch (caso) {
            case 1:
                sql = "SELECT * FROM datosClientes WHERE ESTPER = 'A'";
                break;
            case 2:
                sql = "SELECT * FROM datosClientes WHERE ESTPER = 'I'";
                break;
            case 3:
                sql = "SELECT * FROM datosClientes";
                break;
        }

        try {
            listado = new ArrayList();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                cli = new Cliente();
                cli.setCodigo(rs.getInt("IDPER"));
                cli.setNombre(rs.getString("NOMPER"));
                cli.setApellido(rs.getString("APEPER"));
                cli.setDni(rs.getString("DNIPER"));
                cli.setCelular(rs.getString("CELPER"));
                cli.setDirreccion(rs.getString("DIRPER"));
                cli.setEstado(rs.getString("ESTPER"));
                cli.setUbigeo(rs.getString("CODUBI"));
                cli.setDistrito(rs.getString("DISUBI"));

                listado.add(cli);

            }
            rs.close();
            st.close();

        } catch (Exception e) {
            System.out.println("Error en listar Cliente " + e.getMessage());
        }
        return listado;
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

    @Override
    public List<Cliente> listar() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
