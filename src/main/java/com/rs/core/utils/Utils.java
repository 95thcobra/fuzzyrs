package com.rs.core.utils;

import com.rs.server.Server;
import com.rs.content.actions.skills.Skills;
import com.rs.core.cache.Cache;
import com.rs.player.Player;
import com.rs.world.WorldTile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.Random;

public final class Utils {

    public static final byte[] DIRECTION_DELTA_X = new byte[]{-1, 0, 1, -1,
            1, -1, 0, 1};
    public static final byte[] DIRECTION_DELTA_Y = new byte[]{1, 1, 1, 0, 0,
            -1, -1, -1};
    public static final char[] VALID_CHARS = {'_', 'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
            's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9'};
    private static final Object ALGORITHM_LOCK = new Object();
    private static final Random RANDOM = new Random();
    public static char[] aCharArray6385 = {'\u20ac', '\0', '\u201a', '\u0192',
            '\u201e', '\u2026', '\u2020', '\u2021', '\u02c6', '\u2030',
            '\u0160', '\u2039', '\u0152', '\0', '\u017d', '\0', '\0', '\u2018',
            '\u2019', '\u201c', '\u201d', '\u2022', '\u2013', '\u2014',
            '\u02dc', '\u2122', '\u0161', '\u203a', '\u0153', '\0', '\u017e',
            '\u0178'};
    private static long timeCorrection;
    private static long lastTimeUpdate;

    private Utils() {

    }

    public static synchronized long currentTimeMillis() {
        final long l = System.currentTimeMillis();
        if (l < lastTimeUpdate) {
            timeCorrection += lastTimeUpdate - l;
        }
        lastTimeUpdate = l;
        return l + timeCorrection;
    }

    public static byte[] cryptRSA(final byte[] data, final BigInteger exponent,
                                  final BigInteger modulus) {
        return new BigInteger(data).modPow(exponent, modulus).toByteArray();
    }

    public static byte[] encryptUsingMD5(final byte[] buffer) {
        // prevents concurrency problems with the algorithm
        synchronized (ALGORITHM_LOCK) {
            try {
                final MessageDigest algorithm = MessageDigest
                        .getInstance("MD5");
                algorithm.update(buffer);
                final byte[] digest = algorithm.digest();
                algorithm.reset();
                return digest;
            } catch (final Throwable e) {
                Logger.handle(e);
            }
            return null;
        }
    }

    public static boolean inCircle(final WorldTile location,
                                   final WorldTile center, final int radius) {
        return getDistance(center, location) < radius;
    }

    public static void copyFile(final File sourceFile, final File destFile)
            throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    public static int getDistance(final WorldTile t1, final WorldTile t2) {
        return getDistance(t1.getX(), t1.getY(), t2.getX(), t2.getY());
    }

