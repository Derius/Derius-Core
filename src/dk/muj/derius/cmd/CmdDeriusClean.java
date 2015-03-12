package dk.muj.derius.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.DeriusPerm;

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
		
		this.addRequirements(ReqHasPerm.get(DeriusPerm.CLEAN.getNode()));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		throw new MassiveException().addMessage("This command is temporarily disabled");
		// Command functions again, cleaning isn't ready though
		
		/*
		List<Skill> skillList = new ArrayList<Skill>();
		List<DPlayer> dplayerList = new ArrayList<DPlayer>();
		
		// Arg 0: all
		if (this.arg(0).equalsIgnoreCase("all") && Perm.CLEAN_SKILL_ALL.has(sender, true))
		{
			skillList.addAll(SkillColl.getAllSkills());
		}
		else
		{
			return;
		}
		
		// Arg 0: ID
		if (this.argIsSet(0))
		{
			skillList.add(SkillColl.get().get(this.arg(0)));
		}
		else
		{
			return;
		}
		
		// Arg 1: All
		if (this.arg(1).equalsIgnoreCase("all") && Perm.CLEAN_PLAYER_ALL.has(sender, true))
		{
			dplayerList.addAll(MPlayerColl.get().getAll());
		}
		else
		{
			return;
		}
		
		// Arg 1: Yourself
		if (this.argIsSet(1) && Perm.CLEAN_PLAYER.has(sender, true))
		{
			DPlayer dplayer = this.arg(1, ARDPlayer.getAny(), dsender);
			
			// Arg 1: Player other
			if (dplayer != dsender && Perm.CLEAN_PLAYER_OTHER.has(sender, true)) return;

			dplayerList.add(dplayer);
		}
		else
		{
			return;
		}
		
		// Arg 2: force or not
		String force = this.argConcatFrom(2, ARString.get(), "no");
		String forceYes = "Yes, I want to force this";
		if ( ! force.equals(forceYes)) return;
	
		// Execute the cleaning
		for (Skill skill : skillList)
		{
			for (DPlayer dplayer : dplayerList)
			{
				dplayer.c
				dplayer.cleanNoCheck(skill.getId());
			}
		}
		
		return;
		*/
	}

}
