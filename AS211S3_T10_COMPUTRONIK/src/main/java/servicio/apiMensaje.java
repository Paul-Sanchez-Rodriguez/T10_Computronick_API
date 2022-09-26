package servicio;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONException;
import org.primefaces.shaded.json.JSONObject;

public class apiMensaje {

    public static JSONObject enviarSMS(String nro,String mensaje) throws UnirestException {
        System.out.println(nro);
        String autenticacion = "amVmZXJzb24ucGFsb21pbm9AdmFsbGVncmFuZGUuZWR1LnBlOkVFMDIwNzMxLTVDRTgtNzAzOC02NkUyLUY1RDgzMkU3QjUyQw==";
        String RapiKey = "cce274e66emsh12b8bc2b9dbca57p19c690jsn3d383fb5cf7d";
        HttpResponse<String> response = Unirest.post("https://clicksend.p.rapidapi.com/sms/send")
                .header("content-type", "application/json")
                .header("Authorization", "Basic " + autenticacion)
                .header("Content-Type", "application/json")
                .header("X-RapidAPI-Key", RapiKey)
                .header("X-RapidAPI-Host", "clicksend.p.rapidapi.com")
                .body("{\n  \"messages\": [\n    {\n    "
                        + "  \"source\": \"mashape\",\n    "
                        + "  \"from\": \"Test\",\n    "
                        + "  \"body\": \"+" +mensaje+"\",\n  "
                        + "    \"to\": \"+" + nro + "\",\n     "
                        + " \"schedule\": \"1452244637\",\n    "
                        + "  \"custom_string\": \"this is a test\"\n   "
                        + " }\n  ]\n}")
                .asString();

        JSONObject cadenaJson = new JSONObject(response.getBody());
        JSONObject cadena = cadenaJson.getJSONObject("data");
        JSONArray mensajes = cadena.getJSONArray("messages");
        JSONObject cuerpo = mensajes.getJSONObject(0);

        return cuerpo;

    }

    public static void main(String[] args) throws Exception {

        JSONObject json = apiMensaje.enviarSMS("+51912602977","que buena estas XD");
        System.out.println("mensaje que se envio es " + json.getString("body"));

    }
}
