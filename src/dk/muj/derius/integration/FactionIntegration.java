package dk.muj.derius.integration;

import org.bukkit.Bukkit;

import com.massivecraft.factions.entity.MFlag;

import dk.muj.derius.Const;
import dk.muj.derius.entity.MConf;

public class FactionIntegration
{
	private static boolean enabled = false;
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	/**
	 * Enables faction integration.
	 * Does nothing if already enabled.
	 * @return True if faction is working
	 */
	public static boolean EstablishIntegration()
	{
		if(FactionIntegration.enabled == true)
			;
		else if(Bukkit.getServer().getPluginManager().getPlugin("Factions") != null)
		{
			MFlag.getCreative(5100, Const.FACTION_FLAG_SKILLS_USE, Const.FACTION_FLAG_SKILLS_USE, "Can you use Derius skills in this faction?", 
					"You can use Derius skills in this faction", "You can NOT use Derius skills in this faction", 
					MConf.get().factionFlagUseSkillsDefaultValue, MConf.get().factionFlagUseSkillsEditableByUser, MConf.get().factionFlagUseSkillsVisibleByUser);
			
			MFlag.getCreative(5200, Const.FACTION_FLAG_SKILLS_EARN, Const.FACTION_FLAG_SKILLS_EARN, "Can you earn experience for Derius skills in this faction?", 
					"You can earn experience for Derius skills in this faction", "You can NOT earn experience for Derius skills in this faction", 
					MConf.get().factionFlagEarnDefaultValue, MConf.get().factionFlagEarnEditableByUser, MConf.get().factionFlagEarnVisibleByUser);
			
			MFlag.getCreative(5300, Const.FACTION_FLAG_ABILITIES_USE, Const.FACTION_FLAG_ABILITIES_USE, "Can you use Derius active abilities in this faction?", 
					"You can use Derius active abilities in this faction", "You can NOT use Derius active abilities in this faction", 
					MConf.get().factionFlagUseAbilitiesDefaultValue, MConf.get().factionFlagUseAbilitiesEditableByUser, MConf.get().factionFlagUseAbilitiesVisibleByUser);
			
			MFlag.getCreative(5400, Const.FACTION_FLAG_SKILLS_OVERRIDE_WORLD, Const.FACTION_FLAG_SKILLS_OVERRIDE_WORLD, "Does this factions flags override the skill settings for the world?", 
					"This factions flags overrides the worlds skill settings", "This factions flags does not override the worlds skill settings", 
					MConf.get().factionFlagOverrideWorldDefaultValue, MConf.get().factionFlagOverrideWorldEditableByUser, MConf.get().factionFlagOverrideWorldVisibleByUser);
			
			FactionIntegration.enabled = true;
		}
		else
		{
			FactionIntegration.enabled = false;
		}

		return FactionIntegration.enabled;
	}
	
}
