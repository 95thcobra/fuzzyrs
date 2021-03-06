package com.rs.content.economy.exchange.itemsets;

import java.util.concurrent.atomic.AtomicInteger;

public class ItemSetsKeyGenerator {

	private final static AtomicInteger nextKey = new AtomicInteger(500);

	public static int generateKey() {
		int key = nextKey.getAndDecrement();
		if (key > 0 && key <= 100)
			nextKey.set(-1); // starts at negative
		return key;
	}
}
