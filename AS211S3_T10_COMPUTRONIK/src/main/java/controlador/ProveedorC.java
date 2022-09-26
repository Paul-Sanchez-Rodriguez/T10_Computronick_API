package controlador;

import dao.ProveedorImpl;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import modelo.Proveedor;
import modelo.Ubigeo;
import servicio.apiRUC;

@Named(value = "proveedorC")
@SessionScoped
public class ProveedorC implements Serializable {

    private int caso = 3;
    private Proveedor pro;
    private ProveedorImpl dao;
    private Ubigeo ubigeo;
    private List<Proveedor> listadoPro;
    private List<Ubigeo> listUbigeo;

    //variables de txt
    private boolean rucencontrado = true;
    private boolean seleccionarubigeo = true;
    private boolean nombre = true;
    private boolean apellido = true;
    private boolean celular = true;

    public ProveedorC() {
        pro = new Proveedor();
        dao = new ProveedorImpl();
        ubigeo = new Ubigeo();
    }

    public void regitrar() throws Exception {
        try {
            if (pro.getRUC().length() == 11 && pro.getCELULAR().length() == 9) {
                if (existe(pro, listadoPro)) {
                    pro.setESTADO("A");
                    dao.registrar(pro);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "se ha registrado"));
                    limpiar();
                    listar();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "RUC repetido"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Cantidad de digitos invalido en RUC o celular"));
            }
        } catch (Exception e) {
        }
    }

    public boolean existe(Proveedor modelo, List<Proveedor> listadoPro) {
        for (Proveedor prov : listadoPro) {
            if (modelo.getRUC().equals(prov.getRUC())) {
                return false;
            }
        }
        return true;
    }

    public void modificar() throws Exception {
        try {
            if (modificarExiste(pro, listadoPro)) {
                if (pro.getRUC().length() == 11 && pro.getCELULAR().length() == 9) {
                    dao.modificar(pro);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Se ha modificado"));
                    limpiar();
                    listar();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Digistos invalidos en RUC o celular"));

                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Numero de RUC repetido"));

            }
        } catch (Exception e) {
            System.out.println("Error al modificarC: " + e.getMessage());
        }
    }

    public boolean modificarExiste(Proveedor modelo, List<Proveedor> listadoPro) {
        for (Proveedor prov : listadoPro) {
            if (modelo.getRUC().equals(prov.getRUC())) {
                return modelo.getIDPROV() == prov.getIDPROV();
            }
        }
        return true;

    }

    public void eliminar() throws Exception {
        pro.setESTADO("I");
        dao.eliminar(pro);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "OK", "Se ha eliminado"));
    }

    public void habilitar() {

        try {
            dao.habilitar(pro);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "OK", "Producto habilitado"));
        } catch (Exception e) {
            System.out.println("Error en habilitar productosC " + e.getMessage());
        }
    }

    public List<Ubigeo> listarUbig() {
        try {
            listUbigeo = dao.listaUbigeo();
        } catch (Exception e) {
            System.out.println("Error al listUbigeoC: " + e.getMessage());
        }
        return listUbigeo;
    }

    public List<String> completUbi(String query) throws SQLException, Exception {
        ProveedorImpl proimpl = new ProveedorImpl();
        return proimpl.autocompleteUbigeo(query);
    }
//    public void limpiar() {
//        pro = new Proveedor();
//    }

    public void limpiar() {
        pro = new Proveedor();
        nombre = true;
        apellido = true;
        celular = true;
    }

    public void listar() {
        try {
            listadoPro = dao.listar(caso);
            
        } catch (Exception e) {
            System.out.println("Error al listarC: " + e.getMessage());
        }
    }

//    public void buscarApi() {
//        try {
//            apiRUC.buscarRuc(pro);
//        } catch (Exception e) {
//            System.out.println("Error al buscar ApiC :" + e.getMessage());
//        }
//    }
    public void activarInput() {
        try {
            int respuests = apiRUC.buscarRuc(pro);
            System.out.println(respuests);
            if (respuests == 200) {
                rucencontrado = true;
                nombre = false;
                apellido = false;
                celular = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bien", "se busco correctamente"));
            } else {
                rucencontrado = false;
                seleccionarubigeo = false;
                nombre = false;
                apellido = false;
                celular = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Success", "No se encontro datos"));
            }
        } catch (Exception e) {
            rucencontrado = false;
            seleccionarubigeo = false;
            nombre = false;
            apellido = false;
            celular = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El número de RUC ingresado es inválido"));
        }

    }
//Metodos Generados

    public Proveedor getPro() {
        return pro;
    }

    public void setPro(Proveedor pro) {
        this.pro = pro;
    }

    public ProveedorImpl getDao() {
        return dao;
    }

    public void setDao(ProveedorImpl dao) {
        this.dao = dao;
    }

    public List<Proveedor> getListadoPro() {
        return listadoPro;
    }

    public void setListadoPro(List<Proveedor> listadoPro) {
        this.listadoPro = listadoPro;
    }

    public Ubigeo getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(Ubigeo ubigeo) {
        this.ubigeo = ubigeo;
    }

    public List<Ubigeo> getListUbigeo() {
        return listUbigeo;
    }

    public void setListUbigeo(List<Ubigeo> listUbigeo) {
        this.listUbigeo = listUbigeo;
    }

    public int getCaso() {
        return caso;
    }

    public void setCaso(int caso) {
        this.caso = caso;
    }

    public boolean isRucencontrado() {
        return rucencontrado;
    }

    public void setRucencontrado(boolean rucencontrado) {
        this.rucencontrado = rucencontrado;
    }

    public boolean isSeleccionarubigeo() {
        return seleccionarubigeo;
    }

    public void setSeleccionarubigeo(boolean seleccionarubigeo) {
        this.seleccionarubigeo = seleccionarubigeo;
    }

    public boolean isNombre() {
        return nombre;
    }

    public void setNombre(boolean nombre) {
        this.nombre = nombre;
    }

    public boolean isApellido() {
        return apellido;
    }

    public void setApellido(boolean apellido) {
        this.apellido = apellido;
    }

    public boolean isCelular() {
        return celular;
    }

    public void setCelular(boolean celular) {
        this.celular = celular;
    }

}
