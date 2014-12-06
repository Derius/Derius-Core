package dk.muj.derius;

import org.bukkit.permissions.Permissible;

import com.massivecraft.massivecore.util.PermUtil;

// The Permissions Enum of Derius
public enum Perm
{
	// -------------------------------------------- //
	// ENUM
	// -------------------------------------------- //
	
	BASECOMMAND("basecommand"),
	ADMIN("admin"),
	VERSION("version"),
	SKILL("skill"),
	LIST("list"),
	INSPECT("inspect"),
	
	
	// END OF LIST
	;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public final String node;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	Perm(final String node)
	{
		this.node = "derius."+ node;
	}
	
	// -------------------------------------------- //
	// ToString
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return node;
	}
	
	// -------------------------------------------- //
	// HAS
	// -------------------------------------------- //
	
	public boolean has(Permissible permissible, boolean informSenderIfNot)
	{
		return PermUtil.has(permissible, this.node, informSenderIfNot);
	}
	
	public boolean has(Permissible permissible)
	{
		return has(permissible, false);
	}
	
}
