package dk.muj.derius.entity;

import com.massivecraft.massivecore.store.Entity;

public class MLang extends Entity<MConf>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	protected static transient MLang i;
	public static MLang get() { return i; }
	
	// -------------------------------------------- //
	// MESSAGES: RANDOM
	// -------------------------------------------- //
	
	public String prefix = "<green>[DERIUS]";
	
	public String levelUp ="<i>You leveled up to level <lime>%s<i> in <aqua>%s<i>.";
	public String exhausted = "<i> You are still exhausted. You will be ready again in %s";
	
	public String levelStatusFormat = "<navy>LVL: <lime>%s  <navy>XP: <lime>%s<yellow>/<lime>%s";
	public String levelStatusFormatMini = "<navy>LVL: <lime>%s";
	
	public String toolPrepared = "<i>You prepared your <h>%s";
	public String toolNotPrepared = "<i>You lowered your <h>%s";
	
	// -------------------------------------------- //
	// MESSAGES: ABILITIES
	// -------------------------------------------- //
	
	public String abilityActivated = "<i>You activated %s";
	public String abilityDeactivated = "<i>The ability %s <i>ran out";
	public String abilityAreaIllegal = "<b>Sorry, %s<b> can't be used in this area";
	
	public String abilityColorPlayerCanUse = "<pink>";
	public String abilityColorPlayerCantUse = "<gray>";
	
	public String abilityInvalidId = "<i>The Ability Id <h>%s <i>not valid/not in use.";
	
	public String abilityDisplayedDescription = "%s: <i>%s";
	
	// -------------------------------------------- //
	// MESSAGES: SKILLS
	// -------------------------------------------- //
	
	public String skillInfoPassiveAbilities ="<art>____[<aqua>Passive abilities<art>]____";
	public String skillInfoActiveAbilities =	"<art>____[<aqua> Active abilities <art>]____";
	public String skillInfoLevelStats ="<art>____[<aqua>Level stats<art>]____";
	
	public String skillColorPlayerCanUse = "<aqua>";
	public String skillColorPlayerCantUse = "<grey>";
	public String skillColorPlayerIsSpecialised = "<gold>";	

	public String specialisationAutoAssigned = "<b>The skill %s <b> is automatically specialised";
	public String specialisationBlacklisted = "<b>It is not possible to specialise in %s";
	public String specialisationHasAlready = "<b>You already have %s";
	public String specialisationTooMany = "<b>You don't have room for more specialisations";
	public String specialisationSuccess = "<i>You are now specialised in %s";
	public String specialisationRemoved = "<i>You are no longer specialised in %s";
	public String specialisationIsnt = "<b>You do not have the Skill %s <b>specialised";
	
	// -------------------------------------------- //
	// MESSAGES: CHAT KEYS
	// -------------------------------------------- //
	
	public String keyAddSuccess = "<i>You have succesfully registered the key %s to the ability %s.";
	public String keysAlreadyHas = "<i>The key %s is already in use. Please use another one.";
	public String keysRemoveSuccess = "<i>You have succesfully removed the key %s from your list of keys.";
	public String keysClearWarning = "<i>Do you really want to clear all your entries?  Type <aqua> /d k c true";
	public String keysClearSuccess = "<i>You have successfully cleared your chat activation keys.";
	
	// -------------------------------------------- //
	// TITLE TIMES
	// -------------------------------------------- //
	
	public int timeLvlUpFadeIn = 5;
	public int timeLvlUpStay = 60;
	public int timeLvlUpFadeOut = 5;
	
	public int timeAbilityActivateFadeIn = 5;
	public int timeAbilityActivateStay = 50;
	public int timeAbilityActivateFadeOut = 5;
	
	public int timeAbilityDeactivateFadeIn = 5;
	public int timeAbilityDeactivateStay = 50;
	public int timeAbilityDeactivateFadeOut = 5;

}
