package dk.muj.derius.skill;

import java.util.function.Function;

@FunctionalInterface
public interface LvlStatusCalculator extends Function<Long, LvlStatus>
{
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //

	public LvlStatus calculateLvlStatus(long exp);
	
	// -------------------------------------------- //
	// DEFAULT
	// -------------------------------------------- //
	
	default LvlStatus apply(Long exp)
	{
		return this.calculateLvlStatus(exp);
	}
	
}
