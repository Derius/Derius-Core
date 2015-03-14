package dk.muj.derius.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.MStore;

import dk.muj.derius.DeriusConst;
import dk.muj.derius.DeriusCore;
import dk.muj.derius.adapter.GsonAbility;
import dk.muj.derius.api.ability.Ability;

public final class AbilityColl extends Coll<Ability>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static AbilityColl i = new AbilityColl();
	public static AbilityColl get() { return i; }
	private AbilityColl()
	{
		super(DeriusConst.COLLECTION_ABILITIES, Ability.class, MStore.getDb(), DeriusCore.get());
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
			if (ability instanceof GsonAbility) continue;
			abilities.add(ability);
		}
		
		return abilities;
	}
	
	// -------------------------------------------- //
	// OVERRIDE: COLL
	// -------------------------------------------- //
	
	@Override
	public Ability createNewInstance()
	{
		return new GsonAbility();
	}
	
	@Override
	public void copy(Object ofrom, Object oto)
	{
		if (ofrom == null) throw new IllegalArgumentException("ofrom");
		if (oto == null) throw new IllegalArgumentException("oto");
		
		if ( ! (ofrom instanceof Ability)) throw new IllegalArgumentException("ofrom must be an ability");
		if ( ! (oto instanceof Ability)) throw new IllegalArgumentException("ofrom must be an ability");
		
		Ability afrom = (Ability) ofrom;
		Ability ato = (Ability) oto;
		
		ato.load(afrom);
	}
	
	@Override
	public String fixId(Object oid)
	{
		if (oid instanceof String) return (String) oid;
		if (oid instanceof Ability) return ((Ability) oid).getId();
		return null;
	}
	
}
