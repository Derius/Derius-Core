package dk.muj.derius.req;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import com.massivecraft.massivecore.util.PermUtil;

import dk.muj.derius.api.Req;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.req.util.ReqToDefault;

public class ReqHasPerm implements Req, ReqToDefault
{	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static ReqHasPerm get(String perm) { return new ReqHasPerm(perm); }
	public ReqHasPerm(String perm) { this.perm = perm; }
	
	public static ReqHasPerm get(Permission perm) { return new ReqHasPerm(perm); }
	public ReqHasPerm(Permission perm) { this.perm = perm.getName(); }

	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final String perm;
	public String getPerm() { return this.perm; }
	
	// -------------------------------------------- //
	// OVERRIDE: VERBOSE LEVEL
	// -------------------------------------------- //
	
	@Override
	public VerboseLevel getVerboseLevel()
	{
		return VerboseLevel.LOW;
	}
	
	// -------------------------------------------- //
	// OVERRIDE: DEFAULT
	// -------------------------------------------- //

	@Override
	public boolean apply(CommandSender sender)
	{
		return PermUtil.has(sender, perm);
	}
	
	@Override
	public String createErrorMessage(CommandSender sender)
	{
		return PermUtil.getDeniedMessage(perm);
	}
	
}
