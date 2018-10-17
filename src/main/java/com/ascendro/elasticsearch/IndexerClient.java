package com.ascendro.elasticsearch;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ascendro.configuration.Configuration;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class IndexerClient{
	
	Logger logger = LoggerFactory.getLogger(IndexerClient.class);
	
	private Configuration pluginConfiguration;
	
	private RestClient restClient;
	
	@Inject
	public IndexerClient(
			Configuration configuration
			) {
		this.pluginConfiguration = configuration;
    }
	
	/**
     * This method is executed after the instantiation of the whole class.
     */
    @PostConstruct
    public void postConstruct() {
    	// @todo get resource from settings.
		restClient = RestClient.builder(
	        new HttpHost("192.168.99.100", 9200, "http")).build();
		createIfNotExistsIndex();
    }
    
    /**
     * Create the index if is missing.
     */
    private void createIfNotExistsIndex() {
    	String index =  pluginConfiguration.getProperty("index", "elasticsearch");
    	
    	// Add check and create index if is not exists.
    	restClient.performRequestAsync(IndexerOperation.GET.name(),  "/" + index, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
            	logger.info("Successful tested index : {}", index);
            }

            @Override
            public void onFailure(Exception exception) {
				logger.error("Failure on check index, try to recreate : message: {}, index: {}", exception.getMessage(), index);
				createIndex(index);
            }
        }); 
    }
    
	/**
	 * Put a document in a indexer.
	 * @param indexerModel the document index model.
	 */
    public void put(IndexerModel indexerModel) {
    	
    	Map<String, String> params = Collections.emptyMap();
    	HttpEntity entity = new NStringEntity(indexerModel.getDocumentJsonString(), ContentType.APPLICATION_JSON);
    	
    	restClient.performRequestAsync(IndexerOperation.PUT.name(),  indexerModel.getDocumentIndexUrl(), params, entity,  new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
            	logger.info("Successful on PUT index operation : DocumentIndexUrl: {}",
            			 indexerModel.getDocumentIndexUrl());
            }

            @Override
            public void onFailure(Exception exception) {
					logger.error("Failure on PUT index operation : message: {}, DocumentIndexUrl: {}, content: {} , entity: {}",	
							exception.getMessage(), indexerModel.getDocumentIndexUrl(), indexerModel.getDocumentJsonString(), indexerModel.getDocumentJsonString());             
            }
        }); 

    }
    
    /**
	 * Delete a document from indexer.
	 * @param indexerModel the document index model.
	 */
    public void delete(IndexerModel indexerModel) {
    	Map<String, String> params = Collections.emptyMap();
    	HttpEntity entity = new NStringEntity(indexerModel.getDocumentJsonString(), ContentType.APPLICATION_JSON);
    	
    	restClient.performRequestAsync(IndexerOperation.DELETE.name(),  indexerModel.getDocumentIndexUrl(), params, entity,  new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
            	logger.info("Successful on DELETE index operation : DocumentIndexUrl: {}",
           			 indexerModel.getDocumentIndexUrl());
            }

            @Override
            public void onFailure(Exception exception) {
            	logger.error("Failure on DELETE index operation : message: {}, DocumentIndexUrl: {}",	
            			exception.getMessage(), indexerModel.getDocumentIndexUrl());
               
            }
        }); 
    }
    
    /**
	 * Create a index on elasticsearch.
	 * @param index string
	 */
    private void createIndex(String index) {
    	restClient.performRequestAsync(IndexerOperation.PUT.name(),  index,  new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
            	logger.info("Successful on CREATE index operation : index: {}", index);
            }

            @Override
            public void onFailure(Exception exception) {
            	logger.error("Failure on CREATE index : message: {}, index: {}", exception.getMessage(), index);
               
            }
        }); 
    }


}
