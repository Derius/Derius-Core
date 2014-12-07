package dk.muj.derius.cmd.arg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.arg.ARAbstractSelect;

import dk.muj.derius.skill.Skill;
import dk.muj.derius.skill.Skills;

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
		arg = getComparable(arg);
		
		String skillstr;
		for (Skill skill : Skills.GetAllSkills())
		{
			skillstr = getComparable(arg);
			if(skill.getName().toLowerCase().startsWith(skillstr.toLowerCase()))
			{
				return skill;
			}
		}
		
		return null;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static String getComparable(String str)
	{
		str = str.toLowerCase();
		str = str.replace("_", "");
		str = str.replace(" ", "");
		return str;
	}

	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		List<String> ret = new ArrayList<String>();
		
		for (Skill skill : Skills.GetAllSkills())
		{
			ret.add(skill.getName());
		}
		return ret;
	}
}
