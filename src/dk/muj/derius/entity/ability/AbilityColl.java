package dk.muj.derius.entity.ability;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.MStore;

import dk.muj.derius.DeriusConst;
import dk.muj.derius.DeriusCore;
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
		super(DeriusConst.COLLECTION_ABILITIES, DeriusAbility.class, MStore.getDb(), DeriusCore.get());
		this.setLowercasing(true);
		this.setCreative(false);
	}
	
	// -------------------------------------------- //
	// STACK TRACEABILITY
	// -------------------------------------------- //
	
	@Override
	public void onTick()
	{
		super.onTick();
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
			if ( ! ability.isEnabled()) continue;
			abilities.add(ability);
		}
		
		return abilities;
	}
	
	// -------------------------------------------- //
	// OVERRIDE: COLL
	// -------------------------------------------- //
	
	@Override
	public DeriusAbility createNewInstance()
	{
		return new GsonAbility();
	}
	
}
