package org.solace.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.solace.RunClient;

public class AppletPanel extends JPanel {

	private static final long serialVersionUID = 3811773474473081991L;

	public AppletPanel(RunClient runClient, int width, int height) {
		applet = runClient.getApplet();
		applet.setSize(width - 20, height - 130);
		add(applet);
		setSize(new Dimension(width - 20, height - 130));
		setBackground(Color.BLACK);
	}

	public static Component getApplet() {
		return applet;
	}

	public static void setAppletSize(int x) {
		applet.setSize(x - 20, applet.getHeight());
	}

	private static Component applet;

}
