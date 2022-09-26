/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import modelo.Cliente;
import org.json.JSONObject;

/**
 *
 * @author Lab 25
 */
public class apiDNI {

    public static int consumiendoAPI(Cliente cli) throws UnirestException {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.get("https://apiperu.dev/api/dni/" + cli.getDni())
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer 863727277fb9814a6baf20e286a0c2fb79497989d5b27fa2168bebda485b24ed")
                .asString();

        //lineas añadidas al OKhttp
        if (response.getStatus() == 200) {
            JSONObject cadenaJson = new JSONObject(response.getBody());
            JSONObject cadena = cadenaJson.getJSONObject("data");
            cli.setNombre(cadena.getString("nombres"));
            cli.setApellido(cadena.getString("apellido_paterno"));
        }
        System.out.println("nombre " + cli.getNombre());
        System.out.println("apelldio " + cli.getApellido());
        return response.getStatus();
    }
//    
//    public static void main(String[] args) throws Exception {
//        persona per = new persona();
//        per.setDNI("70782063");
//        JSONObject json = apiDNI.consumiendoAPI(per);
//        System.out.println("nombre completo " + json.getString("nombre_completo"));
//    }
}
