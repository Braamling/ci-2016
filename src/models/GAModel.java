package models;

public class GAModel {
	private int _generations;
	private int _individual;
	private int _generationSize = 30;
	private double _bestResult = Double.POSITIVE_INFINITY;
    private Double[] _genResults = new Double [_generationSize];
    
    public GAModel(){
    	_generations = 0;
    	_individual = 0;
    }
    
    public int getGenerationSize(){
    	return _generationSize;
    }
    
	public int getIndividual() {
		return _individual;
	}
	public void setIndividual(int _individual) {
		this._individual = _individual;
	}
	public int getGenerations() {
		return _generations;
	}
	public void setGenerations(int _generations) {
		this._generations = _generations;
	}
	public double getBestResult() {
		return _bestResult;
	}
	public void setBestResult(double _bestResult) {
		this._bestResult = _bestResult;
	}
	public Double[] getGenResults() {
		return _genResults;
	}
	public void setGenResults(Double[] _genResults) {
		this._genResults = _genResults;
	}
	
	public double getGenResult(int index) {
		return _genResults[index];
	}
	public void setGenResult(int index, double _genResults) {
		this._genResults[index] = _genResults;
	}

	public void generationPlusPLus() {
		this._generations++;
	}

	public void individualPlusPlus() {
		this._individual++;
	}
	
	
}
