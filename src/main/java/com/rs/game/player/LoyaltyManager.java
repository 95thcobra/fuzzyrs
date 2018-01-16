package com.rs.game.player;

import java.util.TimerTask;
import java.util.Date;
import com.rs.cores.CoresManager;

public class LoyaltyManager {
 
	private transient Player player;
	
	public LoyaltyManager(Player player) {
		this.player = player;
	}
	
	public void startTimer() {
	CoresManager.fastExecutor.schedule(new TimerTask() {
			int timer = 1800;
			@Override
			public void run() {
				if (timer == 1) {
					player.setLoyaltyPoints(player.getLoyaltyPoints() + 250);
					player.setLoyaltyTokens(player.getLoyaltyTokens() + 250);
					timer = 1800; 
					player.getPackets().sendGameMessage("<col=008000>You have recieved 250 loyalty points for playing for 30 minutes.");
					player.getPackets().sendGameMessage("<col=008000>You now have " + player.getLoyaltyPoints() + " Loyalty Points.");
					player.getPackets().sendGameMessage("<col=ff0033>250 Loyalty tokens have been added to your bank.");
					player.getBank().addItem(23113, 250, true);
				}
				if (timer > 0) {
					timer--;
				}
			}
		}, 0L, 1000L);
	}
}