package com.example.demo.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Offer {

	private Map<String, Integer> productQuantityMap;
	private Long price;

	public Map<String, Integer> getProductQuantityMap() {
		return productQuantityMap;
	}

	public void setProductQuantityMap(Map<String, Integer> productQuantityMap) {
		this.productQuantityMap = productQuantityMap;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}
	
	public Offer(Map<String, Integer> productQuantityMap, Long price) {
		this.price = price;
		this.productQuantityMap = new HashMap<>();
		for(Entry<String, Integer> entry : productQuantityMap.entrySet()) {
			this.productQuantityMap.put(entry.getKey(), entry.getValue());
		}
	}
}
