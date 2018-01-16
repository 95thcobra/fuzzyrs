package com.rs.core.utils.tools;

import com.rs.core.utils.Utils;

public class MapUtils {

    public static Area getArea(final int minX, final int minY, final int maxX,
                               final int maxY) {
        return getArea(Structure.TILE, minX, minY, maxX, maxY);
    }

    public static Area getArea(final Structure structure, final int minX,
                               final int minY, final int maxX, final int maxY) {
        return new Area(structure, minX, minY, maxX - minY, maxY - minY);
    }

    /*
     * returns converted area
     */
    public static Area convert(final Structure to, final Area area) {
        final int x = area.getMapX() / to.getWidth();
        final int y = area.getMapY() / to.getHeight();
        final int width = area.getMapWidth() / to.getWidth();
        final int height = area.getMapHeight() / to.getHeight();
        return new Area(to, x, y, width, height);
    }

    /*
     * converted pos return converted x and y
     */
    public static int[] convert(final Structure from, final Structure to,
                                final int x, final int y) {
        return new int[]{x * from.getWidth() / to.getWidth(),
                y * from.getHeight() / to.getHeight()};
    }

    public static int getHash(final Structure structure, final int x,
                              final int y, final int plane) {
        return structure.getHash(x, y, plane);
    }

    public int getHash(final Structure structure, final int x, final int y) {
        return getHash(structure, x, y, 0);
    }

    public enum Structure {

        TILE(null, 1, 1, new StructureHash() {
            @Override
            public int generateHash(final int x, final int y, final int plane) {
                return y | (x << 14) | (plane << 28);
            }
        }), CHUNK(TILE, 8, 8, new StructureHash() {
            @Override
            public int generateHash(final int x, final int y, final int plane) {
                return (x << 14) | (y << 3) | (plane << 24);
            }
        }), REGION(CHUNK, 8, 8, new StructureHash() {
            @Override
            public int generateHash(final int x, final int y, final int plane) {
                return ((x << 8) | y | (plane << 16));
            }
        }), MAP(REGION, 255, 255);

        private final Structure child;
        private final int width, height;
        private final StructureHash hash;

		/*
         * width * height squares. For instance 4x4: S S S S S S S S S S S S
		 */

        Structure(final Structure child, final int width,
                  final int height, final StructureHash hash) {
            this.child = child;
            this.width = width;
            this.height = height;
            this.hash = hash;
        }

        Structure(final Structure child, final int width,
                  final int height) {
            this(child, width, height, null);
        }

        public int getWidth() {
            int x = width;
            Structure nextChild = child;
            while (nextChild != null) {
                x *= nextChild.width;
                nextChild = nextChild.child;
            }
            return x;
        }

        public int getChildWidth() {
            return width;
        }

        public int getHeight() {
            int y = height;
            Structure nextChild = child;
            while (nextChild != null) {
                y *= nextChild.height;
                nextChild = nextChild.child;
            }
            return y;
        }

        public int getHash(final int x, final int y) {
            return getHash(x, y, 0);
        }

        public int getHash(final int x, final int y, final int plane) {
            return hash == null ? -1 : hash.generateHash(x, y, plane);
        }

        public int getChildHeight() {
            return width;
        }

        @Override
        public String toString() {
            return Utils.formatPlayerNameForDisplay(name());
        }
    }

    private interface StructureHash {

        int generateHash(int x, int y, int plane);

    }

    public static final class Area {

        private final Structure structure;
        private final int x, y, width, height;

        public Area(final Structure structure, final int x, final int y,
                    final int width, final int height) {
            this.structure = structure;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getMapX() {
            return x * structure.getWidth();
        }

        public int getMapY() {
            return y * structure.getHeight();
        }

        public int getMapWidth() {
            return width * structure.getWidth();
        }

        public int getMapHeight() {
            return height * structure.getHeight();
        }

        public Structure getStructure() {
            return structure;
        }

        @Override
        public int hashCode() {
            return structure.getHash(x, y, 0);
        }

        @Override
        public String toString() {
            return "Structure: " + structure.toString() + ", x: " + x + ", y: "
                    + y + ", width: " + width + ", height: " + height;
        }
    }

}
