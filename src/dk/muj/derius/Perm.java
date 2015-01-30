package dk.muj.derius;

import org.bukkit.permissions.Permissible;

import com.massivecraft.massivecore.util.PermUtil;

public enum Perm
{
	// -------------------------------------------- //
	// ENUM
	// -------------------------------------------- //
	
	ABILITY_USE("ability.use."),
	ABILITY_SEE("ability.see."),
	ADMIN("admin"),
	BASECOMMAND("basecommand"),
	CLEAN("clean"),
	CLEAN_SKILL_ALL("clean.skill.all"),
	CLEAN_PLAYER("clean.player"),
	CLEAN_PLAYER_OTHER("clean.player.other"),
	CLEAN_PLAYER_ALL("clean.player.all"),
	DEBUG("debug"),
	EXP_MULTIPLIER("derius.multiplier."),
	INSPECT("inspect"),
	INSPECT_OTHERS("inspect.other"),
	KEYS("keys"),
	KEYS_ABILITYID("keys.abilityid"),
	KEYS_ADD("keys.add"),
	KEYS_CLEAR("keys.clear"),
	KEYS_LIST("keys.list"),
	KEYS_REMOVE("keys.remove"),
	LIST("list"),
	NOTIFY_CHUNK("notify.chunk"),
	SKILL("skill"),
	SKILL_LEARN("skill.learn."),
	SKILL_SEE("skill.see."),
	SKILL_SPECIALISE("skill.sp."),
	SPECIALISATION("sp"),
	SPECIALISATION_INFO("sp.info"),
	SPECIALISATION_LEARN("sp.learn"),
	SPECIALISATION_LIST("sp.list"), 
	SPECIALISATION_LIST_OTHER("sp.list.other"),
	SPECIALISATION_SLOTS("sp.slots."),
	SPECIALISATION_UNLEARN("sp.unlearn"), 
	VERSION("version"),
	
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
