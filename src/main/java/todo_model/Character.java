package todo_model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="characters")
public class Character {

	@Column
	@Id
	private int id;

	public Integer getID() {
		return id;
	}
	
	public void setID(Integer val) {
		id = val;
	}

	@Column
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String val) {
		name = val;
	}
}
