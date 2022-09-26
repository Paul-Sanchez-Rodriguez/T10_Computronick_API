package controlador;

import dao.ClienteImpl;
import java.io.IOException;
import javax.inject.Named;
import java.io.Serializable;
import java.sql.SQLException;
import javax.enterprise.context.SessionScoped;
import modelo.Cliente;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import modelo.Ubigeo;
import reporte.reportesCliente;
import servicio.apiDNI;
import servicio.apiSMS;

@Named(value = "ClienteC")
@SessionScoped
public class ClienteC implements Serializable {

    private int caso = 3;

    //variables para editar
    private boolean dniencontrado = true;
    private boolean nombre = true;
    private boolean apellido = true;
    private boolean celular = true;
    private boolean direccion = true;
    private boolean distrito = true;

    private Cliente cli;
    private Ubigeo ubi;
    private ClienteImpl dao;
    private List<Cliente> listadoCli;
    private List<Ubigeo> listarUbi;

    public ClienteC() {
        System.out.println("Estoy por aqui");
        cli = new Cliente();
        dao = new ClienteImpl();
        ubi = new Ubigeo();
    }

    public void registrar() throws Exception {
        System.out.println("Estoy entro");
        try {
            if (cli.getDni().length() == 8 && cli.getCelular().length() == 9) {
                if (existe(cli, listadoCli)) {
//                cli.getCelular().l
                    cli.setRolper("C");
                    cli.setEstado("A");
                    dao.registrar(cli);
                    apiSMS.consumiendoAPI2(cli.getCelular());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Registrado con éxito"));
                    limpiar();
                    listar();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Error Cliente ya registado"));

                }

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Ingrese datos coherentes"));

            }

        } catch (Exception e) {
            System.out.println("Error en registrarC " + e.getMessage());
        }
    }

    public void modificar() throws Exception {
        try {
            if (cli.getDni().length() == 8 && cli.getCelular().length() == 9) {
                if (modificarExiste(cli, listadoCli)) {
                    dao.modificar(cli);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Modificado con éxito"));
                    limpiar();
                    listar();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Numero de DNI repetido"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Ingrese datos coherentes"));
            }
        } catch (Exception e) {
            System.out.println("Error en modificarC" + e.getMessage());
        }
    }

    public void eliminar() throws Exception {
        try {
            cli.setEstado("I");
            dao.eliminar(cli);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "OK", "Eliminado con éxito"));
            limpiar();
            listar();

        } catch (Exception e) {
            System.out.println("Error en eliminarC" + e.getMessage());
        }
    }

    public boolean existe(Cliente modelo, List<Cliente> listaModelo) {
        for (Cliente clie : listaModelo) {
            if (modelo.getDni().equals(clie.getDni())) {
                return false;
            }
        }
        return true;
    }

    public boolean modificarExiste(Cliente modelo, List<Cliente> listaModelo) {
        for (Cliente clie : listaModelo) {
            if (modelo.getDni().equals(clie.getDni())) {
                return modelo.getCodigo() == clie.getCodigo();
            }
        }

        return true;
    }

    public void habilitar() {
        try {
            cli.setEstado("A");
            dao.habilitarCli(cli);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Cliente Habilitado"));

        } catch (Exception e) {
            System.out.println("Error en habiliatr CienteC " + e.getMessage());
        }
    }

    public void limpiar() {
        cli = new Cliente();
        celular = true;
        direccion = true;
        distrito = true;
    }

    public void listar() {
        try {
            listadoCli = dao.listar(caso);

        } catch (Exception e) {
            System.out.println("Error en listarC " + e.getMessage());
        }
    }

    public List<Ubigeo> listarUbig() {

        try {
            listarUbi = dao.listarUbi();
        } catch (Exception e) {
            System.out.println("Error en listar Ubigeo" + e.getMessage());
        }
        return listarUbi;
    }

    public void verReportePDFEST() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {

        String ruta ="Reportes/Clientes.jasper";
        reportesCliente reCliente = new reportesCliente();
        FacesContext facescontext = FacesContext.getCurrentInstance();
        ServletContext servletcontext = (ServletContext) facescontext.getExternalContext().getContext();
        String root = servletcontext.getRealPath(ruta);
        switch (caso) {
            case 1:
                reCliente.getReportePdf(root, "A");
                FacesContext.getCurrentInstance().responseComplete();
                break;
            case 2:
                reCliente.getReportePdf(root, "I");
                FacesContext.getCurrentInstance().responseComplete();
                break;
            case 3:
                reCliente.getReportePdf(root, "A");
                FacesContext.getCurrentInstance().responseComplete();
                break;
        }
    }

    public void consumientdoApi() {
        try {
            int respuests = apiDNI.consumiendoAPI(cli);
            System.out.println(respuests);
            if (respuests == 200) {
                dniencontrado = true;
                nombre = true;
                apellido = true;
                celular = false;
                direccion = false;
                distrito = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bien", "se busco correctamente"));
            } else {
                dniencontrado = false;
                nombre = false;
                apellido = false;
                celular = false;
                direccion = false;
                distrito = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Success", "No se encontro datos"));
            }
        } catch (Exception e) {
            dniencontrado = false;
            nombre = false;
            apellido = false;
            celular = false;
            direccion = false;
            distrito = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El número de RUC ingresado es inválido"));
        }

    }

    public Cliente getCli() {
        if (cli == null) {
            cli = new Cliente();
            System.out.println("2");
        }
        return cli;
    }

    public void setCli(Cliente cli) {
        this.cli = cli;
    }

    public ClienteImpl getDao() {
        return dao;
    }

    public void setDao(ClienteImpl dao) {
        this.dao = dao;
    }

    public List<Cliente> getListadoCli() {
        return listadoCli;
    }

    public void setListadoCli(List<Cliente> listadoCli) {
        this.listadoCli = listadoCli;
    }

    public Ubigeo getUbi() {
        return ubi;
    }

    public void setUbi(Ubigeo ubi) {
        this.ubi = ubi;
    }

    public List<Ubigeo> getListarUbi() {

        return listarUbi;
    }

    public void setListarUbi(List<Ubigeo> listarUbi) {
        this.listarUbi = listarUbi;
    }

    public int getCaso() {
        return caso;
    }

    public void setCaso(int caso) {
        this.caso = caso;
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

    public boolean isDniencontrado() {
        return dniencontrado;
    }

    public void setDniencontrado(boolean dniencontrado) {
        this.dniencontrado = dniencontrado;
    }

    public boolean isCelular() {
        return celular;
    }

    public void setCelular(boolean celular) {
        this.celular = celular;
    }

    public boolean isDireccion() {
        return direccion;
    }

    public void setDireccion(boolean direccion) {
        this.direccion = direccion;
    }

    public boolean isDistrito() {
        return distrito;
    }

    public void setDistrito(boolean distrito) {
        this.distrito = distrito;
    }

}
