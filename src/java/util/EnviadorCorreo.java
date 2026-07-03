package util;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class EnviadorCorreo
{
    private static final String SMTP_USUARIO = "fsilveriocuesta@gmail.com";
    private static final String SMTP_PASSWORD = "liyl jybw bmvi htzf";

    public static boolean enviarCodigoVerificacion(String correoDestino, String nombreDestino, String codigo)
    {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(SMTP_USUARIO, SMTP_PASSWORD);
            }
        });

        try
        {
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(SMTP_USUARIO, "Registro de Alumnos"));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoDestino));
            mensaje.setSubject("Tu código de confirmación - Registro de Alumnos");

            String cuerpo =
                "<div style=\"font-family:Arial, sans-serif; color:#1b2a4a; max-width:480px;\">"
              + "<h2 style=\"margin-bottom:4px;\">Hola " + nombreDestino + "</h2>"
              + "<p>Gracias por registrarte en el sistema de Registro de Alumnos.</p>"
              + "<p>Para activar tu cuenta, escribe el siguiente código en la página de confirmación:</p>"
              + "<p style=\"font-size:28px; font-weight:bold; letter-spacing:4px; color:#1b2a4a; "
              + "background:#f2f4f8; padding:12px 18px; text-align:center; border-radius:8px;\">"
              + codigo + "</p>"
              + "<p style=\"font-size:12.5px; color:#8a7f66; margin-top:32px;\">"
              + "Si tú no creaste esta cuenta, puedes ignorar este correo.</p>"
              + "</div>";

            mensaje.setContent(cuerpo, "text/html; charset=UTF-8");

            Transport.send(mensaje);
            return true;
        }
        catch (MessagingException | UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}