package com.ascendro.prismatic;

import com.hivemq.spi.message.PUBLISH;
import com.hivemq.spi.security.ClientData;

public interface ForwardService {

    /**
     * This method is used to redirect a message.
     * 
     * @param publish    The publish message send by the client.
     * @param clientData Useful information about the clients authentication state and credentials.
     */
	public void forwardMessage(PUBLISH publish, ClientData clientData);
	
	/**
	 * Check if a topic needs to get prismatic (forwarded).
	 * 
	 * @param publish PUBLISH
	 * @return boolean
	 */
	public boolean isAPrismaticTopic(PUBLISH publish);
	
}
