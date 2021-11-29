package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class OrderController {

	@Autowired
	PromotionEngine promotionEngine;
	
	public void applyPromotion(Map<String, Integer> productUnitMap) {
		promotionEngine=new PromotionEngine();
		PromotionEngine.init();
		System.out.println("price after applyling offer "+ promotionEngine.applyBestPromotion(productUnitMap));
	}
	
	public static void main(String[] args) {
		OrderController order=new OrderController();
		Map<String, Integer> productUnitMap=new HashMap<String, Integer>();
		productUnitMap.put("A", 4);
		order.applyPromotion(productUnitMap);
	}
}
