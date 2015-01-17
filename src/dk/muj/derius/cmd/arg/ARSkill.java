package dk.muj.derius.cmd.arg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.arg.ARAbstractSelect;

import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;

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
		return "skill";
	}
	
	@Override
	public Skill select(String arg, CommandSender sender)
	{
		
		arg = arg.toLowerCase();
		for (Skill skill : Skill.getAllSkills())
		{
			if (skill.getName().toLowerCase().startsWith(arg))
			{
				if ( ! MPlayer.get(sender).canSeeSkill(skill)) continue;
				return skill;
			}
		}
		
		return null;
	}

	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		List<String> ret = new ArrayList<String>();
		
		for (Skill skill : Skill.getAllSkills())
		{
			if ( ! MPlayer.get(sender).canSeeSkill(skill)) continue;
			ret.add(skill.getName());
		}
		return ret;
	}
}
