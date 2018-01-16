package com.rs.content.minigames.soulwars;

import com.rs.content.actions.skills.Skills;
import com.rs.content.player.points.PlayerPoints;
import com.rs.core.net.decoders.impl.WorldPacketsDecoder;
import com.rs.core.utils.Utils;
import com.rs.player.controlers.Controller;
import com.rs.world.World;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;
import com.rs.world.item.Item;
import com.rs.world.npc.NPC;

/**
 * @author John (FuzzyAvacado) on 1/6/2016.
 */
public class SoulWarsAreaController extends Controller {

    private static final int XP_MODIFIER = 2;

    private final static int[] SKILLS = {Skills.SLAYER, Skills.HITPOINTS,
            Skills.DEFENCE, Skills.STRENGTH, Skills.RANGE, Skills.ATTACK,
            Skills.PRAYER, Skills.MAGIC};

    // added by james
    private final static Item[][] GAMBLE_REWARDS = {
            { //COMMON
                    new Item(995, 20000000 + Utils.getRandom(25000000)),
                    new Item(18831, 100 + Utils.getRandom(200)),
                    new Item(15260, 1 + Utils.getRandom(3)),
                    new Item(527, 1000 + Utils.getRandom(5000)),
                    new Item(990, 2 + Utils.getRandom(4)),
                    new Item(1464),
                    new Item(2, 1000 + Utils.getRandom(10000)),
                    new Item(6730, 200 + Utils.getRandom(400)),
            },
            { //UNCOMMON

                    new Item(21465), new Item(21466),
                    new Item(21470), new Item(21471),
                    new Item(21475), new Item(21476),
            },
            { //RARE
                    // vanguard
                    new Item(21473), new Item(21474), new Item(21472),
                    // trickster
                    new Item(21467), new Item(21468), new Item(21469),
                    // battlemage
                    new Item(21462), new Item(21463), new Item(21464),

            },

            { //HOLY SHIT RARE AS NIGGERSHIT HOLY SHIT
                    new Item(22408),
            }
    };

    private final int[] CHARMS = {12158, 12159, 12160, 12163};

    public static boolean isInArea(WorldTile tile) {
        return (tile.getY() <= 3186 && tile.getY() >= 3151) && (tile.getX() >= 1864 && tile.getX() <= 1914);
    }

    @Override
    public void start() {
        player.getMusicsManager().playMusic(597);
        ((AreaTask) World.getSoulWars().getTasks().get(SoulWarsManager.PlayerType.OUTSIDE_LOBBY)).getPlayers().add(player);
        sendInterfaces();
    }

