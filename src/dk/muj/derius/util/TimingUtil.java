package dk.muj.derius.util;

import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Derius;
import dk.muj.derius.entity.MConf;

public class TimingUtil
{	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	/*
	 * We store this info for the logging, that might happen. 
	 * Without this info, it would be impossible to keep track
	 * of multiple timings in the project.
	 */
	private static long startTime;
	private static String declaringClass = "";
	private static String methodName = "";
	
	
	// -------------------------------------------- //
	// TIMER CONTROLE
	// -------------------------------------------- //	
	
	/**
	 * Starts the timing process.
	 * @param {String} The Name of the class this timer is placed.
	 * @param {String} The Name of the method this timer is placed.
	 */
	public static void startTiming(String className, String methodName)
	{
		TimingUtil.declaringClass = className;
		TimingUtil.methodName = methodName;
		
		TimingUtil.startTime = System.nanoTime();
	}
	
	/**
	 * Ends the timing process and creates a log if the time
	 * is over the timingMax in MConf.
	 * @param {String} The Name of the class this timer is placed.
	 * @param {String} The Name of the method this timer is placed.
	 */
	public static void endTiming(String className, String methodName)
	{
		if (TimingUtil.hasError(className, methodName)) { return; }
		
		long startTime = TimingUtil.startTime;
		long endTime = System.nanoTime();
		
		double difference = (endTime - startTime) / 1000_000.0;
		
		if (difference > MConf.get().timingMax)
		{
			MassivePlugin p = Derius.get();
			p.log(Txt.parse("<i>The Timing in class <lime>%s <i>and method <lime>%s <i>has shown a run time of <lime>&s <i>seconds. It might be of value to take a look at it.", TimingUtil.declaringClass, TimingUtil.methodName, difference));
		}
	}
	
	// -------------------------------------------- //
	// PRIVAT
	// -------------------------------------------- //

	private static boolean hasError (String className, String methodName)
	{
		if (TimingUtil.declaringClass != className || TimingUtil.methodName != methodName) 
		{ 
			return true; 
		}
		else
		{
			return false;
		}	
	}
}
