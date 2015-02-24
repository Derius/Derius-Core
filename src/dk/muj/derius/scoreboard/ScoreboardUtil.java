package dk.muj.derius.scoreboard;

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

import dk.muj.derius.DeriusCore;
import dk.muj.derius.api.DPlayer;

public class ScoreboardUtil
{
	// -------------------------------------------- //
	// CONSTRUCTOR (FORBIDDEN)
	// -------------------------------------------- //
	
	private ScoreboardUtil()
	{
		
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public static ScoreboardManager manager = Bukkit.getScoreboardManager();
	
	// -------------------------------------------- //
	// STAMINA SCOREBOARD
	// -------------------------------------------- //
	
	/**
	 * Updates the Stamina Scoreboard and shows it for the given ticks
	 * ticks can be any positive amount
	 * @param {DPlayer} the player we want to update
	 * @param {int} the amount of ticks we want it to be shown
	 */
	public static void updateStaminaScore(DPlayer dplayer, long ticks)
	{
		Validate.notNull(dplayer, "DPlayer musn't be null for setting Scoreboards.");
		Scoreboard score = getScoreProgressBar(dplayer);
		ticks = Math.abs(ticks);
		
		setScoreBoard(dplayer, score);
		
		if (dplayer.getStaminaBoardStay()) return;
		resetScoreboardTask(dplayer, ticks);
		
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
		setScoreBoard(dplayer, manager.getNewScoreboard());
	}
	
	// -------------------------------------------- //
	// PRIVATE SCOREBOARD METHODS
	// -------------------------------------------- //
	
	private static Scoreboard getScoreProgressBar(DPlayer dplayer)
	{
		Scoreboard board = manager.getNewScoreboard();
		Objective objective = board.registerNewObjective("derius_stamina", "dummy");
		
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(Txt.parse("<a>____.[<i>Stamina<a>].____"));
		
		double progressbarQuota = dplayer.getStamina() / dplayer.getStaminaMax();
		String bar = Progressbar.HEALTHBAR_CLASSIC.withQuota(progressbarQuota).withWidth(30).render();
		
		Score progressBar = objective.getScore(bar);
		progressBar.setScore((int) Math.round(dplayer.getStamina()));
		return board;
	}
	
	private static void resetScoreboardTask(DPlayer dplayer, long tick) 
	{
		Bukkit.getScheduler().runTaskLater(DeriusCore.get(), () -> resetScoreBoard(dplayer), tick);
	}

}
