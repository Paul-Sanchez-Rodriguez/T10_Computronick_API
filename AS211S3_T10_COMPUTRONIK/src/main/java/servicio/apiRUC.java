package servicio;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import java.io.IOException;
import modelo.Proveedor;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONException;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author Jeferson Palomino
 */
public class apiRUC {

    public static int buscarRuc(Proveedor pro) throws Exception, JSONException {
        String token = "3d8048074ee866026997fdcb3714ac12e78b7a09cce0a5a1a5aaa520ce027450";
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.get("https://www.apisperu.net/api/ruc/" + pro.getRUC())
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .asString();
        if (response.getStatus() == 200) {
            JSONObject cadenaJson = new JSONObject(response.getBody());
            JSONObject cadena = cadenaJson.getJSONObject("data");
            pro.setRUC(cadena.getString("ruc"));
            pro.setNOMPREPROV(cadena.getString("nombre_o_razon_social"));
            pro.setDIRECCION(cadena.getString("direccion"));
            pro.setDepartamento(cadena.getString("departamento"));
            pro.setProvincia(cadena.getString("provincia"));
            pro.setDistrito(cadena.getString("distrito"));
            pro.setCODIGOUBIGEO(cadena.getString("ubigeo_sunat"));
            System.out.println("entre a la info de " + pro.getRUC());
            System.out.println("entre a la info de " + pro.getCODIGOUBIGEO());
        }
        return response.getStatus();

    }

    public static void main(String[] args) throws IOException, Exception {
        Proveedor pro = new Proveedor();
        pro.setRUC("20100070970");
//        JSONObject json = apiRUC.buscarRuc(pro);

//        System.out.println("nombre completo " + json.getString("nombre_o_razon_social"));
    }
}
