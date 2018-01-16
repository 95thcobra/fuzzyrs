package com.rs.player;

import com.rs.core.utils.DateUtils;
import com.rs.core.utils.TextUtils;

public class LoginMessages {

    private static final String[] MESSAGES = {
            TextUtils.BLUE + "Double Xp Weekend is " + (DateUtils.isWeekend() ? "ON" : "OFF") + "!</col>"
    };

	public static void send(Player player) {
        for (String s : MESSAGES) {
            player.sendMessage(s);
		}
	}
}
