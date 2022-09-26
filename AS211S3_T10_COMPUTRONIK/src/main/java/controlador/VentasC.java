package controlador;

import dao.LoginImpl;
import dao.VentaImpl;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import modelo.Cliente;
import modelo.Empleado;
import modelo.Productos;
import modelo.VentaDetalle;
import modelo.Ventas;
import reporte.reportesVenta;

@Named(value = "VentasC")
@SessionScoped
public class VentasC implements Serializable {

    // variables para la tabla venta
    private int item;
    private double subtotal;
    private double total;

    //modelos
    private Productos pro;
    private Cliente cli;
    private VentaImpl dao;
    private Ventas ven;
    private VentaDetalle vd;
    private Empleado emp;
    private LoginImpl login;
    //listados
    private List<Cliente> listado;
    private List<Productos> ListarProductos;
    private List<VentaDetalle> listarVentaDealle;
    private List<Ventas> listarventa;

    public VentasC() {
        cli = new Cliente();
        dao = new VentaImpl();
        emp = new Empleado();
        pro = new Productos();
        login = new LoginImpl();
        ven = new Ventas();
        vd = new VentaDetalle();
        listarVentaDealle = new ArrayList();
    }

    public void registrarVenta(int codigo) throws Exception {
        try {
            if (cli.getNombre() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Falta seleccionar el CLiente"));
            } else {
                if (listarVentaDealle.isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "no ha selecionados productos"));
                } else {
                    System.out.println("empleado de venta "+ codigo);
                    System.out.println("cliente de venta "+ cli.getCodigo());
                    ven.setCodigoEmpleado(codigo);
                    ven.setCodigoCliente(cli.getCodigo());
                    dao.registrar(ven);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Transaccion correcta"));
                    registrarDetalle();
                    limpiar();
                }

            }

        } catch (Exception e) {
            System.out.println("Error en regiistrarVentaC " + e.getMessage());
        }
    }

    public void registrarDetalle() throws Exception {

        int paul = dao.ventasMaximas();
//        dao.ventasMaximas(venDet);
        for (int i = 0; i < listarVentaDealle.size(); i++) {
            vd = new VentaDetalle();
            vd.setCantidad(listarVentaDealle.get(i).getCantidad());
            vd.setCodigoVenta(paul);
            vd.setCodigoPRoducto(listarVentaDealle.get(i).getCodigoPRoducto());

            dao.registrarVentaDetalle(vd);
            dao.ActualizarStock(vd);

        }
    }

    public void mostrarDatos() throws Exception {
        try {
            dao.mostrarDatos(pro);
//            listarVentaDealle.add(vd);
        } catch (Exception e) {
            System.out.println("Errror en mostrarC" + e.getMessage());
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

    public List<String> autocompleteCliente(String query) throws Exception {
        try {
            return dao.autocompletarCliente(query);

        } catch (Exception e) {
            System.out.println("Error en autocompletarclienteC " + e.getMessage());
            throw e;
        }
    }

    public void filtrado() throws Exception {
        try {
            System.out.println("?? " + cli.getNombrecompleto());
            dao.filtrarCliente(cli);
            System.out.println("?? " + cli.getNombre());
        } catch (Exception e) {
            System.out.println("Error en filtrar: " + e.getMessage());
        }
    }

    public void actualizarTablaTemporal(Productos producto) {
        try {
            int i = 0;
            for (VentaDetalle ventaDet : listarVentaDealle) {
                if (listarVentaDealle.get(i).getNuevacantidad() > 0) {
                    if (listarVentaDealle.get(i).getNuevacantidad() <= listarVentaDealle.get(i).getStock()) {
                        ventaDet.setCantidad(listarVentaDealle.get(i).getNuevacantidad());
                        ventaDet.setNuevacantidad(0);
                        ventaDet.setSubtotal(listarVentaDealle.get(i).getCantidad() * listarVentaDealle.get(i).getPrcio());
                        total = 0;

                        listarVentaDealle.set(i, ventaDet);
                        calcularTotalVenta();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "PRODUCTO MODIFICADO"));
                        break;
                    } else {
                        ventaDet.setNuevacantidad(0);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "CANTIDAD DE PRODUCTO NO DISPONIBLE"));
                    }

                }
                i++;
            }
        } catch (Exception e) {
            System.out.println("Error en actualizarTablaC " + e.getMessage());
        }
    }

    public void listarTablaTemporal() {

        total = 0.0;
        item = item + 1;
        subtotal = pro.getPrecio_venta() * pro.getCantidadVender();

        vd = new VentaDetalle();

        vd.setItem(item);
        vd.setCodigoPRoducto(pro.getCodigo());
        vd.setCantidad(pro.getCantidadVender());
        vd.setDescripcion(pro.getDescripcion());
        vd.setNompro(pro.getNombre());
        vd.setPrcio(pro.getPrecio_venta());
        vd.setStock(pro.getStock());
        vd.setSubtotal(subtotal);

        listarVentaDealle.add(vd);
        calcularTotalVenta();
        pro = new Productos();
    }

    public void calcularTotalVenta() {
        for (int i = 0; i < listarVentaDealle.size(); i++) {
            total = total + listarVentaDealle.get(i).getSubtotal();
        }
    }

    public void verReportePDFEST() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        int idven = dao.ventasMaximas();
        System.out.println("venta " + idven);
        String ruta ="Reportes/venta.jasper";
        reportesVenta reporte = new reportesVenta();
        FacesContext facescontext = FacesContext.getCurrentInstance();
        ServletContext servletcontext = (ServletContext) facescontext.getExternalContext().getContext();
        String root = servletcontext.getRealPath(ruta);
        String numeroinformesocial = String.valueOf(idven);
        reporte.getReportePdf(root, numeroinformesocial);
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void listarVenta() {
        try {
            listarventa = dao.ListarVentas();

        } catch (Exception e) {
            System.out.println("Error en controlador listarVenta" + e.getMessage());
        }
    }

    public void limpiar() {
        listarVentaDealle = new ArrayList();
        pro = new Productos();
        vd.setCantidad(0);
        cli = new Cliente();
        total = 0;
    }

    public void elimianarProductoAgregado(String Codigo) {
        int i = 0;
        try {
            for (VentaDetalle vent : listarVentaDealle) {
                if (vent.getCodigoPRoducto().equals(Codigo)) {
                    total = total - listarVentaDealle.get(i).getSubtotal() ;
                    listarVentaDealle.remove(i);
                    break;
                }
                i++;
            }
//            CalcularTotalVenta();
        } catch (Exception e) {
            System.out.println("Error en Eliminar Producto " + e.getMessage());
        }
    }

    public void validadorRepetido(Productos productos) {
        System.out.println("hola");
        int indice = 0;
        int cantidad = 0;
        if (productos.getCodigo() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "PRODUCTO NO SELECCIONADO"));
        } else {
            if (pro.getCantidadVender() > 0) {
                try {

                    if (listarVentaDealle.isEmpty()) {
                        if (pro.getCantidadVender() > pro.getStock()) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Cantidad de producto no disponible"));

                        } else {
                            listarTablaTemporal();
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Producto agregado con éxito"));
                        }

                    } else {
                        for (VentaDetalle ventaDetalle : listarVentaDealle) {
                            if (ventaDetalle.getCodigoPRoducto().equals(productos.getCodigo())) {
                                cantidad = listarVentaDealle.get(indice).getCantidad() + productos.getCantidadVender();
                                if (cantidad <= pro.getStock()) {

                                    subtotal = listarVentaDealle.get(indice).getPrcio() * cantidad;
                                    total = 0;
                                    ventaDetalle.setCantidad(cantidad);
                                    ventaDetalle.setSubtotal(subtotal);

                                    listarVentaDealle.set(indice, ventaDetalle);
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Producto agregado con éxito"));

                                    calcularTotalVenta();
                                    pro = new Productos();
                                    break;

                                } else {
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "OK", "cantidad de producto no disponible"));
                                }

                            } else {
                                indice++;
                                if (indice == listarVentaDealle.size()) {
                                    if (pro.getCantidadVender() > pro.getStock()) {
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Cantidad de producto no disponible"));

                                    } else {
                                        listarTablaTemporal();
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Producto agregado con éxito"));
                                        break;
                                    }
                                }

                            }

                        }

                    }

                } catch (Exception e) {
                    System.out.println("error en calidarProducto " + e.getMessage());
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Ingrese la la cantidad de venta"));
            }

        }

    }


    public Cliente getCli() {
        return cli;
    }

    public void setCli(Cliente cli) {
        this.cli = cli;
    }

    public VentaImpl getDao() {
        return dao;
    }

    public void setDao(VentaImpl dao) {
        this.dao = dao;
    }

    public List<Cliente> getListado() {

        return listado;
    }

    public void setListado(List<Cliente> listado) {
        this.listado = listado;
    }

    public Productos getPro() {
        return pro;
    }

    public void setPro(Productos pro) {
        this.pro = pro;
    }

    public List<Productos> getListarProductos() {
        return ListarProductos;
    }

    public void setListarProductos(List<Productos> ListarProductos) {
        this.ListarProductos = ListarProductos;
    }

    public List<VentaDetalle> getListarVentaDealle() {
        return listarVentaDealle;
    }

    public void setListarVentaDealle(List<VentaDetalle> listarVentaDealle) {
        this.listarVentaDealle = listarVentaDealle;
    }

    public Ventas getVen() {
        return ven;
    }

    public void setVen(Ventas ven) {
        this.ven = ven;
    }

    public VentaDetalle getVd() {
        return vd;
    }

    public void setVd(VentaDetalle vd) {
        this.vd = vd;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<Ventas> getListarventa() {
        return listarventa;
    }

    public void setListarventa(List<Ventas> listarventa) {
        this.listarventa = listarventa;
    }

    public Empleado getEmp() {
        return emp;
    }

    public void setEmp(Empleado emp) {
        this.emp = emp;
    }

    public LoginImpl getLogin() {
        return login;
    }

    public void setLogin(LoginImpl login) {
        this.login = login;
    }

}
