
package modelo;

import java.util.Date;
import java.util.GregorianCalendar;


public class GraficoBarra {
    private Date fecha = GregorianCalendar.getInstance().getTime(); 
    private int Cantidad;


    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int Cantidad) {
        this.Cantidad = Cantidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
