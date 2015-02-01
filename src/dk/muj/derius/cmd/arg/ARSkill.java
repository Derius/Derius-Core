package dk.muj.derius.cmd.arg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.arg.ARAbstractSelect;

import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.Skill;
import dk.muj.derius.entity.SkillColl;
import dk.muj.derius.util.SkillUtil;

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
	public String typename()
	{
		return "skills";
	}
	
	@Override
	public Skill select(String arg, CommandSender sender)
	{
		if (arg == null) return null;
		MPlayer mplayer = MPlayer.get(sender);
		arg = arg.toLowerCase();
		for (Skill skill : SkillColl.getAllSkills())
		{
			if (skill == null || skill.getName() == null) continue;
			if (skill.getName().toLowerCase().startsWith(arg))
			{
				if ( ! SkillUtil.canPlayerSeeSkill(mplayer, skill, false)) continue;
				return skill;
			}
		}
		
		return null;
	}

	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		MPlayer mplayer = MPlayer.get(sender);
		
		List<String> ret = new ArrayList<String>();
	
		for (Skill skill : SkillColl.getAllSkills())
		{
			if ( ! SkillUtil.canPlayerSeeSkill(mplayer, skill, false)) continue;
			ret.add(skill.getName());
		}
		return ret;
	}
	
}
