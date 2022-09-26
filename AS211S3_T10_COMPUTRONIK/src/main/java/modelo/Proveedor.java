
package modelo;

public class Proveedor {
    private int IDPROV;
    private String RUC;
    private String NOMPREPROV;
    private String NOMBRE;
    private String APELLIDO;
    private String DIRECCION;
    private String CELULAR;
    private String CODIGOUBIGEO;
    private String DISUBI;
    private String ESTADO;
    private String departamento;
    private String provincia;
    private String distrito;
    
    
    
    public void limpiar(){
        IDPROV = 0;
        RUC = null;
        NOMPREPROV = null;
        NOMBRE = null;
        APELLIDO = null;
        DIRECCION = null;
        CELULAR = null;
        
    }

    public int getIDPROV() {
        return IDPROV;
    }

    public void setIDPROV(int IDPROV) {
        this.IDPROV = IDPROV;
    }

    public String getRUC() {
        return RUC;
    }

    public void setRUC(String RUC) {
        this.RUC = RUC;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public String getAPELLIDO() {
        return APELLIDO;
    }

    public void setAPELLIDO(String APELLIDO) {
        this.APELLIDO = APELLIDO;
    }
    

    public String getDIRECCION() {
        return DIRECCION;
    }

    public void setDIRECCION(String DIRECCION) {
        this.DIRECCION = DIRECCION;
    }

    public String getCELULAR() {
        return CELULAR;
    }

    public void setCELULAR(String CELULAR) {
        this.CELULAR = CELULAR;
    }

    public String getCODIGOUBIGEO() {
        return CODIGOUBIGEO;
    }

    public void setCODIGOUBIGEO(String CODIGOUBIGEO) {
        this.CODIGOUBIGEO = CODIGOUBIGEO;
    }

    public String getNOMPREPROV() {
        return NOMPREPROV;
    }

    public void setNOMPREPROV(String NOMPREPROV) {
        this.NOMPREPROV = NOMPREPROV;
    }

    public String getESTADO() {
        return ESTADO;
    }

    public void setESTADO(String ESTADO) {
        this.ESTADO = ESTADO;
    }

    public String getDISUBI() {
        return DISUBI;
    }

    public void setDISUBI(String DISUBI) {
        this.DISUBI = DISUBI;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }
    
        
}
