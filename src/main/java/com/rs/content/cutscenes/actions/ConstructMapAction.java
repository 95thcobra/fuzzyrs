package com.rs.content.cutscenes.actions;

import com.rs.player.Player;
import com.rs.content.cutscenes.Cutscene;

public class ConstructMapAction extends CutsceneAction {

	private final int baseChunkX, baseChunkY, widthChunks, heightChunks;

	public ConstructMapAction(final int baseChunkX, final int baseChunkY,
			final int widthChunks, final int heightChunks) {
		super(-1, -1);
		this.baseChunkX = baseChunkX;
		this.baseChunkY = baseChunkY;
		this.widthChunks = widthChunks;
		this.heightChunks = heightChunks;
	}

	@Override
	public void process(final Player player, final Object[] cache) {
		final Cutscene scene = (Cutscene) cache[0];
		scene.constructArea(player, baseChunkX, baseChunkY, widthChunks,
				heightChunks);
	}
}
