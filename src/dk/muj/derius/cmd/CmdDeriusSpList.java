package dk.muj.derius.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Perm;
import dk.muj.derius.cmd.arg.ARMPlayer;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;

public class CmdDeriusSpList  extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
		
	public CmdDeriusSpList()
	{
		super.setDesc("lists specialised skills");
		
		super.addOptionalArg("player", "you");
		
		super.addRequirements(ReqHasPerm.get(Perm.SPECIALISATION_LIST.node));
	}
		
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		MPlayer target = super.arg(0, ARMPlayer.getAny(), msender);
		if(target == null) return;
		if(args.size() == 1 && !Perm.SPECIALISATION_LIST_OTHER.has(sender, true))
			return;
		List<Skill> skills = target.getSpecialisedSkills();

		List<String> msgLines = new ArrayList<String>();
		this.msg(Txt.titleize(target.getDisplayName(msender)+ "'s specilisations"));
		for(Skill s: skills)
			msgLines.add(s.getDisplayName(msender)+": "+target.getLvlStatus(s).toString());
		msender.msg(msgLines);
	}
	

	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusSpList;
    }

}
