package com.ascendro.prismatic;

import com.hivemq.spi.message.PUBLISH;
import com.hivemq.spi.security.ClientData;

public interface PrismaticProxyService {
	/**
    * This method is used to do a "prismatic" message effect.
    * 
    * @param publish    The publish message send by the client.
    * @param clientData Useful information about the clients authentication state and credentials.
    */
	public void doPrismatic(PUBLISH publish, ClientData clientData);
}
