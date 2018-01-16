package com.rs.core.net.io;

import com.rs.core.utils.Utils;
import com.rs.player.Player;

public final class OutputStream extends Stream {

	private static final int[] BIT_MASK = new int[32];

	static {
		for (int i = 0; i < 32; i++) {
			BIT_MASK[i] = (1 << i) - 1;
		}
	}

	private int opcodeStart = 0;

	public OutputStream(final int capacity) {
		setBuffer(new byte[capacity]);
	}

	public OutputStream() {
		setBuffer(new byte[16]);
	}

	public OutputStream(final byte[] buffer) {
		this.setBuffer(buffer);
		this.offset = buffer.length;
		length = buffer.length;
	}

	public OutputStream(final int[] buffer) {
		setBuffer(new byte[buffer.length]);
		for (final int value : buffer) {
			writeByte(value);
		}
	}

	public void checkCapacityPosition(final int position) {
		if (position >= getBuffer().length) {
			final byte[] newBuffer = new byte[position + 16];
			System.arraycopy(getBuffer(), 0, newBuffer, 0, getBuffer().length);
			setBuffer(newBuffer);
		}
	}

	public void skip(final int length) {
		setOffset(getOffset() + length);
	}

	public void setOffset(final int offset) {
		this.offset = offset;
	}

	public void writeBytes(final byte[] b, final int offset, final int length) {
		checkCapacityPosition(this.getOffset() + length - offset);
		System.arraycopy(b, offset, getBuffer(), this.getOffset(), length);
		this.setOffset(this.getOffset() + (length - offset));
	}

	public void writeBytes(final byte[] b) {
		final int offset = 0;
		final int length = b.length;
		checkCapacityPosition(this.getOffset() + length - offset);
		System.arraycopy(b, offset, getBuffer(), this.getOffset(), length);
		this.setOffset(this.getOffset() + (length - offset));
	}

	public void addBytes128(final byte[] data, final int offset, final int len) {
		for (int k = offset; k < len; k++) {
			writeByte((byte) (data[k] + 128));
		}
	}

	public void addBytesS(final byte[] data, final int offset, final int len) {
		for (int k = offset; k < len; k++) {
			writeByte((byte) (-128 + data[k]));
		}
	}

	public void addBytes_Reverse(final byte[] data, final int offset,
			final int len) {
		for (int i = len - 1; i >= 0; i--) {
			writeByte((data[i]));
		}
	}

	public void addBytes_Reverse128(final byte[] data, final int offset,
			final int len) {
		for (int i = len - 1; i >= 0; i--) {
			writeByte((byte) (data[i] + 128));
		}
	}

	public void writeByte(final int i) {
		writeByte(i, offset++);
	}

	public void writeNegativeByte(final int i) {
		writeByte(-i, offset++);
	}

	public void writeByte(final int i, final int position) {
		checkCapacityPosition(position);
		getBuffer()[position] = (byte) i;
	}

	public void writeByte128(final int i) {
		writeByte(i + 128);
	}

	public void writeByteC(final int i) {
		writeByte(-i);
	}

	public void write128Byte(final int i) {
		writeByte(128 - i);
	}

	public void writeShortLE128(final int i) {
		writeByte(i + 128);
		writeByte(i >> 8);
	}

	public void writeShort128(final int i) {
		writeByte(i >> 8);
		writeByte(i + 128);
	}

	public void writeSmart(final int i) {
		if (i >= 128) {
			writeShort(i + 32768);
		} else {
			writeByte(i);
		}
	}

	public void writeBigSmart(final int i) {
		if (i >= Short.MAX_VALUE) {
			writeInt(i - Integer.MAX_VALUE - 1);
		} else {
			writeShort(i >= 0 ? i : 32767);
		}
	}

	public void writeShort(final int i) {
		writeByte(i >> 8);
		writeByte(i);
	}

	public void writeShortLE(final int i) {
		writeByte(i);
		writeByte(i >> 8);
	}

	public void write24BitInteger(final int i) {
		writeByte(i >> 16);
		writeByte(i >> 8);
		writeByte(i);
	}

	public void write24BitIntegerV2(final int i) {
		writeByte(i >> 16);
		writeByte(i);
		writeByte(i >> 8);
	}

	public void writeInt(final int i) {
		writeByte(i >> 24);
		writeByte(i >> 16);
		writeByte(i >> 8);
		writeByte(i);
	}

