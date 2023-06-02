package com.tarzan.nav.email;



import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author tarzan liu
 * @date 2020/5/9
 */
public abstract class EmailUtil {

    private static Session session = null;

    private static EmailAuthenticator authenticator = null;

    static {
        InputStream inputStream = null;
        try {
            inputStream = EmailUtil.class.getResourceAsStream("/email.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            authenticator = new EmailAuthenticator();
            String username = properties.getProperty("email.username");
            authenticator.setUsername(username);
            String password = properties.getProperty("email.password");
            authenticator.setPassword(password);
            String smtpHostName = "smtp." + username.split("@")[1];
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.host", smtpHostName);
            session = Session.getInstance(properties, authenticator);
        } catch (Exception e) {
            throw new RuntimeException("init error.");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private EmailUtil() { }

    /**
     * 通用发邮件方法
     */
    private static void send(List<String> recipients, SimpleEmail email) throws MessagingException {
        final MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(authenticator.getUsername()));
        InternetAddress[] addresses = new InternetAddress[recipients.size()];
        for (int index = 0; index < recipients.size(); index ++) {
            addresses[index] = new InternetAddress(recipients.get(index));
        }
        message.setRecipients(RecipientType.TO, addresses);
        message.setSubject(email.getSubject());
        message.setContent(email.getContent(), "text/html;charset=utf-8");

        Transport.send(message);
    }

    /**
     * 发送邮件
     */
    public static void send(String recipient, SimpleEmail email) throws MessagingException {
        List<String> recipients = new ArrayList<String>();
        recipients.add(recipient);
        send(recipients, email);
    }

    /**
     * 群发邮件
     */
    public static void massSend(List<String> recipients, SimpleEmail email) throws MessagingException {
        send(recipients, email);
    }

    public static void main(String[] args) throws Exception {
        SimpleEmail simpleEmail = new SimpleEmail();
        simpleEmail.setSubject("今天你学习了么？");
        simpleEmail.setContent("今天你写博客了么");

        List<String> recipients = new ArrayList<String>();
        recipients.add("1334512682@qq.com");

        massSend(recipients, simpleEmail);
    }
}
