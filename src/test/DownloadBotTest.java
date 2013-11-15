package test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.elite.jdcbot.examples.DownloadBot;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DownloadBotTest {
	public static Document getList() {
		try {
			ByteArrayOutputStream osXml = new ByteArrayOutputStream();
			PrintStream osLog = new PrintStream(new FileOutputStream("log.log", true));
			new DownloadBot("DCEMN-Updater", "172.16.123.253", "172.16.123.253", "Clement", osXml, osLog, 2, false);

			//System.out.println("fini");
			InputStream is = new ByteArrayInputStream(osXml.toByteArray());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			return dbf.newDocumentBuilder().parse(is);
		} catch (ParserConfigurationException | SAXException | IOException e) {
//			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws InterruptedException {
		while (true) {
			System.out.println(getList());
			System.out.println("Attente");
			Thread.sleep(10 * 1000);
			System.out.println();
		}
	}
}