package com.hatraz.bucketlist.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.hatraz.bucketlist.model.Item;


public interface ItemService {
	public Item create(Item item);
	public Item delete(int id) throws Exception;
	public List<Item> findAll();
	public Item update(Item item) throws Exception;
	public Item findById(int id);
	public List<Item> searchByName(String name, Pageable pageable);
}
