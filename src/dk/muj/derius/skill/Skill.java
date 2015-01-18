package dk.muj.derius.skill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.ability.Ability;
import dk.muj.derius.entity.MConf;
import dk.muj.derius.entity.MLang;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.events.SkillRegisteredEvent;
import dk.muj.derius.exceptions.IdAlreadyInUseException;
import dk.muj.derius.req.Req;

public abstract class Skill
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private String desc = "";
	private String name = "";
	
	// The list of existing skills in a class-variable
	private static List<Skill> skillList = new CopyOnWriteArrayList<Skill>();
	
	// The lists of of active and passive abilities
	private List<Ability> passiveAbilities = new CopyOnWriteArrayList<Ability>();
	private List<Ability> activeAbilities = new CopyOnWriteArrayList<Ability>();
	
	private List<String> earnExpDesc = new CopyOnWriteArrayList<String>();
	
	protected List<Req> seeRequirements = new CopyOnWriteArrayList<Req>();
	protected List<Req> learnRequirements = new CopyOnWriteArrayList<Req>();
	protected List<Req> specialiseRequirements = new CopyOnWriteArrayList<Req>();
	
	//Lambda sw@g
	LvlStatusCalculator expToLvlStatus = (long exp) -> 	
	{	//This is the default algorithm
		int level = 0, nextLvlExp;
		for(nextLvlExp = 1024; nextLvlExp < exp; nextLvlExp *= 1.01, level++)
		{
			exp -= nextLvlExp;
		}
		
		return new LvlStatusDefault(level, (int) exp, nextLvlExp);
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
		//TODO Doesn't work
		return Optional.empty();
		/*
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
		}*/
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
			if(skill.getName().startsWith(skillName)) return skill;
		}
		return null;
	}
	
	/**
	 * Gets a list of ALL skills
	 * @return {List<Skill>} all registered skills
	 */
	public static List<Skill> getAllSkills(){ return new ArrayList<Skill>(skillList); }
	
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
		CommandSender sender = Bukkit.getConsoleSender();
		sender.sendMessage("REGISTER 1");
		Object before = getSkillById(this.getId());
		sender.sendMessage("REGISTER 2");
		if(before != null)
		{
			sender.sendMessage("REGISTER 3");
			int id = this.getId();
			sender.sendMessage("REGISTER 4");
			try
			{
				sender.sendMessage("REGISTER 5");
				throw new IdAlreadyInUseException("The id: "+ id + " is already registered by " + before.toString()
						+ " but "+this.getName() + " is trying to use it");
				
			}
			catch (IdAlreadyInUseException e)
			{
				sender.sendMessage("REGISTER 6");
				e.printStackTrace();
				return;
			}
			finally
			{
				sender.sendMessage("REGISTER 7");
			}
		}
		
		sender.sendMessage("REGISTER 8");
		SkillRegisteredEvent event = new SkillRegisteredEvent(this);
		sender.sendMessage("REGISTER 9");
		event.run();
		sender.sendMessage("REGISTER 10");
		if (event.isCancelled()) return;
		sender.sendMessage("REGISTER 11");
		skillList.add(this);
		sender.sendMessage("REGISTER 12");
		skillList.sort(SkillComparator.get());
		sender.sendMessage("REGISTER 13");
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
	public String getDisplayName ( Object watcherObject)
	{
		MPlayer player = MPlayer.get(watcherObject);
		if (player.isSpecialisedIn(this))
		{
			return Txt.parse(MLang.get().skillColorPlayerIsSpecialised + this.getName());
		}
		else if (this.canPlayerLearnSkill(player))
		{
			return Txt.parse(MLang.get().skillColorPlayerCanUse + this.getName());
		}
		else
		{
			return  Txt.parse(MLang.get().skillColorPlayerCantUse + this.getName());
		}
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
	 * @param {LvlStatusCalculator} The new algorithm to calculate levels for this skill
	 */
	public final void setLvlStatusAlgorithm(LvlStatusCalculator algorithm)
	{
		this.expToLvlStatus = algorithm;
	}
	
	/**
	 * Each skill can have a different way of calculating levels.
	 * We don't know it, but we store the exp.
	 * This will get the level calculation algorithm for this skill
	 * @param {LvlStatusCalculator} The new algorithm to calculate levels for this skill
	 */
	public final LvlStatusCalculator getLvlStatusAlgorithm()
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
	public boolean canSkillBeEarnedInArea(PS ps)
	{
		if (ps.getWorld() == null) throw new IllegalStateException("PS must include world");
		return MConf.get().worldSkillsEarn.get(this.getId()).contains(ps.getWorld());
	}
	
	/**
	 * Tells whether or not the player can learn said skill.
	 * This is based on the skill requirements
	 * @param {MPlayer} the player you want to check
	 * @return {boolean} true if the player can learn said skill
	 */
	public final boolean canPlayerLearnSkill(MPlayer p)
	{
		for (Req req : this.getLearnRequirements())
		{
			if ( ! req.apply(p.getSender())) return false;
		}
		return true;
	}
	
	/**
	 * Tells whether or not the player can see said skill.
	 * This is based on the skill requirements
	 * @param {MPlayer} the player you want to check
	 * @return {boolean} true if the player can see said skill
	 */
	public final boolean canPlayerSeeSkill(MPlayer p)
	{
		for (Req req : this.getSeeRequirements())
		{
			if ( ! req.apply(p.getSender())) return false;
		}
		return true;
	}
	
	/**
	 * Tells whether or a player can specialise in this skill
	 * @param {MPlayer} player to check for
	 * @param {boolean} inform them if they can't
	 * @return {boolean} true if player can specialise in skill
	 */
	public boolean canPlayerSpecialiseSkill(MPlayer p, boolean informIfNot)
	{
		for (Req req : this.getSpecialiseRequirements())
		{
			if ( ! req.apply(p.getSender()))
			{
				if (informIfNot)
				{
					p.sendMessage(req.createErrorMessage(p.getSender(), this));
				}
				return false;
			}
		}
		return true;
	}
	
	// -------------------------------------------- //
	// MCONF
	// -------------------------------------------- //
	
	/**
	 * @return {boolean} true if a player is automatically specialised in this skill
	 */
	public boolean isSpAutoAssigned()
	{
		return MConf.get().specialisationAutomatic.contains(this.getId());
	}
	
	/**
	 * @return {boolean} true if this skill can not be specialised.
	 */
	public boolean isSpBlackListed()
	{
		return MConf.get().specialisationBlacklist.contains(this.getId());
	}
	
	// -------------------------------------------- //
	// REQ GETTERS & SETTERS
	// -------------------------------------------- //
	
	/**
	 * This will give the list of requirements
	 * that must be filled in order for a player to see the skill
	 * if they can't see the skill, they should not see it anywhere.
	 * @return {List<Req>} list of requirements to see the skill
	 */
	public List<Req> getSeeRequirements() { return this.seeRequirements; }
	
	/**
	 * This will set the list of requirements
	 * that must be filled in order for a player to see the skill
	 * if they can't see the skill, they should not see it anywhere.
	 * (old requirements will NOT be kept)
	 * @param {List<Req>} list of requirements to see the skill 
	 */
	public void setSeeRequirements(List<Req> requirements) { this.seeRequirements = requirements; }
	
	/**
	 * This will add  to the list of requirements
	 * that must be filled in order for a player to see the skill
	 * if they can't see the skill, they should not see it anywhere.
	 * (old requirements WILL be kept)
	 * @param {List<Req>} added requirements to see the skill
	 */
	public void addSeeRequirements(Req... requirements) { this.seeRequirements.addAll(Arrays.asList(requirements)); }
	
	/**
	 * This will give the list of requirements
	 * that must be filled in order for a player to learn the skill (earn exp)
	 * @return {List<Req>} list of requirements to learn the skill
	 */
	public List<Req> getLearnRequirements() { return this.learnRequirements; }
	
	/**
	 * This will set the list of requirements
	 * that must be filled in order for a player to learn the skill (earn exp)
	 * (old requirements will NOT be kept)
	 * @param {List<Req>} list of requirements to learn the skill 
	 */
	public void setLearnRequirements(List<Req> requirements) { this.learnRequirements = requirements; }
	
	/**
	 * This will add  to the list of requirements
	 * that must be filled in order for a player to learn the skill (earn exp)
	 * (old requirements WILL be kept)
	 * @param {List<Req>} added requirements to learn the skill
	 */
	public void addLearnRequirements(Req... requirements) { this.learnRequirements.addAll(Arrays.asList(requirements)); }
	
	/**
	 * This will give the list of requirements
	 * that must be filled in order for a player to specialise in the skill
	 * @return {List<Req>} list of requirements to specialise the skill
	 */
	public List<Req> getSpecialiseRequirements() { return this.specialiseRequirements; }
	
	/**
	 * This will set the list of requirements
	 * that must be filled in order for a player to specialise in the skill
	 * (old requirements will NOT be kept)
	 * @param {List<Req>} list of requirements to specialise the skill 
	 */
	public void setSpecialiseRequirements(List<Req> requirements) { this.specialiseRequirements = requirements; }
	
	/**
	 * This will add  to the list of requirements
	 * that must be filled in order for a player to specialise in the skill
	 * (old requirements WILL be kept)
	 * @param {List<Req>} added requirements to specialise the skill
	 */
	public void addSpecialiseRequirements(Req... requirements) { this.specialiseRequirements.addAll(Arrays.asList(requirements)); }
	
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
	
	// -------------------------------------------- //
	// EQUALS & HASH CODE
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object obj)
	{		
		return obj == this;
	}
	
	@Override
	public int hashCode()
	{
		int result = 1;
		
		result += this.getId();
		
		return result;
	}
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		return getName();
	}
	
}