    public static int getDistance(final int coordX1, final int coordY1,
                                  final int coordX2, final int coordY2) {
        final int deltaX = coordX2 - coordX1;
        final int deltaY = coordY2 - coordY1;
        return ((int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
    }

    public static int getNpcMoveDirection(final int dd) {
        if (dd < 0)
            return -1;
        return getNpcMoveDirection(DIRECTION_DELTA_X[dd], DIRECTION_DELTA_Y[dd]);
    }

    public static int getNpcMoveDirection(final int dx, final int dy) {
        if (dx == 0 && dy > 0)
            return 0;
        if (dx > 0 && dy > 0)
            return 1;
        if (dx > 0 && dy == 0)
            return 2;
        if (dx > 0 && dy < 0)
            return 3;
        if (dx == 0 && dy < 0)
            return 4;
        if (dx < 0 && dy < 0)
            return 5;
        if (dx < 0 && dy == 0)
            return 6;
        if (dx < 0 && dy > 0)
            return 7;
        return -1;
    }

    public static int[][] getCoordOffsetsNear(final int size) {
        final int[] xs = new int[4 + (4 * size)];
        final int[] xy = new int[xs.length];
        xs[0] = -size;
        xy[0] = 1;
        xs[1] = 1;
        xy[1] = 1;
        xs[2] = -size;
        xy[2] = -size;
        xs[3] = 1;
        xy[2] = -size;
        for (int fakeSize = size; fakeSize > 0; fakeSize--) {
            xs[(4 + ((size - fakeSize) * 4))] = -fakeSize + 1;
            xy[(4 + ((size - fakeSize) * 4))] = 1;
            xs[(4 + ((size - fakeSize) * 4)) + 1] = -size;
            xy[(4 + ((size - fakeSize) * 4)) + 1] = -fakeSize + 1;
            xs[(4 + ((size - fakeSize) * 4)) + 2] = 1;
            xy[(4 + ((size - fakeSize) * 4)) + 2] = -fakeSize + 1;
            xs[(4 + ((size - fakeSize) * 4)) + 3] = -fakeSize + 1;
            xy[(4 + ((size - fakeSize) * 4)) + 3] = -size;
        }
        return new int[][]{xs, xy};
    }

    public static int getFaceDirection(final int xOffset,
                                       final int yOffset) {
        return ((int) (Math.atan2(-xOffset, -yOffset) * 2607.5945876176133)) & 0x3fff;
    }

    public static int getMoveDirection(final int xOffset,
                                       final int yOffset) {
        if (xOffset < 0) {
            if (yOffset < 0)
                return 5;
            else if (yOffset > 0)
                return 0;
            else
                return 3;
        } else if (xOffset > 0) {
            if (yOffset < 0)
                return 7;
            else if (yOffset > 0)
                return 2;
            else
                return 4;
        } else {
            if (yOffset < 0)
                return 6;
            else if (yOffset > 0)
                return 1;
            else
                return -1;
        }
    }

    public static int getGraphicDefinitionsSize() {
        final int lastArchiveId = Cache.STORE.getIndexes()[21]
                .getLastArchiveId();
        return lastArchiveId
                * 256
                + Cache.STORE.getIndexes()[21]
                .getValidFilesCount(lastArchiveId);
    }

    public static int getAnimationDefinitionsSize() {
        final int lastArchiveId = Cache.STORE.getIndexes()[20]
                .getLastArchiveId();
        return lastArchiveId
                * 128
                + Cache.STORE.getIndexes()[20]
                .getValidFilesCount(lastArchiveId);
    }

    public static int getConfigDefinitionsSize() {
        final int lastArchiveId = Cache.STORE.getIndexes()[22]
                .getLastArchiveId();
        return lastArchiveId
                * 256
                + Cache.STORE.getIndexes()[22]
                .getValidFilesCount(lastArchiveId);
    }

    // 22314

    public static int getObjectDefinitionsSize() {
        final int lastArchiveId = Cache.STORE.getIndexes()[16]
                .getLastArchiveId();
        return lastArchiveId
                * 256
                + Cache.STORE.getIndexes()[16]
                .getValidFilesCount(lastArchiveId);
    }

    public static int getNPCDefinitionsSize() {
        final int lastArchiveId = Cache.STORE.getIndexes()[18]
                .getLastArchiveId();
        return lastArchiveId
                * 128
                + Cache.STORE.getIndexes()[18]
                .getValidFilesCount(lastArchiveId);
    }

    public static int getItemDefinitionsSize() {
        final int lastArchiveId = Cache.STORE.getIndexes()[19]
                .getLastArchiveId();
        return (lastArchiveId * 256 + Cache.STORE.getIndexes()[19]
                .getValidFilesCount(lastArchiveId)) - 22314;
    }

    public static String getFormattedNumber(int value) {
        return new DecimalFormat("#,###,##0").format(value);
    }

    public static String getFormattedNumber(double amount, char seperator) {
        String str = new DecimalFormat("#,###,###").format(amount);
        char[] rebuff = new char[str.length()];
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= '0' && c <= '9')
                rebuff[i] = c;
            else
                rebuff[i] = seperator;
        }
        return new String(rebuff);
    }

    public static boolean itemExists(final int id) {
        if (id >= getItemDefinitionsSize()) // setted because of custom items
            return false;
        return Cache.STORE.getIndexes()[19].fileExists(id >>> 8, 0xff & id);
    }

    public static int getInterfaceDefinitionsSize() {
        return Cache.STORE.getIndexes()[3].getLastArchiveId() + 1;
    }

    public static int getInterfaceDefinitionsComponentsSize(
            final int interfaceId) {
        return Cache.STORE.getIndexes()[3].getLastFileId(interfaceId) + 1;
    }

