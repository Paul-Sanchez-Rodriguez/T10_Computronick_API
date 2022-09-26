package dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import modelo.Cliente;
import modelo.Ventas;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import modelo.Productos;
import modelo.VentaDetalle;

public class VentaImpl extends Conexion implements ICRUD<Ventas> {
    DateFormat formato = new SimpleDateFormat("dd-MM-YYYY");
    
    @Override
    public void registrar(Ventas ven) throws Exception {
        try {
            String sql = "insert into VENTA"
                    + "(FECVEN, TIPPAG,IDCLI,IDVED)"
                    + "VALUES (?,?,?,?)";

            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1,  formato.format(ven.getFecha()));
            ps.setString(2, ven.getTipoDePago());
            ps.setInt(3, ven.getCodigoCliente());
            ps.setInt(4, ven.getCodigoEmpleado());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en registrar Venta DAO" + e.getMessage());
        }
    }

    @Override
    public void modificar(Ventas generic) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminar(Ventas generic) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Ventas> listar() throws Exception {
        //throws new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return null;
    }

    public List<String> autocompletar(String consulta) throws Exception {

        Productos pro = new Productos();
        List<String> listado = new ArrayList<>();
        String sql = "Select * from PRODUCTO WHERE NOMPRO LIKE ?";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setString(1, consulta + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pro.setNombre(rs.getString("NOMPRO"));
                listado.add(rs.getString("NOMPRO"));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en autocompletar Producto " + e.getMessage());
        }
        return listado;

    }

    public void mostrarDatos(Productos pro) throws Exception {
        try {
            String sql = "Select * from PRODUCTO where NOMPRO =?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, pro.getNombre());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pro.setCodigo(rs.getString("CODPRO"));
                pro.setNombre(rs.getString("NOMPRO"));
                pro.setMarca(rs.getString("MARPRO"));
                pro.setPrecio_compra(rs.getDouble("PRECOMPRO"));
                pro.setPrecio_venta(rs.getDouble("PREVENPRO"));
                pro.setStock(rs.getInt("STOPRO"));
                pro.setDescripcion(rs.getString("DESPRO"));
                pro.setEstado(rs.getString("ESTPRO"));
                pro.setImagen(rs.getString("IMAPRO"));
            }
            rs.close();
            ps.close();

        } catch (Exception e) {
            System.out.println("Error en mostrar datos" + e.getMessage());
        }
    }

    public List<String> autocompletarCliente(String consulta) throws Exception {

        Cliente cli = new Cliente();
        List<String> listado = new ArrayList<>();
        String sql = "Select * from Cliente_Venta WHERE NOMBRE_APELLIDO LIKE ?";
        PreparedStatement ps = dao.Conexion.conectar().prepareCall(sql);
        try {

            ps.setString(1, consulta + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               
                listado.add(rs.getString("NOMBRE_APELLIDO"));
            }

            rs.close();

        } catch (Exception e) {
            System.out.println("Error en ventaImpl " +e.getMessage());
        } finally {
            ps.close();
        }
        return listado;
    }
    public void filtrarCliente(Cliente cli) throws Exception {
           String sql = "select * from Cliente_Venta where NOMBRE_APELLIDO=?";
        PreparedStatement ps = dao.Conexion.conectar().prepareStatement(sql);
        try {
            ps.setString(1, cli.getNombrecompleto());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cli.setCodigo(rs.getInt("IDPER"));
                cli.setNombre(rs.getString("NOMBRE_APELLIDO"));
                cli.setDni(rs.getString("DNIPER"));
                cli.setCelular(rs.getString("CELPER"));
                cli.setDirreccion(rs.getString("DIRPER"));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("Error en ventaImpl " +e.getMessage());
        }

    }

    public int ventasMaximas(){
        int nroVentas = 0;
        String sql = "SELECT MAX(IDVEN) FROM VENTA";
        try {
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                nroVentas = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Error en ventas Maximas" + e.getMessage());
        }
        return nroVentas;

    }
    
    public void registrarVentaDetalle(VentaDetalle Vendet) throws Exception {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String sql = "INSERT INTO VENTA_DETALLE"
                +"(CANVENDET,IDVEN,CODPRO)"
                +"VALUES (?,?,?)";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1,Vendet.getCantidad());
            ps.setInt(2,Vendet.getCodigoVenta());
            ps.setString(3,Vendet.getCodigoPRoducto());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en registrar VentasDetalle " +e.getMessage());
        }
    }

    public List<Ventas> ListarVentas() throws Exception {
        List<Ventas> listadoVenta = null;
        Ventas vd;
        String sql = "select * from VentasDatos";
        try {
            
            listadoVenta = new ArrayList<>();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                vd = new Ventas();
                vd.setCodigoVenta(rs.getInt("IDVEN"));
                vd.setFecha(rs.getDate("FECVEN"));
                vd.setTipoDePago(rs.getString("TIPPAG"));
                vd.setCodigoCliente(rs.getInt("IDCLI"));
                vd.setDatos(rs.getString("nombre"));
                listadoVenta.add(vd);
            }
            rs.close();
            st.close();

        } catch (Exception e) {
            System.out.println("Error en kistar Producto DAO" + e.getMessage());
        }
        return listadoVenta;

    }
    
    public void ActualizarStock(VentaDetalle vd) {
        int cant = vd.getCantidad();
        String codpro = vd.getCodigoPRoducto();
        String sql = "UPDATE producto SET stopro = stopro - '" + cant + "' where codpro = '" + codpro + "'";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en Actualizar Stock" + e.getMessage());
        }
    }
}
