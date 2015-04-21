package dk.muj.derius.cmd.arg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.arg.ARAbstractSelect;

import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.skill.Skill;
import dk.muj.derius.api.util.SkillUtil;

public class ARSkill extends ARAbstractSelect<Skill>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARSkill i = new ARSkill();
	public static ARSkill get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Skill select(String arg, CommandSender sender)
	{
		if (arg == null) return null;
		DPlayer dplayer = DeriusAPI.getDPlayer(sender);
		arg = arg.toLowerCase();
		for (Skill skill : DeriusAPI.getAllSkills())
		{
			if (skill == null || skill.getName() == null) continue;
			if (skill.getName().toLowerCase().startsWith(arg))
			{
				if (dplayer != null && ! SkillUtil.canPlayerSeeSkill(dplayer, skill, VerboseLevel.ALWAYS)) continue;
				return skill;
			}
		}
		
		return null;
	}

	@Override
	public List<String> altNames(CommandSender sender)
	{
		DPlayer dplayer = DeriusAPI.getDPlayer(sender);
		
		List<String> ret = new ArrayList<String>();
	
		for (Skill skill : DeriusAPI.getAllSkills())
		{
			if ( ! SkillUtil.canPlayerSeeSkill(dplayer, skill, VerboseLevel.ALWAYS)) continue;
			ret.add(skill.getName());
		}
		return ret;
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
}
