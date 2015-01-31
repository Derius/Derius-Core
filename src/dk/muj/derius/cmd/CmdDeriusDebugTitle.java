package dk.muj.derius.cmd;

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
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		Integer fadeIn = this.arg(0, ARInteger.get());
		if (fadeIn == null) return;
		Integer stay = this.arg(1, ARInteger.get());
		if (stay == null) return;
		Integer fadeOut = this.arg(2, ARInteger.get());
		if (fadeOut == null) return;
		
		String title = this.arg(3);
		if (title.equalsIgnoreCase("null")) title = null;
		String subtitle = this.arg(4);
		if (subtitle.equalsIgnoreCase("null")) subtitle = null;
		
		Mixin.sendTitleMsg(sender, fadeIn, stay, fadeOut, title, subtitle);
	}
	
}
