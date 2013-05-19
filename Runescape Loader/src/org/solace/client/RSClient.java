package org.solace.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.solace.RunClient;
import org.solace.client.addons.impl.HighscoresCalculator;

@SuppressWarnings("serial")
public class RSClient extends JFrame implements ActionListener {

	private static Component client;

	public static int screenWidth = 800;
	public static int screenHeight = 710;
	public HighscoresCalculator highCalculator;

	public JMenuBar menuBar = new JMenuBar();
	public Object[][] menuButtons = new Object[][] {
			{ "File", new JMenuItem("Exit") },
			{ "Tools", new JMenuItem("Highscores calculator"),
					new JMenuItem("Take Screenshot") } };

	private boolean showCalc = false;

	public RSClient(final RunClient runClient) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException, MalformedURLException {
		super("Solace Loader");
		highCalculator = new HighscoresCalculator();
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		client = new AppletPanel(runClient, screenWidth, screenHeight);
		add(client, BorderLayout.WEST);
		setSize(client.getSize());
		setMinimumSize(client.getSize());
		setMenuBar();
		highCalculator.setBackground(UIManager.getColor(UIManager
				.getCrossPlatformLookAndFeelClassName()));
		highCalculator.setForeground(Color.black);
		add(highCalculator, BorderLayout.EAST);
		pack();
	}

	public void init() {
		setResizable(false);
		setVisible(true);
	}

	public void setMenuBar() {
		int actionId = 0;
		for (int y = 0; y < menuButtons.length; y++) {
			JMenu menu = new JMenu();
			for (int i = 0; i < menuButtons[y].length; i++) {
				if (i == 0) {
					menu.setText((String) menuButtons[y][i]);
				} else if (menuButtons[y][i] instanceof JMenuItem) {
					JMenuItem item = (JMenuItem) menuButtons[y][i];
					menu.add(item);
					item.setActionCommand("" + actionId++);
					item.addActionListener(this);
				} else {
					menu.add((JSeparator) menuButtons[y][i]);
				}
			}
			menuBar.add(menu);
		}
		setJMenuBar(menuBar);
	}

	public static Component getClient() {
		return client;
	}

	public void actionPerformed(ActionEvent e) {
		switch (Integer.parseInt(e.getActionCommand())) {
		case 0:
			if (JOptionPane.showConfirmDialog(this,
					"Do you really want to exit?") == 0)
				System.exit(0);
			break;
		case 1:
			showCalc = !showCalc;
			break;
		}
	}

}
