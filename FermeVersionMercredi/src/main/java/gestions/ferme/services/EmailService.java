package gestions.ferme.services;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

public class EmailService {

    public static void sendPasswordByEmail(String recipientEmail, String password) {
        final String senderEmail = "jlassinour278@gmail.com";
        final String appPassword = "bbnsvygzqclqmszq"; // Ton mot de passe d'application

        // Configuration du serveur SMTP
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.port", "587");

        // Session authentifiée
        Session session = Session.getInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, appPassword);
            }
        });

        session.setDebug(true); // Optionnel : utile pour voir les logs SMTP

        try {
            // Création du message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Réinitialisation de mot de passe");
            message.setText("Bonjour,\n\nVoici votre mot de passe : " + password + "\n\nCordialement,\nL'équipe de support.");

            // Envoi
            Transport.send(message);
            System.out.println("Mot de passe envoyé avec succès à : " + recipientEmail);

        } catch (Exception e) {
            System.out.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
