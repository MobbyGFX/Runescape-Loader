package org.solace;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.Color;
import java.awt.Component;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.UnsupportedLookAndFeelException;

import org.solace.client.RSClient;
import org.solace.client.RSUpdater;

public class RunClient {

	private static String worldPage = "http://world2.runescape.com/";
	private static Map<String, String> params = new HashMap<String, String>();
	private static File loaderFile = new File(System.getProperty("user.home")
			+ "//Solace Loader/loader.jar");
	public Applet client;
	public int[] nums = new int[10];

	public RunClient() throws IOException, InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			UnsupportedLookAndFeelException {
		File[] homeDireFiles = new File[] {
				new File(System.getProperty("user.home") + "//Solace Loader/"),
				new File(System.getProperty("user.home")
						+ "//Solace Loader/plugins"),
				new File(System.getProperty("user.home")
						+ "//Solace Loader/screenshots") };
		File paramFile = new File(System.getProperty("user.home")
				+ "//Solace Loader/parameters.txt");
		for (File f : homeDireFiles) {
			if (!f.exists())
				f.mkdirs();
		}
		if (!paramFile.exists()) {
			try {
				generateParameters();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!loaderFile.exists()) {
			fetchGamePack(loaderFile);
			System.out.println("Updated loader jar.");
		}
		hasUpdate();
		fetchGamePack(loaderFile);
		fetchParams();
		initializeClient();
	}

	public boolean hasUpdate() {
		if (RSUpdater.fetchLocalVersion())
			return true;
		return false;
	}

	@SuppressWarnings("resource")
	private void initializeClient() throws IOException, InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			UnsupportedLookAndFeelException {
		System.out.println("Initializing client...");
		URLClassLoader classLoader = new URLClassLoader(new URL[] { loaderFile
				.toURI().toURL() });
		System.out.println("Loading Runescape class instance");
		client = (Applet) classLoader.loadClass("Rs2Applet").newInstance();
		client.setBackground(Color.BLACK);
		client.setStub(new AppletStub() {
			public boolean isActive() {
				return true;
			}

			public String getParameter(String name) {
				return params.get(name);
			}

			public URL getDocumentBase() {
				try {
					return new URL(worldPage);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				return null;
			}

			public URL getCodeBase() {
				try {
					return new URL(worldPage);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				return null;
			}

			public AppletContext getAppletContext() {
				return null;
			}

			public void appletResize(int width, int height) {

			}
		});
		System.out.println("Initalizing Game Loader Applet...");
		client.init();
		System.out.println("Starting Game Loader Applet...");
		client.start();
		client.setSize(765, 503);
		System.out.println("Finishing up - Loading Frame..");
		new RSClient(this).init();
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		try {
			RunClient runClient = new RunClient();
		} catch (IOException | InstantiationException | IllegalAccessException
				| ClassNotFoundException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	public void fetchParams() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				System.getProperty("user.home") + "//Runeception/params.txt")));
		String detail;
		while ((detail = reader.readLine()) != null) {
			params.put(detail.split("<value>")[0], detail.split("<value>")[1]);
		}
		reader.close();
		System.out.println("Fetched paramaters");
	}

	public void fetchGamePack(File loader) throws IOException {
		String fileLocation = fetchPageSource().split("archive=")[1].split(" ")[0];
		BufferedInputStream input = new BufferedInputStream(new URL(worldPage
				+ fileLocation).openStream());
		BufferedOutputStream output = new BufferedOutputStream(
				new FileOutputStream(loader));
		int bytesRead = 0;
		while ((bytesRead = input.read()) != -1) {
			output.write(bytesRead);
		}
		System.out.println("Fetching Game Pack Files...");
		System.out.println("Grabbed Game Pack files...");
		input.close();
		output.close();
	}

	public static ArrayList<String> fetchPageDetails()
			throws MalformedURLException, IOException {
		ArrayList<String> pageSource = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new URL(worldPage).openStream()));
		String line;
		while ((line = reader.readLine()) != null)
			pageSource.add(line);
		reader.close();
		return pageSource;
	}

	public String fetchPageSource() throws IOException {
		URL website = new URL(worldPage);
		HttpURLConnection connection = (HttpURLConnection) website
				.openConnection();
		connection.setRequestMethod("GET");
		InputStream input = connection.getInputStream();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] pageBuffer = new byte[1024];
		int bytesRead = 0;
		while ((bytesRead = input.read(pageBuffer)) != -1)
			output.write(pageBuffer, 0, bytesRead);
		input.close();
		output.close();
		return new StringBuilder().append(output).toString();
	}

	public static void generateParameters() throws IOException {
		System.out.println("Generating Client Parameters...");
		BufferedWriter writer = new BufferedWriter(
				new FileWriter(new File(System.getProperty("user.home")
						+ "//Solace Loader/params.txt")));
		for (String line : fetchPageDetails()) {
			if (line.contains("<param name=")) {
				String key = line.split("<param name=\"")[1].split("\" ")[0];
				String value = line.split("value=\"")[1].split("\">'")[0];
				if (value.isEmpty())
					value = " ";
				writer.write(key + "<value>" + value);
				writer.newLine();
			}
		}
		writer.close();
	}

	public Component getApplet() {
		return client;
	}

}
