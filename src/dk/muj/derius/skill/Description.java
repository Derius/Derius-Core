package dk.muj.derius.skill;

import com.massivecraft.massivecore.util.Txt;

/**
 * This class is used for storing data about skill abilities
 * It has a name which is short & then a description which is longer.
 * The toString method is implemented & should be used when displaying to players.
 */
public class Description
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private String name;
	public String getName () {	return name;	}
	public void setName (String name) {	this.name = name;	}
	
	private String desc;
	public String getDesc () {	return desc;	}
	public void setDesc (String desc) {	this.desc = desc;	}
	
	// -------------------------------------------- //
	// CONSTRUCTOR
	// -------------------------------------------- //
	
	public Description(String name, String desc)
	{
		this.setName(name);
		this.setDesc(desc);
	}
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	

	@Override
	/**
	 * Should just be displayed to players
	 */
	public String toString()
	{
		return Txt.parse("<pink>"+name+": <yellow>"+desc);
	}
}
