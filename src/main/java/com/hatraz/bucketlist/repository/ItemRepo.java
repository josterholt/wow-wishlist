package com.hatraz.bucketlist.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hatraz.bucketlist.model.Item;


@Repository
public interface ItemRepo extends JpaRepository<Item, Integer> {
	@Query("SELECT i FROM Item i WHERE i.name LIKE  :name_search")
	public List<Item> searchByName(@Param("name_search") String name_search, Pageable pageable);
}
