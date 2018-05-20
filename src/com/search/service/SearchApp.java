package com.search.service;

import java.io.File;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.search.index.Index;
import com.search.indexer.IssueIndexer;



public class SearchApp {
	String searchFolder;
	Index index;

	public SearchApp(String searchFolder) {
		this.searchFolder = searchFolder;
		index = new Index();
		IssueIndexer indexer = new IssueIndexer(searchFolder, index);
		indexer.build();
		index.dumpStats();
	}
	
	Index getIndex() {
		return index;
	}

	static SearchApp theApp;
	public static SearchApp getApp() {
		return theApp;
	}
	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage com.search.service.SearchApp <path to folder containing issue dump>");
			return;
		}
		
		String path = args[0];
		File f = new File(path);
		if (!f.isDirectory()) {
			System.out.println("Invalid folder. Usage com.search.service.SearchApp <path to folder containing issue dump>");
			return;
		}
		
		theApp = new SearchApp(path); 
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        Server jettyServer = new Server(8080);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(
             org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        jerseyServlet.setInitParameter(
           "jersey.config.server.provider.classnames",
           SearchService.class.getCanonicalName());

        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            jettyServer.destroy();
        }
	}

}
