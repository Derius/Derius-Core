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
		super.addAliases("title");
		
		super.addRequiredArg("fadeIn");
		super.addRequiredArg("stay");
		super.addRequiredArg("fadeOut");
		
		super.addRequiredArg("title");
		super.addRequiredArg("subTitle");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		Integer in = super.arg(0, ARInteger.get());
		if (in == null) return;
		Integer stay = super.arg(1, ARInteger.get());
		if (stay == null) return;
		Integer out = super.arg(2, ARInteger.get());
		if (out == null) return;
		
		String title = this.arg(3);
		if (title.equalsIgnoreCase("null")) title = null;
		String subtitle = this.arg(4);
		if (subtitle.equalsIgnoreCase("null")) subtitle = null;
		
		Mixin.sendTitleMsg(sender, in, stay, out, title, subtitle);
	}
	
}
