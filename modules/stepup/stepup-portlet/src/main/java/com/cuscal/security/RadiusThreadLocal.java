package com.cuscal.security;

import net.sourceforge.jradiusclient.RadiusPacket;

public class RadiusThreadLocal {

	public static final ThreadLocal<RadiusPacket> radiusThreadLocal =
		new ThreadLocal<>();

	public static RadiusPacket get() {
		return (RadiusPacket)radiusThreadLocal.get();
	}

	public static void set(RadiusPacket radiusPacket) {
		radiusThreadLocal.set(radiusPacket);
	}

	public static void unset() {
		radiusThreadLocal.remove();
	}

}