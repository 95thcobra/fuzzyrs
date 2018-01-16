package com.rs.player;

public class PublicChatMessage {

	private final String message;
	private final int effects;

	public PublicChatMessage(final String message, final int effects) {
		this.message = message;
		this.effects = effects;
	}

	public String getMessage() {
		return message;
	}

	public int getEffects() {
		return effects;
	}

}
