package com.rs.player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SpinsManager {

	private Player player;
	private boolean gotSpins = false;
	private boolean ipSucess = false;

	public SpinsManager(final Player player) {
		this.player = player;
	}

	public static String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("MM dd yyyy");
		Date date = new Date();
		final String currentDate = dateFormat.format(date);
		date = null;
		dateFormat = null;
		return currentDate;
	}

	public void checkIP() {
		try {
			ipSucess = false;
			final File file1 = new File("./data/playersaves/ipcheckspin/"
					+ getDate() + " " + player.getSession().getIP() + ".txt");
			final boolean success = file1.createNewFile();
			if (success) {
				ipSucess = true;
				Writer output = null;
				output = new BufferedWriter(new FileWriter(file1));
				output.write("Username: " + player.getUsername() + "");
				output.close();
			} else
				return;
		} catch (final IOException e) {
		}
	}

	public void addSpins() {
		checkIP();
		try {
			gotSpins = false;
			final File file = new File("./data/playersaves/spins/" + getDate()
					+ " " + player.getUsername() + ".txt");
			final boolean success = file.createNewFile();
			if (success) {
				if (ipSucess == false)
					return;
				gotSpins = true;
				player.getPackets()
						.sendGameMessage(
								"You received two free spins for the Squeal Of Fortune.");
				player.setSpins(player.getSpins() + 2);
				Writer output = null;
				output = new BufferedWriter(new FileWriter(file));
				output.write("" + gotSpins + "");
				output.close();
			}
		} catch (final IOException e) {
		}
	}
}