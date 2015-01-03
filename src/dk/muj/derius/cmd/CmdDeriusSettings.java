package dk.muj.derius.cmd;

import java.util.List;

import com.massivecraft.massivecore.cmd.HelpCommand;

import dk.muj.derius.entity.MConf;

// In the future we might want to offer more settings for the Player, so I added this middlecommands
public class CmdDeriusSettings extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusSettings()
	{
		super.addSubCommand(HelpCommand.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
    public List<String> getAliases()
    {
    	return MConf.get().innerAliasesDeriusSettings;
    }
	
}
