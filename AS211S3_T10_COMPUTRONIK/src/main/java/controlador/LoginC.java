package controlador;

import dao.ClienteImpl;
import dao.LoginImpl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import modelo.Cliente;
import modelo.Empleado;
import modelo.Ubigeo;
import servicio.apiCorreo;

@Named(value = "LoginC")
@SessionScoped
public class LoginC implements Serializable {

    private LoginImpl dao;
//    private List<Login> listaLogin;

    private Cliente cli;
    private ClienteImpl daoC;
    private List<Ubigeo> listarUbi;
    private List<Cliente> listaCliente;
    private apiCorreo enviar;
    private Empleado emp;

    public LoginC() {
        emp = new Empleado();
        dao = new LoginImpl();
        cli = new Cliente();
        daoC = new ClienteImpl();

    }

    public String redireccion() {
        System.out.println("Estoy por aqui");
        String redirigir = null;
        try {
            if (dao.verificar(cli)) {
                if (cli.getRolper().equals("C")) {
                    
                    redirigir = "/faces/Inicio.xhtml";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Ingreso con exito"));
                } else {
                    System.out.println("Empleado "+ cli.getCodigo());
                    emp.setCodigo(cli.getCodigo());
                    emp.setNombre(cli.getNombre());
                    redirigir = "/faces/vistas/home/dashboard.xhtml";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Ingreso con exito"));
                }

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Usuario no encontrado"));
            }

        } catch (Exception e) {
            System.out.println("Error en loginC " + e.getMessage());
        }
        return redirigir;

    }
    


//    public void datosCliente() {
//        dao.cliente(cli, cli.getCodigo());
//        System.out.println("cliente " + cli.getNombre());
//        System.out.println("codigo " + cli.getCodigo());
//    }
    
    public List<Ubigeo> listarUbig() {

        try {
            listarUbi = dao.listarUbi();
        } catch (Exception e) {
            System.out.println("Error en listar Ubigeo" + e.getMessage());
        }
        return listarUbi;
    }

    public void registrar() throws Exception {
        try {
            cli.setRolper("C");
            cli.setEstado("A");
            dao.registrar(cli);
            enviarCorreo();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ok", "Se registro correctamenta"));
        } catch (Exception e) {
            System.out.println("Error al registrarC: " + e.getMessage());
        }
    }

    public void enviarCorreo() throws Exception {
//        listaLogin = dao.listar();
        String correoelec = cli.getUsername();
        String contraseña = cli.getPassword();
        enviar.enviarCorreo(correoelec, contraseña);
        
    }
    
    public void limpiar(){
        cli = new Cliente();
    }

    public LoginImpl getDao() {
        return dao;
    }

    public void setDao(LoginImpl dao) {
        this.dao = dao;
    }

    public Cliente getCli() {
        return cli;
    }

    public void setCli(Cliente cli) {
        this.cli = cli;
    }

    public ClienteImpl getDaoC() {
        return daoC;
    }

    public void setDaoC(ClienteImpl daoC) {
        this.daoC = daoC;
    }

    public List<Cliente> getListaCliente() {
        return listaCliente;
    }

    public void setListaCliente(List<Cliente> listaCliente) {
        this.listaCliente = listaCliente;
    }

    public apiCorreo getEnviar() {
        return enviar;
    }

    public void setEnviar(apiCorreo enviar) {
        this.enviar = enviar;
    }

    public List<Ubigeo> getListarUbi() {
        return listarUbi;
    }

    public void setListarUbi(List<Ubigeo> listarUbi) {
        this.listarUbi = listarUbi;
    }

    public Empleado getEmp() {
        return emp;
    }

    public void setEmp(Empleado emp) {
        this.emp = emp;
    }

}
