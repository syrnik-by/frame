package ru.autotestframework.util;

import com.sun.mail.smtp.SMTPTransport;
import java.io.File;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Smtp helper.
 */
@Slf4j
public class SMTPHelper {

    /**
     * Send mail.
     *
     * @param rb       the rb
     * @param userName the user name
     * @param file     the file
     * @param text     the text
     * @param subject  the subject
     */
    @SneakyThrows
    public static void sendMail(ResourceBundle rb, String userName, File file, String text, String subject) {
        var from = rb.getString("username");
        char[] password = rb.getString("password").toCharArray();
        var mailPostfix = rb.getString("mail_postfix");

        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", System.getProperty("mail.smtp.host"));
        properties.setProperty("mail.smtp.port", System.getProperty("mail.smtp.port"));
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true"); // Enforce secure transmission over TLS/SSL

        var session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from.concat(mailPostfix), String.valueOf(password));
            }
        });

        try {
            var mimeMultipart = new MimeMultipart();

            BodyPart bp = new MimeBodyPart();
            bp.setText(text);
            mimeMultipart.addBodyPart(bp);

            var attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(file);
            mimeMultipart.addBodyPart(attachmentPart);

            var message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from.concat(mailPostfix)));
            message.setSubject(subject);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(userName.concat(mailPostfix)));
            message.setContent(mimeMultipart);

            SMTPTransport.send(message);

        } catch (MessagingException mex) {
            log.error("Ошибка отправки письма", mex);
        }
    }
}
