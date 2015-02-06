package dk.muj.derius.cmd;

import com.massivecraft.massivecore.cmd.MassiveCommandException;
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
	public void perform() throws MassiveCommandException
	{
		Integer fadeIn = this.arg(0, ARInteger.get());
		Integer stay = this.arg(1, ARInteger.get());
		Integer fadeOut = this.arg(2, ARInteger.get());
		
		String title = this.argConcatFrom(3);

		Mixin.sendTitleMsg(dsender, fadeIn, stay, fadeOut, title, null);
		
		return;
	}
	
}
