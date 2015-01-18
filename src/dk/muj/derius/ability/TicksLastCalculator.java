package dk.muj.derius.ability;

import java.util.function.Function;

@FunctionalInterface
public interface TicksLastCalculator extends Function<Integer, Integer>
{
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //

	public Integer calculateCooldown(int level);
	
	// -------------------------------------------- //
	// DEFAULT
	// -------------------------------------------- //
	
	default Integer apply(Integer level)
	{
		return this.calculateCooldown(level);
	}
}
