package controlador;

import dao.CompraImpl;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import modelo.Compra;
import modelo.Compra_Detalle;
import modelo.Empleado;
import modelo.Productos;
import modelo.Proveedor;
import reporte.reportesCompras;
import reporte.reportesVenta;

@Named(value = "CompraC")
@SessionScoped
public class CompraC implements Serializable {

    // variables para la tablaTemporal compra
    private double total;
    private int itemCom;
    private double subtotalCompra;

    //modelo
    private Proveedor proveedor;
    private Productos pro;
    private CompraImpl dao;
    private Compra compra;
    private Compra_Detalle comDetalle;
    private Empleado emp;
    //listado de modelo
    private List<Proveedor> listProveedor;
    private List<Productos> listProducto;
    private List<Compra_Detalle> listDetalle;
    private List<Compra> listadoCompra;

    public CompraC() {

        System.out.println("Iniciando constructor");
        pro = new Productos();
        this.proveedor = new Proveedor();
//        this.emp = new Empleado();
        this.dao = new CompraImpl();
        this.compra = new Compra();
        this.comDetalle = new Compra_Detalle();
        this.listDetalle = new ArrayList();
    }

    public void registroCompra(int idEmpleado) throws Exception {
        try {
            System.out.println("empleado " + idEmpleado);
            compra.setCodigoEmpleado(idEmpleado);
            compra.setCodigoProveedor(proveedor.getIDPROV());
            dao.registrar(compra);
            registroDetalle();
            limpiar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "se ha registrado"));

        } catch (Exception e) {
            System.out.println("Error al registrar CompraC: " + e.getMessage());
        }
    }

    public void registroDetalle() throws Exception {
        int numeroFilas = dao.maxCompra();
        for (int i = 0; i < listDetalle.size(); i++) {
            comDetalle = new Compra_Detalle();
            comDetalle.setCantCompra(listDetalle.get(i).getCantCompra());
            comDetalle.setCodCompra(numeroFilas);
            comDetalle.setCodProducto(listDetalle.get(i).getCodProducto());
            dao.registrarCompraDetalle(comDetalle);
            dao.ActualizarStock(comDetalle);
        }
    }

    public void filtrar() throws Exception {
        try {
            System.out.println("estoy pasando por aqui");
            dao.FiltrarProveedor(proveedor);

        } catch (Exception e) {
            System.out.println("Error en FiltrarProovedorC: " + e.getMessage());
        }

    }

    public List<String> autocompleteProveedor(String query) throws Exception {
        try {
            return dao.autocompletarProveedor(query);

        } catch (Exception e) {
            System.out.println("Error en autocompletarclienteC " + e.getMessage());
            throw e;
        }
    }

    public void buscarProductos() throws Exception {
        try {
            pro.setNombre(pro.getNombre());
            dao.mostrarDatos(pro);
            System.out.println(pro.getCodigo());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Se busco correctamente el producto"));
        } catch (Exception e) {
            System.out.println("Error al buscarProductos: " + e.getMessage());
        }
    }

    public List<String> autocompletePrueba(String query) throws Exception {
        try {

            return dao.autocompletar(query);
        } catch (Exception e) {
            System.out.println("Erro please" + e.getMessage());
            throw e;
        }
    }

    public void tablaTemporal() {
        total = 0.0;
        itemCom = itemCom + 1;
        subtotalCompra = pro.getPrecio_compra() * pro.getCantidadCompra();

        comDetalle = new Compra_Detalle();

        comDetalle.setItemCom(itemCom);
        comDetalle.setCodProducto(pro.getCodigo());
        comDetalle.setNombreCom(pro.getNombre());
        comDetalle.setPrecioCom(pro.getPrecio_compra());
        comDetalle.setDescCompra(pro.getDescripcion());
        comDetalle.setCantCompra(pro.getCantidadCompra());
        comDetalle.setSubtotalCompra(subtotalCompra);
        listDetalle.add(comDetalle);
        calcularTotal();
        pro = new Productos();

    }

    public void verReportePDFEST() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        int idven = dao.maxCompra();
        System.out.println("venta " + idven);
        reportesCompras reporte = new reportesCompras();
        FacesContext facescontext = FacesContext.getCurrentInstance();
        ServletContext servletcontext = (ServletContext) facescontext.getExternalContext().getContext();
        String root = servletcontext.getRealPath("Reportes/Boleta.jasper");
        String numeroinformesocial = String.valueOf(idven);
        reporte.getReportePdf(root, numeroinformesocial);
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void actualizarTablaTemporal() {
        try {
            int i = 0;
            for (Compra_Detalle comDet : listDetalle) {
                if (listDetalle.get(i).getNuevaCantidad() > 0) {
                    comDet.setCantCompra(listDetalle.get(i).getNuevaCantidad());
                    comDet.setNuevaCantidad(0);
                    comDet.setSubtotalCompra(listDetalle.get(i).getCantCompra() * listDetalle.get(i).getPrecioCom());
                    total = 0;
                    listDetalle.set(i, comDet);
                    calcularTotal();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "CANTIDADdaaa MODIFICADO"));
                    break;
                }
                i++;
            }
        } catch (Exception e) {
            System.err.println("Error en actualizar cantidad " + e.getMessage());
        }

    }

    public void calcularTotal() {
        for (int i = 0; i < listDetalle.size(); i++) {
            total = total + listDetalle.get(i).getSubtotalCompra();
        }
    }

    public void eliminarProducto(String Codigo) {
        int i = 0;
        try {
            for (Compra_Detalle detalle : listDetalle) {
                if (detalle.getCodProducto().equals(Codigo)) {
                    total = total - listDetalle.get(i).getSubtotalCompra();
                    listDetalle.remove(i);
                    break;
                }
                i++;
            }
        } catch (Exception e) {
            System.out.println("Error al eliminarProducto: " + e.getMessage());
        }
    }

    public void listarCom() {
        try {
            listadoCompra = dao.ListarCompra();
        } catch (Exception e) {
            System.out.println("Error el ListarCom/Controlador: " + e.getMessage());
        }
    }

    public void productoNull() {
        if (pro.getCodigo() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccionar producto"));
        } else {
            cantidadNull();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Producto seleccionado"));
        }
    }

    public void cantidadNull() {
        if (pro.getCantidadCompra() == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese cantidad"));
        } else {
            productoRepit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Se ingreso cantidad"));
        }
    }

    public void productoRepit() {
        int indice = 0;
        if (listDetalle.isEmpty()) {
            tablaTemporal();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Producto agregado con exito"));
        } else {
            for (Compra_Detalle comDet : listDetalle) {
                if (comDet.getCodProducto().equals(pro.getCodigo())) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Producto ya registrado"));
                } else {
                    indice++;
                    if (indice == listDetalle.size()) {
                        tablaTemporal();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Producto agregado con éxito"));
                        break;
                    }
                }
            }
        }
    }

    public void limpiar() {
        listDetalle = new ArrayList();
        pro = new Productos();
        comDetalle.setCantCompra(0);
        proveedor = new Proveedor();
        total = 0;
    }

    //metodos gettes and setters
    public int getItemCom() {
        return itemCom;
    }

    public void setItemCom(int itemCom) {
        this.itemCom = itemCom;
    }

    public double getSubtotalCompra() {
        return subtotalCompra;
    }

    public void setSubtotalCompra(double subtotalCompra) {
        this.subtotalCompra = subtotalCompra;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public CompraImpl getDao() {
        return dao;
    }

    public void setDao(CompraImpl dao) {
        this.dao = dao;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Compra_Detalle getComDetalle() {
        return comDetalle;
    }

    public void setComDetalle(Compra_Detalle comDetalle) {
        this.comDetalle = comDetalle;
    }

    public List<Proveedor> getListProveedor() {
        return listProveedor;
    }

    public void setListProveedor(List<Proveedor> listProveedor) {
        this.listProveedor = listProveedor;
    }

    public List<Productos> getListProducto() {
        return listProducto;
    }

    public void setListProducto(List<Productos> listProducto) {
        this.listProducto = listProducto;
    }

    public List<Compra_Detalle> getListDetalle() {
        return listDetalle;
    }

    public void setListDetalle(List<Compra_Detalle> listDetalle) {
        this.listDetalle = listDetalle;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<Compra> getListadoCompra() {
        return listadoCompra;
    }

    public void setListadoCompra(List<Compra> listadoCompra) {
        this.listadoCompra = listadoCompra;
    }

    public Productos getPro() {
        return pro;
    }

    public void setPro(Productos pro) {
        this.pro = pro;
    }

    public Empleado getEmp() {
        return emp;
    }

    public void setEmp(Empleado emp) {
        this.emp = emp;
    }

}
