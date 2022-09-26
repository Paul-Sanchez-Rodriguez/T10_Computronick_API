/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicio;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import modelo.prueba;

/**
 *
 * @author diego
 */
public class apiCorreo {

    public static void enviarCorreo(String correoReceptor, String contra) throws MessagingException {
        try {
            System.out.println("esto es el correoreceptor: " + correoReceptor);
            System.out.println("esto es el asunto: " + contra);
//            System.out.println("esto es el mensaje: " + mensaje);
            
            String correoemisor = "computronick2022@gmail.com";
            String contraseñaemisor = "znvvbcqnuyuuwamn";
            String correoreceptor = correoReceptor;
            String contraseña = contra;
//            String asuntoemail = asunto;

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.user", correoemisor);
            props.put("mail.smtp.clave", contraseñaemisor);

            Session session = Session.getDefaultInstance(props);

            MimeMessage msg = new MimeMessage(session);

            try {
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(correoreceptor));
            } catch (AddressException ex) {
                Logger.getLogger(prueba.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error al enviar correoreceptro: " + ex.getMessage());
            }
            msg.setSubject("Recepcion de contraseñas");
            msg.setText("Su contraseña ingresada es " + contraseña);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", correoemisor, contraseñaemisor);
            transport.sendMessage(msg, msg.getAllRecipients());

            transport.close();

            System.out.println("Se envio");

        } catch (MessagingException e) {
            System.out.println("Error al enviar correo: " + e.getMessage());
        }

    }
}
