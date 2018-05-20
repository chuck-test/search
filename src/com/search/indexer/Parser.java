package com.search.indexer;

import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.TreeMap;

import com.search.index.Issue;
import com.search.index.Result;

import java.util.HashSet;

public class Parser {
	
	HashSet<String> skipWords = new HashSet<String>(Arrays.asList(
			"a", "an", "and", "the", "is", "are", "or", "on", "to"
			));
	
	public Parser() {
		
	}
	
	private String preProcess(String text) {
		if (text != null)
			text = text.replaceAll("[^A-Za-z0-9]+", " ");
		else
			text = "";

		//Should also get rid of markup...
		return text;
	}
	
	private boolean isSkipWord(String word) {
		if (skipWords.contains(word)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public TreeMap<String, Result> parse(Issue issue) {
		TreeMap<String, Result> docIndex = new TreeMap<String, Result>();
		
		String text = issue.getBody();
		text = preProcess(text);
		
		StringTokenizer st = new StringTokenizer(text);
		while(st.hasMoreTokens()){
			String word = st.nextToken().toLowerCase();
			if (isSkipWord(word)) {
				continue;
			}
			if (docIndex.containsKey(word)) {
				//If word already in index, increment score.
				Result result = docIndex.get(word);
				result.setScore(result.getScore() + 1);
			}
			else {
				//Add word not in index if not already there, and set score to 1
				Result result = new Result(issue.getUri(), 1);
				docIndex.put(word, result);
			}
		}
		return docIndex;
	}
}
