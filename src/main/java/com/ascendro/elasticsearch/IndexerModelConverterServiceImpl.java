package com.ascendro.elasticsearch;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IndexerModelConverterServiceImpl implements IndexerModelConverterService {
	
	Logger logger = LoggerFactory.getLogger(IndexerModelConverterServiceImpl.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public List<IndexerModel> toObjectList(String json) {
		ObjectMapper mapper = new ObjectMapper();
		List<IndexerModel> list = null;
		
		try {
			list = (List<IndexerModel>) mapper.readValue(json,  mapper.getTypeFactory().constructCollectionType(List.class, IndexerModel.class));
		} catch (JsonMappingException e) {
			logger.error("Json decoding error on mapping: {}, json: {}", e.getMessage(), json);
		} catch (JsonParseException e) {
			logger.error("Json decoding error on parsing: {}, json: {}", e.getMessage(), json);
		} catch (IOException e) {
			logger.error("Json decoding error on IO: {}, json: {}", e.getMessage(), json);
		}
		
		return  list;
	}
}
