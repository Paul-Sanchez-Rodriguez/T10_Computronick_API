package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import modelo.Compra;
import modelo.Compra_Detalle;

import modelo.Productos;
import modelo.Proveedor;

public class CompraImpl extends Conexion implements ICRUD<Compra> {

    DateFormat sdfDate = new SimpleDateFormat("dd/MM/YYYY");

    @Override
    public void registrar(Compra com) throws Exception {
        try {
            String sql = "INSERT INTO COMPRA(FECCOM,TIPPAGCOM,IDPROV,IDADM) VALUES(?,?,?,?)";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, sdfDate.format(com.getFecha()));
            ps.setString(2, com.getTipoPago());
            ps.setInt(3, com.getCodigoProveedor());
            ps.setInt(4, com.getCodigoEmpleado());
            ps.executeUpdate();
            ps.close();
            
        } catch (Exception e) {
            System.out.println("Error al registrar en CompraImpl: " + e.getMessage());
        }
    }

    @Override
    public void modificar(Compra generic) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminar(Compra generic) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Compra> listar() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int maxCompra() {
        int nroCompra = 0;
        String sql = "SELECT MAX(IDCOM) FROM COMPRA";
        try {
            Statement vs = this.conectar().createStatement();
            ResultSet rs = vs.executeQuery(sql);
            while (rs.next()) {
                nroCompra = rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Error al Registrar maxCompra: " + e.getMessage());
        }
        return nroCompra;

    }

    public void registrarCompraDetalle(Compra_Detalle comDetalle) throws Exception {
        String sql = "INSERT INTO COMPRA_DETALLE(CANTCOMDET,IDCOM,CODPRO)\n"
                + "VALUES(?,?,?)";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, comDetalle.getCantCompra());
            ps.setInt(2, comDetalle.getCodCompra());
            ps.setString(3, comDetalle.getCodProducto());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error al registrar Compra_Detalle: " + e.getMessage());
        }
    }

    public void FiltrarProveedor(Proveedor pro) throws Exception {
        try {
            String sql = "SELECT * FROM PROVEEDOR WHERE NOMPREPROV=?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, pro.getNOMPREPROV());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pro.setIDPROV(rs.getInt("IDPROV"));
                pro.setRUC(rs.getString("RUCPROV"));
                pro.setNOMPREPROV(rs.getString("NOMPREPROV"));
                pro.setNOMBRE(rs.getString("NOMPROV"));
                pro.setAPELLIDO(rs.getString("APEPROV"));
                pro.setDIRECCION(rs.getString("DIRPROV"));
                pro.setCELULAR(rs.getString("CELPROV"));
                pro.setESTADO(rs.getString("ESTPROV"));
                pro.setCODIGOUBIGEO(rs.getString("CODUBI"));
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("Error al filtrarProveedorImpl: " + e.getMessage());
        }
    }

    public List<String> autocompletarProveedor(String consulta) throws Exception {

        Proveedor cli = new Proveedor();
        List<String> listado = new ArrayList<>();
        String sql = "Select * from PROVEEDOR WHERE NOMPREPROV LIKE ?";
        PreparedStatement ps = dao.Conexion.conectar().prepareCall(sql);
        try {

            ps.setString(1, consulta + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                listado.add(rs.getString("NOMPREPROV"));
            }

            rs.close();

        } catch (Exception e) {
            System.out.println("Error en ventaImpl " + e.getMessage());
        } finally {
            ps.close();
        }
        return listado;
    }

    public void mostrarDatos(Productos pro) throws Exception {
        try {
            String sql = "Select * from PRODUCTO where NOMPRO=?";
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

    public List<String> autocompletar(String consulta) throws Exception {

        Productos pro = new Productos();
        List<String> listado = new ArrayList<>();
        String sql = "Select * from PRODUCTO WHERE NOMPRO LIKE ? AND stopro > 0";
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

    public List<Compra> ListarCompra() throws Exception {
        List<Compra> listadoCompra = null;
        Compra com;
        String sql = "select * from ListaCompra";
        try {
            listadoCompra = new ArrayList<>();
            Statement ps = this.conectar().createStatement();
            ResultSet rs = ps.executeQuery(sql);
            while (rs.next()) {
                com = new Compra();
                com.setCodigoCompra(rs.getInt("IDCOM"));
                com.setCodigoProveedor(rs.getInt("IDPROV"));
                com.setFecha(rs.getDate("FECCOM"));
                com.setTipoPago(rs.getString("TIPPAGCOM"));
                com.setDatos(rs.getString("Nombre"));
                listadoCompra.add(com);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error al ListarCompra: " + e.getMessage());
        }

        return listadoCompra;
    }

    public void ActualizarStock(Compra_Detalle comPro) {
        int cant = comPro.getCantCompra();
        double preCompra = comPro.getPrecioCom();
        String codpro = comPro.getCodProducto();
        
        String sql = "UPDATE producto SET stopro = stopro + "+ cant +" where codpro = '"+codpro+"'";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en Actualizar Stock" + e.getMessage());
        }
    }
}
