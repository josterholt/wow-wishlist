package com.hatraz.bucketlist.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hatraz.bucketlist.model.Item;
import com.hatraz.bucketlist.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
	@Query("SELECT u FROM User u WHERE u.twitter_id = :twitter_id")
	public User findByTwitterId(@Param("twitter_id") Long id);

	public User findByUsername(String username);
	
	@Query("SELECT i FROM User u left join u.favorite_items i WHERE u.id = :userID AND i.id IN (:ints) ")
	public Set<Item> getFilteredFavoriteItems(@Param("userID") Integer userID, @Param("ints") List<Integer> ints);
}
