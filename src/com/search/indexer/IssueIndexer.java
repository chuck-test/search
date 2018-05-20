package com.search.indexer;
import java.util.TreeMap;

import java.io.File;

import com.search.index.Index;
import com.search.index.Issue;
import com.search.index.Result;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;;

public class IssueIndexer {
	
	Index index;
	String root;
	// Populates the index from a dump of issues specified in the folder 
	public IssueIndexer(String root, Index index) {
		this.root = root;
		this.index = index;
	}
	
	void processIssue(Issue issue) {
		Parser parser = new Parser();
		TreeMap<String, Result> docIndex = parser.parse(issue);
		for(String keyword: docIndex.keySet()) {
			Result result = docIndex.get(keyword);
			index.add(keyword, result.getUri(), result.getScore());
		}
	}
	
	int processFile(String file) {
		int issueCount = 0;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
			Issue[] issues = objectMapper.readValue(new File(file), Issue[].class);
			
			for(int count = 0; count < issues.length; count ++) {
				processIssue(issues[count]);
 				issueCount++;
 			}
		}
		catch(Exception e) {
			System.out.println("Error processing " + file);
			System.out.println(e);
			e.printStackTrace();
		}
		return issueCount;
	}
	
	public void build() {
		File directory = new File(this.root);
		File[] fList = directory.listFiles();
		int fileCount = 0;
		int issueCount = 0;
		for (File file : fList) {
			if (file.isFile()) {
				issueCount += processFile(file.getAbsolutePath());
				fileCount++;
			}
		}
		System.out.println("Processed "+Integer.toString(fileCount)+" files");
		System.out.println("Processed "+Integer.toString(issueCount)+" issues");
	}
}
	
