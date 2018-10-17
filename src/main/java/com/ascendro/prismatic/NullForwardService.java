package com.ascendro.prismatic;

import com.google.inject.Singleton;

import com.hivemq.spi.message.PUBLISH;
import com.hivemq.spi.security.ClientData;

@Singleton
public class NullForwardService implements ForwardService{
	@Override
	public void forwardMessage(PUBLISH publish, ClientData clientData) {

	}

	@Override
	public boolean isAPrismaticTopic(PUBLISH publish) {
		// TODO Auto-generated method stub
		return true;
	}
	
}
