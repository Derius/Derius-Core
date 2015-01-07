package dk.muj.derius.util;

import com.massivecraft.massivecore.MassivePlugin;

import dk.muj.derius.Derius;
import dk.muj.derius.entity.MConf;

public class TimingUtil
{		
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private static long startTime;
	private static String declaringclass = "";
	private static String methodName = "";
	
	
	// -------------------------------------------- //
	// TIMER CONTROLE
	// -------------------------------------------- //	
	
	public static void startTiming()
	{
		TimingUtil.startTime = System.nanoTime();
	}
	
	public static void endTiming()
	{
		long startTime = TimingUtil.startTime;
		long endTime= System.nanoTime();
		
		double difference = (endTime - startTime) / 1000_000.0;
		
		if (difference > MConf.get().timingMax)
		{
			MassivePlugin p = Derius.get();
			p.log("");
		}
	}
	
	// -------------------------------------------- //
	// PRIVAT
	// -------------------------------------------- //


}
