package dk.muj.derius.util;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.massivecraft.massivecore.Progressbar;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.DeriusPlugin;
import dk.muj.derius.api.player.DPlayer;

public final class ScoreboardUtil
{
	// -------------------------------------------- //
	// CONSTRUCTOR (FORBIDDEN)
	// -------------------------------------------- //
	
	private ScoreboardUtil()
	{
		
	}
	
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final int SCOREBOARD_WIDTH = 30;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public static ScoreboardManager getManager()
	{
		return Bukkit.getScoreboardManager();
	}
	
	// -------------------------------------------- //
	// STAMINA SCOREBOARD
	// -------------------------------------------- //
	
	/**
	 * Updates the Stamina Scoreboard and shows it for the given ticks
	 * ticks can be any positive amount
	 * @param {DPlayer} the player we want to update
	 * @param {int} the amount of ticks we want it to be shown
	 */
	public static void updateStaminaScore(DPlayer dplayer, final int ticks, double newStamina)
	{
		Validate.notNull(dplayer, "DPlayer musn't be null for setting Scoreboards.");
		Validate.isTrue(ticks > 0, "ticks must be positive");
		
		Scoreboard board = getManager().getNewScoreboard();

		Bukkit.getScheduler().runTaskAsynchronously(DeriusPlugin.get(), () ->
		{
			Scoreboard score = loadScoreBoardProgressBar(dplayer, newStamina, board);
		
			setScoreBoard(dplayer, score);
		
			if (dplayer.getStaminaBoardStay()) return;
			resetScoreboardTask(dplayer, newStamina, ticks);
		});
		
		return;
	}
	
	// -------------------------------------------- //
	// SCOREBOARD
	// -------------------------------------------- //
	
	/**
	 * Sets the scoreboard of the given player
	 * @param {DPlayer} the player we want to apply it to
	 * @param {Scoreboard} the scoreboard we want to set
	 */
	public static void setScoreBoard(DPlayer dplayer, Scoreboard score)
	{
		Validate.notNull(dplayer, "DPlayer musn't be null for setting Scoreboards.");
		Validate.notNull(score, "The scoreboard mustn't be null for setting scoreboards.");
		if ( ! dplayer.isPlayer()) return;
		
		Player player = dplayer.getPlayer();
		boolean online = MUtil.getOnlinePlayers().contains(player);
		
		if ( ! dplayer.getBoardShowAtAll()) return;
		
		if ( ! online) return;
		player.setScoreboard(score);
		
		return;
	}
	
	/**
	 * Resets the scoreboard of the player to an emtpy one
	 * @param {DPlayer} the player we want to reset
	 */
	public static void resetScoreBoard(DPlayer dplayer)
	{
		setScoreBoard(dplayer, getManager().getNewScoreboard());
	}
	
	// -------------------------------------------- //
	// PRIVATE SCOREBOARD METHODS
	// -------------------------------------------- //
	
	private static Scoreboard loadScoreBoardProgressBar(DPlayer dplayer, double oldStamina, Scoreboard board)
	{
		Objective objective = board.getObjective("derius_stamina");
		if (objective == null) objective = board.registerNewObjective("derius_stamina", "dummy");
		
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(Txt.parse("<a>____.[ <i>Stamina<a> ].____"));
		
		final double maxStamina = dplayer.getStaminaMax();
		final double quota = dplayer.getStamina() / maxStamina;
		
		for (String entry : board.getEntries())
		{
			board.resetScores(entry);	
		}
		String bar = Progressbar.HEALTHBAR_CLASSIC.withQuota(quota).withWidth(30).render();
		
		Score progressBar = objective.getScore(bar);
		progressBar.setScore((int) Math.round(dplayer.getStamina()));
		return board;
	}
	
	private static void resetScoreboardTask(DPlayer dplayer, double newStamina, long tick) 
	{
		Bukkit.getScheduler().runTaskLater(DeriusPlugin.get(), new Runnable()
		{
			@Override
			public void run()
			{
				if ( dplayer.getStamina() >= newStamina) return;
				resetScoreBoard(dplayer);
			}
		}, tick);
	}

}
