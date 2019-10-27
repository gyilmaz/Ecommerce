package com.ecommerce.controllers;

import java.util.List;
import java.util.Optional;

import com.ecommerce.model.persistence.Item;
import com.ecommerce.model.persistence.repositories.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/item")
public class ItemController {
	Logger logger= LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private ItemRepository itemRepository;
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		logger.info("Retrieving all items");
		List<Item> items=itemRepository.findAll();
		return items.size() == 0 ? ResponseEntity.notFound().build() : ResponseEntity.ok(items);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		logger.info("Retrieving item id " + id);
		Optional<Item> item=itemRepository.findById(id);
		return item == null ? ResponseEntity.notFound().build() : ResponseEntity.of(item);
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		List<Item> items = itemRepository.findByName(name);
		logger.info("Retrieving by "+ name +" item/s is/are " + items.toString() );
		return items == null || items.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(items);
	}
	
}
