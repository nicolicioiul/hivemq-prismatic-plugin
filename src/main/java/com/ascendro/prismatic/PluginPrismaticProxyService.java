package com.ascendro.prismatic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ascendro.callbacks.PublishReceived;
import com.ascendro.configuration.Configuration;
import com.google.inject.Inject;
import com.hivemq.spi.message.PUBLISH;
import com.hivemq.spi.security.ClientData;

public class PluginPrismaticProxyService implements PrismaticProxyService{
	Logger logger = LoggerFactory.getLogger(PublishReceived.class);
		
	private ForwardService pluginMailForwardService;
	
	private ForwardService pluginNullMailForwardService;
	
	private ForwardService pluginElasticsearchForwardService;
	
	private Configuration pluginConfiguration;
	
	@Inject
	public PluginPrismaticProxyService(
			Configuration configuration,
			MailForwardService mailForwardService,
			NullForwardService nullMailForwardService,
			ElasticsearchIndexer pluginElasticsearchForwardService
			) {
		this.pluginConfiguration = configuration;
		this.pluginMailForwardService = mailForwardService;
		this.pluginNullMailForwardService = nullMailForwardService;
		this.pluginElasticsearchForwardService = pluginElasticsearchForwardService;
    }
	
	@Override
	public void doPrismatic(PUBLISH publish, ClientData clientData) {
		
		if(pluginConfiguration.isEnabled("prismatic.nullforward")) {
			forwardMessage(pluginNullMailForwardService, publish, clientData);
        }

		if(pluginConfiguration.isEnabled("prismatic.mailforward")) {
			forwardMessage(pluginMailForwardService, publish, clientData);
        }
		
		if(pluginConfiguration.isEnabled("prismatic.elasticsearchforward")) {
			forwardMessage(pluginElasticsearchForwardService, publish, clientData);
        }
	}
	/**
	 * Forward a message. 
	 * @param forwardService
	 * @param publish
	 * @param clientData
	 */
	private void forwardMessage(ForwardService forwardService, PUBLISH publish, ClientData clientData) {
		if(forwardService.isAPrismaticTopic(publish)) {
			forwardService.forwardMessage(publish, clientData);
			logger.info("Forward message : service: {}, client: {}, topic: {}",	forwardService.getClass().getSimpleName(),  clientData.getClientId(), publish.getTopic());
		}
	}
}
