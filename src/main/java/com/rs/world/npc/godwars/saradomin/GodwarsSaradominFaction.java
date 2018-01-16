package com.rs.world.npc.godwars.saradomin;

import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Entity;
import com.rs.world.World;
import com.rs.world.WorldTile;
import com.rs.world.item.Item;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.NPCCombatDefinitions;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class GodwarsSaradominFaction extends NPC {

    public GodwarsSaradominFaction(final int id, final WorldTile tile,
                                   final int mapAreaNameHash, final boolean canBeAttackFromOutOfArea,
                                   final boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
    }

    @Override
    public ArrayList<Entity> getPossibleTargets() {
        final ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
        for (final int regionId : getMapRegionsIds()) {
            final List<Integer> playerIndexes = World.getRegion(regionId)
                    .getPlayerIndexes();
            if (playerIndexes != null) {
                for (final int npcIndex : playerIndexes) {
                    final Player player = World.getPlayers().get(npcIndex);
                    if (player == null
                            || player.isDead()
                            || player.hasFinished()
                            || !player.isRunning()
                            || !player
                            .withinDistance(
                                    this,
                                    getCombatDefinitions()
                                            .getAttackStyle() == NPCCombatDefinitions.MELEE ? 4
                                            : getCombatDefinitions()
                                            .getAttackStyle() == NPCCombatDefinitions.SPECIAL ? 16
                                            : 8)
                            || ((!isAtMultiArea() || !player.isAtMultiArea())
                            && player.getAttackedBy() != this && player
                            .getAttackedByDelay() > Utils
                            .currentTimeMillis())
                            || !clipedProjectile(player, false)
                            || !hasGodItem(player)) {
                        continue;
                    }
                    possibleTarget.add(player);
                }
            }
            final List<Integer> npcsIndexes = World.getRegion(regionId)
                    .getNPCsIndexes();
            if (npcsIndexes != null) {
                for (final int npcIndex : npcsIndexes) {
                    final NPC npc = World.getNPCs().get(npcIndex);
                    if (npc == null
                            || npc == this
                            || npc instanceof GodwarsSaradominFaction
                            || npc.isDead()
                            || npc.hasFinished()
                            || !npc.withinDistance(
                            this,
                            getCombatDefinitions().getAttackStyle() == NPCCombatDefinitions.MELEE ? 4
                                    : getCombatDefinitions()
                                    .getAttackStyle() == NPCCombatDefinitions.SPECIAL ? 16
                                    : 8)
                            || !npc.getDefinitions().hasAttackOption()
                            || ((!isAtMultiArea() || !npc.isAtMultiArea())
                            && npc.getAttackedBy() != this && npc
                            .getAttackedByDelay() > Utils
                            .currentTimeMillis())
                            || !clipedProjectile(npc, false)) {
                        continue;
                    }
                    possibleTarget.add(npc);
                }
            }
        }
        return possibleTarget;
    }

    private boolean hasGodItem(final Player player) {
        for (final Item item : player.getEquipment().getItems().getItems()) {
            if (item == null) {
                continue; // shouldn't happen
            }
            final String name = item.getDefinitions().getName();
            // using else as only one item should count
            if (name.contains("Saradomin coif")
                    || name.contains("Citharede hood")
                    || name.contains("Saradomin mitre")
                    || name.contains("Saradomin full helm")
                    || name.contains("Saradomin halo")
                    || name.contains("Torva full helm")
                    || name.contains("Pernix cowl")
                    || name.contains("Virtus mask"))
                return true;
            else if (name.contains("Saradomin cape")
                    || name.contains("Saradomin cloak"))
                return true;
            else if (name.contains("Holy symbol")
                    || name.contains("Citharede symbol")
                    || name.contains("Saradomin stole"))
                return true;
            else if (name.contains("Saradomin arrow"))
                return true;
            else if (name.contains("Saradomin godsword")
                    || name.contains("Saradomin sword")
                    || name.contains("Saradomin staff")
                    || name.contains("Saradomin crozier")
                    || name.contains("Zaryte Bow"))
                return true;
            else if (name.contains("Saradomin robe top")
                    || name.contains("Saradomin d'hide")
                    || name.contains("Citharede robe top")
                    || name.contains("Monk's robe top")
                    || name.contains("Saradomin platebody")
                    || name.contains("Torva platebody")
                    || name.contains("Pernix body")
                    || name.contains("Virtus robe top"))
                return true;
            else if (name.contains("Illuminated holy book")
                    || name.contains("Holy book")
                    || name.contains(" Saradomin kiteshield"))
                return true;
        }
        return false;
    }
}
