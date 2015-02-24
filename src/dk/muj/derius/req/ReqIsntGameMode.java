package dk.muj.derius.req;

import java.util.Arrays;
import java.util.Collection;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.Req;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.req.util.ReqToDefault;

public class ReqIsntGameMode implements Req, ReqToDefault
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static ReqIsntGameMode get(GameMode... gameModes) { return get(Arrays.asList(gameModes)); }
	public static ReqIsntGameMode get(Collection<GameMode> gameModes) { return new ReqIsntGameMode(gameModes); }
	public ReqIsntGameMode(Collection<GameMode> gameModes) { this.gameModes = gameModes; }
	
	// -------------------------------------------- //
	// OVERRIDE: VERBOSE LEVEL
	// -------------------------------------------- //
	
	@Override
	public VerboseLevel getVerboseLevel()
	{
		return VerboseLevel.HIGH;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Collection<GameMode> gameModes;
	public Collection<GameMode> getGameModes() { return this.gameModes; }
	
	// -------------------------------------------- //
	// OVERRIDE: DEFAULT
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender)
	{
		for (GameMode gm : gameModes)
		{
			if ( IdUtil.isGameMode(sender, gm, false)) return false;
		}
		return true;
	}
	
	@Override
	public String createErrorMessage(CommandSender sender)
	{
		return Txt.parse(MLang.get().mustNotBeGamemode, Txt.getNicedEnum(IdUtil.getGameMode(sender, null)));
	}

}
