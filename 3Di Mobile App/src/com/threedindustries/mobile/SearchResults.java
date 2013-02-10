package com.threedindustries.mobile;

public class SearchResults {
	  public String product_name;
	  public String product_description;
	  public String image_url;
	  public String product_url;
	   
	  public SearchResults(String product_name, String description, String url, String product_url) {
		// TODO Auto-generated constructor stub
		    this.product_name = product_name;
		    this.product_description = description;
		    this.image_url = url;
		    this.product_url = product_url;
	}
}
