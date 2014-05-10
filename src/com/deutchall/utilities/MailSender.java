package com.deutchall.utilities;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;   
import javax.mail.PasswordAuthentication;   
import javax.mail.Session;   
import javax.mail.Transport;   
import javax.mail.internet.InternetAddress;   
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;      
import javax.mail.internet.MimeMultipart;

import android.content.Context;
//import android.graphics.drawable.Drawable;
//import java.io.File;
//import java.net.URL;
import java.util.Properties;

public class MailSender extends javax.mail.Authenticator {
	
    private String mailhost = "smtp.gmail.com";   
    private String user;   
    private String password;   
    private Session session;
    private Context context;
    
    public MailSender(String user, String password,Context c) {
    	
        this.user = user;   
        this.password = password;
        this.context = c;

        Properties props = new Properties();   
        props.setProperty("mail.transport.protocol", "smtp");   
        props.setProperty("mail.host", mailhost);   
        props.put("mail.smtp.auth", "true");   
        props.put("mail.smtp.port", "465");   
        props.put("mail.smtp.socketFactory.port", "465");   
        props.put("mail.smtp.socketFactory.class",   
                "javax.net.ssl.SSLSocketFactory");   
        props.put("mail.smtp.socketFactory.fallback", "false");   
        props.setProperty("mail.smtp.quitwait", "false");   

        session = Session.getDefaultInstance(props, this);   
    }   

    protected PasswordAuthentication getPasswordAuthentication() {   
        return new PasswordAuthentication(user, password);   
    }   

    public synchronized void sendMail(String subject, String body, String sender, String recipients) {   
        
    	try{
        	
	        MimeMessage message = new MimeMessage(session);
	        //DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
	        message.setSender(new InternetAddress(sender));   
	        message.setSubject(subject);
	        
	        
	        MimeMultipart multipart = new MimeMultipart("related");
	        BodyPart messageBodyPart = new MimeBodyPart();
	        messageBodyPart.setContent("<H1>Hello</H1><img src=\"cid:image\">", "text/html");
	        multipart.addBodyPart(messageBodyPart);
	        
	        messageBodyPart = new MimeBodyPart();
	        
	        
	        String uri = "@drawable/logo";
	        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
	        System.out.println("ID: " + imageResource);
	        String name = context.getResources().getString(imageResource);
	        System.out.println("Name: " + name);
	        DataSource source = new FileDataSource(name);
	        System.out.println("Longitud imagen: " + source.getInputStream().available());
	        messageBodyPart.setDataHandler(new DataHandler(source));
	        messageBodyPart.setHeader("Content-ID","<image>");
	        multipart.addBodyPart(messageBodyPart);
	        
	        message.setContent(multipart);
	        //message.setDataHandler(handler);

	        if (recipients.indexOf(',') > 0)   
	            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
	        else 
	            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
	        
	        Transport transport = session.getTransport();
	        transport.connect();
	        transport.sendMessage(message,message.getRecipients(Message.RecipientType.TO));
	        transport.close();
        
    	} catch(Exception e) {
        	System.out.println("Ha habido una excepcion: " + e.getMessage());
        } 
    }   
}  
