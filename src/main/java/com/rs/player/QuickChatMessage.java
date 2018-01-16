package com.rs.player;

public class QuickChatMessage extends PublicChatMessage {

	private int fileId;

	public QuickChatMessage(final int fileId, final byte[] data) {
		super(data == null ? null : new String(data), 0x8000);
		this.fileId = fileId;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(final int fileId) {
		this.fileId = fileId;
	}

}
