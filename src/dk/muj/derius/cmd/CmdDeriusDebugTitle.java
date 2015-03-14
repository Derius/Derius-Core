package dk.muj.derius.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.mixin.Mixin;

public class CmdDeriusDebugTitle extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdDeriusDebugTitle()
	{
		this.addAliases("title");
		
		this.addRequiredArg("fadeIn");
		this.addRequiredArg("stay");
		this.addRequiredArg("fadeOut");
		
		this.addRequiredArg("title");
		this.addRequiredArg("subTitle");
		
		this.setErrorOnToManyArgs(false);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		Integer fadeIn = this.arg(0, ARInteger.get());
		Integer stay = this.arg(1, ARInteger.get());
		Integer fadeOut = this.arg(2, ARInteger.get());
		
		// Use quotes and stuff in the args.
		String title = this.arg(3);
		String subTitle = this.arg(4);
		
		Mixin.sendTitleMsg(dsender, fadeIn, stay, fadeOut, title, subTitle);
		
		return;
	}
	
}
