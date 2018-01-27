package com.rs.player.content;

import com.rs.content.player.PlayerRank;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.world.World;

/**
 * Handles sending of yell command.
 * 
 * @author FuzzyAvacado
 */
public class YellHandler {

    private static final String OWNER_DEV_YELL = "[<col=1589FF>Owner/Developer</col>]";
    private static final String OWNER_YELL = "[<col=1589FF>Owner</col>]";
    private static final String ADMIN_YELL = "[<col=800000>Administrator</col>]";
	private static final String MOD_YELL = "[<col=A020F0>Moderator</col>]";
	private static final String FORUM_MOD_YELL = "[<col=ff000>Forum Moderator</col>]";
	private static final String HELPER_YELL = "[<col=A020F0>Helper</col>] ";

	private static final String[] COLOR = { ": <col=1589FF>", ": <col=800000>",
		": <col=3300FF>", ": <col=A020F0>", ": <col=FFD700>",
		": <col=87CEEB>", ": <col=ff000>", ": <col=ff0000>", ": <col=008000>"
    };
	private static final String END_COLOR = "</col>";

	public static void sendYell(final Player player, final String message) {
		if (player.getMuted() > Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage(
					"You temporary muted. Recheck in 48 hours.");
			return;
		}
		if (!player.getRank().isMinimumRank(PlayerRank.ADMIN)) {
			final String[] invalid = { "<euro", "<img", "<img=", "<col",
					"<col=", "<shad", "<shad=", "<str>", "<u>" };
			for (final String s : invalid)
				if (message.contains(s)) {
					player.getPackets().sendGameMessage(
							"You cannot add additional code to the message.");
					return;
				}
		}
		final String yellTitle = getYellTitle(player);
		World.sendGlobalMessage(yellTitle + " " + message + END_COLOR);
	}

	private static String getYellTitle(final Player player) {
		switch (player.getRank()) {
			case FORUM_MODERATOR:
				return FORUM_MOD_YELL + "<img=" + player.getRank().getMessageIcon() + ">" + player.getDisplayName() + COLOR[2];
			case HELPER:
				return HELPER_YELL + "<img=" + player.getRank().getMessageIcon() + ">" + player.getDisplayName() + COLOR[2];
			case MOD:
				return MOD_YELL + "<img=" + player.getRank().getMessageIcon() + ">" + player.getDisplayName() + COLOR[2];
			case ADMIN:
				return ADMIN_YELL + "<img=" + player.getRank().getMessageIcon() + ">" + player.getDisplayName() + COLOR[1];
			case OWNER:
				return OWNER_YELL + "<img=" + player.getRank().getMessageIcon() + ">" + player.getDisplayName() + COLOR[0];
            case OWNER_AND_DEVELOPER:
                return OWNER_DEV_YELL + "<img=" + player.getRank().getMessageIcon() + ">" + player.getDisplayName() + COLOR[0];
            default:
			    return "[Player] " + player.getDisplayName();
		}
	}
}
