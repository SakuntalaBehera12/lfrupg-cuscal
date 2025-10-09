//
// Decompiled by Procyon v0.6-prerelease

//

package com.cuscal.common.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.Properties;

import org.apache.regexp.RE;

public class TextTemplate {

	public static String substituteProperties(
		final Properties variables, String text) {

		if (text == null) {
			return null;
		}

		if ((variables == null) || (variables.size() == 0)) {
			return text;
		}

		for (final Object key : variables.keySet()) {
			String value = null;
			final Object val = variables.get(key);

			if (val == null) {
				value = "";
			}
			else {
				value = val.toString();
			}

			final RE regex = new RE("\\$\\{" + key + "\\}", 2);
			text = regex.subst(text, value);
		}

		return text;
	}

	public static String substitutePropertiesFile(
			final Properties variables, final InputStream textStream)
		throws IOException {

		final StringWriter stringWriter = new StringWriter();
		final PrintWriter writer = new PrintWriter(stringWriter);
		final BufferedReader reader = new BufferedReader(
			new InputStreamReader(textStream));

		for (String line = reader.readLine(); line != null;
			 line = reader.readLine()) {

			writer.println(line);
		}

		final String results = stringWriter.getBuffer(
		).toString();
		writer.close();
		reader.close();

		return substituteProperties(variables, results);
	}

}