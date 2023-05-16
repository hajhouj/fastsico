package com.hajhouj.fastsico;

/**
 * 
 * Result class
 * 
 * Represents the result of a string similarity calculation.
 * 
 * @author Mohammed Hajhouj
 * 
 * @version 1.0
 * 
 * @since 2023-02-02
 */
public class Result {
	private double similarityScore;
	private int index;

	public Result(int index, double similarityScore) {
		this.index = index;
		this.similarityScore = similarityScore;
	}

	/**
	 * 
	 * Returns the similarity score.
	 * 
	 * @return the similarity score
	 */
	public double getSimilarityScore() {
		return similarityScore;
	}

	/**
	 * 
	 * Sets the similarity score.
	 * 
	 * @param similarityScore the similarity score
	 */
	public void setSimilarityScore(double similarityScore) {
		this.similarityScore = similarityScore;
	}

	/**
	 * 
	 * Returns the target index.
	 * 
	 * @return the target index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * 
	 * Sets the target index.
	 * 
	 * @param target the target index
	 */
	public void setTarget(int index) {
		this.index = index;
	}
}