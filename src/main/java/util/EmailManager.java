package util;

import entity.Invoice;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;



public class EmailManager
{
    private final String emailServerName = "rstransport.com.sg";
    private String smtpAuthUser;
    private String smtpAuthPassword;



    public EmailManager()
    {
    }

    public EmailManager(String smtpAuthUser, String smtpAuthPassword)
    {
        this.smtpAuthUser = smtpAuthUser;
        this.smtpAuthPassword = smtpAuthPassword;
    }



    public Boolean sendEmail(Invoice invoice, InputStream pdf)
    {
        String emailBody = "Hi ";
        if (invoice.getParentName2() != null) {
            emailBody += invoice.getParentName1() + " and " + invoice.getParentName2();
        } else {
            emailBody += invoice.getParentName1();
        }

        emailBody += ",\n\nYour invoice for " + invoice.getMth() + "'s school bus fees is attached. Please make payment of $" + invoice.getBalDue() + " by " + invoice.getDueDate() + ".";
        emailBody += "\nIf you have any questions, please reply to this email. Thank you.";
        emailBody += "\n\nKind regards,";
        emailBody += "\nRS Transport";
        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", emailServerName);
            props.put("mail.smtp.debug", "true");

            Session session = Session.getInstance(props,
                    new jakarta.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(smtpAuthUser, smtpAuthPassword);
                        }
                    });

            List<String> toEmailAddress = new ArrayList<>();
            toEmailAddress.add(invoice.getParentEmail1());
            if (invoice.getParentEmail2() != null) {
                toEmailAddress.add(invoice.getParentEmail2());
            }

            InternetAddress[] addresses = new InternetAddress[toEmailAddress.size()];
            for (int i = 0; i < addresses.length; i++) {
                addresses[i] = new InternetAddress(toEmailAddress.get(i));
            }

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(smtpAuthUser, "RS Transport"));
            msg.setRecipients(Message.RecipientType.TO, addresses);
            msg.setSubject("Invoice from RS Transport for " + invoice.getMth());
            msg.setSentDate(new Date());

            //set the first text message part
            BodyPart msgBodyPart = new MimeBodyPart();
            msgBodyPart.setText(emailBody);

            //set the second part, which is the attachment
            BodyPart attachmentBodyPart = new MimeBodyPart();
            DataSource source = new ByteArrayDataSource(pdf, "application/pdf");
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            attachmentBodyPart.setFileName(invoice.getFileName());

            //combine parts
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(msgBodyPart);
            multipart.addBodyPart(attachmentBodyPart);

            msg.setContent(multipart);
            //msg.setText(emailBody);

            Transport.send(msg);
            for (String email : toEmailAddress) {
                System.out.println("Email successfully sent to: " + email);
            }
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }
}
