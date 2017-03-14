package com.akansh.myapplication;

public class Diseases {

	private String diseaseName;
	private String diseaseFileName;
	private float accuracy;

	public boolean isGroupHeader() {
		return isGroupHeader;
	}

	public void setGroupHeader(boolean groupHeader) {
		isGroupHeader = groupHeader;
	}

	public String getDiseaseName() {
		return diseaseName;
	}

	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}

	public String getDiseaseFileName() {
		return diseaseFileName;
	}

	public void setDiseaseFileName(String diseaseFileName) {
		this.diseaseFileName = diseaseFileName;
	}

	public float getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}

	private boolean isGroupHeader = false;


	public Diseases(String diseaseName, String diseaseFileName,float accuracy) {
		super();
		this.diseaseName = diseaseName;
		this.diseaseFileName = diseaseFileName;
		this.accuracy = accuracy;
	}


	
	
	
}
