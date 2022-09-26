/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author diego
 */
public class prueba {

    public static void main(String[] args) throws MessagingException {
        try {
            String correoRemitente = "computronick2022@gmail.com";
            String passwordRemitente = "znvvbcqnuyuuwamn";

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.user", correoRemitente);
            props.put("mail.smtp.clave", passwordRemitente);

            Session session = Session.getDefaultInstance(props);

            MimeMessage msg = new MimeMessage(session);

            try {
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress("jeferson.palomino@vallegrande.edu.pe"));
            } catch (AddressException ex) {
                Logger.getLogger(prueba.class.getName()).log(Level.SEVERE, null, ex);
            }
            msg.setSubject("este es nuevaaaa");
            msg.setText("QUe fueeee funciono?");
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", correoRemitente, passwordRemitente);
            transport.sendMessage(msg, msg.getAllRecipients());

            transport.close();

            System.out.println("Se envio");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Correo", "Se envio el correo con exito"));
        } catch (MessagingException e) {
            System.out.println("Error al enviar correo: " + e.getMessage());
        }
    }
}
