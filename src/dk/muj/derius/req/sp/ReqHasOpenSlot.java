package dk.muj.derius.req.sp;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.ability.Ability;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.req.Req;
import dk.muj.derius.skill.Skill;

public class ReqHasOpenSlot implements Req
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
    private static ReqHasOpenSlot i = new ReqHasOpenSlot();
	public static ReqHasOpenSlot get() { return i; }
	private ReqHasOpenSlot() {}
	
	// -------------------------------------------- //
	// OVERRIDE: SKILL
	// -------------------------------------------- //

	@Override
	public boolean apply(CommandSender sender, Skill skill)
	{
		return MPlayer.get(sender).getOpenSpecialisationSlots() > 0;
	}

	@Override
	public String createErrorMessage(CommandSender sender, Skill skill)
	{
		return Txt.parse(MConf.get().msgSkillSpecialisationTooMany);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: ABILITY
	// -------------------------------------------- //

	@Override
	public boolean apply(CommandSender sender, Ability ability)
	{
		return MPlayer.get(sender).getOpenSpecialisationSlots() > 0;
	}

	@Override
	public String createErrorMessage(CommandSender sender, Ability ability)
	{
		return Txt.parse(MConf.get().msgSkillSpecialisationTooMany);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: DEFAULT
	// -------------------------------------------- //

	@Override
	public boolean apply(CommandSender sender)
	{
		return MPlayer.get(sender).getOpenSpecialisationSlots() > 0;
	}
	
	@Override
	public String createErrorMessage(CommandSender sender)
	{
		return Txt.parse(MConf.get().msgSkillSpecialisationTooMany);
	}

	// -------------------------------------------- //
	// OVERRIDE: CMD
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender, MassiveCommand cmd)
	{
		return MPlayer.get(sender).getOpenSpecialisationSlots() > 0;
	}
	
	@Override
	public String createErrorMessage(CommandSender sender, MassiveCommand cmd)
	{
		return Txt.parse(MConf.get().msgSkillSpecialisationTooMany);
	}

}
