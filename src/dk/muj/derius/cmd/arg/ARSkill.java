package dk.muj.derius.cmd.arg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.arg.ARAbstractSelect;

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
		
		String skillstr = arg.toLowerCase();
		for (Skill skill : Skill.getAllSkills())
		{
			if (skill.getName().toLowerCase().startsWith(skillstr))
			{
				return skill;
			}
		}
		
		return null;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //

	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		List<String> ret = new ArrayList<String>();
		
		for (Skill skill : Skill.getAllSkills())
		{
			ret.add(skill.getName());
		}
		return ret;
	}
}
