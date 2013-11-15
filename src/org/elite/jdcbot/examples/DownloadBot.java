/*
 * DownloadBot.java
 *
 * Copyright (C) 2008 AppleGrew
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA. 
 */
package org.elite.jdcbot.examples;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.elite.jdcbot.framework.BotException;
import org.elite.jdcbot.framework.DUEntity;
import org.elite.jdcbot.framework.User;
import org.elite.jdcbot.framework.jDCBot;

/**
 * Created on 31-May-08<br>
 * This exmple bot will download any file from a user when that user sends the
 * magnet URI of the file in private message to this bot.
 * <p>
 * The bot will immediately quit if anybody sends <code>+quit</code> as private
 * message to this bot.
 * 
 * @author AppleGrew
 * @since 0.7.1
 * @version 0.1
 */
public class DownloadBot extends jDCBot {

//	private FileOutputStream fos;
//	private String _fileName;
	private String _clientName;
	private boolean _terminated;
	private OutputStream _os;
	private boolean _debug;

	public DownloadBot(String botName, String botIp, String hubIp, String clientName, OutputStream os, PrintStream logStream, int userSearchDuration, boolean debug) {
		this(botName, botIp, hubIp, clientName, os, logStream, 60, userSearchDuration, debug);
	}

	public DownloadBot(String botName, String botIp, String hubIp, String clientName, OutputStream os, PrintStream logStream, int timeOut, int userSearchDuration, boolean debug) {
		super(botName, // Bot's name
				botIp, // Bot's IP
				9020, // Bot's listen port
				"", // Password
				"", // Description
				"LAN(T1)1", // Connection type
				"", // Email
				"0", // Share size in bytes
				3, // No. of upload slots
				3, // No of download slots.
				false, // Is passive
				createLogStream(logStream), // PrintStream where debug messages will go
				userSearchDuration
				);
//		_fileName = fileName;
		_os = os;
		_clientName = clientName;
		_debug = debug;
		try {
			connect(hubIp, 411);
		} catch (Exception e) {
			e.printStackTrace();
			terminate();
		}
		int c = 0;
		while (!_terminated && c < timeOut) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			c++;
		}
		if (_debug)
			System.out.println("terminated : " + _terminated);
//		if (!_terminated)
			terminate();
	}

	private static PrintStream createLogStream(PrintStream logStream) {
		if (logStream != null)
			return logStream;
		return new PrintStream(new OutputStream() {

			@Override
			public void write(int arg0) throws IOException {

			}
		});
	}
	
	private void pm(String user, String msg) {
		try {
			SendPrivateMessage(user, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void onDownloadComplete(User user, DUEntity due, boolean success,	BotException e) {
		pm(user.username(), "I just now "
				+ (success ? "successfully" : "unsuccessfully")
				+ " completed download of " + due.file + " from you.");
		if (success) {
			try {
				_os.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (_debug)
				System.out.println("Liste de fichiers récupérée");
		} else {
			pm(user.username(), "I got this exception: " + e.getMessage());
			System.out.println("Echec de la récupération de la liste de fichiers");
		}
		_terminated = true;
		terminate();
	}

	@Override
	protected void onConnect() {
		if (_debug)
			System.out.println("Connecté");
		if (um.userExist(_clientName)) {
			if (_debug)
				System.out.println("Utilisateur trouvé");
			User clem = getUser(_clientName);
			if (clem != null)
				try {
//					fos = new FileOutputStream(_fileName, false);
					clem.downloadFileList(_os, 0);
				} catch (BotException e) {
					e.printStackTrace();
					System.out.println("Echec de la récupération de la liste de fichiers");
					System.out.println("Exception : "+e.getMessage());
				}
		}
	}
}
