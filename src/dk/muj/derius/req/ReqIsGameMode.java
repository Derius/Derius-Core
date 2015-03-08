package dk.muj.derius.req;

import org.bukkit.GameMode;

import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.Req;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.api.config.DLang;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.req.util.ReqToDefault;

public class ReqIsGameMode implements Req, ReqToDefault
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static ReqIsGameMode get(GameMode gameMode) { return new ReqIsGameMode(gameMode); }
	public ReqIsGameMode(GameMode gameMode) { this.gameMode = gameMode; }
	
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
	
	private final GameMode gameMode;
	public GameMode getGameMode() { return this.gameMode; }
	
	// -------------------------------------------- //
	// OVERRIDE: DEFAULT
	// -------------------------------------------- //
	
	@Override
	public boolean apply(DPlayer dplayer)
	{
		return IdUtil.isGameMode(dplayer, gameMode, false);
	}
	
	@Override
	public String createErrorMessage(DPlayer dplayer)
	{
		return Txt.parse(DLang.get().getMustBeGamemode().replace("{gm}", Txt.getNicedEnum(gameMode)));
	}
	
}
