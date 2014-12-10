package dk.muj.derius.engine;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;

import dk.muj.derius.Derius;
import dk.muj.derius.ability.Abilities;
import dk.muj.derius.ability.Ability;

public class AbilityEngine extends EngineAbstract
{

	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static AbilityEngine i = new AbilityEngine();
	public static AbilityEngine get() { return i; }
	public AbilityEngine() {}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return Derius.get();
	}
	
	@Override
	public Long getPeriod()
	{
		return (long) (20*10);
	}
	
	@Override
	public Long getDelay()
	{
		return (long) (20*60);
	}
	
	// -------------------------------------------- //
	// REPEAT TASK
	// -------------------------------------------- //

	@Override
	public void run()
	{
		for(Ability a: Abilities.GetAllAbilities())
		{
			if(!a.DidChange())
				return;
			
			for(Material m: a.getRemovedBlockBreakKeys())
				Abilities.removeBlockBreakKey(m);
			a.getRemovedBlockBreakKeys().clear();
			
			for(Material m: a.getRemovedInteractKeys())
				Abilities.removeInteractKey(m);
			a.getRemovedInteractKeys().clear();
			
			for(Material m: a.getBlockBreakKeys())
				Abilities.addBlockBreakKey(m, a);
			
			for(Material m: a.getInteractKeys())
				Abilities.addInteractKey(m, a);
			
			a.setChange(false);
		}
	}
}
