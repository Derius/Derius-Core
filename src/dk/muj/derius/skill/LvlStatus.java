package dk.muj.derius.skill;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.entity.MConf;

/**
 * This class is used for passing data about a players progress
 * in a certain skill around between methods and classes.
 * It has 3 variables.
 * It contains the players level
 * It contains the players leftover exp (after calculating level)
 * It contains the amount of exp required to reach next level (ignoring leftover exp).
 * The getters & setters should be obvious.
 */
public class LvlStatus
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	/**
	 * This is the current lvl
	 */
	private int lvl = 0;
	public int getLvl () {	return lvl;	}
	public void setLvl (int level) {	this.lvl = level;	}
	
	/**
	 * This is the exp left after calculating the lvl.
	 * This is /not/ the total exp
	 */
	private int exp = 0;
	public int getExp () {	return exp;	}
	public void setExp (int exp) {	this.exp = exp;	}
	
	/**
	 * This is how much exp is required to reach next lvl
	 * It ignores the current exp
	 */
	private int expToNextLvl = 0;
	public int getExpToNextLvl () {	return expToNextLvl;	}
	public void setExpToNextLvl (int expToNextLvl) {	this.expToNextLvl = expToNextLvl;	}

	// -------------------------------------------- //
	// CONSTRUCTOR
	// -------------------------------------------- //
	
	public LvlStatus(int level, int currentExperience, int expToNextLvl)
	{
		this.setLvl(level);
		this.setExp(currentExperience);
		this.setExpToNextLvl(expToNextLvl);
	}
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	/**
	 * Should just be displayed to players
	 */
	public String toString()
	{
		//  Example Output (before applying the colors): "<navy>LVL: <lime>1 <navy>XP: <lime>120<yellow>/<lime>5000"
		return Txt.parse(MConf.get().msgLvlStatusFormat, lvl, exp, expToNextLvl);
	}
	
	// -------------------------------------------- //
	// EQUALS
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		if(!(obj instanceof LvlStatus))
			return false;
		LvlStatus that = (LvlStatus) obj;
		if(that.getLvl() == this.getLvl() && that.getExp() == this.getExp() && that.getExpToNextLvl() == this.getExpToNextLvl())
			return true;
		return false;
	}
}
