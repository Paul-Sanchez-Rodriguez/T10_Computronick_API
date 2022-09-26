package dao;

import java.sql.Array;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import modelo.Productos;
import java.sql.ResultSet;
import modelo.Categoria;
import modelo.SubCategoria;

public class ProductoImpl extends Conexion implements ICRUD<Productos> {

    @Override
    public void registrar(Productos pro) throws Exception {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        try {
            String sql = "insert into producto"
                    + "(CODPRO,NOMPRO,MARPRO,PRECOMPRO,PREVENPRO,STOPRO,DESPRO,ESTPRO,IMAPRO,IDSUBFAM)"
                    + " values (?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, pro.getCodigo());
            ps.setString(2, pro.getNombre());
            ps.setString(3, pro.getMarca());
            ps.setDouble(4, pro.getPrecio_compra());
            ps.setDouble(5, pro.getPrecio_venta());
            ps.setInt(6, pro.getStock());
            ps.setString(7, pro.getDescripcion());
            ps.setString(8, "A");
            ps.setString(9, pro.getImagen());
            ps.setInt(10, pro.getCategoria());
            ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
            System.out.println("Error en registrar Productos: " + e.getMessage());
        }
    }

    @Override
    public void modificar(Productos pro) throws Exception {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        try {
            String sql = "UPDATE PRODUCTO SET "
                    + "NOMPRO=?,MARPRO=?,PRECOMPRO=?,PREVENPRO=?,STOPRO=?,DESPRO=?,ESTPRO=?,IMAPRO=?"
                    + " WHERE CODPRO =?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, pro.getNombre());
            ps.setString(2, pro.getMarca());
            ps.setDouble(3, pro.getPrecio_compra());
            ps.setDouble(4, pro.getPrecio_venta());
            ps.setInt(5, pro.getStock());
            ps.setString(6, pro.getDescripcion());
            ps.setString(7, pro.getEstado());
            ps.setString(8, pro.getImagen());
            ps.setString(9, pro.getCodigo());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en modificar producto" + e.getMessage());
        }
    }

    @Override
    public void eliminar(Productos pro) throws Exception {
        String sql = "update producto set Estpro=? where codpro=?";
        PreparedStatement ps = this.conectar().prepareStatement(sql);
        try {
            ps.setString(1, pro.getEstado());
            ps.setString(2, pro.getCodigo());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error en elimnar " + e.getMessage());
        }
    }

    public void habilitar(Productos pro) throws Exception {
        String sql = "update producto set estpro=? where codpro=?";
        PreparedStatement ps = this.conectar().prepareStatement(sql);
        try {
            ps.setString(1, "A");
            ps.setString(2, pro.getCodigo());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error en habilitar producto " + e.getMessage());
        }

    }

    public List listar(int caso) throws Exception {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        List<Productos> listado = null;
        String sql = "";
        Productos prov;
        switch (caso) {
            case 1:
                sql = "select * from PRODUCTO";
                break;
            case 2:
                sql = "select * from PRODUCTO where ESTPRO = 'A'";
                break;
            case 3:
                sql = "select * from PRODUCTO where ESTPRO = 'I'";
                break;
            default:
                System.out.println("Error en los casos de listar");
        }
        try {
            listado = new ArrayList();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                prov = new Productos();
                prov.setCodigo(rs.getString("CODPRO"));
                prov.setNombre(rs.getString("NOMPRO"));
                prov.setMarca(rs.getString("MARPRO"));
                prov.setPrecio_compra(rs.getDouble("PRECOMPRO"));
                prov.setPrecio_venta(rs.getDouble("PREVENPRO"));
                prov.setStock(rs.getInt("STOPRO"));
                prov.setDescripcion(rs.getString("DESPRO"));
                prov.setEstado(rs.getString("ESTPRO"));
                prov.setImagen(rs.getString("IMAPRO"));

                listado.add(prov);

            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en lsistarImpl" + e.getMessage());
        }
        return listado;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
public List<Categoria> listarCategoria() throws Exception {
        List<Categoria> listaCat = null;
        Categoria cat;
        String sql = "Select * from FAMILIA";
        listaCat = new ArrayList<>();

        try (Statement st = dao.Conexion.conectar().createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                cat = new Categoria();
                cat.setNombre(rs.getString("NOMFAM"));
                cat.setId(rs.getInt("IDFAM"));
                listaCat.add(cat);
            }
            rs.close();
        } catch (Exception e) {
           
        }
        return listaCat;
    }

    public List<SubCategoria> Subfamilia(int id) throws Exception {
        List<SubCategoria> subfam = null;
        String sql = "Select * from SUBFAMILIA where IDFAM=?";
        SubCategoria sub;
        subfam = new ArrayList<>();
        PreparedStatement ps = this.conectar().prepareStatement(sql);
        try {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sub = new SubCategoria();
                sub.setId(rs.getInt("IDSUBFAM"));
                sub.setNombre(rs.getString("NOMSUBFAM"));
                subfam.add(sub);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en listar Subfamilia" + e.getMessage());
        }
        return subfam;

    }
    public void actualizarEstado(String codigo) {

        String sql = "update Producto set estpro = 'I' where codpro=?";
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, codigo);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en actualizar estado cuando este en cero " + e.getMessage());
        }

    }

    @Override
    public List<Productos> listar() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