    public static String formatPlayerNameForProtocol(String name) {
        if (name == null)
            return "";
        name = name.replaceAll(" ", "_");
        name = name.toLowerCase();
        return name;
    }

    public static String formatPlayerNameForDisplay(String name) {
        if (name == null)
            return "";
        name = name.replaceAll("_", " ");
        name = name.toLowerCase();
        final StringBuilder newName = new StringBuilder();
        boolean wasSpace = true;
        for (int i = 0; i < name.length(); i++) {
            if (wasSpace) {
                newName.append(("" + name.charAt(i)).toUpperCase());
                wasSpace = false;
            } else {
                newName.append(name.charAt(i));
            }
            if (name.charAt(i) == ' ') {
                wasSpace = true;
            }
        }
        return newName.toString();
    }

    public static int getRandom(final int maxValue) {
        return (int) (Math.random() * (maxValue + 1));
    }

    public static int random(final int min, final int max) {
        final int n = Math.abs(max - min);
        return Math.min(min, max) + (n == 0 ? 0 : random(n));
    }

    public static double random(final double min, final double max) {
        final double n = Math.abs(max - min);
        return Math.min(min, max) + (n == 0 ? 0 : random((int) n));
    }

    public static int next(final int max, final int min) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    public static double getRandomDouble(final double maxValue) {
        return (Math.random() * (maxValue + 1));

    }

    public static int random(final int maxValue) {
        if (maxValue <= 0)
            return 0;
        return RANDOM.nextInt(maxValue);
    }

    public static String longToString(long l) {
        if (l <= 0L || l >= 0x5b5b57f8a98a5dd1L)
            return null;
        if (l % 37L == 0L)
            return null;
        int i = 0;
        final char ac[] = new char[12];
        while (l != 0L) {
            final long l1 = l;
            l /= 37L;
            ac[11 - i++] = VALID_CHARS[(int) (l1 - l * 37L)];
        }
        return new String(ac, 12 - i, i);
    }

    public static boolean invalidAccountName(final String name) {
        return name.length() < 2 || name.length() > 12 || name.startsWith("_")
                || name.endsWith("_") || name.contains("__")
                || containsInvalidCharacter(name);
    }

    public static boolean invalidAuthId(final String auth) {
        return auth.length() != 10 || auth.contains("_")
                || containsInvalidCharacter(auth);
    }

    public static boolean containsInvalidCharacter(final char c) {
        for (final char vc : VALID_CHARS) {
            if (vc == c)
                return false;
        }
        return true;
    }

    public static boolean containsInvalidCharacter(final String name) {
        for (final char c : name.toCharArray()) {
            if (containsInvalidCharacter(c))
                return true;
        }
        return false;
    }

    public static long stringToLong(final String s) {
        long l = 0L;
        for (int i = 0; i < s.length() && i < 12; i++) {
            final char c = s.charAt(i);
            l *= 37L;
            if (c >= 'A' && c <= 'Z') {
                l += (1 + c) - 65;
            } else if (c >= 'a' && c <= 'z') {
                l += (1 + c) - 97;
            } else if (c >= '0' && c <= '9') {
                l += (27 + c) - 48;
            }
        }
        while (l % 37L == 0L && l != 0L) {
            l /= 37L;
        }
        return l;
    }

    public static int packGJString2(final int position,
                                    final byte[] buffer, final String String) {
        final int length = String.length();
        int offset = position;
        for (int index = 0; length > index; index++) {
            final int character = String.charAt(index);
            if (character > 127) {
                if (character > 2047) {
                    buffer[offset++] = (byte) ((character | 919275) >> 12);
                    buffer[offset++] = (byte) (128 | ((character >> 6) & 63));
                    buffer[offset++] = (byte) (128 | (character & 63));
                } else {
                    buffer[offset++] = (byte) ((character | 12309) >> 6);
                    buffer[offset++] = (byte) (128 | (character & 63));
                }
            } else {
                buffer[offset++] = (byte) character;
            }
        }
        return offset - position;
    }

