//
// Decompiled by Procyon v0.6-prerelease

//

package com.cuscal.common.command;

public abstract class Command {

	public abstract void execute() throws CommandException;

	public String name() {
		return this.getClass(
		).getName();
	}

}