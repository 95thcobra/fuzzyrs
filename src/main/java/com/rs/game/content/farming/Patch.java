/**
 * @author Zephyr. tense0087 - skype
 */
package com.rs.game.content.farming;

/**
 * @author Zephyr. tense0087 - skype
 */
public class Patch {
	
	public boolean IsComposted, IsScomposted;
	private int progress;
	public int start, limit;
	public boolean IsCorrupted;
	public int configId;
	
	public Patch(int start, int limit, int configId) {
		IsComposted = false;
		IsScomposted = false;
		progress = 0;
		this.start = start;
		this.limit = limit;
		this.configId = configId;
	}
	
	
	public int GetProgress() {
		return progress;
	}
	
	public void SetProgress(int progress) {
		this.progress = progress;
	}

}
