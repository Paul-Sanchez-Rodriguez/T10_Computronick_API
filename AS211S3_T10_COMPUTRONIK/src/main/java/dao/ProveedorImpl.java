package dao;

import dao.Conexion;
import dao.ICRUD;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Proveedor;
import modelo.Ubigeo;

public class ProveedorImpl extends Conexion implements ICRUD<Proveedor> {

    @Override
    public void registrar(Proveedor proveedor) throws Exception {
        try {
            String sql = "INSERT INTO PROVEEDOR"
                    + "(RUCPROV,NOMPREPROV,NOMPROV,APEPROV,DIRPROV,CELPROV,ESTPROV,CODUBI)"
                    + "VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, proveedor.getRUC());
            ps.setString(2, proveedor.getNOMPREPROV());
            ps.setString(3, proveedor.getNOMBRE());
            ps.setString(4, proveedor.getAPELLIDO());
            ps.setString(5, proveedor.getDIRECCION());
            ps.setString(6, proveedor.getCELULAR());
            ps.setString(7, proveedor.getESTADO());
            ps.setString(8, proveedor.getCODIGOUBIGEO());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en ProveedorImpl/registrar: " + e.getMessage());
        }

    }

    @Override
    public void modificar(Proveedor proveedor) throws Exception {
        try {
            String sql = "UPDATE PROVEEDOR SET RUCPROV = ?,NOMPREPROV=?, NOMPROV =? ,APEPROV =?,DIRPROV =?,CELPROV =? ,ESTPROV =?WHERE IDPROV =?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setString(1, proveedor.getRUC());
            ps.setString(2, proveedor.getNOMPREPROV());
            ps.setString(3, proveedor.getNOMBRE());
            ps.setString(4, proveedor.getAPELLIDO());
            ps.setString(5, proveedor.getDIRECCION());
            ps.setString(6, proveedor.getCELULAR());
            ps.setString(7, proveedor.getESTADO());
            ps.setInt(8, proveedor.getIDPROV());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en ProveedorImpl/modificar: " + e.getMessage());
        }

    }

    @Override
    public void eliminar(Proveedor proveedor) throws Exception {
        try {
            String sql = "update PROVEEDOR set estprov= 'I' WHERE IDPROV = ?";
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            ps.setInt(1, proveedor.getIDPROV());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en Proveedor/eliminar: " + e.getMessage());
        }
    }

    public void habilitar(Proveedor pro) throws Exception {
        String sql = "update proveedor set estprov=? where idprov=?";
        PreparedStatement ps = this.conectar().prepareStatement(sql);
        try {
            ps.setString(1, "A");
            ps.setInt(2, pro.getIDPROV());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error en habilitar producto " + e.getMessage());
        }

    }

//    @Override
    public List<Proveedor> listar(int caso) throws Exception {
        List<Proveedor> lista = null;
        ResultSet rs;
        String sql ="";
        switch (caso) {
            case 1:
                sql = "SELECT * FROM datosProveedor WHERE ESTPROV = 'A'";
                break;
            case 2:
                sql = "SELECT * FROM datosProveedor WHERE ESTPROV = 'I'";
                break;
            case 3:
                sql = "SELECT * FROM datosProveedor";
                break;
            default:
                System.out.println("no se listo adecuadamente");
        }
        lista = new ArrayList<>();
        try {
            PreparedStatement ps = this.conectar().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Proveedor pro = new Proveedor();
                pro.setIDPROV(rs.getInt("IDPROV"));
                pro.setRUC(rs.getString("RUCPROV"));
                pro.setNOMPREPROV(rs.getString("NOMPREPROV"));
                pro.setNOMBRE(rs.getString("NOMPROV"));
                pro.setAPELLIDO(rs.getString("APEPROV"));
                pro.setDIRECCION(rs.getString("DIRPROV"));
                pro.setCELULAR(rs.getString("CELPROV"));
                pro.setESTADO(rs.getString("ESTPROV"));
                pro.setCODIGOUBIGEO(rs.getString("CODUBI"));
                pro.setDISUBI(rs.getString("DISUBI"));
                lista.add(pro);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error ProveedorImpl/listar: " + e.getMessage());
        }
        return lista;
    }

    public List<String> autocompleteUbigeo(String consulta) throws SQLException, Exception {
        List<String> lista = new ArrayList<>();
        String sql = "select top 10 concat(DEPUBI,',', PROUBI,',',DISUBI) AS UBIGEODESC "
                + "from UBIGEO WHERE DISUBI LIKE ?";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setString(1, "%" + consulta + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(rs.getString("UBIGEODESC"));
            }
        } catch (Exception e) {
            System.out.println("Error en autocompleteUbigeo: " + e.getMessage());
        }
        return lista;
    }

    public String obtenercodigoUbi(String cadenaubi) throws SQLException, Exception {
        String sql = "select CODUBI FROM UBIGEO WHERE concat(DEPUBI, ', ', PROUBI, ', ',DISUBI) = ?";
        try {
            PreparedStatement ps = this.conectar().prepareCall(sql);
            ps.setString(1, cadenaubi);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("CODUBI");
            }
            return rs.getString("CODUBI");
        } catch (Exception e) {
            System.out.println("Error en obtenercodigoUbi: " + e.getMessage());
            throw e;
        }

    }

    public List listaUbigeo() {
        List<Ubigeo> listarUbi = null;
        String sql = "select * from UBIGEO";
        try {
            listarUbi = new ArrayList<>();
            Statement st = this.conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Ubigeo ubi = new Ubigeo();
                ubi.setCodigo(rs.getString("CODUBI"));
                ubi.setDepubi(rs.getString("DEPUBI"));
                ubi.setProvubi(rs.getString("PROUBI"));
                ubi.setDisubi(rs.getString("DISUBI"));
                listarUbi.add(ubi);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error al listar Ubi/ProovedorImpl: " + e.getMessage());
        }
        return listarUbi;
    }

    @Override
    public List<Proveedor> listar() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
