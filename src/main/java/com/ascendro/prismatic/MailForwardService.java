package com.ascendro.prismatic;

import com.ascendro.configuration.Configuration;
import com.ascendro.mail.MailerService;
import com.ascendro.mail.GMailService;

import com.google.common.base.Charsets;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.hivemq.spi.message.PUBLISH;
import com.hivemq.spi.security.ClientData;

@Singleton
public class MailForwardService implements ForwardService{

	private MailerService mailerService;

	private Configuration pluginConfiguration;

	@Inject
	public MailForwardService(
			Configuration configuration,
			GMailService mailerService
			) {
		this.pluginConfiguration = configuration;
		this.mailerService = mailerService;
    }
	
	@Override
	public void forwardMessage(PUBLISH publish, ClientData clientData) {
		String to = pluginConfiguration.getProperty("mail.receiver", "primastic");
		String from = pluginConfiguration.getProperty("mail.sender", "primastic");
		String subject = pluginConfiguration.getProperty("mail.subject", "primastic");
		String message = 
				"Topic:" + publish.getTopic() + "\n" +
				"QosNumber:" + publish.getQoS().getQosNumber() + "\n" +
				"isDuplicateDelivery:" + String.valueOf(publish.isDuplicateDelivery()) + "\n" +
				"isRetain:" + String.valueOf(publish.isRetain()) + "\n" +
				"ClientId:" + clientData.getClientId() + "\n" +
				"Message:" + new String(publish.getPayload(), Charsets.UTF_8);
		
		mailerService.send(from, to, subject , message);
	}

	@Override
	public boolean isAPrismaticTopic(PUBLISH publish) {
		return true;
	}
	

}
