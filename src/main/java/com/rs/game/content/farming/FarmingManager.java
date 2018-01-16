/**
 * @author Zephyr. tense0087 - skype
 */
package com.rs.game.content.farming;

import java.io.Serializable;
import java.util.ArrayList;

import com.rs.game.player.Player;

/**
 * @author Zephyr. tense0087 - skype
 */
public class FarmingManager implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Patch> Patches;
	public Player player;
	
	
	public FarmingManager() {
		if(Patches == null)
			Patches = new ArrayList<>();
	}
	
	
	public void SetPlayer(Player player) {
		this.player = player;
	}
	
	public void OnLogin() {
		for(Patch patch : Patches) {
			player.getPackets().sendConfigByFile(patch.configId, patch.GetProgress());
		}
	}

}
