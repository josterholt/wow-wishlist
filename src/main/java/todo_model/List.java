package todo_model;

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.hatraz.bucketlist.model.Item;
import com.hatraz.bucketlist.model.User;


public class List {
	@Id
	@Column
	private int id;
	
	private User user;
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User val) {
		user = val;
	}
	
	private String character;
	
	public String getCharacter() {
		return character;
	}
	
	public void setCharacter(String val) {
		character = val;
	}

	private ArrayList<Item> items;
	
	public ArrayList<Item> getItems() {
		return items;
	}
	
	public void setItems(ArrayList<Item> val) {
		items = val;
	}
	
	public void addItem(Item val) {
		items.add(val);
	}
	
}