	public void writeIntV1(final int i) {
		writeByte(i >> 8);
		writeByte(i);
		writeByte(i >> 24);
		writeByte(i >> 16);
	}

	public void writeIntV2(final int i) {
		writeByte(i >> 16);
		writeByte(i >> 24);
		writeByte(i);
		writeByte(i >> 8);
	}

	public void writeIntLE(final int i) {
		writeByte(i);
		writeByte(i >> 8);
		writeByte(i >> 16);
		writeByte(i >> 24);
	}

	public void writeLong(final long l) {
		writeByte((int) (l >> 56));
		writeByte((int) (l >> 48));
		writeByte((int) (l >> 40));
		writeByte((int) (l >> 32));
		writeByte((int) (l >> 24));
		writeByte((int) (l >> 16));
		writeByte((int) (l >> 8));
		writeByte((int) l);
	}

	public void writePSmarts(final int i) {
		if (i < 128) {
			writeByte(i);
			return;
		}
		if (i < 32768) {
			writeShort(32768 + i);
			return;
		} else {
			System.out.println("Error psmarts out of range:");
			return;
		}
	}

	public void writeString(final String s) {
		checkCapacityPosition(getOffset() + s.length() + 1);
		System.arraycopy(s.getBytes(), 0, getBuffer(), getOffset(), s.length());
		setOffset(getOffset() + s.length());
		writeByte(0);
	}

	public void writeGJString2(final String string) {
		final byte[] packed = new byte[256];
		final int length = Utils.packGJString2(0, packed, string);
		writeByte(0);
		writeBytes(packed, 0, length);
		writeByte(0);
	}

	public void writeGJString(final String s) {
		writeByte(0);
		writeString(s);
	}

	public void putGJString3(final String s) {
		writeByte(0);
		writeString(s);
		writeByte(0);
	}

	public void writePacket(final Player player, final int id) {
		if (player == null) {
			writeSmart(id);
		} else if (id >= 128) {
			writeByte((id >> 8) + 128
					+ player.getIsaacKeyPair().outKey().getNextValue());
			writeByte(id + player.getIsaacKeyPair().outKey().getNextValue());
		} else {
			writeByte(id + player.getIsaacKeyPair().outKey().getNextValue());
		}
	}

	public void writePacketVarByte(final Player player, final int id) {
		writePacket(player, id);
		writeByte(0);
		opcodeStart = getOffset() - 1;
	}

	public void writePacketVarShort(final Player player, final int id) {
		writePacket(player, id);
		writeShort(0);
		opcodeStart = getOffset() - 2;
	}

	public void endPacketVarByte() {
		writeByte(getOffset() - (opcodeStart + 2) + 1, opcodeStart);
	}

	public void endPacketVarShort() {
		final int size = getOffset() - (opcodeStart + 2);
		writeByte(size >> 8, opcodeStart++);
		writeByte(size, opcodeStart);
	}

	public void initBitAccess() {
		bitPosition = getOffset() * 8;
	}

	public void finishBitAccess() {
		setOffset((bitPosition + 7) / 8);
	}

	public int getBitPos(final int i) {
		return 8 * i - bitPosition;
	}

	public void writeBits(int numBits, final int value) {
		int bytePos = bitPosition >> 3;
		int bitOffset = 8 - (bitPosition & 7);
		bitPosition += numBits;
		for (; numBits > bitOffset; bitOffset = 8) {
			checkCapacityPosition(bytePos);
			getBuffer()[bytePos] &= ~BIT_MASK[bitOffset];
			getBuffer()[bytePos++] |= value >> numBits - bitOffset
					& BIT_MASK[bitOffset];
			numBits -= bitOffset;
		}
		checkCapacityPosition(bytePos);
		if (numBits == bitOffset) {
			getBuffer()[bytePos] &= ~BIT_MASK[bitOffset];
			getBuffer()[bytePos] |= value & BIT_MASK[bitOffset];
		} else {
			getBuffer()[bytePos] &= ~(BIT_MASK[numBits] << bitOffset - numBits);
			getBuffer()[bytePos] |= (value & BIT_MASK[numBits]) << bitOffset
					- numBits;
		}
	}

	public void setBuffer(final byte[] buffer) {
		this.buffer = buffer;
	}

}