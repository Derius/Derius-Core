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
	SPECIALISATION("sp"),
	SPECIALISATION_INFO("sp.info"),
	SPECIALISATION_LEARN("sp.learn"),
	SPECIALISATION_UNLEARN("sp.unlearn"), 
	SPECIALISATION_LIST("sp.list"), 
	SPECIALISATION_LIST_OTHER("sp.list.others"), 
	DEBUG("debug"),
	KEYS("keys"),
	KEYS_ADD("keys.add"),
	KEYS_REMOVE("keys.remove"),
	KEYS_LIST("keys.list"),
	KEYS_CLEAR("keys.clear"),
	KEYS_ABILITYID("keys.abilityid"),
	
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
