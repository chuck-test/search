package com.search.index;

public class Result {
	String uri;
	int score;
	public Result() {
		
	}
	public Result(String uri, int score) {
		this.uri = uri;
		this.score = score;
	}
	
	public String getUri() {
		return uri;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public String toString() {
		return uri + " " + Integer.toString(score);
	}
}
