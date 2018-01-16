package com.rs.content.potiontimers;

/**
 * @author John (FuzzyAvacado) on 12/10/2015.
 */
public enum PotionType {

    OVERLOAD(0, 5, 4, 20),
    PRAYER_RENEWAL(1, 6, 4, 20),
    ANTIFIRE(2, 7, 6, 0),
    ANTIPOISON(3, 8, 3, 0),
    SUPER_ANTIPOISON(3, 8, 6, 0),
    SUMMONING(4, 9, 0, 0);

    private final int imageId, textId, mins, seconds;

    PotionType(int imageId, int textId, int mins, int seconds) {
        this.imageId = imageId;
        this.textId = textId;
        this.mins = mins;
        this.seconds = seconds;
    }

    public int getImageId() {
        return imageId;
    }

    public int getTextId() {
        return textId;
    }

    public int getMins() {
        return mins;
    }

    public int getSeconds() {
        return seconds;
    }
}
