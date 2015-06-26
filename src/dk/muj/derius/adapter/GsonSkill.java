package dk.muj.derius.adapter;

import org.bukkit.plugin.Plugin;

import dk.muj.derius.api.skill.SkillAbstract;


public final class GsonSkill extends SkillAbstract
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public GsonSkill() { }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getId()
	{
		return null;
	}
	
	@Override
	public boolean isEnabled()
	{
		return false;
	}

	@Override
	public Plugin getPlugin()
	{
		return null;
	}
	
}