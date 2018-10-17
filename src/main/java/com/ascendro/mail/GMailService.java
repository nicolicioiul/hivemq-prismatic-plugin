package com.ascendro.mail;

import java.util.Properties;

import javax.annotation.PostConstruct;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.mail.util.MailSSLSocketFactory;

import com.ascendro.configuration.Configuration;
import com.google.inject.Inject;

public class GMailService implements MailerService {

	Logger log = LoggerFactory.getLogger(GMailService.class);

	private Configuration pluginConfiguration;

	static private Properties props;

	@Inject
	public GMailService(Configuration configuration) {
		this.pluginConfiguration = configuration;
	}

	@PostConstruct
	public void postConstruct() {
		if (props == null) {
			props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");

			try {
				MailSSLSocketFactory sf = new MailSSLSocketFactory();
				sf.setTrustAllHosts(true);
				props.put("mail.smtp.ssl.enable", "true");
				props.put("mail.smtp.ssl.socketFactory", sf);

			} catch (Exception e) {
				log.error("Error on creating socket factory for mail gmail client, host: " + props.get("mail.smtp.host")
						+ ", port: 465, message:" + e.getMessage() + ", trace: " + e.getStackTrace().toString());
				throw new RuntimeException(e);
			}
		}
	}

	private String getVariable(String name) {
		// Get variable value: mail.smtp.$name
		return pluginConfiguration.getProperty("smtp" + "." + name, "mail");
	}

	@Override
	public void send(String from, String to, String subject, String body) {

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(getVariable("username"), getVariable("password"));
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			message.setText(body);
			Transport.send(message);
		} catch (MessagingException e) {
			log.error("Error on sending message gmail client, host: " + props.get("mail.smtp.host")
					+ ", port: 465, message:" + e.getMessage() + ", trace: " + e.getStackTrace().toString());
			throw new RuntimeException(e);
		}
	}
}