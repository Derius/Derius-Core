package dk.muj.derius.skill;

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
	public String toString()
	{
		return "<pink>"+name+": <yellow>"+desc;
	}
}
