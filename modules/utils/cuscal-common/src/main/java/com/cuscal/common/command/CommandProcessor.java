//
// Decompiled by Procyon v0.6-prerelease

//

package com.cuscal.common.command;

import com.cuscal.common.exceptions.StackTraceUtil;

import org.apache.log4j.Logger;

public class CommandProcessor {

	public CommandProcessor(final Command command) {
		this.command = command;
	}

	public void execute() throws CommandException {
		try {
			CommandProcessor.logger.debug(
				(Object)("Enter " + this.command.name() + " command"));
			this.command.execute();
			CommandProcessor.logger.debug(
				(Object)("Exit " + this.command.name() + " command"));
		}
		catch (CommandException ce) {
			CommandProcessor.logger.error(
				(Object)
					("Error while try to execute \n" + this.command.name() +
						'\n' + StackTraceUtil.getStackTrace(ce)));
			ce.fillInStackTrace();

			throw ce;
		}
	}

	private static final Logger logger;

	static {
		logger = Logger.getLogger((Class)CommandProcessor.class);
	}

	private Command command;

}