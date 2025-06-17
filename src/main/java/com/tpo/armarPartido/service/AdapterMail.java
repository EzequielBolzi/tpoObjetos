package com.tpo.armarPartido.service;

import com.tpo.armarPartido.model.Notificacion;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class AdapterMail implements AdapterNotificacionMail {
    private final String mailFrom;
    private final String username;
    private final String password;
    private final Properties props;

    public AdapterMail(String mailFrom, String username, String password) {
        this.mailFrom = mailFrom;
        this.username = username;
        this.password = password;

        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
    }

    @Override
    public void notificar(Notificacion notificacion) {
        try {
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailFrom));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(notificacion.getUsuario().getCorreo())
            );
            message.setSubject("Notificación de Partido");
            message.setText(notificacion.getMensaje());

            Transport.send(message);

            System.out.println("[EMAIL] Sent to: " + notificacion.getUsuario().getCorreo());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}