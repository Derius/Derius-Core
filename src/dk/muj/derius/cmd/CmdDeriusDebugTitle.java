package dk.muj.derius.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.cmd.arg.ARString;
import com.massivecraft.massivecore.mixin.Mixin;

public class CmdDeriusDebugTitle extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdDeriusDebugTitle()
	{
		this.addAliases("title");
		
		this.addArg(ARInteger.get(), "fadeIn").setDesc("How many seconds it fades in");
		this.addArg(ARInteger.get(), "stay").setDesc("How many seconds it stays");
		this.addArg(ARInteger.get(), "fadeOut").setDesc("How many seconds it fades out");
		
		this.addArg(ARString.get(), "title").setDesc("The upper part of the display");
		this.addArg(ARString.get(), "subTitle").setDesc("The lower part of the display");
		
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		int fadeIn = this.readArg();
		int stay = this.readArg();
		int fadeOut = this.readArg();
		
		// Use quotes and stuff in the args.
		String title = this.readArg();
		String subTitle = this.readArg();
		
		Mixin.sendTitleMsg(dsender, fadeIn, stay, fadeOut, title, subTitle);
		
		return;
	}
	
}
