package com.hatraz.bucketlist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hatraz.bucketlist.model.Item;
import com.hatraz.bucketlist.repository.ItemRepo;


@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private ItemRepo itemRepo;

	@Override
	public Item create(Item item) {
		return itemRepo.save(item);
	}

	@Override
	public Item delete(int id) throws Exception {
		Item item = itemRepo.findOne(id);
		if(item == null) {
			throw new Exception("Item does not exist");
		}
		itemRepo.delete(item);
		return item;
	}

	@Override
	public List<Item> findAll() {
		return itemRepo.findAll();
	}

	@Override
	public Item update(Item item) throws Exception {
		Item updatedItem = itemRepo.findOne(item.getId());
		if(updatedItem == null) {
			throw new Exception("Item not found");
		}
		updatedItem.setId(item.getId());
		updatedItem.setName(item.getName());
		updatedItem.setSummary(item.getSummary());
		return updatedItem;
	}

	@Override
	public Item findById(int id) {
		return itemRepo.findOne(id);
	}

	@Override
	public List<Item> searchByName(String name, Pageable pageable) {
		return itemRepo.searchByName("%" + name + "%", pageable);
	}
}
