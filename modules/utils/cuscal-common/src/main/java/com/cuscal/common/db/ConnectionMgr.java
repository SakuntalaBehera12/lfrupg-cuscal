//
// Decompiled by Procyon v0.6-prerelease

//

package com.cuscal.common.db;

import com.cuscal.common.constants.CuscalCommonPropsValues;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;

public class ConnectionMgr {

	public static Connection getConnection() {
		InitialContext init = null;

		try {
			init = new InitialContext();
		}
		catch (NamingException e) {
			e.printStackTrace();
		}

		try {
			final DataSource dts = (DataSource)init.lookup(
				CuscalCommonPropsValues.BILLER_FILE_ENQUIRY_DB_NAME);
			System.out.println("Conection Object " + dts.getConnection());

			return dts.getConnection();
		}
		catch (Exception e2) {
			e2.printStackTrace();

			return null;
		}
	}

}