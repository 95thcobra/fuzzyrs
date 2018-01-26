package com.rs.player.content;

import com.rs.server.Server;
import com.rs.content.player.PlayerRank;
import com.rs.core.net.io.OutputStream;
import com.rs.core.utils.Utils;
import com.rs.player.FriendsIgnores;
import com.rs.player.Player;
import com.rs.player.QuickChatMessage;
import com.rs.server.file.impl.PlayerFileManager;
import com.rs.world.World;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class FriendChatsManager {

    private static final HashMap<String, FriendChatsManager> cachedFriendChats = new HashMap<>();
    private final String owner;
    private final String ownerDisplayName;
    private final CopyOnWriteArrayList<Player> players;
    private final ConcurrentHashMap<String, Long> bannedPlayers;
    private FriendsIgnores settings;
    private byte[] dataBlock;

    private FriendChatsManager(final Player player) {
        owner = player.getUsername();
        ownerDisplayName = player.getDisplayName();
        settings = player.getFriendsIgnores();
        players = new CopyOnWriteArrayList<>();
        bannedPlayers = new ConcurrentHashMap<>();
    }

    public static void destroyChat(final Player player) {
        synchronized (cachedFriendChats) {
            final FriendChatsManager chat = cachedFriendChats.get(player
                    .getUsername());
            if (chat == null)
                return;
            chat.destroyChat();
            player.getPackets().sendGameMessage(
                    "Your friends chat channel has now been disabled!");
        }
    }

    public static void linkSettings(final Player player) {
        synchronized (cachedFriendChats) {
            final FriendChatsManager chat = cachedFriendChats.get(player
                    .getUsername());
            if (chat == null)
                return;
            chat.settings = player.getFriendsIgnores();
        }
    }

    public static void refreshChat(final Player player) {
        synchronized (cachedFriendChats) {
            final FriendChatsManager chat = cachedFriendChats.get(player
                    .getUsername());
            if (chat == null)
                return;
            chat.refreshChannel();
        }
    }

    public static void joinChat(final String ownerName, final Player player) {
        synchronized (cachedFriendChats) {
            if (player.getCurrentFriendChat() != null)
                return;
            player.getPackets()
                    .sendGameMessage("Attempting to join channel...");
            final String formattedName = PlayerFileManager.formatUserNameForFile(ownerName);
            FriendChatsManager chat = cachedFriendChats.get(formattedName);
            if (chat == null) {
                Player owner = World.getPlayerByDisplayName(ownerName);
                if (owner == null) {
                    if (!Server.getInstance().getPlayerFileManager().exists(formattedName)) {
                        player.getPackets()
                                .sendGameMessage(
                                        "The channel you tried to join does not exist.");
                        return;
                    }
                    Optional<Player> ownerOptional = Server.getInstance().getPlayerFileManager().load(formattedName);
                    if (!ownerOptional.isPresent()) {
                        player.getPackets()
                                .sendGameMessage(
                                        "The channel you tried to join does not exist.");
                        return;
                    }
                    owner = ownerOptional.get();
                    owner.setUsername(formattedName);
                }
                final FriendsIgnores settings = owner.getFriendsIgnores();
                if (!settings.hasFriendChat()) {
                    player.getPackets().sendGameMessage(
                            "The channel you tried to join does not exist.");
                    return;
                }
                if (!player.getUsername().equals(ownerName)
                        && !settings.hasRankToJoin(player.getUsername())
                        && player.getRank().isMinimumRank(PlayerRank.ADMIN)) {
                    player.getPackets()
                            .sendGameMessage(
                                    "You do not have a enough rank to join this friends chat channel.");
                    return;
                }
                chat = new FriendChatsManager(owner);
                cachedFriendChats.put(ownerName, chat);
                chat.joinChatNoCheck(player);
            } else {
                chat.joinChat(player);
            }
        }

    }

    public int getRank(PlayerRank playerRank, final String username) {
        if (playerRank.isMinimumRank(PlayerRank.ADMIN))
            return 127;
        if (username.equals(owner))
            return 7;
        return settings.getRank(username);
    }

    public CopyOnWriteArrayList<Player> getPlayers() {
        return players;
    }

    public int getWhoCanKickOnChat() {
        return settings.getWhoCanKickOnChat();
    }

    public String getOwnerDisplayName() {
        return ownerDisplayName;
    }

    public String getOwnerName() {
        return owner;
    }

    public String getChannelName() {
        return settings.getChatName().replaceAll("<img=", "");
    }

    private void joinChat(final Player player) {
        synchronized (this) {
            if (!player.getUsername().equals(owner)
                    && !settings.hasRankToJoin(player.getUsername())
                    && player.getRank().isMinimumRank(PlayerRank.ADMIN)) {
                player.getPackets()
                        .sendGameMessage(
                                "You do not have a enough rank to join this friends chat channel.");
                return;
            }
            if (players.size() >= 100) {
                player.getPackets().sendGameMessage("This chat is full.");
                return;
            }
            final Long bannedSince = bannedPlayers.get(player.getUsername());
            if (bannedSince != null) {
                if (bannedSince + 3600000 > Utils.currentTimeMillis()) {
                    player.getPackets().sendGameMessage(
                            "You have been banned from this channel.");
                    return;
                }
                bannedPlayers.remove(player.getUsername());
            }
            joinChatNoCheck(player);
        }
    }

    public void leaveChat(final Player player, final boolean logout) {
        synchronized (this) {
            player.setCurrentFriendChat(null);
            players.remove(player);
            if (players.size() == 0) { // no1 at chat so uncache it
                synchronized (cachedFriendChats) {
                    cachedFriendChats.remove(owner);
                }
            } else {
                refreshChannel();
            }
            if (!logout) {
                player.setCurrentFriendChatOwner(null);
                player.getPackets().sendGameMessage(
                        "You have left the channel.");
                player.getPackets().sendFriendsChatChannel();
            }
        }
    }

    public Player getPlayerByDisplayName(final String username) {
        final String formatedUsername = Utils
                .formatPlayerNameForProtocol(username);
        for (final Player player : players) {
            if (player.getUsername().equals(formatedUsername)
                    || player.getDisplayName().equals(username))
                return player;
        }
        return null;
    }

    public void kickPlayerFromChat(final Player player, final String username) {
        String name = "";
        for (final char character : username.toCharArray()) {
            name += Utils.containsInvalidCharacter(character) ? " " : character;
        }
        synchronized (this) {
            final int rank = getRank(player.getRank(), player.getUsername());
            if (rank < getWhoCanKickOnChat())
                return;
            final Player kicked = getPlayerByDisplayName(name);
            if (kicked == null) {
                player.getPackets().sendGameMessage(
                        "This player is not this channel.");
                return;
            }
            if (rank <= getRank(kicked.getRank(), kicked.getUsername()))
                return;
            kicked.setCurrentFriendChat(null);
            kicked.setCurrentFriendChatOwner(null);
            players.remove(kicked);
            bannedPlayers.put(kicked.getUsername(), Utils.currentTimeMillis());
            kicked.getPackets().sendFriendsChatChannel();
            kicked.getPackets().sendGameMessage(
                    "You have been kicked from the friends chat channel.");
            player.getPackets().sendGameMessage(
                    "You have kicked " + kicked.getUsername()
                            + " from friends chat channel.");
            refreshChannel();

        }
    }

    private void joinChatNoCheck(final Player player) {
        synchronized (this) {
            players.add(player);
            player.setCurrentFriendChat(this);
            player.setCurrentFriendChatOwner(owner);
            player.getPackets().sendGameMessage(
                    "You are now talking in the friends chat channel "
                            + settings.getChatName());
            refreshChannel();
        }
    }

    public void destroyChat() {
        synchronized (this) {
            for (final Player player : players) {
                player.setCurrentFriendChat(null);
                player.setCurrentFriendChatOwner(null);
                player.getPackets().sendFriendsChatChannel();
                player.getPackets().sendGameMessage(
                        "You have been removed from this channel!");
            }
        }
        synchronized (cachedFriendChats) {
            cachedFriendChats.remove(owner);
        }

    }

    public void sendQuickMessage(final Player player,
                                 final QuickChatMessage message) {
        synchronized (this) {
            if (!player.getUsername().equals(owner)
                    && !settings.canTalk(player) && player.getRank().isMinimumRank(PlayerRank.ADMIN)) {
                player.getPackets()
                        .sendGameMessage(
                                "You do not have a enough rank to talk on this friends chat channel.");
                return;
            }
            final String formatedName = Utils.formatPlayerNameForDisplay(player
                    .getUsername());
            final String displayName = player.getDisplayName();
            final int rights = player.getRank().getMessageIcon();
            for (final Player p2 : players) {
                p2.getPackets().receiveFriendChatQuickMessage(formatedName,
                        displayName, rights, settings.getChatName(), message);
            }
        }
    }

    public void sendMessage(final Player player, final String message) {
        synchronized (this) {
            if (!player.getUsername().equals(owner)
                    && !settings.canTalk(player) && player.getRank().isMinimumRank(PlayerRank.ADMIN)) {
                player.getPackets()
                        .sendGameMessage(
                                "You do not have a enough rank to talk on this friends chat channel.");
                return;
            }
            final String formatedName = Utils.formatPlayerNameForDisplay(player
                    .getUsername());
            final String displayName = player.getDisplayName();
            final int rights = player.getRank().getMessageIcon();
            for (final Player p2 : players) {
                p2.getPackets().receiveFriendChatMessage(formatedName,
                        displayName, rights, settings.getChatName(), message);
            }
        }
    }

    public void sendDiceMessage(final Player player, final String message) {
        synchronized (this) {
            if (!player.getUsername().equals(owner)
                    && !settings.canTalk(player) && player.getRank().isMinimumRank(PlayerRank.ADMIN)) {
                player.getPackets()
                        .sendGameMessage(
                                "You do not have a enough rank to talk on this friends chat channel.");
                return;
            }
            for (final Player p2 : players) {
                p2.getPackets().sendGameMessage(message);
            }
        }
    }

    private void refreshChannel() {
        synchronized (this) {
            final OutputStream stream = new OutputStream();
            stream.writeString(ownerDisplayName);
            final String ownerName = Utils.formatPlayerNameForDisplay(owner);
            stream.writeByte(getOwnerDisplayName().equals(ownerName) ? 0 : 1);
            if (!getOwnerDisplayName().equals(ownerName)) {
                stream.writeString(ownerName);
            }
            stream.writeLong(Utils.stringToLong(getChannelName()));
            final int kickOffset = stream.getOffset();
            stream.writeByte(0);
            stream.writeByte(getPlayers().size());
            for (final Player player : getPlayers()) {
                final String displayName = player.getDisplayName();
                final String name = Utils.formatPlayerNameForDisplay(player
                        .getUsername());
                stream.writeString(displayName);
                stream.writeByte(displayName.equals(name) ? 0 : 1);
                if (!displayName.equals(name)) {
                    stream.writeString(name);
                }
                stream.writeShort(1);
                final int rank = getRank(player.getRank(),
                        player.getUsername());
                stream.writeByte(rank);
                stream.writeString(Server.getInstance().getSettingsManager().getSettings().getServerName());
            }
            dataBlock = new byte[stream.getOffset()];
            stream.setOffset(0);
            stream.getBytes(dataBlock, 0, dataBlock.length);
            for (final Player player : players) {
                dataBlock[kickOffset] = (byte) (player.getUsername().equals(
                        owner) ? 0 : getWhoCanKickOnChat());
                player.getPackets().sendFriendsChatChannel();
            }
        }
    }

    public byte[] getDataBlock() {
        return dataBlock;
    }
}
