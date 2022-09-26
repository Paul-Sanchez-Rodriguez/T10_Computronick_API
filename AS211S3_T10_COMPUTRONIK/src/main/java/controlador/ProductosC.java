package controlador;

import dao.ProductoImpl;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import modelo.Categoria;
import modelo.Productos;
import modelo.SubCategoria;

@Named(value = "ProductosC")
@SessionScoped
public class ProductosC implements Serializable {

    private int caso = 1;
    private Productos prod = new Productos();
    private ProductoImpl dao;
    private Categoria cat;
    private SubCategoria sub;
    private List<Productos> listadoProd;
    private List<Categoria> lisCategoria;
    private List<SubCategoria> listaSubfamilia;

    public ProductosC() {
        prod = new Productos();
        dao = new ProductoImpl();
        cat = new Categoria();
        sub = new SubCategoria();
    }

    public void registar(Productos prod) throws Exception {
        try {
            if (ValidarCodigo(prod, listadoProd)) {
                if (prod.getStock() > 0 && prod.getPrecio_compra() > 0.0 && prod.getPrecio_venta() > 0.0) {
                    dao.registrar(prod);
                    limpiar();
                    listar();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Producto registrado correctamente"));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO", "Ingrese datos correctos"));

                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Codigo de producto repetido"));
            }

        } catch (Exception e) {
            System.out.println("Error en registrar Productos" + e.getMessage());

        }
    }

    public void limpiar() {
        prod = new Productos();
        sub = new SubCategoria();
        cat = new Categoria();
    }

    public void listar() {
        try {
            listadoProd = dao.listar(caso);
            
//            actulizarEstado();
        } catch (Exception e) {
            System.out.println("Error en listarC" + e.getMessage());
        }
    }

    public void actulizarEstado() {
        
        int indice = 0;
        for (Productos productos : listadoProd) {
            if (productos.getStock() == 0) {
                String codigo = listadoProd.get(indice).getCodigo();
                dao.actualizarEstado(codigo);
            }
            indice++;
        }
    }

    public List<Categoria> mostrarCategoria() {
        try {
            lisCategoria = dao.listarCategoria();
        } catch (Exception e) {
            System.out.println("Error en listar Categoria" + e.getMessage());
        }

        return lisCategoria;

    }
    
    public void eliminar() throws Exception {
        try {
            prod.setEstado("I");
            dao.eliminar(prod);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "OK", "Eliminado con éxito"));
            limpiar();
            listar();

        } catch (Exception e) {
            System.out.println("Error en listar EliinarC" + e.getMessage());
        }
    }

    public void habilitar(){
    
        try {
            dao.habilitar(prod);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "OK", "Producto habilitado"));
        } catch (Exception e) {
            System.out.println("Error en habilitar productosC " +e.getMessage());
        }
    }
    
    public List<SubCategoria> listaSubCategoria(){
        try {
            listaSubfamilia = dao.Subfamilia(cat.getId());
        } catch (Exception e) {
            System.out.println("Error en listarSubfamilia  " +e.getMessage());
        }
        return listaSubfamilia;
    }
    
    public boolean ValidarCodigo(Productos modelo, List<Productos> listaModelo) {
        for (Productos pro : listaModelo) {
            if (modelo.getCodigo().equals(pro.getCodigo())) {
                return false;
            }
        }
        return true;
    }

    public void Modificar() {
        try {
            if (prod.getStock() > 0 && prod.getPrecio_compra() > 0.0 && prod.getPrecio_venta() > 0.0) {
                prod.setEstado("A");
                dao.modificar(prod);
                System.out.println("hola");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Modificado con éxito"));
                limpiar();
                listar();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Ingrese una cantidad de stock mayor a 0"));

            }

        } catch (Exception e) {
            System.out.println("Error en ModificarC " + e.getMessage());
        }
    }

    public Productos getProd() {
        if(prod == null){
        prod = new Productos();
            System.out.println("2");
        
        }
        return prod;
    }

    public void setProd(Productos prod) {
        this.prod = prod;
    }

    public ProductoImpl getDao() {
        return dao;
    }

    public void setDao(ProductoImpl dao) {
        this.dao = dao;
    }

    public List<Productos> getListadoProd() {
        return listadoProd;
    }

    public void setListadoProd(List<Productos> listadoProd) {
        this.listadoProd = listadoProd;
    }

    public List<Categoria> getLisCategoria() {
        return lisCategoria;
    }

    public void setLisCategoria(List<Categoria> lisCategoria) {
        this.lisCategoria = lisCategoria;
    }

    public int getCaso() {
        return caso;
    }

    public void setCaso(int caso) {
        this.caso = caso;
    }

    public Categoria getCat() {
        return cat;
    }

    public void setCat(Categoria cat) {
        this.cat = cat;
    }

    public List<SubCategoria> getListaSubfamilia() {
        return listaSubfamilia;
    }

    public void setListaSubfamilia(List<SubCategoria> listaSubfamilia) {
        this.listaSubfamilia = listaSubfamilia;
    }

    public SubCategoria getSub() {
        return sub;
    }

    public void setSub(SubCategoria sub) {
        this.sub = sub;
    }

}
