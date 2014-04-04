package todo_model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.persistence.Id;

@PersistenceCapable
public class Character {
	@Persistent
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String val) {
		name = val;
	}

	@Persistent
	private Integer character_id;
	
	public Integer getCharacterID() {
		return character_id;
	}
	
	public void setCharacterID(Integer val) {
		character_id = val;
	}
}
