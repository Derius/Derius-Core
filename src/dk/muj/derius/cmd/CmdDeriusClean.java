package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.cmd.arg.ARString;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

import dk.muj.derius.Perm;
import dk.muj.derius.cmd.arg.ARMPlayer;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.entity.MPlayerColl;
import dk.muj.derius.skill.Skill;

public class CmdDeriusClean extends DeriusCommand
{
	public CmdDeriusClean()
	{
		this.addRequiredArg("id of skill/ability or all");
		this.addOptionalArg("player/all", "you");
		this.addOptionalArg("force it", "no");
		
		this.addRequirements(ReqHasPerm.get(Perm.CLEAN.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		List<Skill> skillList = new ArrayList<Skill>();
		List<MPlayer> playerList = new ArrayList<MPlayer>();
		
		// Arg 0: ID or all
		if (this.arg(0).equals("all"))
		{
			if (Perm.CLEAN_SKILL_ALL.has(sender, true) )
			{
				skillList.addAll(Skill.getAllSkills());
			}
			else
			{
				return;
			}
		}
		else
		{
			skillList.add(Skill.getSkillById(this.arg(0, ARInteger.get())));
		}

		// Arg 1: player, yourself or all
		if (this.arg(1).equals("all"))
		{
			if (Perm.CLEAN_PLAYER_ALL.has(sender, true) )
			{
				playerList.addAll(MPlayerColl.get().getAll());
			}
			else
			{
				return;
			}
		}
		else
		{
			MPlayer target = this.arg(1, ARMPlayer.getAny(), msender);
			if(target == null) return;
			
			// Target permission check
			if (target == msender)
			{
				// Clean self
				if (Perm.CLEAN_PLAYER.has(sender, true))
				{
					playerList.add(target);
				}
				else
				{
					return;
				}				
			}
			else
			{
				// Clean somebody else
				if (Perm.CLEAN_PLAYER_OTHER.has(sender, true))
				{
					playerList.add(target);
				}
				else
				{
					return;
				}	
			}
		}
		
		// Arg 2: force or not
		String force = this.argConcatFrom(2, ARString.get(), "no");
		String forceYes = "Yes, I want to force this";
		if(!force.equals(forceYes))
			return;
		

		
		// Execute the cleaning
		for (Skill skill: skillList)
		{
			for(MPlayer target: playerList)
			{
				target.cleanNoCheck(skill.getId());
			}
		}
	}
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusClean;
    }
}
