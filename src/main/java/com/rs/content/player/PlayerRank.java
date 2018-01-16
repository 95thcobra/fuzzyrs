package com.rs.content.player;

import java.io.Serializable;

/**
 * @author FuzzyAvacado
 */
public enum PlayerRank implements Serializable {

    PLAYER(0),
    GRAPHICS_DESIGNER(12),
    FORUM_MODERATOR(10),
    HELPER(6),
    MOD(1),
    ADMIN(2),
    DEVELOPER(11),
    OWNER(7),
    OWNER_AND_DEVELOPER(11);

    private final int messageIcon;
    private boolean trustedDicer;
    private DonateRank donateRank;

    PlayerRank(int messageIcon) {
        this.messageIcon = messageIcon;
    }

    public final boolean isTrustedDicer() {
        return trustedDicer;
    }

    public final void setTrustedDicer(boolean trustedDicer) {
        this.trustedDicer = trustedDicer;
    }

    public final DonateRank getDonateRank() {
        return donateRank == null ? setDonateRank(DonateRank.NONE) : donateRank;
    }

    public final DonateRank setDonateRank(DonateRank donateRank) {
        return this.donateRank = donateRank;
    }

    public final int getMessageIcon() {
        return this == PLAYER ? (donateRank != null ? donateRank.getMessageIcon() : 0) : messageIcon;
    }

    public boolean isMinimumRank(PlayerRank other) {
        return this.ordinal() >= other.ordinal();
    }

    public boolean isStaff() {
        return ordinal() >= 5;
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public boolean isMod() {
        return this == MOD;
    }

    public boolean isOwner() {
        return this == OWNER || this == OWNER_AND_DEVELOPER;
    }

    public enum DonateRank implements Serializable {

        NONE(0),
        DONATOR(4),
        SUPER_DONATOR(4),
        EXTREME_DONATOR(5),
        VIP(5);

        private final int messageIcon;

        DonateRank(int messageIcon) {
            this.messageIcon = messageIcon;
        }

        public int getMessageIcon() {
            return messageIcon;
        }

        public boolean isNone() {
            return this == NONE;
        }

        public boolean isMinimumRank(DonateRank donateRank) {
            return this.ordinal() >= donateRank.ordinal();
        }
    }
}
