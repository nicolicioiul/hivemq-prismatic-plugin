package com.ascendro.elasticsearch;

import java.util.List;

public interface IndexerModelConverterService {

	/**
	 * Convert a string to a list of indexer model.
	 * 
	 * Example: {{
	 *	  "documentIndexerOp":"PUT",
	 *	  "documentIndexUrl":"posts/id/1",
	 *	  "documentJsonString":"{\"name\":\"demo\"}}"
	 *	},
	 *	{
	 *	  "documentIndexerOp":"PUT",
	 *	  "documentIndexUrl":"posts/id/2",
	 *	  "documentJsonString":"{\"name\":\"demo\"}}"
	 *	},
	 *  {
	 *	  "documentIndexerOp":"DELETE",
	 *	  "documentIndexUrl":"posts/id/2",
	 *	}
	 *	}
	 * 
	 * @param json string
	 * @return List<IndexerModel>
	 */	public List<IndexerModel> toObjectList(String json);
}