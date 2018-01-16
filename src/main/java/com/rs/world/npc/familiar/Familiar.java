package com.rs.world.npc.familiar;

import com.rs.content.actions.skills.summoning.Summoning.Pouches;
import com.rs.content.potiontimers.PotionTimerInterface;
import com.rs.content.potiontimers.PotionType;
import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.*;
import com.rs.world.item.Item;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

import java.io.Serializable;

public abstract class Familiar extends NPC implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3255206534594320406L;
    private final Pouches pouch;
    private transient Player owner;
    private int ticks;
    private int trackTimer;
    private int specialEnergy;
    private transient boolean finished = false;
    private boolean trackDrain;
    private BeastOfBurden bob;
    private transient int[][] checkNearDirs;
    private transient boolean sentRequestMoveMessage;
    private transient boolean dead;

    public Familiar(final Player owner, final Pouches pouch,
                    final WorldTile tile, final int mapAreaNameHash,
                    final boolean canBeAttackFromOutOfArea) {
        super(pouch.getNpcId(), tile, mapAreaNameHash,
                canBeAttackFromOutOfArea, false);
        this.owner = owner;
        this.pouch = pouch;
        resetTickets();
        owner.getPackets().sendHideIComponent(PotionTimerInterface.POTION_TIMER_INTERFACE_ID, PotionType.SUMMONING.getImageId(), false);
        specialEnergy = 60;
        if (getBOBSize() > 0) {
            bob = new BeastOfBurden(getBOBSize());
        }
        call(true);
    }

    public static void selectLeftOption(final Player player) {
        final boolean res = player.getInterfaceManager().hasRezizableScreen();
        player.getInterfaceManager().sendTab(res ? 119 : 179, 880);
        sendLeftClickOption(player);
        player.getPackets().sendGlobalConfig(168, 8);// tab id
    }

    public static void confirmLeftOption(final Player player) {
        player.getPackets().sendGlobalConfig(168, 4);// inv tab id
        final boolean res = player.getInterfaceManager().hasRezizableScreen();
        player.getPackets().closeInterface(res ? 119 : 179);
    }

    public static void setLeftclickOption(final Player player,
                                          final int summoningLeftClickOption) {
        if (summoningLeftClickOption == player.getSummoningLeftClickOption())
            return;
        player.setSummoningLeftClickOption(summoningLeftClickOption);
        sendLeftClickOption(player);
    }

    public static void sendLeftClickOption(final Player player) {
        player.getPackets().sendConfig(1493, player.getSummoningLeftClickOption());
        player.getPackets().sendConfig(1494, player.getSummoningLeftClickOption());
    }

    public void store() {
        if (bob == null)
            return;
        bob.open();
    }

    public boolean canStoreEssOnly() {
        return pouch.getNpcId() == 6818;
    }

    public int getOriginalId() {
        return pouch.getNpcId();
    }

    public void resetTickets() {
        ticks = (int) (pouch.getTime() / 1000 / 30);
        trackTimer = 0;
    }

    private void sendFollow() {
        setRun(owner.getNextRunDirection() != -1);
        if (getLastFaceEntity() != owner.getClientIndex()) {
            setNextFaceEntity(owner);
        }
        if (getFreezeDelay() >= Utils.currentTimeMillis())
            return; // if freeze cant move ofc
        final int size = getSize();

        final int distanceX = owner.getX() - getX();
        final int distanceY = owner.getY() - getY();
        // if is under
        if (distanceX < size && distanceX > -1 && distanceY < size && distanceY > -1 && !owner.hasWalkSteps() && !hasWalkSteps()) {
            resetWalkSteps();
            if (!addWalkSteps(owner.getX() + 1, getY())) {
                resetWalkSteps();
                if (!addWalkSteps(owner.getX() - size, getY())) {
                    resetWalkSteps();
                    if (!addWalkSteps(getX(), owner.getY() + 1)) {
                        resetWalkSteps();
                        addWalkSteps(getX(), owner.getY() - size);
                    }
                }
            }
            return;
        }

        if ((!clipedProjectile(owner, true)) || distanceX > size
                || distanceX < -1 || distanceY > size || distanceY < -1) {
            resetWalkSteps();
            addWalkStepsInteract(owner.getX(), owner.getY(), getRun() ? 2 : 1,
                    size, true);
        } else {
            resetWalkSteps();
        }

    }

    @Override
    public void processNPC() {
        if (isDead())
            return;
        unlockOrb();
        trackTimer++;
        if (trackTimer == 50) {
            trackTimer = 0;
            ticks--;
            if (trackDrain) {
                owner.getSkills().drainSummoning(1);
            }
            trackDrain = !trackDrain;
            if (ticks == 2) {
                owner.getPackets().sendGameMessage("You have 1 minute before your familiar vanishes.");
            } else if (ticks == 1) {
                owner.getPackets().sendGameMessage("You have 30 seconds before your familiar vanishes.");
            } else if (ticks == 0) {
                removeFamiliar();
                dissmissFamiliar(false);
                owner.sendMessage("Your familiar has vanished.");
                return;
            }
            sendTimeRemaining();
        }
        if (owner.isCanPvp() && getId() != pouch.getNpcId()) {
            transformIntoNPC(pouch.getNpcId());
            call(false);
            return;
        } else if (!owner.isCanPvp() && getId() == pouch.getNpcId()) {
            transformIntoNPC(pouch.getNpcId() - 1);
            call(false);
            return;
        } else if (!withinDistance(owner, 12)) {
            call(false);
            return;
        }
        if (!getCombat().process()) {
            if (isAgressive() && owner.getAttackedBy() != null
                    && owner.getAttackedByDelay() > Utils.currentTimeMillis()
                    && canAttack(owner.getAttackedBy())
                    && Utils.getRandom(25) == 0) {
                getCombat().setTarget(owner.getAttackedBy());
            } else {
                sendFollow();
            }
        }
    }

    public boolean canAttack(final Entity target) {
        if (target instanceof Player) {
            final Player player = (Player) target;
            if (!owner.isCanPvp() || !player.isCanPvp())
                return false;
        }
        return !target.isDead()
                && ((owner.isAtMultiArea() && isAtMultiArea() && target
                .isAtMultiArea()) || (owner.isForceMultiArea() && target
                .isForceMultiArea()))
                && owner.getControllerManager().canAttack(target);
    }

    public boolean renewFamiliar() {
        if (ticks > 5) {
            owner.getPackets().sendGameMessage("You need to have at least two minutes and fifty seconds remaining before you can renew your familiar.", true);
            return false;
        } else if (!owner.getInventory().getItems()
                .contains(new Item(pouch.getPouchId(), 1))) {
            owner.getPackets().sendGameMessage(
                    "You need a "
                            + ItemDefinitions
                            .getItemDefinitions(pouch.getPouchId())
                            .getName().toLowerCase()
                            + " to renew your familiar's timer.");
            return false;
        }
        resetTickets();
        owner.getInventory().deleteItem(pouch.getPouchId(), 1);
        call(true);
        owner.getPackets().sendGameMessage(
                "You use your remaining pouch to renew your familiar.");
        return true;
    }

    public void takeBob() {
        if (bob == null)
            return;
        bob.takeBob();
    }

    public void sendTimeRemaining() {
        owner.getPackets().sendConfig(1176, ticks * 65);
        owner.getPackets().sendIComponentText(PotionTimerInterface.POTION_TIMER_INTERFACE_ID, PotionType.SUMMONING.getTextId(), " " + ticks / 2 + "m " + (ticks % 2 == 0 ? 0 : 30) + "s");
    }

    public void sendMainConfigs() {
        switchOrb(true);
        owner.getPackets().sendConfig(448, pouch.getPouchId());// configures
        // familiar type
        // based on
        // pouch?
        owner.getPackets().sendConfig(1160, 243269632); // sets npc emote
        refreshSpecialEnergy();
        sendTimeRemaining();
        owner.getPackets().sendConfig(1175, getSpecialAmount() << 23);// check
        owner.getPackets().sendGlobalString(204, getSpecialName());
        owner.getPackets().sendGlobalString(205, getSpecialDescription());
        owner.getPackets().sendGlobalConfig(1436,
                getSpecialAttack() == SpecialAttack.CLICK ? 1 : 0);
        unlockOrb(); // temporary
    }

    public void sendFollowerDetails() {
        final boolean res = owner.getInterfaceManager().hasRezizableScreen();
        owner.getInterfaceManager().sendTab(res ? 119 : 179, 662);
        owner.getPackets().sendHideIComponent(662, 44, true);
        owner.getPackets().sendHideIComponent(662, 45, true);
        owner.getPackets().sendHideIComponent(662, 46, true);
        owner.getPackets().sendHideIComponent(662, 47, true);
        owner.getPackets().sendHideIComponent(662, 48, true);
        owner.getPackets().sendHideIComponent(662, 71, false);
        owner.getPackets().sendHideIComponent(662, 72, false);
        unlock();
        owner.getPackets().sendGlobalConfig(168, 8);// tab id 8
    }

    public void switchOrb(final boolean on) {
        owner.getPackets().sendConfig(1174, on ? -1 : 0);
        if (on) {
            unlock();
        } else {
            lockOrb();
        }
    }

    public void unlockOrb() {
        owner.getPackets().sendHideIComponent(747, 9, false);
        sendLeftClickOption(owner);
    }

    public void unlock() {
        switch (getSpecialAttack()) {
            case CLICK:
                owner.getPackets().sendIComponentSettings(747, 18, 0, 0, 2);
                owner.getPackets().sendIComponentSettings(662, 74, 0, 0, 2);
                break;
            case ENTITY:
                owner.getPackets().sendIComponentSettings(747, 18, 0, 0, 20480);
                owner.getPackets().sendIComponentSettings(662, 74, 0, 0, 20480);
                break;
            case OBJECT:
            case ITEM:
                owner.getPackets().sendIComponentSettings(747, 18, 0, 0, 65536);
                owner.getPackets().sendIComponentSettings(662, 74, 0, 0, 65536);
                break;
        }
        owner.getPackets().sendHideIComponent(747, 9, false);
    }

    public void lockOrb() {
        owner.getPackets().sendHideIComponent(747, 9, true);
    }

    public void call() {
        if (getAttackedBy() != null
                && getAttackedByDelay() > Utils.currentTimeMillis()) {
            // TODO or something as this
            owner.getPackets().sendGameMessage(
                    "You cant call your familiar while it under combat.");
            return;
        }
        call(false);
    }

    public void call(final boolean login) {
        final int size = getSize();
        if (login) {
            if (bob != null) {
                bob.setEntitys(owner, this);
            }
            checkNearDirs = Utils.getCoordOffsetsNear(size);
            sendMainConfigs();
        } else {
            removeTarget();
        }
        WorldTile teleTile = null;
        for (int dir = 0; dir < checkNearDirs[0].length; dir++) {
            final WorldTile tile = new WorldTile(new WorldTile(owner.getX()
                    + checkNearDirs[0][dir], owner.getY()
                    + checkNearDirs[1][dir], owner.getPlane()));
            if (World.canMoveNPC(tile.getPlane(), tile.getX(), tile.getY(),
                    size)) { // if found done
                teleTile = tile;
                break;
            }
        }
        if (login || teleTile != null) {
            WorldTasksManager.schedule(new WorldTask() {
                @Override
                public void run() {
                    setNextGraphics(new Graphics(
                            getDefinitions().size > 1 ? 1315 : 1314));
                }
            });
        }
        if (teleTile == null) {
            if (!sentRequestMoveMessage) {
                owner.getPackets().sendGameMessage(
                        "Theres not enough space for your familiar appear.");
                sentRequestMoveMessage = true;
            }
            return;
        }
        sentRequestMoveMessage = false;
        setNextWorldTile(teleTile);
    }

    public void removeFamiliar() {
        owner.setFamiliar(null);
        owner.getPackets().sendHideIComponent(PotionTimerInterface.POTION_TIMER_INTERFACE_ID, PotionType.SUMMONING.getImageId(), true);
        owner.getPackets().sendIComponentText(PotionTimerInterface.POTION_TIMER_INTERFACE_ID, PotionType.SUMMONING.getTextId(), "");
    }

    public void dissmissFamiliar(final boolean logged) {
        finish();
        if (!logged && !isFinished()) {
            setFinished(true);
            switchOrb(false);
            owner.getPackets().closeInterface(owner.getInterfaceManager().hasRezizableScreen() ? 98 : 212);
            owner.getPackets().sendIComponentSettings(747, 18, 0, 0, 0);
            if (bob != null) {
                bob.dropBob();
            }
        }
    }

    @Override
    public void sendDeath(final Entity source) {
        if (dead)
            return;
        dead = true;
        removeFamiliar();
        final NPCCombatDefinitions defs = getCombatDefinitions();
        resetWalkSteps();
        setCantInteract(true);
        getCombat().removeTarget();
        setNextAnimation(null);
        WorldTasksManager.schedule(new WorldTask() {
            int loop;

            @Override
            public void run() {
                if (loop == 0) {
                    setNextAnimation(new Animation(defs.getDeathEmote()));
                    owner.getPackets().sendGameMessage(
                            "Your familiar slowly begins to fade away..");
                } else if (loop >= defs.getDeathDelay()) {
                    dissmissFamiliar(false);
                    stop();
                }
                loop++;
            }
        }, 0, 1);
    }

    public void respawnFamiliar(final Player owner) {
        this.owner = owner;
        initEntity();
        deserialize();
        call(true);
    }

    public abstract String getSpecialName();

    public abstract String getSpecialDescription();

    public abstract int getBOBSize();

    public abstract int getSpecialAmount();

    public abstract SpecialAttack getSpecialAttack();

    public abstract boolean submitSpecial(Object object);

    public boolean isAgressive() {
        return true;
    }

    public BeastOfBurden getBob() {
        return bob;
    }

    public void refreshSpecialEnergy() {
        owner.getPackets().sendConfig(1177, specialEnergy);
    }

    public void restoreSpecialAttack(final int energy) {
        if (specialEnergy >= 60)
            return;
        specialEnergy = energy + specialEnergy >= 60 ? 60 : specialEnergy
                + energy;
        refreshSpecialEnergy();
    }

    public void setSpecial(final boolean on) {
        if (!on) {
            owner.getTemporaryAttributtes().remove("FamiliarSpec");
        } else {
            if (specialEnergy < getSpecialAmount()) {
                owner.getPackets().sendGameMessage(
                        "You familiar doesn't have enough special energy.");
                return;
            }
            owner.getTemporaryAttributtes().put("FamiliarSpec", Boolean.TRUE);
        }
    }

    public void drainSpecial(final int specialReduction) {
        specialEnergy -= specialReduction;
        if (specialEnergy < 0) {
            specialEnergy = 0;
        }
        refreshSpecialEnergy();
    }

    public void drainSpecial() {
        specialEnergy -= getSpecialAmount();
        refreshSpecialEnergy();
    }

    public boolean hasSpecialOn() {
        if (owner.getTemporaryAttributtes().remove("FamiliarSpec") != null) {
            if (!owner.getInventory().containsItem(pouch.getScrollId(), 1)) {
                owner.getPackets().sendGameMessage(
                        "You don't have the scrolls to use this move.");
                return false;
            }
            owner.getInventory().deleteItem(pouch.getScrollId(), 1);
            drainSpecial();
            return true;
        }
        return false;
    }

    public Player getOwner() {
        return owner;
    }

    public boolean isFinished() {
        return finished;
    }

    public enum SpecialAttack {
        ITEM, ENTITY, CLICK, OBJECT
    }
}
