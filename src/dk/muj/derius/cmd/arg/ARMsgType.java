package dk.muj.derius.cmd.arg;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.arg.ARAbstractSelect;

import dk.muj.derius.entity.MsgType;

public class ARMsgType extends ARAbstractSelect<MsgType>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARMsgType i = new ARMsgType();
	public static ARMsgType get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String typename()
	{
		return "MsgType";
	}
	
	@Override
	public MsgType select(String arg, CommandSender sender)
	{
		
		arg = getComparable(arg);
		
		// TODO: Shouldn't there be a better way to do this?
		switch(arg){
			case "chat":
				return MsgType.CHAT;
			case "title":
				return MsgType.TITLE;
			case "scoreboard":
				return MsgType.SCOREBOARD;
			default:
				return null;
		}
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static String getComparable(String str)
	{
		str = str.toLowerCase();
		str = str.replace("_", "");
		str = str.replace(" ", "");
		return str;
	}

	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		return null;
	}
}
