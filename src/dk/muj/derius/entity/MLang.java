package dk.muj.derius.entity;

import com.massivecraft.massivecore.store.Entity;

import dk.muj.derius.api.config.DLang;

public class MLang extends Entity<MConf> implements DLang
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	protected static transient MLang i;
	public static MLang get() { return i; }
	
	// -------------------------------------------- //
	// MESSAGES: RANDOM
	// -------------------------------------------- //
	
	private String prefix = "<lime>[DERIUS]";
	
	
	private String levelUp ="<i>You leveled up to level <g>{level}<i> in {skill}<i>.";
	private String exhausted = "<b>You are exhausted. <i>Wait {time}";
	
	private String levelStatusFormat = "<yellow>LVL: <h>{level}  <yellow>XP: <silver><<aqua>{exp}<lime> / <aqua>{expToNext}<silver>>";
	private String levelStatusFormatMini = "<yellow>LVL: <h>{level}";
	
	private String toolPrepared = "<i>You prepared your <h>{tool}";
	private String toolNotPrepared = "<i>You lowered your <h>{tool}";
	
	private String mustBeGamemode = "<b>Your gamemode must be <h>{gm} <b>to do this";
	private String mustNotBeGamemode = "<b>Your gamemode can't be <h>{gm} <b>to do this";
	
	private String mustHaveEnoughStamina = "<b>You don't have enough Stamina to use %s<b>.";
	
	private String skill = "Skill";
	private String skills = "Skills";
	private String yourSkills = "Your Skills";
	
	// -------------------------------------------- //
	// MESSAGES: ABILITIES
	// -------------------------------------------- //
	
	private String abilityActivated = "<i>You activated {ability}";
	private String abilityDeactivated = "<i>The ability {ability} <i>ran out";
	private String abilityAreaIllegal = "<b>Sorry, {ability}<b> can't be used in this area";
	
	
	private String abilityColorPlayerCanUse = "<pink>";
	private String abilityColorPlayerCantUse = "<gray>";
	
	
	private String abilityNoSuchId = "<i>The ability id <h>{id} <i>not valid/not in use.";
	private String abilityDisabled = "<b>The ability {ability} <b>is not enabled";
	
	
	private String abilityDisplayedDescription = "{name}: <i>{desc}";
	
	// -------------------------------------------- //
	// MESSAGES: SKILLS
	// -------------------------------------------- //

	private String skillDisplayedDesc = "{name}: <i>{desc}";
	private String skillInfoExpGain = "<k>Exp Gain: <i>";
	private String skillInfoAbilities = "<art>____.[<aqua> Abilities <art>].____";
	private String skillInfoLevelStats = "<art>____.[<aqua> Level Stats <art>].____";
	
	private String skillColorPlayerCanUse = "<lime>";
	private String skillColorPlayerCantUse = "<grey>";
	private String skillColorPlayerIsSpecialised = "<gold>";	
	
	private String skillLevelIsTooLow = "<b>You must be level <h>{level} <b> or higher in {skill}";
	private String skillDisabled = "<b>The skill {skill} <b>is not enabled";
	
	private String specialisationAutoAssigned = "<b>The skill {skill} <b> is automatically specialised";
	private String specialisationBlacklisted = "<b>It is not possible to specialise in {skill}";
	private String specialisationHasAlready = "<b>You already have {skill} specialised.";
	private String specialisationTooMany = "<b>You don't have room for more specialisations";
	private String specialisationSuccess = "<i>You are now specialised in {skill}";
	private String specialisationRemoved = "<i>You are no longer specialised in {skill}";
	private String specialisationIsnt = "<b>You do not have the skill {skill} <b>specialised";
	
	private String specialisationCantChange = "<b>You cannot change specialisation right now";
	private String specialisationMoveCooldown = "<b>please stand still for {time} <b>more";
	private String specialisationChangeCooldown = "<b>please wait {time} <b>before changing any specialisations";
	
	private String specialisationInfo = "<i>When you specialise in a skill you are able to exceed level 1000, and reach level 2000."
			+ " You can only specialise in {max} skills. If you unlearn/unspecialise in a skill you get reset back to level 0";
			
	// -------------------------------------------- //
	// MESSAGES: CHAT KEYS
	// -------------------------------------------- //
	
	private String keyAddSuccess = "<i>You have succesfully registered the key {key} to the ability {ability}.";
	private String keyRemoveSuccess = "<i>You have succesfully removed the key {key} from your list of keys.";
	private String keyAlreadyHas = "<i>The key {key} is already in use. Please use another one.";
	private String keyHasnst = "<i>The key {key} is not in use.";
	private String keysClearSuccess = "<i>You have successfully cleared your chat activation keys.";
	
	// -------------------------------------------- //
	// TITLE TIMES
	// -------------------------------------------- //
	
	private int timeLvlUpFadeIn = 5;
	private int timeLvlUpStay = 60;
	private int timeLvlUpFadeOut = 5;
	
	private int timeAbilityActivateFadeIn = 5;
	private int timeAbilityActivateStay = 50;
	private int timeAbilityActivateFadeOut = 5;
	
	private int timeAbilityDeactivateFadeIn = 5;
	private int timeAbilityDeactivateStay = 50;
	private int timeAbilityDeactivateFadeOut = 5;

	// -------------------------------------------- //
	// EXTENSIONS
	// -------------------------------------------- //
	
	private String mcmmoSkillMustBeDisabled = "<b>To use the skill {skill} <b>you must disable it's mcmmo equivalent <b>{mcmmoSkill}";
	
	// -------------------------------------------- //
	// GETTRS & SETTERS
	// -------------------------------------------- //
	
	@Override public String getSkill(){	return this.skill;}
	@Override public void setSkill(String skill){	this.skill = skill;}
	
	@Override public String getSkills(){	return this.skills;}
	@Override public void setSkills(String skills){	this.skills = skills;}
	
	@Override public String getYourSkills(){	return this.yourSkills;}
	@Override public void setYourSkills(String yourSkills){	this.yourSkills = yourSkills;}
	
	@Override public String getPrefix(){	return this.prefix;}
	@Override public void setPrefix(String prefix){	this.prefix = prefix;}
	
	@Override public String getLevelUp(){	return this.levelUp;}
	@Override public void setLevelUp(String levelUp){	this.levelUp = levelUp;}
	
	@Override public String getExhausted(){	return this.exhausted;}
	@Override public void setExhausted(String exhausted){	this.exhausted = exhausted;}
	
	@Override public String getLevelStatusFormat(){	return this.levelStatusFormat;}
	@Override public void setLevelStatusFormat(String levelStatusFormat){	this.levelStatusFormat = levelStatusFormat;}
	
	@Override public String getLevelStatusFormatMini(){	return this.levelStatusFormatMini;}
	@Override public void setLevelStatusFormatMini(String levelStatusFormatMini){	this.levelStatusFormatMini = levelStatusFormatMini;}
	
	@Override public String getToolPrepared(){	return this.toolPrepared;}
	@Override public void setToolPrepared(String toolPrepared){	this.toolPrepared = toolPrepared;}
	
	@Override public String getToolNotPrepared(){	return this.toolNotPrepared;}
	@Override public void setToolNotPrepared(String toolNotPrepared){	this.toolNotPrepared = toolNotPrepared;}
	
	@Override public String getMustBeGamemode(){	return this.mustBeGamemode;}
	@Override public void setMustBeGamemode(String mustBeGamemode){	this.mustBeGamemode = mustBeGamemode;}
	
	@Override public String getMustNotBeGamemode(){	return this.mustNotBeGamemode;}
	@Override public void setMustNotBeGamemode(String mustNotBeGamemode){	this.mustNotBeGamemode = mustNotBeGamemode;}
	
	@Override public String getMustHaveEnoughStamina(){	return this.mustHaveEnoughStamina;}
	@Override public void setMustHaveEnoughStamina(String mustHaveEnoughStamina){	this.mustHaveEnoughStamina = mustHaveEnoughStamina;}
	
	@Override public String getAbilityActivated(){	return this.abilityActivated;}
	@Override public void setAbilityActivated(String abilityActivated){	this.abilityActivated = abilityActivated;}
	
	@Override public String getAbilityDeactivated(){	return this.abilityDeactivated;}
	@Override public void setAbilityDeactivated(String abilityDeactivated){	this.abilityDeactivated = abilityDeactivated;}
	
	@Override public String getAbilityAreaIllegal(){	return this.abilityAreaIllegal;}
	@Override public void setAbilityAreaIllegal(String abilityAreaIllegal){	this.abilityAreaIllegal = abilityAreaIllegal;}
	
	@Override public String getAbilityColorPlayerCanUse(){	return this.abilityColorPlayerCanUse;}
	@Override public void setAbilityColorPlayerCanUse(String abilityColorPlayerCanUse){	this.abilityColorPlayerCanUse = abilityColorPlayerCanUse;}
	
	@Override public String getAbilityColorPlayerCantUse(){	return this.abilityColorPlayerCantUse;}
	@Override public void setAbilityColorPlayerCantUse(String abilityColorPlayerCantUse){	this.abilityColorPlayerCantUse = abilityColorPlayerCantUse;}
	
	@Override public String getAbilityNoSuchId(){	return this.abilityNoSuchId;}
	@Override public void setAbilityNoSuchId(String abilityNoSuchId){	this.abilityNoSuchId = abilityNoSuchId;}
	
	@Override public String getAbilityDisabled(){	return this.abilityDisabled;}
	@Override public void setAbilityDisabled(String abilityDisabled){	this.abilityDisabled = abilityDisabled;}
	
	@Override public String getAbilityDisplayedDescription(){	return this.abilityDisplayedDescription;}
	@Override public void setAbilityDisplayedDescription(String abilityDisplayedDescription){	this.abilityDisplayedDescription = abilityDisplayedDescription;}
	
	@Override public String getSkillDisplayedDescription(){	return this.skillDisplayedDesc;}
	@Override public void setSkillDIsplayedDescription(String skillDisplayedDesc){	this.skillDisplayedDesc = skillDisplayedDesc;}
	
	@Override public String getSkillInfoExpGain(){	return this.skillInfoExpGain;}
	@Override public void setSkillInfoExpGain(String skillInfoExpGain){	this.skillInfoExpGain = skillInfoExpGain;}
	
	@Override public String getSkillInfoAbilities(){	return this.skillInfoAbilities;}
	@Override public void setSkillInfoAbilities(String skillInfoAbilities){	this.skillInfoAbilities = skillInfoAbilities;}
	
	@Override public String getSkillInfoLevelStats(){	return this.skillInfoLevelStats;}
	@Override public void setSkillInfoLevelStats(String skillInfoLevelStats){	this.skillInfoLevelStats = skillInfoLevelStats;}
	
	@Override public String getSkillColorPlayerCanUse(){	return this.skillColorPlayerCanUse;}
	@Override public void setSkillColorPlayerCanUse(String skillColorPlayerCanUse){	this.skillColorPlayerCanUse = skillColorPlayerCanUse;}
	
	@Override public String getSkillColorPlayerCantUse(){	return this.skillColorPlayerCantUse;}
	@Override public void setSkillColorPlayerCantUse(String skillColorPlayerCantUse){	this.skillColorPlayerCantUse = skillColorPlayerCantUse;}
	
	@Override public String getSkillColorPlayerIsSpecialised(){	return this.skillColorPlayerIsSpecialised;}
	@Override public void setSkillColorPlayerIsSpecialised(		String skillColorPlayerIsSpecialised){	this.skillColorPlayerIsSpecialised = skillColorPlayerIsSpecialised;}
	
	@Override public String getSkillLevelIsTooLow(){	return this.skillLevelIsTooLow;}
	@Override public void setSkillLevelIsTooLow(String skillLevelIsTooLow){	this.skillLevelIsTooLow = skillLevelIsTooLow;}
	
	@Override public String getSkillDisabled(){	return this.skillDisabled;}
	@Override public void setSkillDisabled(String skillDisabled){	this.skillDisabled = skillDisabled;}
	
	@Override public String getSpecialisationAutoAssigned(){	return this.specialisationAutoAssigned;}
	@Override public void setSpecialisationAutoAssigned(String specialisationAutoAssigned){	this.specialisationAutoAssigned = specialisationAutoAssigned;}
	
	@Override public String getSpecialisationBlacklisted(){	return this.specialisationBlacklisted;}
	@Override public void setSpecialisationBlacklisted(String specialisationBlacklisted){	this.specialisationBlacklisted = specialisationBlacklisted;}
	
	@Override public String getSpecialisationHasAlready(){	return this.specialisationHasAlready;}
	@Override public void setSpecialisationHasAlready(String specialisationHasAlready){	this.specialisationHasAlready = specialisationHasAlready;}
	
	@Override public String getSpecialisationTooMany(){	return this.specialisationTooMany;}
	@Override public void setSpecialisationTooMany(String specialisationTooMany){	this.specialisationTooMany = specialisationTooMany;}
	
	@Override public String getSpecialisationSuccess(){	return this.specialisationSuccess;}
	@Override public void setSpecialisationSuccess(String specialisationSuccess){	this.specialisationSuccess = specialisationSuccess;}
	
	@Override public String getSpecialisationRemoved(){	return this.specialisationRemoved;}
	@Override public void setSpecialisationRemoved(String specialisationRemoved){	this.specialisationRemoved = specialisationRemoved;}
	
	@Override public String getSpecialisationIsnt(){	return this.specialisationIsnt;}
	@Override public void setSpecialisationIsnt(String specialisationIsnt){	this.specialisationIsnt = specialisationIsnt;}
	
	@Override public String getSpecialisationCantChange(){	return this.specialisationCantChange;}
	@Override public void setSpecialisationCantChange(String specialisationCantChange){	this.specialisationCantChange = specialisationCantChange;}
	
	@Override public String getSpecialisationMoveCooldown(){	return this.specialisationMoveCooldown;}
	@Override public void setSpecialisationMoveCooldown(String specialisationMoveCooldown){	this.specialisationMoveCooldown = specialisationMoveCooldown;}
	
	@Override public String getSpecialisationChangeCooldown(){	return this.specialisationChangeCooldown;}
	@Override public void setSpecialisationChangeCooldown(String specialisationChangeCooldown){	this.specialisationChangeCooldown = specialisationChangeCooldown;}
	
	@Override public String getSpecialisationInfo(){	return this.specialisationInfo;}
	@Override public void setSpecialisationInfo(String specialisationInfo){	this.specialisationInfo = specialisationInfo;}
	
	@Override public String getKeyAddSuccess(){	return this.keyAddSuccess;}
	@Override public void setKeyAddSuccess(String keyAddSuccess){	this.keyAddSuccess = keyAddSuccess;}
	
	@Override public String getKeyRemoveSuccess(){	return this.keyRemoveSuccess;}
	@Override public void setKeyRemoveSuccess(String keyRemoveSuccess){	this.keyRemoveSuccess = keyRemoveSuccess;}
	
	@Override public String getKeyAlreadyHas(){	return this.keyAlreadyHas;}
	@Override public void setKeyAlreadyHas(String keyAlreadyHas){	this.keyAlreadyHas = keyAlreadyHas;}
	
	@Override public String getKeyHasnt(){	return this.keyHasnst;}
	@Override public void setKeyHasnt(String keyHasnst){	this.keyHasnst = keyHasnst;}
	
	@Override public String getKeysClearSuccess(){	return this.keysClearSuccess;}
	@Override public void setKeysClearSuccess(String keysClearSuccess){	this.keysClearSuccess = keysClearSuccess;}
	
	@Override public int getTimeLvlUpFadeIn(){	return this.timeLvlUpFadeIn;}
	@Override public void setTimeLvlUpFadeIn(int timeLvlUpFadeIn){	this.timeLvlUpFadeIn = timeLvlUpFadeIn;}
	
	@Override public int getTimeLvlUpStay(){	return this.timeLvlUpStay;}
	@Override public void setTimeLvlUpStay(int timeLvlUpStay){	this.timeLvlUpStay = timeLvlUpStay;}
	
	@Override public int getTimeLvlUpFadeOut(){	return this.timeLvlUpFadeOut;}
	@Override public void setTimeLvlUpFadeOut(int timeLvlUpFadeOut){	this.timeLvlUpFadeOut = timeLvlUpFadeOut;}
	
	@Override public int getTimeAbilityActivateFadeIn(){	return this.timeAbilityActivateFadeIn;}
	@Override public void setTimeAbilityActivateFadeIn(int timeAbilityActivateFadeIn){	this.timeAbilityActivateFadeIn = timeAbilityActivateFadeIn;}
	
	@Override public int getTimeAbilityActivateStay(){	return this.timeAbilityActivateStay;}
	@Override public void setTimeAbilityActivateStay(int timeAbilityActivateStay){	this.timeAbilityActivateStay = timeAbilityActivateStay;}
	
	@Override public int getTimeAbilityActivateFadeOut(){	return this.timeAbilityActivateFadeOut;}
	@Override public void setTimeAbilityActivateFadeOut(int timeAbilityActivateFadeOut){	this.timeAbilityActivateFadeOut = timeAbilityActivateFadeOut;}
	
	@Override public int getTimeAbilityDeactivateFadeIn(){	return this.timeAbilityDeactivateFadeIn;}
	@Override public void setTimeAbilityDeactivateFadeIn(int timeAbilityDeactivateFadeIn){	this.timeAbilityDeactivateFadeIn = timeAbilityDeactivateFadeIn;}
	
	@Override public int getTimeAbilityDeactivateStay(){	return this.timeAbilityDeactivateStay;}
	@Override public void setTimeAbilityDeactivateStay(int timeAbilityDeactivateStay){	this.timeAbilityDeactivateStay = timeAbilityDeactivateStay;}
	
	@Override public int getTimeAbilityDeactivateFadeOut(){	return this.timeAbilityDeactivateFadeOut;}
	@Override public void setTimeAbilityDeactivateFadeOut(int timeAbilityDeactivateFadeOut){	this.timeAbilityDeactivateFadeOut = timeAbilityDeactivateFadeOut;}
	
	@Override public String getMcmmoSkillMustBeDisabled() { return this.mcmmoSkillMustBeDisabled; }
	@Override public void setMcmmoSkillMustBeDisabled(String mcmmoSkillMustBeDisabled) { this.mcmmoSkillMustBeDisabled = mcmmoSkillMustBeDisabled; }

}
