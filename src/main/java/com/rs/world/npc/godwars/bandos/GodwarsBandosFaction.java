package com.rs.world.npc.godwars.bandos;

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
public class GodwarsBandosFaction extends NPC {

    public GodwarsBandosFaction(final int id, final WorldTile tile,
                                final int mapAreaNameHash, final boolean canBeAttackFromOutOfArea,
                                final boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
    }

    @Override
    public boolean checkAgressivity() {
        final NPCCombatDefinitions defs = getCombatDefinitions();
        if (defs.getAgressivenessType() == NPCCombatDefinitions.PASSIVE)
            return false;
        final ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
        for (final int regionId : getMapRegionsIds()) {
            final List<Integer> playerIndexes = World.getRegion(regionId)
                    .getPlayerIndexes();
            if (playerIndexes != null) {
                for (final int npcIndex : playerIndexes) {
                    final Player player = World.getPlayers().get(npcIndex);
                    if (player == null
                            || player.isDead()
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
                            || npc instanceof GodwarsBandosFaction
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
        if (!possibleTarget.isEmpty()) {
            setTarget(possibleTarget
                    .get(Utils.getRandom(possibleTarget.size() - 1)));
            return true;
        }
        return false;
    }

    private boolean hasGodItem(final Player player) {
        for (final Item item : player.getEquipment().getItems().getItems()) {
            if (item == null) {
                continue; // shouldn't happen
            }
            final String name = item.getDefinitions().getName();
            // using else as only one item should count
            if (name.contains("Bandos mitre")
                    || name.contains("Bandos Full helm")
                    || name.contains("Bandos coif")
                    || name.contains("Torva full helm")
                    || name.contains("Pernix cowl")
                    || name.contains("Vitus mask"))
                return true;
            else if (name.contains("Bandos cloak"))
                return true;
            else if (name.contains("Bandos stole"))
                return true;
            else if (name.contains("Ancient mace")
                    || name.contains("Granite mace")
                    || name.contains("Bandos godsword")
                    || name.contains("Bandos crozier")
                    || name.contains("Zaryte Bow"))
                return true;
            else if (name.contains("Bandos body")
                    || name.contains("Bandos robe top")
                    || name.contains("Bandos chestplate")
                    || name.contains("Bandos platebody")
                    || name.contains("Torva platebody")
                    || name.contains("Pernix body")
                    || name.contains("Virtus robe top"))
                return true;
            else if (name.contains("Illuminated book of war")
                    || name.contains("Book of war")
                    || name.contains("Bandos kiteshield"))
                return true;
            else if (name.contains("Bandos robe legs")
                    || name.contains("Bandos tassets")
                    || name.contains("Bandos chaps")
                    || name.contains("Bandos platelegs")
                    || name.contains("Bandos plateskirt")
                    || name.contains("Torva platelegs")
                    || name.contains("Pernix chaps")
                    || name.contains("Virtus robe legs"))
                return true;
            else if (name.contains("Bandos vambraces"))
                return true;
            else if (name.contains("Bandos boots"))
                return true;
        }
        return false;
    }
}
