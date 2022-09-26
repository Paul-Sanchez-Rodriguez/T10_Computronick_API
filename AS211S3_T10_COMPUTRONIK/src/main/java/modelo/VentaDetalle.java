
package modelo;

public class VentaDetalle {
    private String Nompro;
    private String Descripcion;
    private int Cantidad;
    private double Prcio;
    private int item;
    private double Subtotal;
    private int codigoVenta;
    private int nuevacantidad;
    private int stock;
    private String CodigoPRoducto;

    public String getNompro() {
        return Nompro;
    }

    public void setNompro(String Nompro) {
        this.Nompro = Nompro;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int Cantidad) {
        this.Cantidad = Cantidad;
    }

    public double getPrcio() {
        return Prcio;
    }

    public void setPrcio(double Prcio) {
        this.Prcio = Prcio;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public double getSubtotal() {
        return Subtotal;
    }

    public void setSubtotal(double Subtotal) {
        this.Subtotal = Subtotal;
    }

    public int getCodigoVenta() {
        return codigoVenta;
    }

    public void setCodigoVenta(int codigoVenta) {
        this.codigoVenta = codigoVenta;
    }

    public String getCodigoPRoducto() {
        return CodigoPRoducto;
    }

    public void setCodigoPRoducto(String CodigoPRoducto) {
        this.CodigoPRoducto = CodigoPRoducto;
    }

    public int getNuevacantidad() {
        return nuevacantidad;
    }

    public void setNuevacantidad(int nuevacantidad) {
        this.nuevacantidad = nuevacantidad;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
