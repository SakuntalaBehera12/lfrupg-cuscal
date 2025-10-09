package au.com.cuscal.common.framework.memory;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;

import org.apache.log4j.Logger;

public class PermGenUtil {

	public static void permGenDump(String msg) {
		if (permGenMx == null) {
			for (MemoryPoolMXBean mx :
					ManagementFactory.getMemoryPoolMXBeans()) {

				String permGenName = "Perm Gen".toUpperCase();
				String mxName = mx.getName(
				).toUpperCase();

				memLogger_.info("Found MXBean " + mx.getName());

				if (mxName.endsWith(permGenName)) {
					permGenMx = mx;
				}
			}
		}

		if (memLogger_.isDebugEnabled() && (permGenMx != null)) {
			MemoryUsage usage = permGenMx.getUsage();
			MemoryUsage peakUsage = permGenMx.getPeakUsage();

			long usedMem = usage.getUsed();
			long delta = usedMem - lastUsedMem;
			lastUsedMem = usedMem;

			String s =
				delimtier + padRight(msg, padChar, padLength) + delimtier +
					permGenMx.getName() + delimtier + usedMem + delimtier +
						padLeft(
							String.valueOf(delta), padChar, numericPadLength) +
								delimtier + usage.getMax() + delimtier +
									"peak" + delimtier + peakUsage.getUsed() +
										delimtier + peakUsage.getMax() +
											delimtier;

			memLogger_.debug(s);
		}
	}

	private static String padLeft(String s, char padChar, int length) {
		while (s.length() <= length) {
			s = padChar + s;
		}

		return s;
	}

	private static String padRight(String s, char padChar, int length) {
		while (s.length() <= length) {
			s = s + padChar;
		}

		return s;
	}

	private static String delimtier = "|";
	private static long lastUsedMem = 0L;
	private static Logger memLogger_ = Logger.getLogger("memory");
	private static int numericPadLength = 15;
	private static char padChar = ' ';
	private static int padLength = 100;
	private static MemoryPoolMXBean permGenMx = null;

}