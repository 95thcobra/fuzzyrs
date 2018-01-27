package com.rs.world.npc;

import com.rs.entity.Entity;
import com.rs.server.Server;
import com.rs.content.actions.skills.Skills;
import com.rs.content.actions.skills.herblore.HerbCleaning;
import com.rs.content.actions.skills.prayer.Burying;
import com.rs.content.player.points.PlayerPoints;
import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.core.cache.loaders.NPCDefinitions;
import com.rs.core.cores.CoresManager;
import com.rs.core.file.data.map.MapAreas;
import com.rs.core.file.data.npc.NPCBonusesFileManager;
import com.rs.core.file.data.npc.NPCCombatDefinitionsFileManager;
import com.rs.core.file.data.npc.NPCDropsFileManager;
import com.rs.core.file.data.npc.NPCNameFileManager;
import com.rs.server.GameConstants;
import com.rs.utils.Logger;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.player.content.FriendChatsManager;
import com.rs.player.controlers.Wilderness;
import com.rs.world.*;
import com.rs.world.Hit.HitLook;
import com.rs.world.item.Item;
import com.rs.world.npc.combat.NPCCombat;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.world.npc.familiar.Familiar;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class NPC extends Entity implements Serializable {

    private static final long serialVersionUID = -4794678936277614443L;

    private static final Item[] CHARMS = {new Item(12158, 10),
            new Item(12159, 8), new Item(12160, 6), new Item(12163, 6),};

    private static final Item[] SPINTICKETS = {new Item(24154, 1),
            new Item(24155, 1),};

    private static final Item[] CLUES = {new Item(2677, 1),};

    private static final Item[] BSLAYER = {new Item(22538, 1),};
    private final WorldTile respawnTile;
    private final int mapAreaNameHash;
    public WorldTile forceWalk;
    private int id;
    private boolean canBeAttackFromOutOfArea;
    private boolean randomwalk;
    private int[] bonuses; // 0 stab, 1 slash, 2 crush,3 mage, 4 range, 5 stab
    // def, blahblah till 9
    private boolean spawned;
    private transient NPCCombat combat;
    private long lastAttackedByTarget;
    private boolean cantInteract;
    private int capDamage;
    private int lureDelay;
    private boolean cantFollowUnderCombat;
    private boolean forceAgressive;
    private int forceTargetDistance;
    private boolean forceFollowClose;
    private boolean forceMultiAttacked;
    private boolean noDistanceCheck;

    // npc masks
    private transient Transformation nextTransformation;
    // name changing masks
    private String name;
    private transient boolean changedName;
    private int combatLevel;
    private transient boolean changedCombatLevel;
    private transient boolean locked;

    public NPC(final int id, final WorldTile tile, final int mapAreaNameHash, final boolean canBeAttackFromOutOfArea) {
        this(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, false);
    }

    /*
     * creates and adds npc
     */
    public NPC(final int id, final WorldTile tile, final int mapAreaNameHash, final boolean canBeAttackFromOutOfArea, final boolean spawned) {
        super(tile);
        this.id = id;
        this.respawnTile = new WorldTile(tile);
        this.mapAreaNameHash = mapAreaNameHash;
        this.canBeAttackFromOutOfArea = canBeAttackFromOutOfArea;
        this.setSpawned(spawned);
        combatLevel = -1;
        setHitpoints(getMaxHitpoints());
        setDirection(getRespawnDirection());
        setRandomWalk(forceRandomWalk(id));
        bonuses = NPCBonusesFileManager.getBonuses(id);
        combat = new NPCCombat(this);
        capDamage = -1;
        lureDelay = 12000;
        // npc is inited on creating instance
        initEntity();
        World.addNPC(this);
        World.updateEntityRegion(this);
        // npc is started on creating instance
        loadMapRegions();
        checkMultiArea();
    }

    public static void moo() {
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                final String[] mooing = {"Moo", "Moof",
                        "Semiskimmed is the elder cow"};
                final int i = Utils.getRandom(2);
                for (final NPC n : World.getNPCs()) {
                    if (!n.getName().equalsIgnoreCase("Cow")) {
                        continue;
                    }
                    n.setNextForceTalk(new ForceTalk(mooing[i]));
                }
            }
        }, 0, 30); // time in seconds
    }

    /*
     * forces npc to random walk even if cache says no, used because of fake
     * cache information
     */
    private boolean forceRandomWalk(final int npcId) {
        switch (npcId) {
            case 11226:
                return true;
            case 3341:
            case 3342:
            case 3343:
                return true;
            case 5110:
            case 5485:
            case 2307:
            case 3541:
            case 593:
            case 602:
            case 524:
            case 1918:
            case 5566:
            case 568:
            case 569:
            case 3006:
            case 14915:
            case 6970:
            case 13768:
            case 542:
            case 2998:
            case 1430:
            case 7935:
            case 6361:
            case 7892:
            case 14722:
            case 529:
            case 1576:
            case 470:
            case 2617:
            case 4288:
            case 3223:
            case 6892:
            case 815:
            case 15612:
            case 4250:
            case 3820:
            case 13955:
            case 538:
            case 587:
            case 72:
            case 5112:
            case 11674:
            case 1699:
            case 2259:
            case 552:
            case 11678:
            case 6070:
            case 554:
            case 589:
            case 551:
            case 534:
            case 585:
            case 1597:
            case 548:
            case 1167:
            case 528:
            case 12:
            case 539:
            case 590:
            case 457:
            case 576:
            case 9964:
                return false;
            default:
                return (getDefinitions().walkMask & 0x2) != 0;
        }
    }

    @Override
    public boolean needMasksUpdate() {
        return super.needMasksUpdate() || nextTransformation != null
                || changedCombatLevel || changedName;
    }

    public void transformIntoNPC(final int id) {
        setNPC(id);
        nextTransformation = new Transformation(id);
    }

    public void setNPC(final int id) {
        this.id = id;
        bonuses = NPCBonusesFileManager.getBonuses(id);
    }

    @Override
    public void resetMasks() {
        super.resetMasks();
        nextTransformation = null;
        changedCombatLevel = false;
        changedName = false;
    }

    public int getMapAreaNameHash() {
        return mapAreaNameHash;
    }

    public void setCanBeAttackFromOutOfArea(final boolean b) {
        canBeAttackFromOutOfArea = b;
    }

    public boolean canBeAttackFromOutOfArea() {
        return canBeAttackFromOutOfArea;
    }

    public NPCDefinitions getDefinitions() {
        return NPCDefinitions.getNPCDefinitions(id);
    }

    public NPCCombatDefinitions getCombatDefinitions() {
        return NPCCombatDefinitionsFileManager.getNPCCombatDefinitions(id);
    }

    @Override
    public int getMaxHitpoints() {
        return getCombatDefinitions().getHitpoints();
    }

    public int getId() {
        return id;
    }

    public void processNPC() {
        if (isDead() || locked)
            return;
        if (!combat.process()) { // if not under combat
            if (!isForceWalking()) {// combat still processed for attack delay
                // go down
                // random walk
                if (!cantInteract) {
                    if (!checkAgressivity()) {
                        if (getFreezeDelay() < Utils.currentTimeMillis()) {
                            if (((hasRandomWalk()) && World.getRotation(
                                    getPlane(), getX(), getY()) == 0) // temporary
                                    // fix
                                    && Math.random() * 1000.0 < 100.0) {
                                final int moveX = (int) Math.round(Math
                                        .random() * 10.0 - 5.0);
                                final int moveY = (int) Math.round(Math
                                        .random() * 10.0 - 5.0);
                                resetWalkSteps();
                                if (getMapAreaNameHash() != -1) {
                                    if (!MapAreas.isAtArea(
                                            getMapAreaNameHash(), this)) {
                                        forceWalkRespawnTile();
                                        return;
                                    }
                                    addWalkSteps(getX() + moveX,
                                            getY() + moveY, 5);
                                } else {
                                    addWalkSteps(respawnTile.getX() + moveX,
                                            respawnTile.getY() + moveY, 5);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (isForceWalking()) {
            if (getFreezeDelay() < Utils.currentTimeMillis()) {
                if (getX() != forceWalk.getX() || getY() != forceWalk.getY()) {
                    if (!hasWalkSteps()) {
                        addWalkSteps(forceWalk.getX(), forceWalk.getY(),
                                getSize(), true);
                    }
                    if (!hasWalkSteps()) { // failing finding route
                        setNextWorldTile(new WorldTile(forceWalk)); // force
                        forceWalk = null; // so ofc reached forcewalk place
                    }
                } else {
                    forceWalk = null;
                }
            }
        }
        String customName = NPCNameFileManager.getCustomName(id);
        if (customName != null) {
            setName(customName);
        }
        if (id == 418) {
            if (Utils.random(10) == 0) {
                setNextForceTalk(new ForceTalk("Talk to me for all the farming supplies you need!"));
            }
        }
        if (id == 7530) {
            if (Utils.random(10) == 0) {
                setNextForceTalk(new ForceTalk("Give me 28 Logs to get started!"));
            }
        }
        if (id == 7909) {
            if (Utils.random(10) == 0) {
                setNextForceTalk(new ForceTalk("Welcome to " + Server.getInstance().getSettingsManager().getSettings().getServerName() + "! Talk to me for the basics."));
            }
        }
        if (id == 7909) {
            setNextAnimation(new Animation(4945));
            setNextGraphics(new Graphics(816));
        }
        if (id == 15172) {
            setCombatLevel(169);
        }
        if (id == 3223) {
            setCombatLevel(1);
        }
        if (id == 3319) {
            setCombatLevel(1337);
        }
        if (id == 3223) {
            setForceFollowClose(false);
        }
        if (id == 15126) {
            setCombatLevel(6969);
        }
        if (id == 6353) {
            setCombatLevel(71);
        }
        if (id == 9964) {
            setCombatLevel(5067);
        }
    }

    @Override
    public void processEntity() {
        super.processEntity();
        processNPC();
    }

    public int getRespawnDirection() {
        final NPCDefinitions definitions = getDefinitions();
        if (definitions.anInt853 << 32 != 0 && definitions.respawnDirection > 0
                && definitions.respawnDirection <= 8)
            return (4 + definitions.respawnDirection) << 11;
        return 0;
    }

    public void sendSoulSplit(final Hit hit, final Entity user) {
        final NPC target = this;
        if (hit.getDamage() > 0) {
            World.sendProjectile(user, this, 2263, 11, 11, 20, 5, 0, 0);
        }
        user.heal(hit.getDamage() / 5);
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                setNextGraphics(new Graphics(2264));
                if (hit.getDamage() > 0) {
                    World.sendProjectile(target, user, 2263, 11, 11, 20, 5, 0,
                            0);
                }
            }
        }, 1);
    }

    @Override
    public void handleIngoingHit(final Hit hit) {
        if (capDamage != -1 && hit.getDamage() > capDamage) {
            hit.setDamage(capDamage);
        }
        if (hit.getLook() != HitLook.MELEE_DAMAGE
                && hit.getLook() != HitLook.RANGE_DAMAGE
                && hit.getLook() != HitLook.MAGIC_DAMAGE)
            return;
        final Entity source = hit.getSource();
        if (source == null)
            return;
        if (source instanceof Player) {
            final Player p2 = (Player) source;
            if (p2.getPrayer().hasPrayersOn()) {
                if (p2.getPrayer().usingPrayer(1, 18)) {
                    sendSoulSplit(hit, p2);
                }
                if (hit.getDamage() == 0)
                    return;
                if (!p2.getPrayer().isBoostedLeech()) {
                    if (hit.getLook() == HitLook.MELEE_DAMAGE) {
                        if (p2.getPrayer().usingPrayer(1, 19)) {
                            p2.getPrayer().setBoostedLeech(true);
                            p2.getPrayer().increaseTurmoilBonus(p2);
                            return;
                        } else if (p2.getPrayer().usingPrayer(1, 1)) { // sap
                            // att
                            if (Utils.getRandom(4) == 0) {
                                if (p2.getPrayer().reachedMax(0)) {
                                    p2.getPackets()
                                            .sendGameMessage(
                                                    "Your opponent has been weakened so much that your sap curse has no effect.",
                                                    true);
                                } else {
                                    p2.getPrayer().increaseLeechBonus(0);
                                    p2.getPackets()
                                            .sendGameMessage(
                                                    "Your curse drains Attack from the enemy, boosting your Attack.",
                                                    true);
                                }
                                p2.setNextAnimation(new Animation(12569));
                                p2.setNextGraphics(new Graphics(2214));
                                p2.getPrayer().setBoostedLeech(true);
                                World.sendProjectile(p2, this, 2215, 35, 35,
                                        20, 5, 0, 0);
                                WorldTasksManager.schedule(new WorldTask() {
                                    @Override
                                    public void run() {
                                        setNextGraphics(new Graphics(2216));
                                    }
                                }, 1);
                                return;
                            }
                        } else {
                            if (p2.getPrayer().usingPrayer(1, 10)) {
                                if (Utils.getRandom(7) == 0) {
                                    if (p2.getPrayer().reachedMax(3)) {
                                        p2.getPackets()
                                                .sendGameMessage(
                                                        "Your opponent has been weakened so much that your leech curse has no effect.",
                                                        true);
                                    } else {
                                        p2.getPrayer().increaseLeechBonus(3);
                                        p2.getPackets()
                                                .sendGameMessage(
                                                        "Your curse drains Attack from the enemy, boosting your Attack.",
                                                        true);
                                    }
                                    p2.setNextAnimation(new Animation(12575));
                                    p2.getPrayer().setBoostedLeech(true);
                                    World.sendProjectile(p2, this, 2231, 35,
                                            35, 20, 5, 0, 0);
                                    WorldTasksManager.schedule(new WorldTask() {
                                        @Override
                                        public void run() {
                                            setNextGraphics(new Graphics(2232));
                                        }
                                    }, 1);
                                    return;
                                }
                            }
                            if (p2.getPrayer().usingPrayer(1, 14)) {
                                if (Utils.getRandom(7) == 0) {
                                    if (p2.getPrayer().reachedMax(7)) {
                                        p2.getPackets()
                                                .sendGameMessage(
                                                        "Your opponent has been weakened so much that your leech curse has no effect.",
                                                        true);
                                    } else {
                                        p2.getPrayer().increaseLeechBonus(7);
                                        p2.getPackets()
                                                .sendGameMessage(
                                                        "Your curse drains Strength from the enemy, boosting your Strength.",
                                                        true);
                                    }
                                    p2.setNextAnimation(new Animation(12575));
                                    p2.getPrayer().setBoostedLeech(true);
                                    World.sendProjectile(p2, this, 2248, 35,
                                            35, 20, 5, 0, 0);
                                    WorldTasksManager.schedule(new WorldTask() {
                                        @Override
                                        public void run() {
                                            setNextGraphics(new Graphics(2250));
                                        }
                                    }, 1);
                                    return;
                                }
                            }

                        }
                    }
                    if (hit.getLook() == HitLook.RANGE_DAMAGE) {
                        if (p2.getPrayer().usingPrayer(1, 2)) { // sap range
                            if (Utils.getRandom(4) == 0) {
                                if (p2.getPrayer().reachedMax(1)) {
                                    p2.getPackets()
                                            .sendGameMessage(
                                                    "Your opponent has been weakened so much that your sap curse has no effect.",
                                                    true);
                                } else {
                                    p2.getPrayer().increaseLeechBonus(1);
                                    p2.getPackets()
                                            .sendGameMessage(
                                                    "Your curse drains Range from the enemy, boosting your Range.",
                                                    true);
                                }
                                p2.setNextAnimation(new Animation(12569));
                                p2.setNextGraphics(new Graphics(2217));
                                p2.getPrayer().setBoostedLeech(true);
                                World.sendProjectile(p2, this, 2218, 35, 35,
                                        20, 5, 0, 0);
                                WorldTasksManager.schedule(new WorldTask() {
                                    @Override
                                    public void run() {
                                        setNextGraphics(new Graphics(2219));
                                    }
                                }, 1);
                                return;
                            }
                        } else if (p2.getPrayer().usingPrayer(1, 11)) {
                            if (Utils.getRandom(7) == 0) {
                                if (p2.getPrayer().reachedMax(4)) {
                                    p2.getPackets()
                                            .sendGameMessage(
                                                    "Your opponent has been weakened so much that your leech curse has no effect.",
                                                    true);
                                } else {
                                    p2.getPrayer().increaseLeechBonus(4);
                                    p2.getPackets()
                                            .sendGameMessage(
                                                    "Your curse drains Range from the enemy, boosting your Range.",
                                                    true);
                                }
                                p2.setNextAnimation(new Animation(12575));
                                p2.getPrayer().setBoostedLeech(true);
                                World.sendProjectile(p2, this, 2236, 35, 35,
                                        20, 5, 0, 0);
                                WorldTasksManager.schedule(new WorldTask() {
                                    @Override
                                    public void run() {
                                        setNextGraphics(new Graphics(2238));
                                    }
                                });
                                return;
                            }
                        }
                    }
                    if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
                        if (p2.getPrayer().usingPrayer(1, 3)) { // sap mage
                            if (Utils.getRandom(4) == 0) {
                                if (p2.getPrayer().reachedMax(2)) {
                                    p2.getPackets()
                                            .sendGameMessage(
                                                    "Your opponent has been weakened so much that your sap curse has no effect.",
                                                    true);
                                } else {
                                    p2.getPrayer().increaseLeechBonus(2);
                                    p2.getPackets()
                                            .sendGameMessage(
                                                    "Your curse drains Magic from the enemy, boosting your Magic.",
                                                    true);
                                }
                                p2.setNextAnimation(new Animation(12569));
                                p2.setNextGraphics(new Graphics(2220));
                                p2.getPrayer().setBoostedLeech(true);
                                World.sendProjectile(p2, this, 2221, 35, 35,
                                        20, 5, 0, 0);
                                WorldTasksManager.schedule(new WorldTask() {
                                    @Override
                                    public void run() {
                                        setNextGraphics(new Graphics(2222));
                                    }
                                }, 1);
                                return;
                            }
                        } else if (p2.getPrayer().usingPrayer(1, 12)) {
                            if (Utils.getRandom(7) == 0) {
                                if (p2.getPrayer().reachedMax(5)) {
                                    p2.getPackets()
                                            .sendGameMessage(
                                                    "Your opponent has been weakened so much that your leech curse has no effect.",
                                                    true);
                                } else {
                                    p2.getPrayer().increaseLeechBonus(5);
                                    p2.getPackets()
                                            .sendGameMessage(
                                                    "Your curse drains Magic from the enemy, boosting your Magic.",
                                                    true);
                                }
                                p2.setNextAnimation(new Animation(12575));
                                p2.getPrayer().setBoostedLeech(true);
                                World.sendProjectile(p2, this, 2240, 35, 35,
                                        20, 5, 0, 0);
                                WorldTasksManager.schedule(new WorldTask() {
                                    @Override
                                    public void run() {
                                        setNextGraphics(new Graphics(2242));
                                    }
                                }, 1);
                                return;
                            }
                        }
                    }

                    // overall

                    if (p2.getPrayer().usingPrayer(1, 13)) { // leech defence
                        if (Utils.getRandom(10) == 0) {
                            if (p2.getPrayer().reachedMax(6)) {
                                p2.getPackets()
                                        .sendGameMessage(
                                                "Your opponent has been weakened so much that your leech curse has no effect.",
                                                true);
                            } else {
                                p2.getPrayer().increaseLeechBonus(6);
                                p2.getPackets()
                                        .sendGameMessage(
                                                "Your curse drains Defence from the enemy, boosting your Defence.",
                                                true);
                            }
                            p2.setNextAnimation(new Animation(12575));
                            p2.getPrayer().setBoostedLeech(true);
                            World.sendProjectile(p2, this, 2244, 35, 35, 20, 5,
                                    0, 0);
                            WorldTasksManager.schedule(new WorldTask() {
                                @Override
                                public void run() {
                                    setNextGraphics(new Graphics(2246));
                                }
                            }, 1);
                        }
                    }
                }
            }
        }

    }

    @Override
    public void reset() {
        super.reset();
        setDirection(getRespawnDirection());
        combat.reset();
        bonuses = NPCBonusesFileManager.getBonuses(id); // back to real bonuses
        forceWalk = null;
    }

    @Override
    public void finish() {
        if (hasFinished())
            return;
        setFinished(true);
        World.updateEntityRegion(this);
        World.removeNPC(this);
    }

    public void setRespawnTask() {
        if (!hasFinished()) {
            reset();
            setLocation(respawnTile);
            finish();
        }
        CoresManager.SLOW_EXECUTOR.schedule(() -> {
                    try {
                        spawn();
                    } catch (final Throwable e) {
                        Logger.handle(e);
                    }
                }, getCombatDefinitions().getRespawnDelay() * 600,
                TimeUnit.MILLISECONDS);
    }

    public void deserialize() {
        if (combat == null) {
            combat = new NPCCombat(this);
        }
        spawn();
    }

    public void spawn() {
        setFinished(false);
        World.addNPC(this);
        setLastRegionId(0);
        World.updateEntityRegion(this);
        loadMapRegions();
        checkMultiArea();
    }

    public NPCCombat getCombat() {
        return combat;
    }

    @Override
    public void sendDeath(final Entity source) {
        final NPCCombatDefinitions defs = getCombatDefinitions();
        resetWalkSteps();
        combat.removeTarget();
        drop();
        setNextAnimation(null);
        WorldTasksManager.schedule(new WorldTask() {
            int loop;

            @Override
            public void run() {
                if (loop == 0) {
                    setNextAnimation(new Animation(defs.getDeathEmote()));
                } else if (loop >= defs.getDeathDelay()) {
                    reset();
                    setLocation(respawnTile);
                    finish();
                    if (!isSpawned()) {
                        setRespawnTask();
                    }
                    stop();
                }
                loop++;
            }
        }, 0, 1);
    }

    public void drop() {
        try {
            final Drop[] drops = NPCDropsFileManager.getDrops(id);
            if (drops == null)
                return;
            final Player killer = getMostDamageReceivedSourcePlayer();
            if (killer == null)
                return;
            if (getId() == 14410) {
                killer.getPlayerPoints().addPoints(PlayerPoints.PK_POINTS, 10);
            }
            if (getId() == 9051) {
                killer.getSkills().addXp(24, 1000);
            }
            if (getId() == 9911) {
                killer.getSkills().addXp(24, 5000);
            }
            if (getId() == 9052) {
                killer.getSkills().addXp(24, 2500);
            }
            if (getId() == 6358) {
                killer.setNextWorldTile(new WorldTile(
                        Server.getInstance().getSettingsManager().getSettings().getRespawnPlayerLocation()));
                killer.getInterfaceManager().sendInterface(1244);
                killer.getPackets().sendIComponentText(1244, 25,
                        "You have completed Grossing Out.");
                killer.getPackets().sendIComponentText(1244, 27,
                        "Quest Points: 0");
                killer.getPackets().sendGlobalString(
                        359,
                        "Access to the Korasi's Shop<br>"
                                + "Two spins on the Squeal of Fortune<br>");
                killer.getPackets().sendItemOnIComponent(1244, 24, 19784, 1);
                killer.getPackets()
                        .sendGameMessage(
                                "Congratulations! You have completed the Grossing Out quest!");
                killer.setSpins(killer.getSpins() + 2);
            }
            if (getId() == 1885) {
                killer.sendMessage("Good job, you finally solved the riddle.");
            }
            /*if (getId() == 15126) {
                killer.setWolverine(killer.getWolverine() + 1);
            }
            if (getId() == 13450) {
                killer.setNex(killer.getNex() + 1);
            }
            if (getId() == 6260) {
                killer.setBandos(killer.getBandos() + 1);
            }
            if (getId() == 6247) {
                killer.setSaradomin(killer.getSaradomin() + 1);
            }
            if (getId() == 9463) {
                World.spawnNPC(9462, new WorldTile(3420, 5658, 0), -1, true);
            }
            if (getId() == 2882) {
                killer.dagprime += 1;
            }
            if (getId() == 3847) {
                killer.kraken += 1;
            }
            if (getId() == 2881) {
                killer.dagsup += 1;
            }
            if (getId() == 2883) {
                killer.dagrex += 1;
            }
            if (getId() == 8596) {
                killer.davatar += 1;
            }
            if (getId() == 5472) {
                killer.icytroll += 1;
            }
            if (getId() == 5421) {
                killer.tarn += 1;
            }
            if (getId() == 3340) {
                killer.giantmole += 1;
            }
            if (getId() == 4972) {
                killer.giantroc += 1;
            }
            if (getId() == 50) {
                killer.kbd += 1;
            }
            if (getId() == 8351) {
                killer.torms += 1;
            }
            if (getId() == 8133) {
                killer.corp += 1;
            }
            if (getId() == 6203) {
                killer.setZamorak(killer.getZamorak() + 1);
            }
            if (getId() == 6222) {
                killer.setArmadyl(killer.getArmadyl() + 1);
            }
            if (getId() == 11872) {
                killer.setThunder(killer.getThunder() + 1);
            }
            if (getId() == 8335) {
                killer.setDard(killer.getDard() + 1);
            }
            if (getId() == 12878) {
                killer.setBlink(killer.getBlink() + 1);
            }*/
            if (getId() == 15172) {
                killer.setNextWorldTile(new WorldTile(2336, 4320, 0));
            }
            if (getId() == 10114) {
                killer.setNextWorldTile(new WorldTile(3182, 5719, 0));
            }
            if (getId() == 14256) {
                killer.setNextWorldTile(new WorldTile(
                        Server.getInstance().getSettingsManager().getSettings().getStartPlayerLocation()));
                killer.getInventory().addItem(21511, 1);
            }
            if (getId() == 1885) {
                killer.getInterfaceManager().sendInterface(1244);
                killer.getPackets().sendIComponentText(1244, 25,
                        "You have completed Bandit Assasination.");
                killer.getPackets().sendIComponentText(1244, 27,
                        "Quest Points: 0");
                killer.getPackets().sendGlobalString(
                        359,
                        "Quest point hood<br>" + "Quest point cape<br>"
                                + "Two spins on the Squeal of Fortune<br>");
                killer.getPackets().sendItemOnIComponent(1244, 24, 9813, 1);
                killer.getPackets()
                        .sendGameMessage(
                                "Congratulations! You have completed the Bandit Assasination quest!");
                killer.setSpins(killer.getSpins() + 2);
            }
            // SlayerTask task = killer.getSlayerTask();
            if (killer.getTask() != null) {
                if (getDefinitions().name.toLowerCase().equalsIgnoreCase(
                        killer.getTask().getName().toLowerCase())) {
                    killer.getSkills().addXp(Skills.SLAYER,
                            killer.getTask().getXPAmount());
                    killer.getTask().decreaseAmount();
                    if (killer.getTask().getTaskAmount() < 1) {
                        if (killer.getEquipment().getRingId() == 13281) {
                            killer.getPlayerPoints().addPoints(PlayerPoints.SLAYER_POINTS, 40);
                            killer.getPackets()
                                    .sendGameMessage(
                                            "You have finished your slayer task, talk to Kuradal for a new task.");
                            killer.getPackets().sendGameMessage(
                                    "Kuradal rewarded you 40 slayerPoints! You now have "
                                            + killer.getPlayerPoints().getPoints(PlayerPoints.SLAYER_POINTS)
                                            + " slayerPoints.");
                        } else {
                            killer.getPlayerPoints().addPoints(PlayerPoints.SLAYER_POINTS, 20);
                            killer.getPackets()
                                    .sendGameMessage(
                                            "You have finished your slayer task, talk to Kuradal for a new task.");
                            killer.getPackets().sendGameMessage(
                                    "Kuradal rewarded you 20 slayerPoints! You now have "
                                            + killer.getPlayerPoints().getPoints(PlayerPoints.SLAYER_POINTS)
                                            + " slayerPoints.");
                        }
                        killer.setTask(null);
                        return;
                    }
                    killer.getTask().setAmountKilled(
                            killer.getTask().getAmountKilled() + 1);
                    killer.getPackets().sendGameMessage(
                            "You need to defeat "
                                    + killer.getTask().getTaskAmount() + " "
                                    + killer.getTask().getName().toLowerCase()
                                    + " to complete your task.");
                }
            }
            final Drop[] possibleDrops = new Drop[drops.length];
            int possibleDropsCount = 0;
            for (final Drop drop : drops) {
                if (drop.getRate() == 100) {
                    sendDrop(killer, drop);
                } else {
                    if ((Utils.getRandomDouble(99) + 1) <= drop.getRate() * Server.getInstance().getSettingsManager().getSettings().getDropRate()) {
                        possibleDrops[possibleDropsCount++] = drop;
                    }
                }
            }
            if (possibleDropsCount > 0) {
                sendDrop(killer,
                        possibleDrops[Utils.getRandom(possibleDropsCount - 1)]);
            }
        } catch (final Exception | Error e) {
            e.printStackTrace();
        }
    }

    public void sendDrop(final Player player, final Drop drop) {
        final int size = getSize();
        final String dropName = ItemDefinitions
                .getItemDefinitions(drop.getItemId()).getName().toLowerCase();
        final Item item = ItemDefinitions.getItemDefinitions(drop.getItemId())
                .isStackable() ? new Item(drop.getItemId(),
                (drop.getMinAmount() * Server.getInstance().getSettingsManager().getSettings().getDropRate())
                        + Utils.getRandom(drop.getExtraAmount()
                        * Server.getInstance().getSettingsManager().getSettings().getDropRate())) : new Item(
                drop.getItemId(), drop.getMinAmount()
                + Utils.getRandom(drop.getExtraAmount()));
        if (player.getInventory().containsItem(18337, 1)// Bonecrusher
                && item.getDefinitions().getName().toLowerCase()
                .contains("bones")) {
            player.getSkills().addXp(Skills.PRAYER,
                    Burying.Bone.forId(drop.getItemId()).getExperience());
            return;
        }
        if (player.getInventory().containsItem(19675, 1)// Herbicide
                && item.getDefinitions().getName().toLowerCase()
                .contains("grimy")) {
            if (player.getSkills().getLevelForXp(Skills.HERBLORE) >= HerbCleaning
                    .getHerb(item.getId()).getLevel()) {
                player.getSkills()
                        .addXp(Skills.HERBLORE,
                                HerbCleaning.getHerb(drop.getItemId())
                                        .getExperience() * 2);
                return;
            }
        }
                        /* LootShare */
        final FriendChatsManager fc = player.getCurrentFriendChat();
        if (player.lootShareEnabled()) {
            if (fc != null) {
                final CopyOnWriteArrayList<Player> players = fc.getPlayers();
                final CopyOnWriteArrayList<Player> playersWithLs = new CopyOnWriteArrayList<Player>();
                for (final Player p : players) {
                    if (p.lootShareEnabled()
                            && p.getRegionId() == player.getRegionId()) {
                        playersWithLs.add(p);
                    }
                }
                final Player luckyPlayer = playersWithLs.get((int) (Math
                        .random() * playersWithLs.size())); // Choose a random
                // player to get the
                // drop.
                World.addGroundItem(item, new WorldTile(getCoordFaceX(size),
                                getCoordFaceY(size), getPlane()), luckyPlayer, false,
                        180, true);
                luckyPlayer.sendMessage(String.format(
                        "<col=115b0d>You received: %sx %s.</col>",
                        item.getAmount(), dropName));
                if (dropName.contains("pernix") || dropName.contains("torva")
                        || dropName.contains("virtus")
                        || dropName.contains("zaryte")
                        || dropName.contains("bandos")
                        || dropName.contains("subjugation")
                        || dropName.contains("akrisae")
                        || dropName.contains("saradomin")
                        && !dropName.contains("brew")
                        || dropName.contains("zamorak")
                        && !dropName.contains("brew")
                        && !dropName.contains("wine")
                        || dropName.contains("falador shield")
                        || dropName.contains("spirit shield")
                        || dropName.contains("divine")
                        || dropName.contains("spectral")
                        || dropName.contains("arcane")
                        || dropName.contains("elysian")
                        || dropName.contains("holy elixir")
                        || dropName.contains("claws")
                        || dropName.contains("visage")
                        || dropName.contains("drygore")
                        || dropName.contains("raider")
                        || dropName.contains("tax")
                        || dropName.contains("shark gloves")
                        || dropName.contains("eva's")
                        || dropName.contains("chaotic")
                        || dropName.contains("armadyl")
                        && !dropName.contains("shard")
                        && !dropName.contains("rune")
                        || dropName.contains("zanik")
                        || dropName.contains("thok")
                        || dropName.contains("blisterwood")
                        || dropName.contains("korasi")
                        || dropName.contains("peahat")
                        || dropName.contains("golden")
                        || dropName.contains("annihilation")
                        || dropName.contains("decimation")
                        || dropName.contains("obliteration")
                        || dropName.contains("primal")
                        || dropName.contains("vigour")
                        || dropName.contains("dragon defender")
                        || dropName.contains("sk�ll")
                        || dropName.contains("skoll")
                        || dropName.contains("celestial")
                        || dropName.contains("keenblade")
                        || dropName.contains("dragonic")
                        || dropName.contains("steadfast")
                        || dropName.contains("ragefire")
                        || dropName.contains("glaiven")) {
                    World.sendWorldMessage(
                            Utils.formatPlayerNameForDisplay(player
                                    .getDisplayName())
                                    + " <col=FF0000>has received a(n) "
                                    + dropName + "", false);
                }
                for (final Player p : playersWithLs) {
                    if (!p.equals(luckyPlayer)) {
                        p.sendMessage(String.format("%s received: %sx %s.",
                                luckyPlayer.getDisplayName(), item.getAmount(),
                                dropName));
                    }
                }
                for (final String itemName : GameConstants.RARE_DROPS) {
                    if (dropName.toLowerCase().contains(itemName.toLowerCase())) {
                    }
                }
                return;
            }
        }
        /* End of LootShare */

        World.addGroundItem(item, new WorldTile(getCoordFaceX(size),
                getCoordFaceY(size), getPlane()), player, false, 180, true);
        sendCharms(player);
        sendTickets(player);
        sendClues(player);
        if (dropName.contains("pernix") || dropName.contains("torva")
                || dropName.contains("virtus") || dropName.contains("zaryte")
                || dropName.contains("bandos")
                || dropName.contains("subjugation")
                || dropName.contains("akrisae")
                || dropName.contains("saradomin") && !dropName.contains("brew")
                || dropName.contains("zamorak") && !dropName.contains("brew")
                && !dropName.contains("wine")
                || dropName.contains("falador shield")
                || dropName.contains("spirit shield")
                || dropName.contains("divine") || dropName.contains("spectral")
                || dropName.contains("arcane") || dropName.contains("elysian")
                || dropName.contains("holy elixir")
                || dropName.contains("claws") || dropName.contains("drygore")
                || dropName.contains("raider") || dropName.contains("tax")
                || dropName.contains("shark gloves")
                || dropName.contains("eva's") || dropName.contains("chaotic")
                || dropName.contains("armadyl") && !dropName.contains("shard")
                && !dropName.contains("rune") || dropName.contains("zanik")
                || dropName.contains("thok") || dropName.contains("visage")
                || dropName.contains("blisterwood")
                || dropName.contains("korasi") || dropName.contains("peahat")
                || dropName.contains("golden")
                || dropName.contains("annihilation")
                || dropName.contains("decimation")
                || dropName.contains("obliteration")
                || dropName.contains("primal") || dropName.contains("vigour")
                || dropName.contains("dragon defender")
                || dropName.contains("sk�ll") || dropName.contains("skoll")
                || dropName.contains("celestial")
                || dropName.contains("keenblade")
                || dropName.contains("dragonic")
                || dropName.contains("steadfast")
                || dropName.contains("ragefire")
                || dropName.contains("glaiven")) {
            World.sendWorldMessage(
                    Utils.formatPlayerNameForDisplay(player.getDisplayName())
                            + " <col=FF0000>has received a(n) " + dropName + "",
                    false);
        }
    }

    @Override
    public int getSize() {
        return getDefinitions().size;
    }

    public int getMaxHit() {
        return getCombatDefinitions().getMaxHit();
    }

    public int[] getBonuses() {
        return bonuses;
    }

    @Override
    public double getMagePrayerMultiplier() {
        return 0;
    }

    @Override
    public double getRangePrayerMultiplier() {
        return 0;
    }

    @Override
    public double getMeleePrayerMultiplier() {
        return 0;
    }

    public WorldTile getRespawnTile() {
        return respawnTile;
    }

    public boolean isUnderCombat() {
        return combat.underCombat();
    }

    @Override
    public void setAttackedBy(final Entity target) {
        super.setAttackedBy(target);
        if (target == combat.getTarget()
                && !(combat.getTarget() instanceof Familiar)) {
            lastAttackedByTarget = Utils.currentTimeMillis();
        }
    }

    public boolean canBeAttackedByAutoRelatie() {
        return Utils.currentTimeMillis() - lastAttackedByTarget > lureDelay;
    }

    public boolean isForceWalking() {
        return forceWalk != null;
    }

    public void setTarget(final Entity entity) {
        if (isForceWalking()) // if force walk not gonna get target
            return;
        combat.setTarget(entity);
        lastAttackedByTarget = Utils.currentTimeMillis();
    }

    public void removeTarget() {
        if (combat.getTarget() == null)
            return;
        combat.removeTarget();
    }

    public void forceWalkRespawnTile() {
        setForceWalk(respawnTile);
    }

    public void setForceWalk(final WorldTile tile) {
        resetWalkSteps();
        forceWalk = tile;
    }

    public boolean hasForceWalk() {
        return forceWalk != null;
    }

    public ArrayList<Entity> getPossibleTargets() {
        final ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
        for (final int regionId : getMapRegionsIds()) {
            final List<Integer> playerIndexes = World.getRegion(regionId)
                    .getPlayerIndexes();
            if (playerIndexes != null) {
                for (final int playerIndex : playerIndexes) {
                    final Player player = World.getPlayers().get(playerIndex);
                    if (player == null
                            || player.isDead()
                            || player.hasFinished()
                            || !player.isRunning()
                            || !player
                            .withinDistance(
                                    this,
                                    forceTargetDistance > 0 ? forceTargetDistance
                                            : (getCombatDefinitions()
                                            .getAttackStyle() == NPCCombatDefinitions.MELEE ? 4
                                            : getCombatDefinitions()
                                            .getAttackStyle() == NPCCombatDefinitions.SPECIAL ? 64
                                            : 8))
                            || (!forceMultiAttacked
                            && (!isAtMultiArea() || !player
                            .isAtMultiArea())
                            && player.getAttackedBy() != this && (player
                            .getAttackedByDelay() > Utils
                            .currentTimeMillis() || player
                            .getFindTargetDelay() > Utils
                            .currentTimeMillis()))
                            || !clipedProjectile(player, false)
                            || (!forceAgressive && !Wilderness.isAtWild(this) && player
                            .getSkills().getCombatLevelWithSummoning() >= getCombatLevel() * 2)) {
                        continue;
                    }
                    possibleTarget.add(player);
                }
            }
        }
        return possibleTarget;
    }

    public boolean checkAgressivity() {
        // if(!(Wilderness.isAtWild(this) &&
        // getDefinitions().hasAttackOption())) {
        if (!forceAgressive) {
            final NPCCombatDefinitions defs = getCombatDefinitions();
            if (defs.getAgressivenessType() == NPCCombatDefinitions.PASSIVE)
                return false;
        }
        // }
        final ArrayList<Entity> possibleTarget = getPossibleTargets();
        if (!possibleTarget.isEmpty()) {
            final Entity target = possibleTarget.get(Utils
                    .random(possibleTarget.size()));
            setTarget(target);
            target.setAttackedBy(target);
            target.setFindTargetDelay(Utils.currentTimeMillis() + 10000);
            return true;
        }
        return false;
    }

    public boolean isCantInteract() {
        return cantInteract;
    }

    public void setCantInteract(final boolean cantInteract) {
        this.cantInteract = cantInteract;
        if (cantInteract) {
            combat.reset();
        }
    }

    public void dropCharm(final Player player, final Item item) {
        final int size = getSize();
        final Item dropItem = new Item(item.getId(), Utils.random(item
                .getDefinitions().isStackable() ? item.getAmount()
                * Server.getInstance().getSettingsManager().getSettings().getDropRate() : item.getAmount()) + 1);

        World.addGroundItem(dropItem, new WorldTile(getCoordFaceX(size),
                getCoordFaceY(size), getPlane()), player, false, 180, true);
    }

    public void sendTickets(final Player player) {
        if (Utils.random(80) == 0) {
            dropTickets(player, SPINTICKETS[Utils.random(SPINTICKETS.length)]);
        }
    }

    public void dropTickets(final Player player, final Item item) {
        final int size = getSize();
        final Item dropItem = new Item(item.getId(), Utils.random(item
                .getDefinitions().isStackable() ? item.getAmount()
                * Server.getInstance().getSettingsManager().getSettings().getDropRate() : item.getAmount()) + 1);

        World.addGroundItem(dropItem, new WorldTile(getCoordFaceX(size),
                getCoordFaceY(size), getPlane()), player, false, 180, true);

    }

    public void sendClues(final Player player) {
        if (Utils.random(250) == 68) {
            dropClues(player, CLUES[Utils.random(CLUES.length)]);
        }
    }

    public void dropClues(final Player player, final Item item) {
        final int size = getSize();
        final Item dropItem = new Item(item.getId(), Utils.random(item
                .getDefinitions().isStackable() ? item.getAmount()
                * Server.getInstance().getSettingsManager().getSettings().getDropRate() : item.getAmount()) + 1);

        World.addGroundItem(dropItem, new WorldTile(getCoordFaceX(size),
                getCoordFaceY(size), getPlane()), player, false, 180, true);

    }

    public void sendBSlayer(final Player player) {
        if (Utils.random(130) == 44) {
            dropBSlayer(player, BSLAYER[Utils.random(BSLAYER.length)]);
        }
    }

    public void dropBSlayer(final Player player, final Item item) {
        final int size = getSize();
        final Item dropItem = new Item(item.getId(), Utils.random(item
                .getDefinitions().isStackable() ? item.getAmount()
                * Server.getInstance().getSettingsManager().getSettings().getDropRate() : item.getAmount()) + 1);

        World.addGroundItem(dropItem, new WorldTile(getCoordFaceX(size),
                getCoordFaceY(size), getPlane()), player, false, 180, true);

    }

    public void sendCharms(final Player player) {
        if (Utils.random(3) != 0) {
            dropCharm(player, CHARMS[Utils.random(CHARMS.length)]);
        }
    }

    public int getCapDamage() {
        return capDamage;
    }

    public void setCapDamage(final int capDamage) {
        this.capDamage = capDamage;
    }

    public int getLureDelay() {
        return lureDelay;
    }

    public void setLureDelay(final int lureDelay) {
        this.lureDelay = lureDelay;
    }

    public boolean isCantFollowUnderCombat() {
        return cantFollowUnderCombat;
    }

    public void setCantFollowUnderCombat(final boolean canFollowUnderCombat) {
        this.cantFollowUnderCombat = canFollowUnderCombat;
    }

    public Transformation getNextTransformation() {
        return nextTransformation;
    }

    @Override
    public String toString() {
        return getDefinitions().name + " - " + id + " - " + getX() + " "
                + getY() + " " + getPlane();
    }

    public boolean isForceAgressive() {
        return forceAgressive;
    }

    public void setForceAgressive(final boolean forceAgressive) {
        this.forceAgressive = forceAgressive;
    }

    public int getForceTargetDistance() {
        return forceTargetDistance;
    }

    public void setForceTargetDistance(final int forceTargetDistance) {
        this.forceTargetDistance = forceTargetDistance;
    }

    public boolean isForceFollowClose() {
        return forceFollowClose;
    }

    public void setForceFollowClose(final boolean forceFollowClose) {
        this.forceFollowClose = forceFollowClose;
    }

    public boolean isForceMultiAttacked() {
        return forceMultiAttacked;
    }

    public void setForceMultiAttacked(final boolean forceMultiAttacked) {
        this.forceMultiAttacked = forceMultiAttacked;
    }

    public boolean hasRandomWalk() {
        return randomwalk;
    }

    public void setRandomWalk(final boolean forceRandomWalk) {
        this.randomwalk = forceRandomWalk;
    }

    public String getCustomName() {
        return name;
    }

    public int getCustomCombatLevel() {
        return combatLevel;
    }

    public int getCombatLevel() {
        return combatLevel >= 0 ? combatLevel : getDefinitions().combatLevel;
    }

    public void setCombatLevel(final int level) {
        combatLevel = getDefinitions().combatLevel == level ? -1 : level;
        changedCombatLevel = true;
    }

    public String getName() {
        return name != null ? name : getDefinitions().name;
    }

    public void setName(final String string) {
        this.name = getDefinitions().name.equals(string) ? null : string;
        changedName = true;
    }

    public boolean hasChangedName() {
        return changedName;
    }

    public boolean hasChangedCombatLevel() {
        return changedCombatLevel;
    }

    public WorldTile getMiddleWorldTile() {
        final int size = getSize();
        return new WorldTile(getCoordFaceX(size), getCoordFaceY(size),
                getPlane());
    }

    public boolean isSpawned() {
        return spawned;
    }

    public void setSpawned(final boolean spawned) {
        this.spawned = spawned;
    }

    public boolean isNoDistanceCheck() {
        return noDistanceCheck;
    }

    public void setNoDistanceCheck(final boolean noDistanceCheck) {
        this.noDistanceCheck = noDistanceCheck;
    }

    public boolean withinDistance(final Player tile, final int distance) {
        return super.withinDistance(tile, distance);
    }

    /**
     * Gets the locked.
     *
     * @return The locked.
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Sets the locked.
     *
     * @param locked The locked to set.
     */
    public void setLocked(final boolean locked) {
        this.locked = locked;
    }
}
