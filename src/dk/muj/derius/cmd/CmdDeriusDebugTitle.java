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
		int fadeIn = this.arg(ARInteger.get());
		int stay = this.arg(ARInteger.get());
		int fadeOut = this.arg(ARInteger.get());
		
		// Use quotes and stuff in the args.
		String title = this.arg();
		String subTitle = this.arg();
		
		Mixin.sendTitleMsg(dsender, fadeIn, stay, fadeOut, title, subTitle);
		
		return;
	}
	
}