    public static int calculateGJString2Length(final String String) {
        final int length = String.length();
        int gjStringLength = 0;
        for (int index = 0; length > index; index++) {
            final char c = String.charAt(index);
            if (c > '\u007f') {
                if (c <= '\u07ff') {
                    gjStringLength += 2;
                } else {
                    gjStringLength += 3;
                }
            } else {
                gjStringLength++;
            }
        }
        return gjStringLength;
    }

    public static int getNameHash(String name) {
        name = name.toLowerCase();
        int hash = 0;
        for (int index = 0; index < name.length(); index++) {
            hash = method1258(name.charAt(index)) + ((hash << 5) - hash);
        }
        return hash;
    }

    public static byte method1258(final char c) {
        byte charByte;
        if (c > 0 && c < '\200' || c >= '\240' && c <= '\377') {
            charByte = (byte) c;
        } else if (c != '\u20AC') {
            if (c != '\u201A') {
                if (c != '\u0192') {
                    if (c == '\u201E') {
                        charByte = -124;
                    } else if (c != '\u2026') {
                        if (c != '\u2020') {
                            if (c == '\u2021') {
                                charByte = -121;
                            } else if (c == '\u02C6') {
                                charByte = -120;
                            } else if (c == '\u2030') {
                                charByte = -119;
                            } else if (c == '\u0160') {
                                charByte = -118;
                            } else if (c == '\u2039') {
                                charByte = -117;
                            } else if (c == '\u0152') {
                                charByte = -116;
                            } else if (c != '\u017D') {
                                if (c == '\u2018') {
                                    charByte = -111;
                                } else if (c != '\u2019') {
                                    if (c != '\u201C') {
                                        if (c == '\u201D') {
                                            charByte = -108;
                                        } else if (c != '\u2022') {
                                            if (c == '\u2013') {
                                                charByte = -106;
                                            } else if (c == '\u2014') {
                                                charByte = -105;
                                            } else if (c == '\u02DC') {
                                                charByte = -104;
                                            } else if (c == '\u2122') {
                                                charByte = -103;
                                            } else if (c != '\u0161') {
                                                if (c == '\u203A') {
                                                    charByte = -101;
                                                } else if (c != '\u0153') {
                                                    if (c == '\u017E') {
                                                        charByte = -98;
                                                    } else if (c != '\u0178') {
                                                        charByte = 63;
                                                    } else {
                                                        charByte = -97;
                                                    }
                                                } else {
                                                    charByte = -100;
                                                }
                                            } else {
                                                charByte = -102;
                                            }
                                        } else {
                                            charByte = -107;
                                        }
                                    } else {
                                        charByte = -109;
                                    }
                                } else {
                                    charByte = -110;
                                }
                            } else {
                                charByte = -114;
                            }
                        } else {
                            charByte = -122;
                        }
                    } else {
                        charByte = -123;
                    }
                } else {
                    charByte = -125;
                }
            } else {
                charByte = -126;
            }
        } else {
            charByte = -128;
        }
        return charByte;
    }

    public static String getUnformatedMessage(
            final int messageDataLength, final int messageDataOffset,
            final byte[] messageData) {
        final char[] cs = new char[messageDataLength];
        int i = 0;
        for (int i_6_ = 0; i_6_ < messageDataLength; i_6_++) {
            int i_7_ = 0xff & messageData[i_6_ + messageDataOffset];
            if ((i_7_ ^ 0xffffffff) != -1) {
                if ((i_7_ ^ 0xffffffff) <= -129 && (i_7_ ^ 0xffffffff) > -161) {
                    int i_8_ = aCharArray6385[i_7_ - 128];
                    if (i_8_ == 0) {
                        i_8_ = 63;
                    }
                    i_7_ = i_8_;
                }
                cs[i++] = (char) i_7_;
            }
        }
        return new String(cs, 0, i);
    }

