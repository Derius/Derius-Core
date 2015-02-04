package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.cmd.MassiveCommandException;
import com.massivecraft.massivecore.cmd.arg.ARString;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.Perm;
import dk.muj.derius.cmd.arg.ARMPlayer;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.MPlayerColl;
import dk.muj.derius.entity.Skill;
import dk.muj.derius.entity.SkillColl;

public class CmdDeriusClean extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusClean()
	{
		this.addRequiredArg("id/all");
		this.addOptionalArg("player/all", "you");
		this.addOptionalArg("force it", "no");
		
		this.addRequirements(ReqHasPerm.get(Perm.CLEAN.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveCommandException
	{
		List<Skill> skillList = new ArrayList<Skill>();
		List<MPlayer> mplayerList = new ArrayList<MPlayer>();
		
		// Arg 0: ID or all
		if (this.arg(0).equalsIgnoreCase("all") && Perm.CLEAN_SKILL_ALL.has(sender, true))
		{
			skillList.addAll(SkillColl.getAllSkills());
		}
		else if (this.argIsSet(0))
		{
			skillList.add(SkillColl.get().get(this.arg(0)));
		}
		else
		{
			return;
		}
		// Arg 1: player, yourself or all
		if (this.arg(1).equalsIgnoreCase("all") && Perm.CLEAN_PLAYER_ALL.has(sender, true))
		{
			mplayerList.addAll(MPlayerColl.get().getAll());
		}
		else if (this.argIsSet(1) && Perm.CLEAN_PLAYER.has(sender, true))
		{
			MPlayer mplayer = this.arg(1, ARMPlayer.getAny(), msender);
			if (mplayer != msender && Perm.CLEAN_PLAYER_OTHER.has(sender, true)) return;

			mplayerList.add(mplayer);
		}
		else
		{
			return;
		}
		
		// Arg 2: force or not
		String force = this.argConcatFrom(2, ARString.get(), "no");
		String forceYes = "Yes, I want to force this";
		if (!force.equals(forceYes))
			return;
	
		// Execute the cleaning
		for (Skill skill : skillList)
		{
			for (MPlayer mplayer : mplayerList)
			{
				mplayer.cleanNoCheck(skill.getId());
			}
		}
		
		return;
	}

}
