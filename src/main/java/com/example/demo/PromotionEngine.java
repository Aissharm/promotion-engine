package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.demo.dto.Offer;

@Service
public class PromotionEngine {

	private static List<Offer> existingOfferList;

	@PostConstruct
	public static void init() {
		existingOfferList = new ArrayList<>();
		Map<String, Integer> productQuantityMap = new HashMap<>();
		productQuantityMap.put("A", 3);
		Offer offerA = new Offer(productQuantityMap, 130L);
		existingOfferList.add(offerA);

		productQuantityMap = new HashMap<>();
		productQuantityMap.put("C", 1);
		productQuantityMap.put("D", 1);
		Offer offerB = new Offer(productQuantityMap, 30L);
		existingOfferList.add(offerB);
	}

	public Long applyBestPromotion(Map<String, Integer> productQuantityMap) {
		Map<String, List<Offer>> offerByProduct = new HashMap<>();
		
		productQuantityMap.entrySet().stream().forEach(entry -> {
			List<Offer> productSpecificOffers = new ArrayList<>();
			productSpecificOffers.addAll(findExistingOffersForProduct(entry.getKey(), entry.getValue()));
			offerByProduct.put(entry.getKey(), productSpecificOffers);
		});
		
		return calculatePromotionPrice(offerByProduct, productQuantityMap);
	}

	private List<Offer> findExistingOffersForProduct(String productName, Integer quantity) {
		return existingOfferList.stream().filter(offer -> (offer.getProductQuantityMap().containsKey(productName)
				&& offer.getProductQuantityMap().get(productName) <= quantity)).collect(Collectors.toList());
	}
	
	private List<Offer> filterOffers(List<Offer> offersForProductTemp, Map<String, Integer> orderedProductQuantityMap) {
		return offersForProductTemp.stream().filter(offer -> {
			boolean flag = true;
			for (Entry<String, Integer> offProductQuantity : offer.getProductQuantityMap().entrySet()) {
				if (Objects.isNull(orderedProductQuantityMap.get(offProductQuantity.getKey()))
						|| (orderedProductQuantityMap.get(offProductQuantity.getKey()).intValue() < offProductQuantity
								.getValue().intValue())) {
					flag = false;
					break;
				}
			}
			return flag;
		}).collect(Collectors.toList());
	}

	private Long calculatePromotionPrice(Map<String, List<Offer>> offerByProduct, Map<String, Integer> orderedProductQuantityMap) {		
		//orderedProductQuantityMap 5A, C, 2B
		orderedProductQuantityMap.entrySet().forEach(entry -> {
			List<Offer> offersForProductTemp = offerByProduct.get(entry.getKey());
			if (!CollectionUtils.isEmpty(offersForProductTemp))
			offerByProduct.put(entry.getKey(), filterOffers(offersForProductTemp, orderedProductQuantityMap));
			//offersForProduct.addAll(filterOffers(offersForProductTemp, orderedProductQuantityMap));
			
		});
		
		//apply offers
		Long price = 0L;
		for (Entry<String, List<Offer>> entry : offerByProduct.entrySet()) {
			if (!CollectionUtils.isEmpty(entry.getValue()))
			price = priceAfterPromotion(entry.getValue().get(0), orderedProductQuantityMap, price);
		}
		return calculateIndividualProductPrice(orderedProductQuantityMap, price);
		
	}
	//A,3, 130
	private Long priceAfterPromotion(Offer applicableOffer, Map<String, Integer> orderedProductQuantityMap, Long price) {
		
		for(Entry<String, Integer> offerProductQuantity : applicableOffer.getProductQuantityMap().entrySet()) {
			int num = orderedProductQuantityMap.get(offerProductQuantity.getKey()) - offerProductQuantity.getValue();
			
			if (num > 0) {
				orderedProductQuantityMap.put(offerProductQuantity.getKey(), num);
			} else {
				orderedProductQuantityMap.remove(offerProductQuantity.getKey());
			}
			price= price + applicableOffer.getPrice();
		}
		
		return price;
	}
	
	private Long calculateIndividualProductPrice(Map<String, Integer> orderedProductQuantityMap, Long price) {
		if (orderedProductQuantityMap.size() > 0) {
			for (Entry<String, Integer> prodQuantity : orderedProductQuantityMap.entrySet()) {
				// switch case of get product rate
				int prodValue = 0;
				switch (prodQuantity.getKey()) {
				case "A":
					prodValue = 50;
					break;
				case "B":
					prodValue = 30;
					break;
				case "C":
					prodValue = 20;
					break;
				case "D":
					prodValue = 15;
					break;
				default:
					break;
				}
				price = price + prodValue * prodQuantity.getValue();
			}
		}
		return price;
	}
}