    public static byte[] getFormatedMessage(final String message) {
        final int i_0_ = message.length();
        final byte[] is = new byte[i_0_];
        for (int i_1_ = 0; (i_1_ ^ 0xffffffff) > (i_0_ ^ 0xffffffff); i_1_++) {
            final int i_2_ = message.charAt(i_1_);
            if (((i_2_ ^ 0xffffffff) >= -1 || i_2_ >= 128)
                    && (i_2_ < 160 || i_2_ > 255)) {
                if ((i_2_ ^ 0xffffffff) != -8365) {
                    if ((i_2_ ^ 0xffffffff) == -8219) {
                        is[i_1_] = (byte) -126;
                    } else if ((i_2_ ^ 0xffffffff) == -403) {
                        is[i_1_] = (byte) -125;
                    } else if (i_2_ == 8222) {
                        is[i_1_] = (byte) -124;
                    } else if (i_2_ != 8230) {
                        if ((i_2_ ^ 0xffffffff) != -8225) {
                            if ((i_2_ ^ 0xffffffff) != -8226) {
                                if ((i_2_ ^ 0xffffffff) == -711) {
                                    is[i_1_] = (byte) -120;
                                } else if (i_2_ == 8240) {
                                    is[i_1_] = (byte) -119;
                                } else if ((i_2_ ^ 0xffffffff) == -353) {
                                    is[i_1_] = (byte) -118;
                                } else if ((i_2_ ^ 0xffffffff) != -8250) {
                                    if (i_2_ == 338) {
                                        is[i_1_] = (byte) -116;
                                    } else if (i_2_ == 381) {
                                        is[i_1_] = (byte) -114;
                                    } else if ((i_2_ ^ 0xffffffff) == -8217) {
                                        is[i_1_] = (byte) -111;
                                    } else if (i_2_ == 8217) {
                                        is[i_1_] = (byte) -110;
                                    } else if (i_2_ != 8220) {
                                        if (i_2_ == 8221) {
                                            is[i_1_] = (byte) -108;
                                        } else if ((i_2_ ^ 0xffffffff) == -8227) {
                                            is[i_1_] = (byte) -107;
                                        } else if ((i_2_ ^ 0xffffffff) != -8212) {
                                            if (i_2_ == 8212) {
                                                is[i_1_] = (byte) -105;
                                            } else if ((i_2_ ^ 0xffffffff) != -733) {
                                                if (i_2_ != 8482) {
                                                    if (i_2_ == 353) {
                                                        is[i_1_] = (byte) -102;
                                                    } else if (i_2_ != 8250) {
                                                        if ((i_2_ ^ 0xffffffff) == -340) {
                                                            is[i_1_] = (byte) -100;
                                                        } else if (i_2_ != 382) {
                                                            if (i_2_ == 376) {
                                                                is[i_1_] = (byte) -97;
                                                            } else {
                                                                is[i_1_] = (byte) 63;
                                                            }
                                                        } else {
                                                            is[i_1_] = (byte) -98;
                                                        }
                                                    } else {
                                                        is[i_1_] = (byte) -101;
                                                    }
                                                } else {
                                                    is[i_1_] = (byte) -103;
                                                }
                                            } else {
                                                is[i_1_] = (byte) -104;
                                            }
                                        } else {
                                            is[i_1_] = (byte) -106;
                                        }
                                    } else {
                                        is[i_1_] = (byte) -109;
                                    }
                                } else {
                                    is[i_1_] = (byte) -117;
                                }
                            } else {
                                is[i_1_] = (byte) -121;
                            }
                        } else {
                            is[i_1_] = (byte) -122;
                        }
                    } else {
                        is[i_1_] = (byte) -123;
                    }
                } else {
                    is[i_1_] = (byte) -128;
                }
            } else {
                is[i_1_] = (byte) i_2_;
            }
        }
        return is;
    }

    public static char method2782(final byte value) {
        int byteChar = 0xff & value;
        if (byteChar == 0)
            throw new IllegalArgumentException("Non cp1252 character 0x"
                    + Integer.toString(byteChar, 16) + " provided");
        if ((byteChar ^ 0xffffffff) <= -129 && byteChar < 160) {
            int i_4_ = aCharArray6385[-128 + byteChar];
            if ((i_4_ ^ 0xffffffff) == -1) {
                i_4_ = 63;
            }
            byteChar = i_4_;
        }
        return (char) byteChar;
    }

    public static int getHashMapSize(int size) {
        size--;
        size |= size >>> -1810941663;
        size |= size >>> 2010624802;
        size |= size >>> 10996420;
        size |= size >>> 491045480;
        size |= size >>> 1388313616;
        return 1 + size;
    }

