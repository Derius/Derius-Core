package dk.muj.derius;

import org.bukkit.permissions.Permissible;

import com.massivecraft.massivecore.util.PermUtil;

import dk.muj.derius.lib.MPerm;

public enum DeriusPerm implements MPerm
{
	// -------------------------------------------- //
	// ENUM
	// -------------------------------------------- //
	
	ADMIN						("admin",				3, "derius admin commands"),
	BASECOMMAND					("basecommand",			0, "derius basecommand"),
	CLEAN						("clean",				1, "clean skills"),
	CLEAN_SKILL_ALL				("clean.skill.all", 	2, "clean all skills"),
	CLEAN_PLAYER				("clean.player",		1, "clean player"),
	CLEAN_PLAYER_OTHER			("clean.player.other",	2, "clean other player"),
	CLEAN_PLAYER_ALL			("clean.player.all",	3, "clean all players"),
	DEBUG						("debug",				2, "derius debug"),
	INSPECT						("inspect",				0, "inspect"),
	INSPECT_OTHERS				("inspect.other",		1, "inspect others"),
	KEYS						("keys",				0, "ability keys"),
	KEYS_ABILITYID				("keys.abilityid",		0, "list ability ids"),
	KEYS_ADD					("keys.add",			0, "add key"),
	KEYS_CLEAR					("keys.clear",			0, "clear keys"),
	KEYS_LIST					("keys.list",			0, "list keys"),
	KEYS_REMOVE					("keys.remove",			0, "remove key"),
	LIST						("list",				0, "list skills"),
	NOTIFY_CHUNK				("notify.chunk",		1, "get notified on chunk cleanup"),
	SETSTAMINA					("setStamina",			2, "set stamina"),
	SCOREBOARD					("scoreboard",			0, "manage your scoreboard settings"),
	SCOREBOARD_SHOW				("scoreboard.show",		0, "change show settings"),
	SCOREBOARD_KEEP				("scoreboard.keep",		0, "change keep settings"),
	SKILL						("skill",				0, "see skill info"),
	SPECIALISATION				("sp",					0, "specialisation basecommand"),
	SPECIALISATION_INFO			("sp.info",				0, "specialisation info"),
	SPECIALISATION_LEARN		("sp.learn",			0, "specialise in a skill"),
	SPECIALISATION_LIST			("sp.list",				0, "list specialisations"), 
	SPECIALISATION_LIST_OTHER	("sp.list.other",		0, "list others specialisations"),
	SPECIALISATION_UNLEARN		("sp.unlearn",			0, "unspecialise in a skill"), 
	VERSION						("version",				0, "see plugin version"),
	
	// END OF LIST
	;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private static int kitAmount = 3;
	
	private final String node;
	public String getNode() { return this.getStartNode() + this.node; }
	
	private final int kit;
	public int getKit() { return kit; }
	
	private String desc = "Do That";
	public String getDescription() { return this.desc; }
	public void setDesc(String desc) { this.desc = desc; update(); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	static
	{
		MPerm.createParents("derius.", kitAmount);
		
		for(DeriusPerm perm : DeriusPerm.values())
		{
			perm.create();
		}
	}
	
	DeriusPerm(final String node, int kit, String desc)
	{
		this.node = node;
		this.kit = kit;
		this.desc = desc;
	}
			public String getStartNode()
	{
		return "derius.";
	}
	
	// -------------------------------------------- //
	// TO STRING
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
