package dk.muj.derius.lambda;

import java.util.function.Function;

@FunctionalInterface
public interface TicksLastCalculator extends Function<Integer, Integer>
{
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //

	public Integer calcDuration(int level);
	
	// -------------------------------------------- //
	// DEFAULT
	// -------------------------------------------- //
	
	default Integer apply(Integer level)
	{
		return this.calcDuration(level);
	}
	
}
