package dk.muj.derius.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.MStore;

import dk.muj.derius.Const;
import dk.muj.derius.Derius;
import dk.muj.derius.api.Ability;

public class AbilityColl extends Coll<DeriusAbility>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static AbilityColl i = new AbilityColl();
	public static AbilityColl get() { return i; }
	private AbilityColl()
	{
		super(Const.COLLECTION_ABILITIES, DeriusAbility.class, MStore.getDb(), Derius.get());
		this.setLowercasing(true);
		this.setCreative(false);
	}
	
	// -------------------------------------------- //
	// CONVENIENCE
	// -------------------------------------------- //
	
	public static Collection<Ability> getAllAbilities()
	{
		Set<Ability> abilities = new HashSet<>();
		
		for (Ability ability : get().getAll())
		{
			if (ability == null) continue;
			abilities.add(ability);
		}
		
		return abilities;
	}
	
}
