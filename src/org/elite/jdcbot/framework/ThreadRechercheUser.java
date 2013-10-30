package org.elite.jdcbot.framework;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ThreadRechercheUser extends Thread {

	private jDCBot socket;
	private List<String> liste;
	private PrintStream log;

	public ThreadRechercheUser(jDCBot jDCBot, PrintStream log) {
		this.socket = jDCBot;
		this.log = log;
		liste = new ArrayList<String>();
	}
	
	@Override
	public void run() {
		String line;
		while (true) {
			try {
				line = socket.ReadCommand();
				log.println(line);
				if (line.startsWith("$MyINFO $ALL ")) {
					liste.add(line.substring(13));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<String> getDescriptionsList() {
		return liste;
	}
}
