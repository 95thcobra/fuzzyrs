package com.rs.world.npc.godwars.armadyl;

import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Entity;
import com.rs.world.World;
import com.rs.world.WorldTile;
import com.rs.world.item.Item;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.world.npc.godwars.zammorak.GodwarsZammorakFaction;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class GodwarsArmadylFaction extends NPC {

    public GodwarsArmadylFaction(final int id, final WorldTile tile,
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
                            || npc instanceof GodwarsZammorakFaction
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
            if (name.contains("Armadyl Helmet")
                    || name.contains("Armadyl mitre")
                    || name.contains("Armadyl full helm")
                    || name.contains("Armadyl coif")
                    || name.contains("Torva full helm")
                    || name.contains("Pernix cowl")
                    || name.contains("Virtus mask"))
                return true;
            else if (name.contains("Armadyl cloak"))
                return true;
            else if (name.contains("Armadyl pendant")
                    || name.contains("Armadyl stole"))
                return true;
            else if (name.contains("Armadyl godsword")
                    || name.contains("Armadyl crozier")
                    || name.contains("Zaryte Bow"))
                return true;
            else if (name.contains("Armadyl body")
                    || name.contains("Armadyl robe top")
                    || name.contains("Armadyl chestplate")
                    || name.contains("Armadyl platebody")
                    || name.contains("Torva platebody")
                    || name.contains("Pernix body")
                    || name.contains("Virtus robe top"))
                return true;
            else if (name.contains("Illuminated book of law")
                    || name.contains("Book of law")
                    || name.contains("Armadyl kiteshield"))
                return true;
            else if (name.contains("Armadyl robe legs")
                    || name.contains("Armadyl plateskirt")
                    || name.contains("Armadyl chaps")
                    || name.contains("Armadyl platelegs")
                    || name.contains("Armadyl Chainskirt")
                    || name.contains("Torva platelegs")
                    || name.contains("Pernix chaps")
                    || name.contains("Virtus robe legs"))
                return true;
            else if (name.contains("Armadyl vambraces"))
                return true;
        }
        return false;
    }
}
