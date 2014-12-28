package dk.muj.derius.skill;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.entity.MConf;


public class LvlStatusDefault implements LvlStatus
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	/**
	 * This is the current lvl
	 */
	private final int lvl = 0;
	public int getLvl () {	return lvl;	}
	public LvlStatus setLvl (int level) { return new LvlStatusDefault(level ,exp, this.expToNextLvl); }
	
	/**
	 * This is the exp left after calculating the lvl.
	 * This is /not/ the total exp
	 */
	private final int exp = 0;
	public int getExp () {	return exp;	}
	public LvlStatus setExp (int exp) { return new LvlStatusDefault(this.lvl,exp, this.expToNextLvl); }
	
	/**
	 * This is how much exp is required to reach next lvl
	 * It ignores the current exp
	 */
	private final int expToNextLvl = 0;
	public int getExpToNextLvl () { return expToNextLvl; }
	public LvlStatus setExpToNextLvl (int expToNextLvl) { return new LvlStatusDefault(this.lvl,this.exp, expToNextLvl); }
	
	// -------------------------------------------- //
	// CONSTRUCTOR
	// -------------------------------------------- //
	
	public LvlStatusDefault(int level, int currentExperience, int expToNextLvl)
	{
		this.setLvl(level);
		this.setExp(currentExperience);
		this.setExpToNextLvl(expToNextLvl);
	}
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		//  Example Output (before applying the colors): "<navy>LVL: <lime>1 <navy>XP: <lime>120<yellow>/<lime>5000"
		return Txt.parse(MConf.get().msgLvlStatusFormat, this.getLvl(), this.getExp(), this.getExpToNextLvl());
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
