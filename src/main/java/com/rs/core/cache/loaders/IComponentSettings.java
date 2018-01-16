package com.rs.core.cache.loaders;

public class IComponentSettings {
	public int settings;
	public int anInt4602;

	public boolean method1879(final boolean arg0) {
		return ((0x2eaa42 & settings) >> -1445214219 ^ 0xffffffff) != -1;
	}

	public boolean unlockedSlot(final int slot, final byte arg1) {
		return (settings >> 1 + slot & 0x1 ^ 0xffffffff) != -1;
	}

	public boolean method1881(final int arg0) {
		return ((0x55f65fb5 & settings) >> 1601172318 ^ 0xffffffff) != -1;
	}

	public boolean method1882(final int arg0) {
		return ((settings & 0x1fa3c81f) >> -233371236 ^ 0xffffffff) != -1;
	}

	public boolean method1883(final byte arg0) {
		return (0x1 & settings >> 111606079 ^ 0xffffffff) != -1;
	}

	public boolean method1884(final int arg0) {
		return (0x1 & settings ^ 0xffffffff) != -1;
	}

	public boolean method1885(final byte arg0) {
		return (0x1 & settings >> 1361505174 ^ 0xffffffff) != -1;
	}

	public int method1887(final int arg0) {
		return (settings & 0x1f873d) >> 1483551026;
	}

	public int method1888(final byte arg0) {
		return 0x7f & settings >> -809958741;
	}

	public IComponentSettings(final int arg0, final int arg1) {
		settings = arg0;
		anInt4602 = arg1;
	}
}
