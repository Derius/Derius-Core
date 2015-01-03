package dk.muj.derius.skill;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.PermUtil;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Const;
import dk.muj.derius.Perm;
import dk.muj.derius.ability.Ability;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.events.SkillRegisteredEvent;
import dk.muj.derius.exceptions.IdAlreadyInUseException;
import dk.muj.derius.integration.FactionIntegration;

public abstract class Skill
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private String desc = "";
	private String name;
	
	// The list of existing skills in a class-variable
	private static List<Skill> skillList = new CopyOnWriteArrayList<Skill>();
	
	// The lists of of active and passive abilities
	private List<Ability> passiveAbilities = new CopyOnWriteArrayList<Ability>();
	private List<Ability> activeAbilities = new CopyOnWriteArrayList<Ability>();
	
	private List<String> earnExpDesc = new CopyOnWriteArrayList<String>();
	
	//Lambda sw@g
	Function<Long, LvlStatus> expToLvlStatus = (Long exp) -> 	
	{	//This is the default algorithm
		int level = 0, nextLvlExp;
		for(nextLvlExp = 1024; nextLvlExp < exp; nextLvlExp *= 1.01, level++)
			exp -= nextLvlExp;
		
		return new LvlStatusDefault(level, exp.intValue(), nextLvlExp);
	};
	
	// -------------------------------------------- //
	// STATIC
	// -------------------------------------------- //
	
	/**
	 * Gets a skill from its id. 
	 * This is the best way to get a skill, since the id never changes.
	 * @param {String} The id of the skill you wanted to get.
	 * @return{Skill} The skill which has this id
	 */
	public static Skill getSkillById(int skillId)
	{	//Just a test for now
		Optional<Skill> binary = binarySkillLookup(skillId);
		if(binary.isPresent()) return binary.get();
		

		
		for(Skill skill: Skill.skillList)
		{
			if(skill.getId() == skillId)
			{	//If binary didn't work there is an issue
				Bukkit.broadcastMessage("SOMETHING IS WRONG");
				return skill;
			}
		}
		return null;
	}
	
	private static Optional<Skill> binarySkillLookup(int idLookup)
	{
		int lower = 0;
		int upper = skillList.size()-1;
		
		if(skillList.size() < 1)
			return Optional.empty();
		
		while(true)
		{
			int middle = (upper-lower)/2 + lower;
			Skill skill = skillList.get(middle);
			int id = skill.getId();
            if      (idLookup < id) upper = middle - 1;
            else if (idLookup > id) lower = middle + 1;
            else return Optional.of(skillList.get(middle));
		}
	}
	
	/**
	 * Gets a skill from its name.
	 * This should only be done by players. Since they don't know the id
	 * @param {String} The name of the skill you wanted to get.
	 * @return{Skill} The skill which starts with this name
	 */
	public static Skill getSkillByName(String skillName)
	{
		for(Skill skill: Skill.skillList)
		{
			if(skill.getName().startsWith(skillName))
				return skill;
		}
		return null;
	}
	
	/**
	 * Gets a list of ALL skills
	 * @return {List<Skill>} all registered skills
	 */
	public static List<Skill> getAllSkills()
	{
		return new ArrayList<Skill>(skillList);
	}
	
	// -------------------------------------------- //
	// REGISTER
	// -------------------------------------------- //
	
	/**
	 * Registers this Skill into our system
	 * You should register skills before abilities
	 * NOTE: create only one instance of your skill, 
	 * from there we will just pass references around
	 */
	public void register()
	{
		Skill before = getSkillById(this.getId());
		if(before != null)
		{
			int id = this.getId();
			try
			{
				throw new IdAlreadyInUseException("The id: "+ id + " is already registered by " + before.getName()
						+ " but "+this.getName() + " is trying to use it");
			}
			catch (IdAlreadyInUseException e)
			{
				e.printStackTrace();
			}
		}
		skillList.add(this);
		skillList.sort(SkillComparator.get());
		SkillRegisteredEvent event = new SkillRegisteredEvent(this);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}
	
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //
	
	/**
	 * Sets the name of the skill. This is seen by players.
	 * This MUST be unique but can always be changed.
	 * @return {String} The skills name
	 */
	protected void setName(String str)
	{
		this.name = str;
	}
	
	/**
	 * Sets the name of the skill. This is seen by players.
	 * This MUST be unique but can always be changed.
	 * @return {String} The skills name
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Gives a short description of the skill.
	 * Should not be more than one or two minecraft chat lines long
	 * @return {String} a short description of the skill
	 */
	protected void setDescription(String str)
	{
		this.desc = str;
	}
	
	/**
	 * Gives a short description of the skill.
	 * Should not be more than one or two minecraft chat lines long
	 * @return {String} a short description of the skill
	 */
	public String getDescription()
	{
		return this.desc;
	}

	/**
	 * Adds a description of how to earn exp in this skill
	 * @param {String} The description to add
	 */
	protected void addEarnExpDesc (String desc) {	this.earnExpDesc.add(desc);	}
	
	/**
	 * Gets a list of the descriptions to earn exp
	 * @return {String} the description
	 */
	public List<String> getEarnExpDescriptions()
	{
		//Yeah it will be immutable
		List<String> descs = new ArrayList<String>();
		for(String desc : earnExpDesc)
			descs.add(desc);
		return descs;
	}
	
	/**
	 * Gets the name & description, as it would be displayed
	 * to the passed player
	 * @param {MPlayer} player to see description
	 * @return {String} how the player should see the description
	 */
	public String getDisplayedDescription(MPlayer watcherObject)
	{
		return Txt.parse("%s: <i>%s", this.getDisplayName(watcherObject), this.getDescription());
	}
	
	/**
	 * Returns a colorcode name
	 * based on the players ability to learn stated skill or not.
	 * @param {MPlayer} The MPlayer we want to check for.
	 * @return {String} The colorcode for the txt.parse method.
	 */
	public String getDisplayName ( MPlayer watcherObject)
	{
		if (watcherObject.isSpecialisedIn(this) == SpecialisationStatus.HAD)
			return Txt.parse(MConf.get().colorSkillIsPlayerSpecialised + this.getName());
		else if (this.canPlayerLearnSkill(watcherObject))
			return Txt.parse(MConf.get().colorSkillCanPlayerUse + this.getName());
		else
			return  Txt.parse(MConf.get().colorSkillCanPlayerNotUse + this.getName());
	}
	
	// -------------------------------------------- //
	// LEVELS
	// -------------------------------------------- //
	
	/**
	 * Each skill can have a different way of calculating levels.
	 * We don't know it, but we store the exp.
	 * The level calculation algorithm can also be change later on.
	 * @param {long} the amount of exp to convert to level
	 * @return {int} The level equivalent to the amount of exp passed.
	 */
	public final LvlStatus getLvlStatusFromExp(long exp)
	{
		return this.expToLvlStatus.apply(exp);
	}
	
	/**
	 * Each skill can have a different way of calculating levels.
	 * We don't know it, but we store the exp.
	 * This will change the algorithm
	 * @param {Function<Long, LvlStatus} The new algorithm to calculate levels for this skill
	 */
	public final void setLvlStatusAlgorithm(Function<Long, LvlStatus> algorithm)
	{
		this.expToLvlStatus = algorithm;
	}
	
	/**
	 * Each skill can have a different way of calculating levels.
	 * We don't know it, but we store the exp.
	 * This will get the level calculation algorithm for this skill
	 * @param {Function<Long, LvlStatus} The new algorithm to calculate levels for this skill
	 */
	public final Function<Long, LvlStatus> getLvlStatusAlgorithm()
	{
		return this.expToLvlStatus;
	}

	// -------------------------------------------- //
	// RESTRICTION
	// -------------------------------------------- //
	
	/**
	 * Tells whether or not experience can be earned for this skill in said area
	 * @param {Location} the are you want to check for
	 * @return {boolean} true if experience in this skill can be earned in the area
	 */
	public boolean canSkillBeEarnedInArea(Location loc)
	{
		if(FactionIntegration.establishIntegration())
		{
			Faction f = BoardColl.get().getFactionAt(PS.valueOf(loc));
			if(f != null)
			{
				if(f.getFlag(Const.FACTION_FLAG_SKILLS_OVERRIDE_WORLD))
				{
					return f.getFlag(Const.FACTION_FLAG_SKILLS_EARN);
				}
			}
			
		}
		return MConf.get().worldSkillsEarn.get(this.getId()).contains(loc.getWorld());
	}
	
	/**
	 * Tells whether or not the player can learn said skill.
	 * The skill can have different reasons the player might not.
	 * Bukkit permission is also checked here.
	 * @param {MPlayer} the player you want to check
	 * @return {boolean} true if the player can learn said skill
	 */
	public final boolean canPlayerLearnSkill(MPlayer p)
	{
		if ( ! PermUtil.has(p.getSender(), Perm.SKILL_LEARN.node + this.getId())) return false;
		return this.canPlayerLearnSkillInner(p);
	}
	
	// -------------------------------------------- //
	// ABILITIES
	// -------------------------------------------- //
	
	/**
	 * Gets the list of active abilities
	 * @return {List<Ability>} all active abilities related to this skill
	 */
	public List<Ability> getActiveAbilities()
	{
		return this.activeAbilities;
	}
	
	/**
	 * Gets the list of passive abilities
	 * @return {List<Ability>} all passive abilities related to this skill
	 */
	public List<Ability> getPassiveAbilities()
	{
		return this.passiveAbilities;
	}
	
	/**
	 * Gets the list of all abilities related to skill
	 * @return {List<Ability>} all abilities related to this skill
	 */
	public List<Ability> getAllAbilities()
	{
		List<Ability> ret = new ArrayList<Ability>();
		ret.addAll(this.getPassiveAbilities());
		ret.addAll(this.getActiveAbilities());
		return ret;
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	/**
	 * Gets the id of the skill. This id is only used by plugins
	 * & is never seen by the player/user.
	 * MUST be unique & should never be changed
	 * This should be lowercase.
	 * @return {int} the skills unique id.
	 */
	public abstract int getId();
	
	/**
	 * This is an inner version of canPlayerLearnSkill
	 * used if the skill developer wants to add extra checks,
	 * beside the ones we made.
	 * @param {MPlayer} the player to check for
	 * @return {boolean} true if player can learn skill (without default checks)
	 */
	public abstract boolean canPlayerLearnSkillInner(MPlayer p);
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return getName();
	}
	
	// -------------------------------------------- //
	// EQUALS
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if ( ! (obj instanceof Skill))
			return false;
		Skill that = (Skill) obj;
		if (that.getId() == this.getId() && that.getName().equals(this.getName()))
			return true;
		return false;
	}
	
}
