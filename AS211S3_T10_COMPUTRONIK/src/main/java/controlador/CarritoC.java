/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import dao.ProductoImpl;
import dao.VentaImpl;
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
import modelo.Cliente;
import modelo.Productos;
import modelo.VentaDetalle;
import modelo.Ventas;
import reporte.reportesVenta;

@Named(value = "CarritoC")
@SessionScoped
public class CarritoC implements Serializable {

    private int item;
    private double subtotal;
    private double total;
    private VentaDetalle vd;
    private Ventas ve;
    private List<VentaDetalle> listarVentaDealle;
    private ProductoImpl ProductoImpl;
    private VentaImpl dao;
    private Productos pro;
    private Cliente cli;
    private List<Productos> listadoProd;

    public CarritoC() {
        dao = new VentaImpl();
        pro = new Productos();
        vd = new VentaDetalle();
        ve = new Ventas();
        cli = new Cliente();
        ProductoImpl = new ProductoImpl();
        listarVentaDealle = new ArrayList();
    }

    public void registrarVenta(int codigo) throws Exception {
        try {
            if (listarVentaDealle.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "no ha selecionados productos"));
            } else {
                System.out.println("cliente " + codigo);
                ve.setCodigoEmpleado(codigo);
                ve.setCodigoCliente(codigo);
                ve.setTipoDePago("T");
                dao.registrar(ve);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Transaccion correcta"));
                registrarDetalle();
                limpiar();
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

    public void limpiar() {
        listarVentaDealle = new ArrayList();
    }

    public void agregar(String codigo) {
        try {
            System.out.println("producto2 " + codigo);
            pro.setNombre(codigo);
            dao.mostrarDatos(pro);

        } catch (Exception e) {
            System.out.println("error en buscar ProductoC " + e.getMessage());
        }
    }

    public void elimianarProductoAgregado(String Codigo) {
        int i = 0;
        try {
            for (VentaDetalle vent : listarVentaDealle) {
                if (vent.getCodigoPRoducto().equals(Codigo)) {
                    total = total - listarVentaDealle.get(i).getSubtotal();
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

    public void listarTablaTemporal() {
        total = 0.0;
        subtotal = pro.getPrecio_venta() * pro.getCantidadVender();

        vd = new VentaDetalle();

        vd.setCodigoPRoducto(pro.getCodigo());
        vd.setCantidad(pro.getCantidadVender());
        vd.setDescripcion(pro.getDescripcion());
        vd.setNompro(pro.getNombre());
        vd.setPrcio(pro.getPrecio_venta());
        vd.setStock(pro.getStock());
        vd.setSubtotal(subtotal);
        listarVentaDealle.add(vd);
        System.out.println("ya guarde");
        calcularTotalVenta();
        pro = new Productos();
    }

    public void calcularTotalVenta() {
        for (int i = 0; i < listarVentaDealle.size(); i++) {
            total = total + listarVentaDealle.get(i).getSubtotal();
        }
    }

    public void listar() {
        try {
            listadoProd = ProductoImpl.listar(2);
        } catch (Exception e) {
            System.out.println("Error en listarC" + e.getMessage());
        }
    }

    public void validadorRepetido(Productos productos) {
        System.out.println("producto " + pro.getCodigo());
        int indice = 0;
        int cantidad = 0;
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

    public VentaImpl getDao() {
        return dao;
    }

    public void setDao(VentaImpl dao) {
        this.dao = dao;
    }

    public Productos getPro() {
        return pro;
    }

    public void setPro(Productos pro) {
        this.pro = pro;
    }

    public List<VentaDetalle> getListarVentaDealle() {
        return listarVentaDealle;
    }

    public void setListarVentaDealle(List<VentaDetalle> listarVentaDealle) {
        this.listarVentaDealle = listarVentaDealle;
    }

    public List<Productos> getListadoProd() {
        return listadoProd;
    }

    public void setListadoProd(List<Productos> listadoProd) {
        this.listadoProd = listadoProd;
    }

    public VentaDetalle getVd() {
        return vd;
    }

    public void setVd(VentaDetalle vd) {
        this.vd = vd;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public ProductoImpl getProductoImpl() {
        return ProductoImpl;
    }

    public void setProductoImpl(ProductoImpl ProductoImpl) {
        this.ProductoImpl = ProductoImpl;
    }

    public Ventas getVe() {
        return ve;
    }

    public void setVe(Ventas ve) {
        this.ve = ve;
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

    public Cliente getCli() {
        return cli;
    }

    public void setCli(Cliente cli) {
        this.cli = cli;
    }
}
