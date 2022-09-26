/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicio;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Jeferson
 */
@FacesConverter("pagoConverte")
public class tipoPago implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent component, Object value) {
    String pago = "";
        if (value != null) {
            pago = (String) value;
            switch (pago) {
                case "E":
                    pago = "Efectivo";
                    break;
                case "T":
                    pago = "Tarjeta";
                    break;
            }
        }
        return pago;    
    }
    
}