    @Override
    public void sendInterfaces() {
        player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 34 : 10, 199);
    }

    @Override
    public void forceClose() {
        player.getPackets().closeInterface(
                player.getInterfaceManager().hasRezizableScreen() ? 34 : 10);
        ((AreaTask) World.getSoulWars().getTasks().get(SoulWarsManager.PlayerType.OUTSIDE_LOBBY)).getPlayers().remove(player);
    }

    @Override
    public void magicTeleported(int type) {
        forceClose();
        removeControler();
    }

    @Override
    public boolean processObjectClick1(WorldObject object) {
        switch (object.getId()) {
            case 42220:
                player.getControllerManager().forceStop();
                player.useStairs(-1, new WorldTile(3082, 3475, 0), 0, 1);
                return false;
            case 42029:
            case 42030:
            case 42031:
                World.getSoulWars().passBarrier(SoulWarsManager.PlayerType.OUTSIDE_LOBBY, player, object);
                //player.getPackets().sendGameMessage("This option is currently disabled, please use the Balance Portal.");
                return false;
            //case 42029:
            //case 42030:
            //case 42031:
            //World.getSoulWars().passBarrier(SoulWarsManager.PlayerType.OUTSIDE_LOBBY, player, object);
            //return false;
        }
        return true;
    }

    @Override
    public boolean processNPCClick1(NPC npc) {
        if (npc.getId() == 8526) {
            sendShop();
            return false;
        }
        return true;
    }

    @Override
    public boolean processNPCClick2(NPC npc) {
        if (npc.getId() == 8526) {
            sendShop();
            return false;
        }
        return true;
    }

    @Override
    public boolean processButtonClick(int interfaceId, int componentId, int slotId, int packetId) {
        if (interfaceId == 276) {
            switch (componentId) {
                case 8:
                    switch (packetId) {
                        case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
                            player.getPackets().sendGameMessage("For every 2 zeals you exchange, you get a random reward!");
                            break;
                        case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
                            if (checkZeals(2, false)) {
                                if (!player.getInventory().hasFreeSlots()) {
                                    player.getPackets().sendGameMessage("You don't have any space to obain your special reward!");
                                    return false;
                                }
                                checkZeals(2, true);
                                final int foundation = getRandomRewardIndex();
                                final Item reward = GAMBLE_REWARDS[foundation][Utils.random(GAMBLE_REWARDS[foundation].length)];
                                player.getInventory().addItem(reward);
                            }
                            break;
                    }
                    break;
                case 24:
                case 25:
                case 26:
                case 27:
                    switch (packetId) {
                        case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
                            player.getPackets().sendGameMessage("For every 2 zeals you exchange, you receive a random amount of charms.");
                            break;
                        case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
                            if (checkZeals(2, false) && player.getInventory().addItem(CHARMS[componentId - 24], 1 + Utils.getRandom(4)))
                                checkZeals(2, true);
                            break;
                    }
                    break;
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                    switch (packetId) {
                        case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
                            player.getPackets().sendGameMessage("For each zeal you exchange, you receive " + calculateSkillExperience(componentId, 1) + " experience in " + Skills.SKILL_NAME[SKILLS[componentId - 32]].toLowerCase() + ".");
                            break;
                        case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
                            exchangeZealsForXp(componentId, 1);
                            break;
                        case WorldPacketsDecoder.ACTION_BUTTON3_PACKET:
                            exchangeZealsForXp(componentId, 10);
                            break;
                        case WorldPacketsDecoder.ACTION_BUTTON4_PACKET:
                            exchangeZealsForXp(componentId, 100);
                            break;
                    }
                    break;
                default:
                    if (componentId != 22 && componentId != 51 && componentId != 5 && componentId != 21)
                        player.getPackets().sendGameMessage("This option is not added yet!");
                    break;
            }
        }
        return true;
    }

    private int getRandomRewardIndex() {
        final double wheel = Math.random() * 1000D;
        if (wheel < 1D)
            return 3; //super rare
        else if (wheel < 150D)
            return 2; //rare
        else if (wheel < 450D)
            return 1;
        return 0;
    }

    private void exchangeZealsForXp(int componentId, final int zeals) {
        if (checkZeals(zeals, false)) {
            final int xp = calculateSkillExperience(componentId, zeals == 10 ? 11 : zeals == 100 ? 110 : zeals);
            checkZeals(zeals, true);
            player.getPackets().sendConfigByFile(5827, player.getPlayerPoints().getPoints(PlayerPoints.ZEALS));
            player.getTemporaryAttributtes().put("soul_wars_shop_xp", Boolean.TRUE);
            player.getSkills().addXp(SKILLS[componentId - 32], xp);
            player.getPackets().sendGameMessage("You have received " + xp + " experience in " + Skills.SKILL_NAME[SKILLS[componentId - 32]].toLowerCase() + "!");
        }
    }

    private int calculateSkillExperience(int componentId, int zeals) {
        final int skill = SKILLS[componentId - 32];
        final int playerLevel = player.getSkills().getLevelForXp(skill);
        double base;
        switch (skill) {
            case Skills.HITPOINTS:
            case Skills.ATTACK:
            case Skills.STRENGTH:
            case Skills.DEFENCE:
                base = Math.floor(Math.pow(playerLevel, 2) / 600) * 525;
                break;
            case Skills.RANGE:
            case Skills.MAGIC:
                base = Math.floor(Math.pow(playerLevel, 2) / 600) * 480;
                break;
            case Skills.PRAYER:
                base = Math.floor(Math.pow(playerLevel, 2) / 600) * 270;
                break;
            default:
                if (playerLevel <= 30)
                    base = Math.floor(Math.pow(1.1048, playerLevel) * 6.788);
                else
                    base = (Math.floor(Math.pow(playerLevel, 2) / 349) + 1) * 45;
        }
        return (int) base * zeals * XP_MODIFIER;
    }

    private boolean checkZeals(int zeals, boolean remove) {
        if (player.getPlayerPoints().getPoints(PlayerPoints.ZEALS) < zeals) {
            player.getPackets().sendGameMessage("You don't have " + (player.getPlayerPoints().getPoints(PlayerPoints.ZEALS) == 0 ? "any" : "enough") + " zeals to spend!");
            return false;
        } else if (remove) {
            player.getPlayerPoints().removePoints(PlayerPoints.ZEALS, zeals);
            player.getPackets().sendConfigByFile(5827, player.getPlayerPoints().getPoints(PlayerPoints.ZEALS));
        }
        return true;
    }

    @Override
    public boolean login() {
        start();
        return false;
    }

    @Override
    public boolean logout() {
        return false;
    }

    public void sendShop() {
        player.getInterfaceManager().sendInterface(276);
        player.getPackets().sendConfigByFile(5827, player.getPlayerPoints().getPoints(PlayerPoints.ZEALS));
    }
}
