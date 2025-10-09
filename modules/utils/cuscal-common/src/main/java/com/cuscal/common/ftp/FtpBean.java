package com.cuscal.common.ftp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;

import org.apache.commons.net.ftp.FTPClient;

public class FtpBean {

	public static void ftpSendFile(
			final String fileName, final String hostId, final String pathName,
			final String userId, final String userPassword,
			final InputStream inputFileStream)
		throws IOException {

		final FTPClient fTarget = new FTPClient();

		try {
			fTarget.connect(
				new InetSocketAddress(hostId, fTarget.getDefaultPort()).getAddress());
			fTarget.login(userId, userPassword);

			if (!pathName.equals("NULL")) {
				fTarget.changeWorkingDirectory(pathName);
			}

			fTarget.setFileType(FTPClient.BINARY_FILE_TYPE);

			final BufferedReader brIn = new BufferedReader(
				new InputStreamReader(inputFileStream));
			final BufferedWriter brOut = new BufferedWriter(
				new OutputStreamWriter(fTarget.storeFileStream(fileName)));

			if ((brIn != null) && (brOut != null)) {
				String str = "";

				while ((str = brIn.readLine()) != null) {
					final int cr = 13;
					final int fe = 10;
					brOut.write(
						String.valueOf(str) +
							Character.valueOf(
								(char)cr
							).toString() +
								Character.valueOf(
									(char)fe
								).toString());
				}
			}

			brIn.close();
			brOut.close();
			fTarget.disconnect();
		}
		catch (Exception exception) {
			throw new IOException(exception);
		}
	}

	public FtpBean() {
		this.fcMyFtp = new FTPClient();
	}

	public void closeServer() throws IOException {
		this.fcMyFtp.disconnect();
	}

	public void ftpLogin(
			final String hostIP, final String uName, final String pwd)
		throws IOException {

		FtpBean.logger.info("Enter ftpLogin method ");

		try {
			this.fcMyFtp.connect(
				new InetSocketAddress(hostIP, fcMyFtp.getDefaultPort()).getAddress());
			this.fcMyFtp.login(uName, pwd);
			this.fcMyFtp.setFileType(FTPClient.BINARY_FILE_TYPE);
		}
		catch (Exception e) {
			throw new IOException(e);
		}

		FtpBean.logger.info("Exit ftpLogin method");
	}

	private static Logger logger;

	static {
		FtpBean.logger = Logger.getLogger(FtpBean.class);
	}

	private final FTPClient fcMyFtp;

}