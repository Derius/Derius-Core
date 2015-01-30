package dk.muj.derius.skill;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.entity.MLang;


public class LvlStatusDefault implements LvlStatus
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	/**
	 * This is the current lvl
	 */
	private final int level;
	public int getLvl () {	return level;	}
	public LvlStatus setLvl (int level) { return new LvlStatusDefault(level, this.exp, this.expToNext); }
	
	/**
	 * This is the exp left after calculating the lvl.
	 * This is /not/ the total exp
	 */
	private final Integer exp;
	public Integer getExp () {	return exp;	}
	public LvlStatus setExp (Integer exp) { return new LvlStatusDefault(this.level, exp, this.expToNext); }
	
	/**
	 * This is how much exp is required to reach next lvl
	 * It ignores the current exp
	 */
	private final Integer expToNext;
	public Integer getExpToNextLvl () { return expToNext; }
	public LvlStatus setExpToNextLvl (Integer expToNextLvl) { return new LvlStatusDefault(this.level,this.exp, expToNextLvl); }
	
	// -------------------------------------------- //
	// CONSTRUCTOR
	// -------------------------------------------- //
	
	public LvlStatusDefault(int level, Integer currentExperience, Integer expToNextLvl)
	{
		this.level = level;
		this.exp = currentExperience;
		this.expToNext = expToNextLvl;
	}
	
	// -------------------------------------------- //
	// TO STRING
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		//  Example Output (before applying the colors): "<navy>LVL: <lime>1 <navy>XP: <lime>120<yellow>/<lime>5000"
		if ( exp == null || expToNext == null) return Txt.parse(MLang.get().levelStatusFormatMini, level);
		return Txt.parse(MLang.get().levelStatusFormat, this.getLvl(), this.getExp(), this.getExpToNextLvl());
	}
	
	// -------------------------------------------- //
	// EQUALS
	// -------------------------------------------- //
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) return false;
		if ( ! (obj instanceof LvlStatus)) return false;
		
		LvlStatus that = (LvlStatus) obj;
		if(that.getLvl() == this.getLvl() && that.getExp() == this.getExp() && that.getExpToNextLvl() == this.getExpToNextLvl()) return true;
		return false;
	}

}
