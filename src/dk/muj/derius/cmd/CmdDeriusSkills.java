package dk.muj.derius.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;

import dk.muj.derius.Perm;

public class CmdDeriusSkills extends DeriusCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdDeriusSkills()
	{
		this.addRequirements(ReqHasPerm.get(Perm.SKILL.getNode()), ReqIsPlayer.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		/* List<ItemStack> skills = new ArrayList<ItemStack>();
		
		for (Skill skill : DeriusAPI.getAllSkills())
		{
			LvlStatus status = dsender.getLvlStatus(skill);
			
			// Message construction
			List<String> msgs = new ArrayList<String>();
			
			msgs.add("<lime>" + skill.getDesc()); // Description
			
			// Swapping between default and user inserted value
			msgs.add(status.toString());

			// All Abilities
			msgs.add(MLang.get().skillInfoAbilities);
			for (Ability ability : skill.getAbilities())
			{
				if ( ! AbilityUtil.canPlayerSeeAbility(dsender, ability, VerboseLevel.ALWAYS)) continue;
				msgs.add(ability.getDisplayedDescription(dsender));
			}
			msgs.add(MLang.get().skillInfoLevelStats);
			for (Ability ability : skill.getAbilities())
			{
				if ( ! AbilityUtil.canPlayerSeeAbility(dsender, ability, VerboseLevel.ALWAYS)) continue;
				msgs.add(String.format("%s: <i>%s", ability.getDisplayName(dsender), ability.getLvlDescriptionMsg(status.getLvl())));
			}
			
			msgs = Txt.parse(msgs);
			
			ItemStack skillItem = new ItemStack(skill.getIcon(), 1);
			ItemMeta meta = skillItem.getItemMeta();
			msg(skill.getIcon().toString());
			if (meta == null) throw new MassiveException().addMessage("item meta was null");
			meta.setDisplayName(Txt.titleize(skill.getDisplayName(dsender)));
			meta.setLore(msgs);
			
			skillItem.setItemMeta(meta);
			skills.add(skillItem);
		}
		
		int size = skills.size() - (skills.size() % 9);
		if (size != skills.size()) size += 9;
		Inventory inv = Bukkit.createInventory(null, size, Txt.parse("<lime>Derius Skills"));
		for (ItemStack stack: skills)
		{
			inv.addItem(stack);
		}
		
		me.openInventory(inv); */
	}
}