    /**
     * Walk dirs 0 - South-West 1 - South 2 - South-East 3 - West 4 - East 5 -
     * North-West 6 - North 7 - North-East
     */
    public static int getPlayerWalkingDirection(final int dx, final int dy) {
        if (dx == -1 && dy == -1)
            return 0;
        if (dx == 0 && dy == -1)
            return 1;
        if (dx == 1 && dy == -1)
            return 2;
        if (dx == -1 && dy == 0)
            return 3;
        if (dx == 1 && dy == 0)
            return 4;
        if (dx == -1 && dy == 1)
            return 5;
        if (dx == 0 && dy == 1)
            return 6;
        if (dx == 1 && dy == 1)
            return 7;
        return -1;
    }

    public static int getPlayerRunningDirection(final int dx, final int dy) {
        if (dx == -2 && dy == -2)
            return 0;
        if (dx == -1 && dy == -2)
            return 1;
        if (dx == 0 && dy == -2)
            return 2;
        if (dx == 1 && dy == -2)
            return 3;
        if (dx == 2 && dy == -2)
            return 4;
        if (dx == -2 && dy == -1)
            return 5;
        if (dx == 2 && dy == -1)
            return 6;
        if (dx == -2 && dy == 0)
            return 7;
        if (dx == 2 && dy == 0)
            return 8;
        if (dx == -2 && dy == 1)
            return 9;
        if (dx == 2 && dy == 1)
            return 10;
        if (dx == -2 && dy == 2)
            return 11;
        if (dx == -1 && dy == 2)
            return 12;
        if (dx == 0 && dy == 2)
            return 13;
        if (dx == 1 && dy == 2)
            return 14;
        if (dx == 2 && dy == 2)
            return 15;
        return -1;
    }

