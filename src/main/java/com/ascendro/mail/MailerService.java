package com.ascendro.mail;

public interface MailerService {
	/**
	 * Mailer service definition.
	 *  
	 * @param from sender address 
	 * @param to receiver address
	 * @param subject mail subject
	 * @param body mail body
	 * @throws RuntimeException
	 */
	public void send(String from, String to, String subject, String body);
}
