package com.search.index;

import java.util.TreeMap;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;


public class Index {
	private TreeMap<String, TreeMap<Integer, Set<String> > >  keywordIndex = 
			new TreeMap<String, TreeMap<Integer, Set<String>>>();
	
	public Index() {
	}
	
	public void add(String keyword, String uri, Integer score) {

		if (!keywordIndex.containsKey(keyword)) {
			keywordIndex.put(keyword, new TreeMap<Integer, Set<String>>());
		}
		
		TreeMap<Integer, Set<String>> hits = keywordIndex.get(keyword);
		if (hits.containsKey(score)) {
			Set<String> docList = hits.get(score);;
			docList.add(uri);
		}
		else {
			Set<String> docList = new HashSet<String>();
			docList.add(uri);
			hits.put(score, docList);
		}
		
	}

	//Find a single keyword match
	TreeMap<Integer, Set<String>>  searchKeyword(String keyword) {
		TreeMap<Integer, Set<String>> hits = keywordIndex.get(keyword);
		return hits;
	}
	
	//uri as key and corresponding score as value for a keyword result (to merge multi-keyword search results)
	TreeMap<String, Integer> flattenResult(TreeMap<Integer, Set<String>> result) {
		TreeMap<String, Integer> flattendResult = new TreeMap<String, Integer>();
		for(Integer score: result.descendingKeySet()) {
			Set<String>hits = result.get(score);
			for(String hit: hits)
				flattendResult.put(hit, score);
		}
		return flattendResult;
	}
	
	//search using one or more keywords. 
	public ArrayList<Result> search(String searchString) {
		String[] keywords = searchString.toLowerCase().split(" ");
		TreeMap<Integer, Set<String>> results = null;
		for(String keyword :keywords) {
			TreeMap<Integer, Set<String>> result = searchKeyword(keyword);
			if (result == null)
				result = new TreeMap<Integer, Set<String>>(); //no matches - 
			if (results == null) {
				results = result;
			}
			else {
				//find the intersection, add scores to determine ranking
				TreeMap<String, Integer> combinedUriList = flattenResult(results); 
				TreeMap<String, Integer> uriList = flattenResult(result);
				Set<String> intersectionKeys = combinedUriList.keySet();
				intersectionKeys.retainAll(uriList.keySet());

				//update scores and rebuild results
				TreeMap<Integer, Set<String>>mergedResults = new TreeMap<Integer, Set<String>>();
				for(String hit: combinedUriList.keySet()) {
					int combinedScore = combinedUriList.get(hit) + uriList.get(hit);
					if (mergedResults.containsKey(combinedScore))
						mergedResults.get(combinedScore).add(hit);
					else {
						Set<String> docList = new HashSet<String>();
						docList.add(hit);
						mergedResults.put(combinedScore, docList);
					}
				}
				results = mergedResults;
			}
		}
		ArrayList<Result> hits = new ArrayList<Result>();
		if (results!= null) {
			for(Integer score : results.descendingKeySet()){
				Set<String> uriList =  results.get(score);
				for(String uri : uriList) {
					hits.add(new Result(uri, score));
				}
			}
		}
		return hits;
	}
	
	public void dumpStats(){
		System.out.println("Indexed "+Integer.toString(keywordIndex.size())+" keywords");
		/*for(String key: keywordIndex.keySet()) {
			System.out.println(key);
			System.out.println(keywordIndex.get(key));
		}*/
		
	
	}
	
}
