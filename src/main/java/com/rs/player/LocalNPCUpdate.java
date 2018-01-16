package com.rs.player;

import com.rs.core.net.io.OutputStream;
import com.rs.core.settings.GameConstants;
import com.rs.core.utils.Utils;
import com.rs.world.Hit;
import com.rs.world.World;
import com.rs.world.npc.NPC;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class LocalNPCUpdate {

	private final Player player;
	private final LinkedList<NPC> localNPCs;

	public LocalNPCUpdate(final Player player) {
		this.player = player;
		localNPCs = new LinkedList<NPC>();
	}

	public void reset() {
		localNPCs.clear();
	}

	public OutputStream createPacketAndProcess() {
		final boolean largeSceneView = player.hasLargeSceneView();
		final OutputStream stream = new OutputStream();
		final OutputStream updateBlockData = new OutputStream();
		stream.writePacketVarShort(player, largeSceneView ? 64 : 72);
		processLocalNPCsInform(stream, updateBlockData, largeSceneView);
		stream.writeBytes(updateBlockData.getBuffer(), 0,
				updateBlockData.getOffset());
		stream.endPacketVarShort();
		return stream;
	}

	private void processLocalNPCsInform(final OutputStream stream,
			final OutputStream updateBlockData, final boolean largeSceneView) {
		stream.initBitAccess();
		processInScreenNPCs(stream, updateBlockData, largeSceneView);
		addInScreenNPCs(stream, updateBlockData, largeSceneView);
		if (updateBlockData.getOffset() > 0) {
			stream.writeBits(15, 32767);
		}
		stream.finishBitAccess();
	}

	private void processInScreenNPCs(final OutputStream stream,
			final OutputStream updateBlockData, final boolean largeSceneView) {
		stream.writeBits(8, localNPCs.size());
		// for (NPC n : localNPCs.toArray(new NPC[localNPCs.size()])) {
		for (final Iterator<NPC> it = localNPCs.iterator(); it.hasNext();) {
			final NPC n = it.next();
			if (n.hasFinished()
					|| !n.withinDistance(player, largeSceneView ? 126 : 14)
					|| n.hasTeleported()) {
				stream.writeBits(1, 1);
				stream.writeBits(2, 3);
				it.remove();
				continue;
			}
			final boolean needUpdate = n.needMasksUpdate();
			final boolean walkUpdate = n.getNextWalkDirection() != -1;
			stream.writeBits(1, (needUpdate || walkUpdate) ? 1 : 0);
			if (walkUpdate) {
				stream.writeBits(2, n.getNextRunDirection() == -1 ? 1 : 2);
				if (n.getNextRunDirection() != -1) {
					stream.writeBits(1, 1);
				}
				stream.writeBits(3,
						Utils.getNpcMoveDirection(n.getNextWalkDirection()));
				if (n.getNextRunDirection() != -1) {
					stream.writeBits(3,
							Utils.getNpcMoveDirection(n.getNextRunDirection()));
				}
				stream.writeBits(1, needUpdate ? 1 : 0);
			} else if (needUpdate) {
				stream.writeBits(2, 0);
			}
			if (needUpdate) {
				appendUpdateBlock(n, updateBlockData, false);
			}
		}
	}

	private void addInScreenNPCs(final OutputStream stream,
			final OutputStream updateBlockData, final boolean largeSceneView) {
		for (final int regionId : player.getMapRegionsIds()) {
			final List<Integer> indexes = World.getRegion(regionId)
					.getNPCsIndexes();
			if (indexes == null) {
				continue;
			}
			for (final int npcIndex : indexes) {
				if (localNPCs.size() == GameConstants.LOCAL_NPCS_LIMIT) {
					break;
				}
				final NPC n = World.getNPCs().get(npcIndex);
				if (n == null || n.hasFinished() || localNPCs.contains(n)
						|| !n.withinDistance(player, largeSceneView ? 126 : 14)
						|| n.isDead()) {
					continue;
				}
				stream.writeBits(15, n.getIndex());
				final boolean needUpdate = n.needMasksUpdate()
						|| n.getLastFaceEntity() != -1;
				int x = n.getX() - player.getX();
				int y = n.getY() - player.getY();
				if (largeSceneView) {
					if (x < 127) {
						x += 256;
					}
					if (y < 127) {
						y += 256;
					}
				} else {
					if (x < 15) {
						x += 32;
					}
					if (y < 15) {
						y += 32;
					}
				}
				stream.writeBits(3, (n.getDirection() >> 11) - 4);
				stream.writeBits(largeSceneView ? 8 : 5, y);
				stream.writeBits(15, n.getId());
				stream.writeBits(largeSceneView ? 8 : 5, x);
				stream.writeBits(2, n.getPlane());
				stream.writeBits(1, needUpdate ? 1 : 0);
				stream.writeBits(1, n.hasTeleported() ? 1 : 0);
				localNPCs.add(n);
				if (needUpdate) {
					appendUpdateBlock(n, updateBlockData, true);
				}
			}
		}
	}

	private void appendUpdateBlock(final NPC n, final OutputStream data,
			final boolean added) {
		int maskData = 0;
		if (n.getNextForceTalk() != null) {
			maskData |= 0x20;
		}
		if (n.getNextFaceEntity() != -2
				|| (added && n.getLastFaceEntity() != -1)) {
			maskData |= 0x10;
		}
		if (n.getNextFaceWorldTile() != null && n.getNextRunDirection() == -1
				&& n.getNextWalkDirection() == -1) {
			maskData |= 0x80;
		}
		if (n.getNextAnimation() != null) {
			maskData |= 0x8;
		}
		// Change render animation id mask 0x200
		if (!n.getNextHits().isEmpty()) {
			maskData |= 0x4;
		}
		if (n.getNextGraphics4() != null) {
			maskData |= 0x2000000;
		}
		if (n.getNextGraphics1() != null) {
			maskData |= 0x40;
		}
		if (n.getNextGraphics2() != null) {
			maskData |= 0x100;
		}
		if (n.getNextForceMovement() != null) {
			maskData |= 0x800;
		}
		if (n.getNextGraphics3() != null) {
			maskData |= 0x1000000;
		}
		if (n.hasChangedName() || (added && n.getCustomName() != null)) {
			maskData |= 0x800000;
		}
		if (n.hasChangedCombatLevel()
				|| (added && n.getCustomCombatLevel() >= 0)) {
			maskData |= 0x40000;
		}
		if (n.getNextTransformation() != null) {
			maskData |= 0x1;
		}
		if (maskData > 0xff) {
			maskData |= 0x2;
		}
		if (maskData > 0xffff) {
			maskData |= 0x8000;
		}
		if (maskData > 0xffffff) {
			maskData |= 0x10000;
		}

		data.writeByte(maskData);

		if (maskData > 0xff) {
			data.writeByte(maskData >> 8);
		}
		if (maskData > 0xffff) {
			data.writeByte(maskData >> 16);
		}
		if (maskData > 0xffffff) {
			data.writeByte(maskData >> 24);
		}

		if (n.getNextForceTalk() != null) {
			applyForceTalkMask(n, data);
		}
		if (n.getNextFaceEntity() != -2
				|| (added && n.getLastFaceEntity() != -1)) {
			applyFaceEntityMask(n, data);
		}
		if (n.getNextFaceWorldTile() != null) {
			applyFaceWorldTileMask(n, data);
		}
		if (n.getNextAnimation() != null) {
			applyAnimationMask(n, data);
		}
		// Change render animation id mask 0x200
		if (!n.getNextHits().isEmpty()) {
			applyHitMask(n, data);
		}
		if (n.getNextGraphics4() != null) {
			applyGraphicsMask4(n, data);
		}
		if (n.getNextGraphics1() != null) {
			applyGraphicsMask1(n, data);
		}
		if (n.getNextGraphics2() != null) {
			applyGraphicsMask2(n, data);
		}
		if (n.getNextForceMovement() != null) {
			applyForceMovementMask(n, data);
		}
		if (n.getNextGraphics3() != null) {
			applyGraphicsMask3(n, data);
		}
		if (n.hasChangedName() || (added && n.getCustomName() != null)) {
			applyNameChangeMask(n, data);
		}
		if (n.hasChangedCombatLevel()
				|| (added && n.getCustomCombatLevel() >= 0)) {
			applyChangeLevelMask(n, data);
		}
		if (n.getNextTransformation() != null) {
			applyTransformationMask(n, data);
		}
	}

	private void applyChangeLevelMask(final NPC n, final OutputStream data) {
		data.writeShortLE(n.getCombatLevel());
	}

	private void applyNameChangeMask(final NPC npc, final OutputStream data) {
		data.writeString(npc.getName());
	}

	private void applyTransformationMask(final NPC n, final OutputStream data) {
		data.writeBigSmart(n.getNextTransformation().getToNPCId());
	}

	private void applyForceTalkMask(final NPC n, final OutputStream data) {
		data.writeString(n.getNextForceTalk().getText());
	}

	private void applyForceMovementMask(final NPC n, final OutputStream data) {
		data.write128Byte(n.getNextForceMovement().getToFirstTile().getX()
				- n.getX());
		data.writeByte(n.getNextForceMovement().getToFirstTile().getY()
				- n.getY());
		data.writeByteC(n.getNextForceMovement().getToSecondTile() == null ? 0
				: n.getNextForceMovement().getToSecondTile().getX() - n.getX());
		data.writeByteC(n.getNextForceMovement().getToSecondTile() == null ? 0
				: n.getNextForceMovement().getToSecondTile().getY() - n.getY());
		data.writeShortLE((n.getNextForceMovement().getFirstTileTicketDelay() * 600) / 20);
		data.writeShortLE128(n.getNextForceMovement().getToSecondTile() == null ? 0
				: ((n.getNextForceMovement().getSecondTileTicketDelay() * 600) / 20));
		data.writeShort128(n.getNextForceMovement().getDirection());
	}

	private void applyFaceWorldTileMask(final NPC n, final OutputStream data) {
		data.writeShort128((n.getNextFaceWorldTile().getX() << 1) + 1);
		data.writeShortLE128((n.getNextFaceWorldTile().getY() << 1) + 1);
	}

	private void applyHitMask(final NPC n, final OutputStream data) {
		final int count = n.getNextHits().size();
		data.writeByteC(count);
		if (count > 0) {
			int hp = n.getHitpoints();
			final int maxHp = n.getMaxHitpoints();
			if (hp > maxHp) {
				hp = maxHp;
			}
			final int hpBarPercentage = hp * 255 / maxHp;
			for (final Hit hit : n.getNextHits()) {
				final boolean interactingWith = hit.interactingWith(player, n);
				if (hit.getDamage() < 0) {
					hit.setDamage(0);
				}
				if (hit.missed() && !interactingWith) {
					data.writeSmart(32766);
				} else {
					if (hit.getSoaking() != null) {
						data.writeSmart(32767);
						data.writeSmart(hit.getMark(player, n));
						data.writeSmart(hit.getDamage());
						data.writeSmart(hit.getSoaking().getMark(player, n));
						data.writeSmart(hit.getSoaking().getDamage());
					} else {
						data.writeSmart(hit.getMark(player, n));
						data.writeSmart(hit.getDamage());
					}
				}
				data.writeSmart(hit.getDelay());
				data.writeByte128(hpBarPercentage);
			}
		}
	}

	private void applyFaceEntityMask(final NPC n, final OutputStream data) {
		data.writeShortLE128(n.getNextFaceEntity() == -2 ? n
				.getLastFaceEntity() : n.getNextFaceEntity());
	}

	private void applyAnimationMask(final NPC n, final OutputStream data) {
		for (final int id : n.getNextAnimation().getIds()) {
			data.writeBigSmart(id);
		}
		data.writeByte(n.getNextAnimation().getSpeed());
	}

	private void applyGraphicsMask4(final NPC n, final OutputStream data) {
		data.writeShort128(n.getNextGraphics4().getId());
		data.writeInt(n.getNextGraphics4().getSettingsHash());
		data.writeByte128(n.getNextGraphics4().getSettings2Hash());
	}

	private void applyGraphicsMask3(final NPC n, final OutputStream data) {
		data.writeShortLE128(n.getNextGraphics3().getId());
		data.writeInt(n.getNextGraphics3().getSettingsHash());
		data.write128Byte(n.getNextGraphics3().getSettings2Hash());
	}

	private void applyGraphicsMask2(final NPC n, final OutputStream data) {
		data.writeShort128(n.getNextGraphics2().getId());
		data.writeIntLE(n.getNextGraphics2().getSettingsHash());
		data.writeByteC(n.getNextGraphics2().getSettings2Hash());
	}

	private void applyGraphicsMask1(final NPC n, final OutputStream data) {
		data.writeShortLE128(n.getNextGraphics1().getId());
		data.writeIntV1(n.getNextGraphics1().getSettingsHash());
		data.write128Byte(n.getNextGraphics1().getSettings2Hash());
	}

}
