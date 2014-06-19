package com.example.gridimagesearch;

import java.io.Serializable;

public class QueryOption implements Serializable {
	
	String imageColor;
	String imageSize;
	String imageType;
	String siteRestriction;
	
	public QueryOption() {}
		
	public String getImageColor() {
		return imageColor;
	}
	public String getImageSize() {
		return imageSize;
	}
	public String getImageType() {
		return imageType;
	}
	public String getSiteRestriction() {
		return siteRestriction;
	}
	
	public void setImageColor(String imageColor) {
		this.imageColor = imageColor;
	}
	public void setImageSize(String imageSize) {
		this.imageSize = imageSize;
	}
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	public void setSiteRestriction(String siteRestriction) {
		this.siteRestriction = siteRestriction;
	}
	
	

}
