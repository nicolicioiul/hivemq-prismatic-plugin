package com.ascendro.elasticsearch;

public class IndexerModel {
	
	private String documentIndexerOp;
	
	private String documentIndexUrl;
	
	private String documentJsonString;
	
	public String getDocumentIndexUrl() {
		return documentIndexUrl;
	}
	public void setDocumentIndexUrl(String documentIndexUrl) {
		this.documentIndexUrl = documentIndexUrl;
	}
	public String getDocumentJsonString() {
		return documentJsonString;
	}
	public void setDocumentJsonString(String documentJsonString) {
		this.documentJsonString = documentJsonString;
	}
	public String getDocumentIndexerOp() {
		return documentIndexerOp;
	}
	public void setDocumentIndexerOp(String documentIndexerOp) {
		this.documentIndexerOp = documentIndexerOp;
	}
}
