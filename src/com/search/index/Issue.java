package com.search.index;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class Issue {
	String uri;
	String body;
	
	public Issue(){
		body = "";
	}

	public Issue(String uri, String body) {
		this.uri = uri;
		this.body = body;
	}
	
	public String getUri() {
		return uri;
	}
	
	public String getBody() {
		return body;
	}
}
