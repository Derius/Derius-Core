package dk.muj.derius.entity;

import java.util.Collection;

import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.MStore;

import dk.muj.derius.Const;
import dk.muj.derius.Derius;

public class AbilityColl extends Coll<Ability>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static AbilityColl i = new AbilityColl();
	public static AbilityColl get() { return i; }
	private AbilityColl()
	{
		super(Const.COLLECTION_ABILITIES, Ability.class, MStore.getDb(), Derius.get());
		this.setLowercasing(true);
		this.setCreative(false);
	}
	
	// -------------------------------------------- //
	// CONVENIENCE
	// -------------------------------------------- //
	
	public static Collection<Ability> getAllAbilities()
	{
		return get().getAll();
	}
	
}
