/*
 * Copyright 2015 dc-square GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ascendro.callbacks;

import com.hivemq.spi.callback.CallbackPriority;
import com.hivemq.spi.callback.events.OnPublishReceivedCallback;
import com.hivemq.spi.callback.exception.OnPublishReceivedException;
import com.hivemq.spi.message.PUBLISH;
import com.hivemq.spi.security.ClientData;
import com.ascendro.configuration.Configuration;

import com.ascendro.prismatic.ForwardService;
import com.ascendro.prismatic.MailForwardService;
import com.ascendro.prismatic.PluginPrismaticProxyService;
import com.ascendro.prismatic.PrismaticProxyService;
import com.google.common.base.Charsets;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the {@link OnPublishReceivedCallback}, which is triggered everytime
 * a new message is published to the broker. This callback enables a custom handling of a
 * MQTT message, for forwarding messages.
 *
 * @author Nicolicioiu Liviu
 */
public class PublishReceived implements OnPublishReceivedCallback {
	
    Logger logger = LoggerFactory.getLogger(PublishReceived.class);
	
	private final PrismaticProxyService pluginPrismaticProxy;
	
	@Inject
	public PublishReceived(PluginPrismaticProxyService pluginPrismaticProxy) {
		this.pluginPrismaticProxy = pluginPrismaticProxy;
    }
	
    
    /**
     * This method is called from the HiveMQ, when a new MQTT {@link PUBLISH} message arrives
     * at the broker. In this acme the method is just logging each message to the console.
     *
     * @param publish    The publish message send by the client.
     * @param clientData Useful information about the clients authentication state and credentials.
     * @throws OnPublishReceivedException When the exception is thrown, the publish is not
     *                                    accepted and will NOT be delivered to the subscribing clients.
     */
    @Override
    public void onPublishReceived(PUBLISH publish, ClientData clientData) throws OnPublishReceivedException {
        String clientID = clientData.getClientId();
        String topic = publish.getTopic();
        String message = new String(publish.getPayload(), Charsets.UTF_8);

        logger.info("Client " + clientID + " sent a message to topic " + topic + ": " + message);
        pluginPrismaticProxy.doPrismatic(publish, clientData);
    }

    /**
     * The priority is used when more than one OnConnectCallback is implemented to determine the order.
     * If there is only one callback, which implements a certain interface, the priority has no effect.
     *
     * @return callback priority
     */
    @Override
    public int priority() {
        return CallbackPriority.HIGH;
    }

}
