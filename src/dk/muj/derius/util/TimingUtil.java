package dk.muj.derius.util;

import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.Derius;
import dk.muj.derius.entity.MConf;

public class TimingUtil
{	
	// -------------------------------------------- //
	// Construct
	// -------------------------------------------- //
	
	public TimingUtil(String desc)
	{
		this.methodDesc = desc;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	/*
	 * We store this info for the logging, that might happen. 
	 * Without this info, it would be impossible to keep track
	 * of multiple timings in the project.
	 */
	private long startTime;
	private long endTime;
	private String methodDesc = "";
	
	
	// -------------------------------------------- //
	// TIMER CONTROLE
	// -------------------------------------------- //	
	
	/**
	 * Starts the timing process.
	 */
	public  void startTiming()
	{
		startTime = System.nanoTime();
	}
	
	/**
	 * Ends the timing process and creates a log if the time
	 * is over the timingMax in MConf.
	 * @param {String} The Name of the class this timer is placed.
	 * @param {String} The Name of the method this timer is placed.
	 */
	public  void endTiming()
	{
		endTime = System.nanoTime();
		double difference = (endTime - startTime) / 1000_000.0;
		
		if (difference > MConf.get().timingMax)
		{
			MassivePlugin p = Derius.get();
			p.log(Txt.parse("<i>The execution of <h>%s <i>took <h>%d <i>milliseconds. It might be of value to take a look at it.", methodDesc, difference));
		}
	}
	
}
