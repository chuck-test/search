package com.search.service;

import java.util.List;

import com.search.index.Result;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/search")
public class SearchService {
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(	@QueryParam("q") String searchString, 
    						@DefaultValue("0")  @QueryParam("page") int page, 
    						@DefaultValue("30") @QueryParam("per_page") int perPage) {
		System.out.println(searchString);
		List<Result> results = SearchApp.getApp().getIndex().search(searchString);
		
		int startIndex = page*perPage;
		int endIndex = (page+1)*perPage;
		
		if (endIndex > results.size())
			endIndex = results.size();
		
		if (startIndex > results.size())
			startIndex = results.size();
		
		System.out.println(results.size());
		return Response.ok(results.subList(startIndex, endIndex)).build();
	}
		
}