    public static byte[] completeQuickMessage(final Player player,
                                              final int fileId, byte[] data) {
        if (fileId == 1) {
            data = new byte[]{(byte) player.getSkills().getLevelForXp(
                    Skills.AGILITY)};
        } else if (fileId == 8) {
            data = new byte[]{(byte) player.getSkills().getLevelForXp(
                    Skills.ATTACK)};
        } else if (fileId == 13) {
            data = new byte[]{(byte) player.getSkills().getLevelForXp(
                    Skills.CONSTRUCTION)};
        } else if (fileId == 16) {
            data = new byte[]{(byte) player.getSkills().getLevelForXp(
                    Skills.COOKING)};
        } else if (fileId == 23) {
            data = new byte[]{(byte) player.getSkills().getLevelForXp(
                    Skills.CRAFTING)};
        } else if (fileId == 30) {
            data = new byte[]{(byte) player.getSkills().getLevelForXp(
                    Skills.DEFENCE)};
        } else if (fileId == 34) {
            data = new byte[]{(byte) player.getSkills().getLevelForXp(
                    Skills.FARMING)};
        } else if (fileId == 41) {
            data = new byte[]{(byte) player.getSkills().getLevelForXp(
                    Skills.FIREMAKING)};
        } else if (fileId == 47) {
            data = new byte[]{(byte) player.getSkills().getLevelForXp(
                    Skills.FISHING)};
        } else if (fileId == 55) {
            data = new byte[]{(byte) player.getSkills().getLevelForXp(
                    Skills.FLETCHING)};
        } else if (fileId == 62) {
            data = new byte[]{(byte) player.getSkills().getLevelForXp(
                    Skills.HERBLORE)};
        } else if (fileId == 70) {
            data = new byte[]{(byte) player.getSkills().getLevelForXp(
                    Skills.HITPOINTS)};
        } else if (fileId == 74) {
            data = new byte[]{(byte) player.getSkills().getLevelForXp(
                    Skills.HUNTER)};
        } else if (fileId == 135) {
            data = new byte[]{(byte) player.getSkills().getLevelForXp(
                    Skills.MAGIC)};
        } else if (fileId == 127) {
            data = new byte[]{(byte) player.getSkills().getLevelForXp(
                    Skills.MINING)};
        } else if (fileId == 120) {
            data = new byte[]{(byte) player.getSkills().getLevelForXp(
                    Skills.PRAYER)};
        } else if (fileId == 116) {
            data = new byte[]{(byte) player.getSkills().getLevelForXp(
                    Skills.RANGE)};
        } else if (fileId == 111) {
            data = new byte[]{(byte) player.getSkills().getLevelForXp(
                    Skills.RUNECRAFTING)};
        } else if (fileId == 103) {
            data = new byte[]{(byte) player.getSkills().getLevelForXp(
                    Skills.SLAYER)};
        } else if (fileId == 96) {
            data = new byte[]{(byte) player.getSkills().getLevelForXp(
                    Skills.SMITHING)};
        } else if (fileId == 92) {
            data = new byte[]{(byte) player.getSkills().getLevelForXp(
                    Skills.STRENGTH)};
        } else if (fileId == 85) {
            data = new byte[]{(byte) player.getSkills().getLevelForXp(
                    Skills.SUMMONING)};
        } else if (fileId == 79) {
            data = new byte[]{(byte) player.getSkills().getLevelForXp(
                    Skills.THIEVING)};
        } else if (fileId == 142) {
            data = new byte[]{(byte) player.getSkills().getLevelForXp(
                    Skills.WOODCUTTING)};
        } else if (fileId == 990) {
            data = new byte[]{(byte) player.getSkills().getLevelForXp(
                    Skills.DUNGEONEERING)};
        } else if (fileId == 965) {
            final int value = player.getHitpoints();
            data = new byte[]{(byte) (value >> 24), (byte) (value >> 16),
                    (byte) (value >> 8), (byte) value};
        } else if (fileId == 1108) {
            final int value = player.getDominionTower().getKilledBossesCount();
            data = new byte[]{(byte) (value >> 24), (byte) (value >> 16),
                    (byte) (value >> 8), (byte) value};
        } else if (fileId == 1109) {
            final long value = player.getDominionTower().getTotalScore();
            data = new byte[]{(byte) (value >> 24), (byte) (value >> 16),
                    (byte) (value >> 8), (byte) value};
        } else if (fileId == 1110) {
            final int value = player.getDominionTower().getMaxFloorClimber();
            data = new byte[]{(byte) (value >> 24), (byte) (value >> 16),
                    (byte) (value >> 8), (byte) value};
        } else if (fileId == 1111) {
            final int value = player.getDominionTower().getMaxFloorEndurance();
            data = new byte[]{(byte) (value >> 24), (byte) (value >> 16),
                    (byte) (value >> 8), (byte) value};
        } else if (fileId == 1134) {
            final int value = player.getCrucibleHighScore();
            data = new byte[]{(byte) (value >> 24), (byte) (value >> 16),
                    (byte) (value >> 8), (byte) value};
        } else if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
            Logger.info("Utils", "qc: " + player.getUsername() + ", "
                    + (data == null ? 0 : data.length));
        }
        return data;
    }

    public static String fixChatMessage(final String message) {
        final StringBuilder newText = new StringBuilder();
        boolean wasSpace = true;
        boolean exception = false;
        for (int i = 0; i < message.length(); i++) {
            if (!exception) {
                if (wasSpace) {
                    newText.append(("" + message.charAt(i)).toUpperCase());
                    if (!String.valueOf(message.charAt(i)).equals(" ")) {
                        wasSpace = false;
                    }
                } else {
                    newText.append(("" + message.charAt(i)).toLowerCase());
                }
            } else {
                newText.append(("" + message.charAt(i)));
            }
            if (String.valueOf(message.charAt(i)).contains(":")) {
                exception = true;
            } else if (String.valueOf(message.charAt(i)).contains(".")
                    || String.valueOf(message.charAt(i)).contains("!")
                    || String.valueOf(message.charAt(i)).contains("?")) {
                wasSpace = true;
            }
        }
        return newText.toString();
    }

    public static String getCompleted(final String[] cmd, final int index) {
        final StringBuilder sb = new StringBuilder();
        for (int i = index; i < cmd.length; i++) {
            if (i == cmd.length - 1 || cmd[i + 1].startsWith("+"))
                return sb.append(cmd[i]).toString();
            sb.append(cmd[i]).append(" ");
        }
        return "null";
    }

    public enum EntityDirection {
        NORTH(8192), SOUTH(0), EAST(12288), WEST(4096), NORTHEAST(10240), SOUTHEAST(
                14366), NORTHWEST(6144), SOUTHWEST(2048);

        private int value;

        EntityDirection(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
