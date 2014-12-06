package dk.muj.derius.skill;

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
	 * This si /not/ the total exp
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
	public String toString()
	{
		return "<silver>LVL<art>: "+ lvl+ "  <silver>XP: <lime>"+exp+"<yellow>/<lime>"+expToNextLvl;
	}

}
