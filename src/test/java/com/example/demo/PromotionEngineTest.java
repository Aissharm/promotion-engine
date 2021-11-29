package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@RunWith(SpringJUnit4ClassRunner.class)
public class PromotionEngineTest {

	@InjectMocks
	private PromotionEngine promotionEngine;
	
	@BeforeMethod
	public void setup() {
		MockitoAnnotations.openMocks(this);
		PromotionEngine.init();
	}
	
	@DataProvider(name = "orderSamples")
	public Object[][] mockRequests() {
		Map<String, Integer> orderedProductQuantityMap1 = new HashMap<>();
		orderedProductQuantityMap1.put("A", 2);
		orderedProductQuantityMap1.put("C", 1);
		Map<String, Integer> orderedProductQuantityMap2 = new HashMap<>();
		orderedProductQuantityMap2.put("A", 3);
		return new Object[][] { new Object[] {orderedProductQuantityMap1, 120L}, new Object[] {orderedProductQuantityMap2, 130L} };
	}
	
	@Test(dataProvider = "orderSamples")
	public void applyPromo(Map<String, Integer> orderedProductQUantityMap, Long expectedResult) {
		Long result = promotionEngine.applyBestPromotion(orderedProductQUantityMap);
		Assert.assertEquals(result, expectedResult);
	}
}
