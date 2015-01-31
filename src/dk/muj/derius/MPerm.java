package dk.muj.derius;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public interface MPerm
{
	public int getKit();
	public String getNode();
	public String getStartNode();
	public String getDescription();
	
	public default void update()
	{
		Permission perm = Bukkit.getPluginManager().getPermission(this.getNode());
		perm.setDescription(this.getDescription());
	}
	
	public default void create()
	{
		// Create the perm
		Permission perm = new Permission(this.getNode(), this.getDescription(), PermissionDefault.FALSE);
		
		// Add parent kit
		perm.addParent(this.getStartNode() + "kit.rank" + this.getKit(), true);
		// Add parent op
		perm.addParent(this.getStartNode() + "*", true);
		
		Bukkit.getPluginManager().addPermission(perm);
	}
	
	public static void createParents(String startNode, final int highestKit)
	{
		if (highestKit < 0) throw new IllegalArgumentException("highest kit must be positive");
		// Default kits
		Permission rankFull = new Permission(startNode + "*", "full kit for derius", PermissionDefault.FALSE);
		Permission rankOp= new Permission(startNode + "kit.rank.op", "rank 3 derius kit", PermissionDefault.OP);
		Permission rankDef = new Permission(startNode + "kit.default", "default derius kit", PermissionDefault.TRUE);

		
		Permission[] ranks = new Permission[highestKit+1];		
		// Rank kits
		for (int i = highestKit ; i >= 0; i--)
		{
			Permission rank = new Permission(startNode + "kit.rank" + i, "rank " + i + " derius kit", PermissionDefault.FALSE);
			ranks[i] = rank;
		}
		
		// Add to bukkit
		Bukkit.getPluginManager().addPermission(rankFull);
		Bukkit.getPluginManager().addPermission(rankOp);
		Bukkit.getPluginManager().addPermission(rankDef);
		for (Permission rank : ranks)
		{
			Bukkit.getPluginManager().addPermission(rank);
		}
		
		// Add the sub
		ranks[0].addParent(rankDef, true);
		
		for(int i = 0; i < ranks.length-1; i++)
		{
			ranks[i].addParent(ranks[i+1], true);
		}
		
		
		// Ops have full access
		rankFull.addParent(rankOp, true);
	}
}
