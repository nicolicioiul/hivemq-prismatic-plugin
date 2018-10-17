package com.ascendro.prismatic;

import java.util.List;

import com.ascendro.configuration.Configuration;
import com.ascendro.elasticsearch.IndexerClient;
import com.ascendro.elasticsearch.IndexerModel;
import com.ascendro.elasticsearch.IndexerModelConverterService;
import com.ascendro.elasticsearch.IndexerModelConverterServiceImpl;
import com.ascendro.elasticsearch.IndexerOperation;
import com.google.common.base.Charsets;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.hivemq.spi.message.PUBLISH;
import com.hivemq.spi.security.ClientData;

@Singleton
public class ElasticsearchIndexer implements ForwardService{
	
	private Configuration pluginConfiguration;
	
	private IndexerModelConverterService indexerModelConverterService;
	
	private IndexerClient indexerClient;

	@Inject
	public ElasticsearchIndexer(
			IndexerModelConverterServiceImpl indexerModelConverterService,
			Configuration pluginConfiguration,
			IndexerClient indexerClient
			) {
		this.indexerModelConverterService = indexerModelConverterService;
		this.indexerClient = indexerClient;
		this.pluginConfiguration = pluginConfiguration;
    }
	
	@Override
	public void forwardMessage(PUBLISH publish, ClientData clientData) {
		String payLoad = new String(publish.getPayload(), Charsets.UTF_8);
		List<IndexerModel> models = indexerModelConverterService.toObjectList(payLoad);
		models.forEach((model) -> {
			if(IndexerOperation.PUT.name().equals(model.getDocumentIndexerOp())) {
				indexerClient.put(model);
			} else if(IndexerOperation.DELETE.name().equals(model.getDocumentIndexerOp())) {
				indexerClient.delete(model);
			}
		});
	}

	@Override
	public boolean isAPrismaticTopic(PUBLISH publish) {
		String index =  pluginConfiguration.getProperty("index", "elasticsearch");
		String topic =  publish.getTopic();
		return topic.equals(index) || topic.startsWith(index + "/");
	}
	
}
