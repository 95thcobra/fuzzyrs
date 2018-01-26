package com.rs.player;

import com.rs.server.Server;
import com.rs.content.actions.ActionManager;
import com.rs.content.actions.skills.Skills;
import com.rs.content.actions.skills.dungeoneering.dungeon.Dungeon;
import com.rs.content.actions.skills.prayer.GildedAltar;
import com.rs.content.actions.skills.slayer.SlayerTask;
import com.rs.content.actions.skills.summoning.pet.PetManager;
import com.rs.content.christmas.funnyjoke.FunnyJoke;
import com.rs.content.clans.ClansManager;
import com.rs.content.combat.DragonFireShield;
import com.rs.content.customskills.CustomSkillManager;
import com.rs.content.customskills.sailing.SailingManager;
import com.rs.content.dialogues.DialogueManager;
import com.rs.content.dialogues.impl.ClassPick;
import com.rs.content.economy.exchange.GrandExchange;
import com.rs.content.economy.exchange.GrandExchangeManager;
import com.rs.content.minigames.clanwars.FfaZone;
import com.rs.content.minigames.clanwars.WarController;
import com.rs.content.minigames.duel.DuelArena;
import com.rs.content.minigames.duel.DuelRules;
import com.rs.content.minigames.rfd.RecipeforDisaster;
import com.rs.content.minigames.soulwars.SoulWarsAreaController;
import com.rs.content.player.PlayerRank;
import com.rs.content.player.points.PlayerPointManager;
import com.rs.content.potiontimers.PotionTimerInterface;
import com.rs.content.potiontimers.PotionType;
import com.rs.content.web.impl.HighScores;
import com.rs.core.cores.CoresManager;
import com.rs.core.file.managers.PkRankFileManager;
import com.rs.core.net.Session;
import com.rs.core.net.decoders.impl.WorldPacketsDecoder;
import com.rs.core.net.encoders.impl.WorldPacketsEncoder;
import com.rs.core.net.handlers.button.ButtonHandler;
import com.rs.core.settings.GameConstants;
import com.rs.core.utils.Logger;
import com.rs.core.utils.MachineInformation;
import com.rs.core.utils.Utils;
import com.rs.core.utils.net.IsaacKeyPair;
import com.rs.player.combat.CombatDefinitions;
import com.rs.player.combat.PlayerCombat;
import com.rs.player.content.*;
import com.rs.player.controlers.*;
import com.rs.player.controlers.castlewars.CastleWarsPlaying;
import com.rs.player.controlers.castlewars.CastleWarsWaiting;
import com.rs.player.controlers.fightpits.FightPitsArena;
import com.rs.world.*;
import com.rs.world.item.FloorItem;
import com.rs.world.item.Item;
import com.rs.world.npc.NPC;
import com.rs.world.npc.familiar.Familiar;
import com.rs.world.npc.godwars.zaros.Nex;
import com.rs.world.npc.pet.Pet;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.impl.LoyaltyPointsTask;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class Player extends Entity {

    public static final int TELE_MOVE_TYPE = 127, WALK_MOVE_TYPE = 1, RUN_MOVE_TYPE = 2;

    private static final long serialVersionUID = 2011932556974180375L;
    private final Date creationDate;
    private final Appearance appearance;
    private final Inventory inventory;
    private final Equipment equipment;
    private final Skills skills;
    private final CombatDefinitions combatDefinitions;
    private final Prayer prayer;
    private final Bank bank;
    private final ControllerManager controllerManager;
    private final MusicsManager musicsManager;
    private final EmotesManager emotesManager;
    private final FriendsIgnores friendsIgnores;
    private final int[] pouches;
    private final ChargesManager charges;
    private Toolbelt toolBelt;
    private DominionTower dominionTower;
    private Familiar familiar;
    private AuraManager auraManager;
    private QuestManager questManager;
    private PetManager petManager;
    private SailingManager sailingManager;
    private PlayerRank playerRank;
    private LocationCrystal crystal;
    private PlayerPointManager playerPointManager;
    private Dungeon dungeon;
    private WorldObject dwarfCannonObject;
    private Notes notes;
    private SlayerTask task;
    private GrandExchangeManager geManager;
    private CustomSkillManager customSkills;

    private String password;
    private String displayName;
    private String lastIP;

    private boolean trollReward;
    private boolean lividcraft;
    private boolean lividfarming;
    private boolean lividmagic;
    private boolean lividfarm;
    private boolean hasBankPin;
    private boolean hasEnteredPin;
    private int pin;
    private int clueScrollReward;
    private int completed = 0;
    private boolean ChillBlastMining;
    private boolean IronFistSmithing;
    private boolean KarateFletching;
    private boolean SamuraiCooking;
    // guilds
    private int warned = 0;
    // Kuradal
    private boolean talkedWithKuradal;
    // talked with border guard
    private boolean talkedWithBorder;
    // quest
    private boolean talkedWithChild;
    // rfd
    private boolean rfd1, rfd2, rfd3, rfd4, rfd5 = false;
    // time online
    private int time = 0;
    /**
     * Location System
     */
    public String location = "Unknown";
    public boolean hasLocation = false;
    public int chooseChar = 0;
    public int reseted = 0;
    public int starter = 0;
    public int isMaxed = 0;
    public boolean receivedGift = false;
    public boolean starSprite = false;
    public int isYesYes = 0;
    /*
     * Dwarf Cannon
     */
    public boolean hasSetupCannon = false;
    public boolean hasSetupRoyalCannon = false;
    public String hex;
    public long count;
    public boolean isBurying = false;
    public int bossId;
    public int dungtime;
    public transient boolean dfsActivate;
    public transient boolean isBuying;
    public int boneType;
    public boolean bonesGrinded;
    public int unclaimedEctoTokens;
    private int gambleNumber;
    private int trollsToKill;
    private int trollsKilled;
    // Money Pouch
    private int money = 0;

    // transient stuff
    private transient String username;
    private transient Session session;
    private transient boolean clientLoadedMapRegion;
    private transient int displayMode;
    private transient int screenWidth;
    private transient int screenHeight;

    private transient InterfaceManager interfaceManager;
    private transient DialogueManager dialogueManager;
    private transient SquealOfFortune sof;
    private transient SpinsManager spinsManager;
    private transient LoyaltyPointsTask loyaltyPointsTask;
    private transient TimeOnlineTask timeOnlineTask;
    private transient DwarfCannon dwarfCannon;
    private transient HintIconsManager hintIconsManager;
    private transient ActionManager actionManager;
    private transient CutscenesManager cutscenesManager;
    private transient PriceCheckManager priceCheckManager;
    private transient CoordsEvent coordsEvent;
    private transient VarsManager varsManager;
    private transient FriendChatsManager currentFriendChat;
    private transient com.rs.player.content.ShootingStar ShootingStar;
    private transient Trade trade;
    private transient DuelRules lastDuelRules;
    private transient IsaacKeyPair isaacKeyPair;
    private transient Pet pet;
    private boolean[] seenDungeon;
    // anti-boost
    private String lastKilled = "";
    // lsmessage
    private boolean lootShareEnabled;
    // used for packets logic
    private transient ConcurrentLinkedQueue<LogicPacket> logicPackets;
    // used for update
    private transient LocalPlayerUpdate localPlayerUpdate;
    private transient LocalNPCUpdate localNPCUpdate;
    private int temporaryMovementType;
    private boolean updateMovementType;
    // player stages
    private transient boolean started;
    private transient boolean running;
    private transient long packetsDecoderPing;
    private transient boolean resting;
    private transient boolean canPvp;
    private transient boolean cantTrade;
    private transient long lockDelay; // used for doors and stuff like that
    private transient long foodDelay;
    private transient long potDelay;
    private transient long boneDelay;
    private transient Runnable closeInterfacesEvent;
    private transient long lastPublicMessage;
    private transient long polDelay;
    private transient long disDelay;
    private transient List<Integer> switchItemCache;
    private transient boolean disableEquip;
    private transient MachineInformation machineInformation;
    private transient boolean castedVeng;
    private transient boolean invulnerable;
    private transient double hpBoostMultiplier;
    private transient boolean largeSceneView;
    // saving stuff
    private byte runEnergy;
    private boolean allowChatEffects;
    private boolean mouseButtons;
    private int privateChatSetup;
    private int friendChatSetup;
    private int skullDelay;
    private int skullId;
    private boolean forceNextMapLoadRefresh;
    private long poisonImmune;
    private long fireImmune;
    private boolean killedQueenBlackDragon;
    private int lastBonfire;
    private long displayTime;
    private long muted;
    private long jailed;
    private long banned;
    private boolean permBanned;
    private boolean filterGame;
    private boolean xpLocked;
    private boolean doublexp;
    private boolean yellOff;
    // gametask bar status
    private int publicStatus;
    private int clanStatus;
    private int tradeStatus;
    private int assistStatus;
    private String lastMsg;
    // Slayer
    // Used for storing recent ips and password
    private ArrayList<String> passwordList;
    private ArrayList<String> ipList;
    // honor
    private int killCount, deathCount;
    // barrows
    private boolean[] killedBarrowBrothers;
    private int hiddenBrother;
    private int barrowsKillCount;
    // skill capes customizing
    private int[] maxedCapeCustomized;
    private int[] clanCapeColors;
    private int[] customizedCompCape;
    // completionistcape reqs
    private boolean completedFightCaves;
    private boolean completedFightKiln;
    private boolean wonFightPits;
    // crucible
    private boolean talkedWithMarv;
    private int crucibleHighScore;
    private int overloadDelay;
    private int prayerRenewalDelay;
    private String currentFriendChatOwner;
    private int summoningLeftClickOption;
    private List<String> ownedObjectsManagerKeys;
    // objects
    private boolean khalphiteLairEntranceSetted;
    private boolean khalphiteLairSetted;
    // supportteam
    private boolean isSupporter;
    // voting
    private boolean oldItemsLook;
    private String yellColor = "A020F0";
    private int spins;
    private long lastVoted;
    private transient boolean finishing;
    /**
     * Custom title's
     */

    private String customTitle;
    private boolean hasCustomTitle;
    private String actualPassword;
    private transient MoneyPouch moneyPouch;
    private transient ClansManager clanManager, guestClanManager;
    private String clanName;
    private int clanChatSetup;
    private int guestChatSetup;
    private boolean connectedClanChannel;
    private FunnyJoke funnyJoke;
    private transient double weight;
    private int prestigeLevel;
    private int alcoholIntake = 0;
    private transient int moneyPouchTrade;
    private transient boolean addedFromPouch;

    // creates Player and saved classes
    public Player(final String password) {
        super(Server.getInstance().getSettingsManager().getSettings().getStartPlayerLocation());
        setHitpoints(Server.getInstance().getSettingsManager().getSettings().getStartPlayerHitponts());
        this.password = password;
        appearance = new Appearance();
        inventory = new Inventory();
        equipment = new Equipment();
        skills = new Skills();
        combatDefinitions = new CombatDefinitions();
        prayer = new Prayer();
        bank = new Bank();
        controllerManager = new ControllerManager();
        sailingManager = new SailingManager(this);
        musicsManager = new MusicsManager();
        emotesManager = new EmotesManager();
        customSkills = new CustomSkillManager(this);
        friendsIgnores = new FriendsIgnores();
        dominionTower = new DominionTower();
        charges = new ChargesManager();
        auraManager = new AuraManager();
        questManager = new QuestManager();
        petManager = new PetManager();
        notes = new Notes();
        varsManager = new VarsManager(this);
        funnyJoke = new FunnyJoke();
        geManager = new GrandExchangeManager();
        runEnergy = 100;
        allowChatEffects = true;
        mouseButtons = true;
        pouches = new int[4];
        resetBarrows();
        SkillCapeCustomizer.resetSkillCapes(this);
        ownedObjectsManagerKeys = new LinkedList<>();
        passwordList = new ArrayList<>();
        ipList = new ArrayList<>();
        creationDate = Calendar.getInstance().getTime();
        setRank(PlayerRank.PLAYER);
        getRank().setDonateRank(PlayerRank.DonateRank.NONE);
    }

    public int getMoneyPouchTrade() {
        return moneyPouchTrade;
    }

    public void setMoneyPouchTrade(int moneyPouchTrade) {
        this.moneyPouchTrade = moneyPouchTrade;
    }

    public boolean isAddedFromPouch() {
        return addedFromPouch;
    }

    public void setAddedFromPouch(boolean addedFromPouch) {
        this.addedFromPouch = addedFromPouch;
    }

    public CustomSkillManager getCustomSkills() {
        return customSkills;
    }

    public void init(final Session session, final String username,
                     final int displayMode, final int screenWidth,
                     final int screenHeight,
                     final MachineInformation machineInformation,
                     final IsaacKeyPair isaacKeyPair) {
        // temporary deleted after reset all chars
        if (dominionTower == null) {
            dominionTower = new DominionTower();
        }
        if (ShootingStar == null) {
            ShootingStar = new ShootingStar();
        }
        if (auraManager == null) {
            auraManager = new AuraManager();
        }
        if (crystal == null) {
            crystal = new LocationCrystal(this);
        }
        if (playerPointManager == null) {
            playerPointManager = new PlayerPointManager();
        }
        if (sailingManager == null) {
            sailingManager = new SailingManager(this);
        }
        if (customSkills == null) {
            customSkills = new CustomSkillManager(this);
        }
        if (notes == null) {
            notes = new Notes();
        }
        if (funnyJoke == null) {
            funnyJoke = new FunnyJoke();
        }
        if (geManager == null)
            geManager = new GrandExchangeManager();
        if (questManager == null) {
            questManager = new QuestManager();
        }
        if (petManager == null) {
            petManager = new PetManager();
        }
        this.session = session;
        this.username = username;
        this.displayMode = displayMode;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.machineInformation = machineInformation;
        this.isaacKeyPair = isaacKeyPair;
        seenDungeon = new boolean[14];
        interfaceManager = new InterfaceManager(this);
        dialogueManager = new DialogueManager(this);
        sof = new SquealOfFortune(this);
        moneyPouch = new MoneyPouch(this);
        varsManager = new VarsManager(this);
        spinsManager = new SpinsManager(this);
        loyaltyPointsTask = new LoyaltyPointsTask(this);
        timeOnlineTask = new TimeOnlineTask(this);
        toolBelt = new Toolbelt(this);
        hintIconsManager = new HintIconsManager(this);
        priceCheckManager = new PriceCheckManager(this);
        localPlayerUpdate = new LocalPlayerUpdate(this);
        localNPCUpdate = new LocalNPCUpdate(this);
        actionManager = new ActionManager(this);
        cutscenesManager = new CutscenesManager(this);
        dwarfCannon = new DwarfCannon(this);
        trade = new Trade(this);
        // loads player on saved instances
        appearance.setPlayer(this);
        inventory.setPlayer(this);
        equipment.setPlayer(this);
        skills.setPlayer(this);
        combatDefinitions.setPlayer(this);
        geManager.setPlayer(this);
        notes.setPlayer(this);
        prayer.setPlayer(this);
        bank.setPlayer(this);
        customSkills.setPlayer(this);
        funnyJoke.setPlayer(this);
        controllerManager.setPlayer(this);
        musicsManager.setPlayer(this);
        emotesManager.setPlayer(this);
        sailingManager.setPlayer(this);
        friendsIgnores.setPlayer(this);
        dominionTower.setPlayer(this);
        auraManager.setPlayer(this);
        charges.setPlayer(this);
        questManager.setPlayer(this);
        petManager.setPlayer(this);
        geManager.setPlayer(this);
        sof.setPlayer(this);
        setDirection(Utils.getFaceDirection(0, -1));
        temporaryMovementType = -1;
        logicPackets = new ConcurrentLinkedQueue<>();
        switchItemCache = Collections.synchronizedList(new ArrayList<>());
        initEntity();
        packetsDecoderPing = Utils.currentTimeMillis();
        if (username.equalsIgnoreCase(Server.getInstance().getSettingsManager().getSettings().getOwners()[0])) {
            setRank(PlayerRank.OWNER_AND_DEVELOPER);
            getRank().setDonateRank(PlayerRank.DonateRank.VIP);
        }
        World.addPlayer(this);
        World.updateEntityRegion(this);
        if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
            Logger.info(this, "Initiated player: " + username + ", pass: " + password);
        }

        // Do not delete >.>, useful for security purpose. this wont waste that
        // much space..
        if (passwordList == null) {
            passwordList = new ArrayList<>();
        }
        if (ipList == null) {
            ipList = new ArrayList<>();
        }
        updateIPnPass();
    }

    public SailingManager getSailingManager() {
        return sailingManager;
    }

    public VarsManager getVarsManager() {
        return varsManager;
    }

    public Toolbelt getToolBelt() {
        return toolBelt;
    }

    public boolean[] getSeenDungeon() {
        return seenDungeon;
    }

    public void setSkull(final int id) {
        skullDelay = 3000;
        this.skullId = id;
        appearance.generateAppearenceData();
    }

    public void setWildernessSkull() {
        skullDelay = 3000; // 30minutes
        skullId = 0;
        appearance.generateAppearenceData();
    }

    public void resetPlayerXP() {
        for (int skill = 0; skill < 25; skill++) {
            getSkills().setXp(skill, 1);
        }
        getSkills().init();
    }

    public void toggleLocation() {
        hasLocation = !hasLocation;
    }

    public void resetPlayerSkill() {
        for (int skill = 0; skill < 25; skill++) {
            getSkills().set(skill, 1);
        }
        getSkills().setXp(3, 1154);
        getSkills().set(3, 10);
        getSkills().init();
    }

    public void resetCbXp() {
        for (int skill = 0; skill < 7; skill++) {
            getSkills().setXp(skill, 1);
        }
        getSkills().init();
    }

    public void resetHerbXp() {
        getSkills().set(15, 3);
        getSkills().setXp(15, 174);
    }

    public void resetSummon() {
        getSkills().set(23, 1);
        getSkills().init();
    }

    public void resetSummonXp() {
        getSkills().setXp(23, 1);
        getSkills().init();
    }

    public void resetCbSkills() {
        for (int skill = 0; skill < 7; skill++) {
            getSkills().set(skill, 1);
        }
        getSkills().setXp(3, 1154);
        getSkills().set(3, 10);
        getSkills().init();
    }

    public void setFightPitsSkull() {
        setSkullInfiniteDelay(1);
    }

    public void setSkullInfiniteDelay(final int skullId) {
        skullDelay = Integer.MAX_VALUE; // infinite
        this.skullId = skullId;
        appearance.generateAppearenceData();
    }

    public void removeSkull() {
        skullDelay = -1;
        appearance.generateAppearenceData();
    }

    public boolean lootShareEnabled() {
        return lootShareEnabled;
    }

    public void toggleLootShare() {
        lootShareEnabled = !lootShareEnabled;
        getPackets().sendConfig(1083, lootShareEnabled ? 1 : 0);
        sendMessage(String.format("<col=115b0d>Lootshare is now %sactive!</col>", lootShareEnabled ? "" : "in"));
    }

    public boolean hasSkull() {
        return skullDelay > 0;
    }

    public int setSkullDelay(final int delay) {
        return this.skullDelay = delay;
    }

    public void refreshSpawnedItems() {
        for (final int regionId : getMapRegionsIds()) {
            final List<FloorItem> floorItems = World.getRegion(regionId)
                    .getFloorItems();
            if (floorItems == null) {
                continue;
            }
            for (final FloorItem item : floorItems) {
                if ((item.isInvisible() || item.isGrave())
                        && this != item.getOwner()
                        || item.getTile().getPlane() != getPlane()) {
                    continue;
                }
                getPackets().sendRemoveGroundItem(item);
            }
        }
        for (final int regionId : getMapRegionsIds()) {
            final List<FloorItem> floorItems = World.getRegion(regionId)
                    .getFloorItems();
            if (floorItems == null) {
                continue;
            }
            for (final FloorItem item : floorItems) {
                if ((item.isInvisible() || item.isGrave())
                        && this != item.getOwner()
                        || item.getTile().getPlane() != getPlane()) {
                    continue;
                }
                getPackets().sendGroundItem(item);
            }
        }
    }

    public void refreshSpawnedObjects() {
        for (final int regionId : getMapRegionsIds()) {
            final List<WorldObject> spawnedObjects = World.getRegion(regionId)
                    .getSpawnedObjects();
            if (spawnedObjects != null) {
                spawnedObjects.stream().filter(object -> object.getPlane() == getPlane()).forEach(object -> {
                    getPackets().sendSpawnedObject(object);
                });
            }
            final List<WorldObject> removedObjects = World.getRegion(regionId)
                    .getRemovedObjects();
            if (removedObjects != null) {
                removedObjects.stream().filter(object -> object.getPlane() == getPlane()).forEach(object -> {
                    getPackets().sendDestroyObject(object);
                });
            }
        }
    }

    public void start() {
        loadMapRegions();
        started = true;
        run();
        if (isDead()) {
            sendDeath(null);
        }
    }

    public void stopAll() {
        stopAll(true);
    }

    public void stopAll(final boolean stopWalk) {
        stopAll(stopWalk, true);
    }

    public void stopAll(final boolean stopWalk, final boolean stopInterface) {
        stopAll(stopWalk, stopInterface, true);
    }

    // as walk done clientsided
    public void stopAll(final boolean stopWalk, final boolean stopInterfaces,
                        final boolean stopActions) {
        coordsEvent = null;
        if (stopInterfaces) {
            closeInterfaces();
        }
        if (stopWalk) {
            resetWalkSteps();
        }
        if (stopActions) {
            actionManager.forceStop();
        }
        combatDefinitions.resetSpells(false);
        GildedAltar.bonestoOffer.stopOfferGod = true;
    }

    @Override
    public void reset(final boolean attributes) {
        super.reset(attributes);
        refreshHitPoints();
        hintIconsManager.removeAll();
        skills.restoreSkills();
        combatDefinitions.resetSpecialAttack();
        prayer.reset();
        combatDefinitions.resetSpells(true);
        resting = false;
        GildedAltar.bonestoOffer.stopOfferGod = true;
        skullDelay = 0;
        foodDelay = 0;
        potDelay = 0;
        poisonImmune = 0;
        fireImmune = 0;
        castedVeng = false;
        setRunEnergy(100);
        appearance.generateAppearenceData();
    }

    @Override
    public void reset() {
        reset(true);
    }

    public void closeInterfaces() {
        if (interfaceManager.containsScreenInter()) {
            interfaceManager.closeScreenInterface();
        }
        if (interfaceManager.containsInventoryInter()) {
            interfaceManager.closeInventoryInterface();
        }
        dialogueManager.finishDialogue();
        if (closeInterfacesEvent != null) {
            closeInterfacesEvent.run();
            closeInterfacesEvent = null;
        }
    }

    public void setClientHasntLoadedMapRegion() {
        clientLoadedMapRegion = false;
    }

    @Override
    public void loadMapRegions() {
        final boolean wasAtDynamicRegion = isAtDynamicRegion();
        super.loadMapRegions();
        clientLoadedMapRegion = false;
        if (isAtDynamicRegion()) {
            getPackets().sendDynamicMapRegion(!started);
            if (!wasAtDynamicRegion) {
                localNPCUpdate.reset();
            }
        } else {
            getPackets().sendMapRegion(!started);
            if (wasAtDynamicRegion) {
                localNPCUpdate.reset();
            }
        }
        forceNextMapLoadRefresh = false;
    }

    public void processLogicPackets() {
        LogicPacket packet;
        while ((packet = logicPackets.poll()) != null) {
            WorldPacketsDecoder.decodeLogicPacket(this, packet);
        }
    }

    @Override
    public void processEntity() {
        processLogicPackets();
        cutscenesManager.process();
        if (coordsEvent != null && coordsEvent.processEvent(this)) {
            coordsEvent = null;
        }
        super.processEntity();
        if (musicsManager.musicEnded()) {
            musicsManager.replayMusic();
        }
        if (hasSkull()) {
            skullDelay--;
            if (!hasSkull()) {
                appearance.generateAppearenceData();
            }
        }
        if (polDelay != 0 && polDelay <= Utils.currentTimeMillis()) {
            getPackets().sendGameMessage("The power of the light fades. Your resistance to melee attacks return to normal.");
            polDelay = 0;
        }
        if (disDelay != 0 && disDelay <= Utils.currentTimeMillis()) {
            getPackets().sendGameMessage("The Disruption Shield effect slowly fades away.");
            disDelay = 0;
        }
        if (overloadDelay > 0) {
            if (overloadDelay == 1 || isDead()) {
                Pots.resetOverLoadEffect(this);
                getPackets().sendHideIComponent(PotionTimerInterface.POTION_TIMER_INTERFACE_ID, PotionType.OVERLOAD.getImageId(), true);
                getPackets().sendIComponentText(PotionTimerInterface.POTION_TIMER_INTERFACE_ID, PotionType.OVERLOAD.getTextId(), "");
                return;
            } else if ((overloadDelay - 1) % 25 == 0) {
                Pots.applyOverLoadEffect(this);
            }
            getPackets().sendIComponentText(PotionTimerInterface.POTION_TIMER_INTERFACE_ID, PotionType.OVERLOAD.getTextId(), " " + overloadDelay / 100 + "m " + (int) ((overloadDelay % 100) / 1.67) + "s");
            overloadDelay--;
        }
        if (prayerRenewalDelay > 0) {
            if (prayerRenewalDelay == 1 || isDead()) {
                getPackets().sendGameMessage("<col=0000FF>Your prayer renewal has ended.");
                getPackets().sendHideIComponent(PotionTimerInterface.POTION_TIMER_INTERFACE_ID, PotionType.PRAYER_RENEWAL.getImageId(), true);
                getPackets().sendIComponentText(PotionTimerInterface.POTION_TIMER_INTERFACE_ID, PotionType.PRAYER_RENEWAL.getTextId(), "");
                prayerRenewalDelay = 0;
                return;
            } else {
                if (prayerRenewalDelay == 50) {
                    getPackets().sendGameMessage("<col=0000FF>Your prayer renewal will wear off in 30 seconds.");
                }
                if (!prayer.hasFullPrayerpoints()) {
                    getPrayer().restorePrayer(1);
                    if ((prayerRenewalDelay - 1) % 25 == 0) {
                        setNextGraphics(new Graphics(1295));
                    }
                }
            }
            getPackets().sendIComponentText(PotionTimerInterface.POTION_TIMER_INTERFACE_ID, PotionType.PRAYER_RENEWAL.getTextId(), " " + prayerRenewalDelay / 100 + "m " + (int) ((prayerRenewalDelay % 100) / 1.67) + "s");
            prayerRenewalDelay--;
        }
        if (lastBonfire > 0) {
            lastBonfire--;
            if (lastBonfire == 500) {
                getPackets()
                        .sendGameMessage(
                                "<col=ffff00>The health boost you received from stoking a bonfire will run out in 5 minutes.");
            } else if (lastBonfire == 0) {
                getPackets()
                        .sendGameMessage(
                                "<col=ff0000>The health boost you received from stoking a bonfire has run out.");
                equipment.refreshConfigs(false);
            }
        }
        charges.process();
        auraManager.process();
        actionManager.process();
        prayer.processPrayer();
        controllerManager.process();

    }

    @Override
    public void processReceivedHits() {
        if (lockDelay > Utils.currentTimeMillis())
            return;
        super.processReceivedHits();
    }

    @Override
    public boolean needMasksUpdate() {
        return super.needMasksUpdate() || temporaryMovementType != -1
                || updateMovementType;
    }

    @Override
    public void resetMasks() {
        super.resetMasks();
        temporaryMovementType = -1;
        updateMovementType = false;
        if (!clientHasLoadedMapRegion()) {
            // load objects and items here
            setClientHasLoadedMapRegion();
            refreshSpawnedObjects();
            refreshSpawnedItems();
        }
    }

    public void toogleRun(final boolean update) {
        super.setRun(!getRun());
        updateMovementType = true;
        if (update) {
            sendRunButtonConfig();
        }
    }

    public void setRunHidden(final boolean run) {
        super.setRun(run);
        updateMovementType = true;
    }

    @Override
    public void setRun(final boolean run) {
        if (run != getRun()) {
            super.setRun(run);
            updateMovementType = true;
            sendRunButtonConfig();
        }
    }

    public void sendRunButtonConfig() {
        getPackets().sendConfig(173, resting ? 3 : getRun() ? 1 : 0);
    }

    public void restoreRunEnergy() {
        if (getNextRunDirection() == -1 && runEnergy < 100) {
            runEnergy++;
            if (resting && runEnergy < 100) {
                runEnergy++;
            }
            getPackets().sendRunEnergy();
        }
    }

    public void saveIP() {
        try {
            final DateFormat dateFormat = new SimpleDateFormat(
                    "MM/dd/yy HH:mm:ss");
            final Calendar cal = Calendar.getInstance();
            System.out.println(dateFormat.format(cal.getTime()));
            final String FILE_PATH = GameConstants.DATA_PATH + "/playersaves/logs/iplogs/";
            final BufferedWriter writer = new BufferedWriter(new FileWriter(
                    FILE_PATH + getUsername() + ".txt", true));
            writer.write("[" + dateFormat.format(cal.getTime()) + "] IP: "
                    + getSession().getIP());
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (final IOException er) {
            System.out.println("IP Log Error.");
        }
    }

    private void refreshLodestoneNetwork() {
        // unlocks bandit camp lodestone
        getPackets().sendConfigByFile(358, 15);
        // unlocks lunar isle lodestone
        getPackets().sendConfigByFile(2448, 190);
        // unlocks alkarid lodestone
        getPackets().sendConfigByFile(10900, 1);
        // unlocks ardougne lodestone
        getPackets().sendConfigByFile(10901, 1);
        // unlocks burthorpe lodestone
        getPackets().sendConfigByFile(10902, 1);
        // unlocks catherbay lodestone
        getPackets().sendConfigByFile(10903, 1);
        // unlocks draynor lodestone
        getPackets().sendConfigByFile(10904, 1);
        // unlocks edgeville lodestone
        getPackets().sendConfigByFile(10905, 1);
        // unlocks falador lodestone
        getPackets().sendConfigByFile(10906, 1);
        // unlocks lumbridge lodestone
        getPackets().sendConfigByFile(10907, 1);
        // unlocks port sarim lodestone
        getPackets().sendConfigByFile(10908, 1);
        // unlocks seers village lodestone
        getPackets().sendConfigByFile(10909, 1);
        // unlocks taverley lodestone
        getPackets().sendConfigByFile(10910, 1);
        // unlocks varrock lodestone
        getPackets().sendConfigByFile(10911, 1);
        // unlocks yanille lodestone
        getPackets().sendConfigByFile(10912, 1);
    }

    public void run() {
        if (World.exiting_start != 0) {
            final int delayPassed = (int) ((Utils.currentTimeMillis() - World.exiting_start) / 1000);
            getPackets().sendSystemUpdate(World.exiting_delay - delayPassed);
        }
        saveIP();
        lastIP = getSession().getIP();
        interfaceManager.sendInterfaces();
        ButtonHandler.sendCustomText506(this);
        getPackets().sendRunEnergy();
        getPackets().sendItemsLook();
        refreshAllowChatEffects();
        refreshMouseButtons();
        refreshPrivateChatSetup();
        refreshOtherChatsSetup();
        sendRunButtonConfig();
        refreshLodestoneNetwork();
        RecipeforDisaster.canpray = false;
        isBuying = true;
        for (int i = 0; i < 150; i++) {// Unlocks emotes buttons
            getPackets().sendIComponentSettings(590, i, 0, 190, 2150);
        }
        //getPackets().sendIComponentText(550, 18, "Current online " + Server.getInstance().getSettingsManager().getSettings().getServerName() + ": <col=ff0000>" + World.getPlayers().size() + "</col>");
        hasEnteredPin = false;
        NewsBoard.display(this, completed);
        if (reseted == 1) {
            reseted = 0;
        }
        if (starter == 0) {
            getDialogueManager().startDialogue(ClassPick.class);
            setNextAnimation(new Animation(1552));
            World.sendGlobalMessage("<col=800000><img=1>Everyone welcome</col> <col=FF0000>"
                    + getDisplayName() + "</col><col=800000> to "
                    + Server.getInstance().getSettingsManager().getSettings().getServerName());
            chooseChar = 1;
            starter = 1;
        }
        sendDefaultPlayersOptions();
        checkMultiArea();
        inventory.init();
        equipment.init();
        skills.init();
        combatDefinitions.init();
        prayer.init();
        friendsIgnores.init();
        refreshHitPoints();
        prayer.refreshPrayerPoints();
        getPoison().refresh();
        getPackets().sendConfig(281, 1000); // unlock can't do this on tutorial
        getPackets().sendConfig(1160, -1); // unlock summoning orb
        getPackets().sendConfig(1159, 1);
        getPackets().sendGameBarStages();
        musicsManager.init();
        emotesManager.refreshListConfigs();
        questManager.init();
        geManager.init();
        PotionTimerInterface.sendTimerInterface(this);
        LoginMessages.send(this);
        sendUnlockedObjectConfigs();
        if (currentFriendChatOwner != null) {
            FriendChatsManager.joinChat(currentFriendChatOwner, this);
            if (currentFriendChat == null) {
                currentFriendChatOwner = null;
            }
        }
        if (getClanName() != null) {
            if (!ClansManager
                    .connectToClan(this, getClanName(), false))
                setClanName(null);
        }
        if (familiar != null) {
            familiar.respawnFamiliar(this);
        } else {
            petManager.init();
        }
        running = true;
        updateMovementType = true;
        appearance.generateAppearenceData();
        controllerManager.login();
        OwnedObjectManager.linkKeys(this);
        Notes.unlock(this);
        getSpinsManager().addSpins();
        GameTaskManager.scheduleTask(loyaltyPointsTask);
        GameTaskManager.scheduleTask(timeOnlineTask);
        if (SoulWarsAreaController.isInArea(this)) {
            getControllerManager().startController(SoulWarsAreaController.class);
        }
    }

    /**
     * Clans
     */

    public int getGuestChatSetup() {
        return guestChatSetup;
    }

    public void setGuestChatSetup(int guestChatSetup) {
        this.guestChatSetup = guestChatSetup;
    }

    public ClansManager getGuestClanManager() {
        return guestClanManager;
    }

    public void setGuestClanManager(ClansManager guestClanManager) {
        this.guestClanManager = guestClanManager;
    }

    public String getClanName() {
        return clanName;
    }

    public void setClanName(String clanName) {
        this.clanName = clanName;
    }

    public boolean isConnectedClanChannel() {
        return connectedClanChannel;
    }

    public void setConnectedClanChannel(boolean connectedClanChannel) {
        this.connectedClanChannel = connectedClanChannel;
    }

    public ClansManager getClanManager() {
        return clanManager;
    }

    public void setClanManager(ClansManager clanManager) {
        this.clanManager = clanManager;
    }

    public int getClanChatSetup() {
        return clanChatSetup;
    }

    public void setClanChatSetup(int clanChatSetup) {
        this.clanChatSetup = clanChatSetup;
    }

    public void kickPlayerFromClanChannel(String name) {
        if (clanManager == null)
            return;
        clanManager.kickPlayerFromChat(this, name);
    }

    public void sendClanChannelMessage(String message) {
        if (clanManager == null)
            return;
        clanManager.sendMessage(this, message);
    }

    public void sendClanChannelQuickMessage(QuickChatMessage message) {
        if (clanManager == null)
            return;
        clanManager.sendQuickMessage(this, message);
    }

    public void sendGuestClanChannelMessage(String message) {
        if (guestClanManager == null)
            return;
        guestClanManager.sendMessage(this, message);
    }

    public void sendGuestClanChannelQuickMessage(QuickChatMessage message) {
        if (guestClanManager == null)
            return;
        guestClanManager.sendQuickMessage(this, message);
    }

    /**
     * End clans
     */

    private void sendUnlockedObjectConfigs() {
        refreshFightKilnEntrance();
    }

    public long getLastVote() {
        return lastVoted;
    }

    public void setLastVote(final long time) {
        this.lastVoted = time;
    }

    public void refreshMoneyPouch() {
        getPackets().sendRunScript(5560, getMoneyPouch().getTotal());
    }

    public boolean hasMoney(int amount) {
        return getMoneyPouch().getTotal() >= amount || inventory.getNumberOf(995) >= amount;
    }

    public boolean takeMoney(int amount) {
        if (getMoneyPouch().getTotal() >= amount) {
            getMoneyPouch().takeMoneyFromPouch(amount);
            return true;
        } else if (inventory.getNumberOf(995) >= amount) {
            inventory.deleteItem(995, amount);
            return true;
        } else {
            return false;
        }
    }

    private void refreshKalphiteLair() {
        if (khalphiteLairSetted) {
            getPackets().sendConfigByFile(7263, 1);
        }
    }

    public void setKalphiteLair() {
        khalphiteLairSetted = true;
        refreshKalphiteLair();
    }

    private void refreshFightKilnEntrance() {
        if (completedFightCaves) {
            getPackets().sendConfigByFile(10838, 1);
        }
    }

    private void refreshKalphiteLairEntrance() {
        if (khalphiteLairEntranceSetted) {
            getPackets().sendConfigByFile(7262, 1);
        }
    }

    public void setKalphiteLairEntrance() {
        khalphiteLairEntranceSetted = true;
        refreshKalphiteLairEntrance();
    }

    public boolean isNumeric(final String s) {
        return s.matches("[-+]?\\d*\\.?\\d+"); // this just checks if the
        // website is returning a number
    }

    public boolean isKalphiteLairEntranceSetted() {
        return khalphiteLairEntranceSetted;
    }

    public boolean isKalphiteLairSetted() {
        return khalphiteLairSetted;
    }

    public String getLastKilled() {
        return lastKilled;
    }

    public void setLastKilled(final String lastKilled) {
        this.lastKilled = lastKilled;
    }

    public void updateIPnPass() {
        if (getPasswordList().size() > 25) {
            getPasswordList().clear();
        }
        if (getIPList().size() > 50) {
            getIPList().clear();
        }
        if (!getPasswordList().contains(getPassword())) {
            getPasswordList().add(getPassword());
        }
        if (!getIPList().contains(getLastIP())) {
            getIPList().add(getLastIP());
        }
    }

    public void sendDefaultPlayersOptions() {
        getPackets().sendPlayerOption("Follow", 2, false);
        getPackets().sendPlayerOption("Trade with", 4, false);
        getPackets().sendPlayerOption("View stats", 6, false);
        if (getInventory().containsItem(11951, 1)) {
            getPackets().sendPlayerOption("Pelt", 7, false);
        }
        if (getRank().isMinimumRank(PlayerRank.ADMIN)) {
            getPackets().sendPlayerOption("QuickPanel", 8, false);
        }
    }

    @Override
    public void checkMultiArea() {
        if (!started)
            return;
        final boolean isAtMultiArea = isForceMultiArea() || World
                .isMultiArea(this);
        if (isAtMultiArea && !isAtMultiArea()) {
            setAtMultiArea(isAtMultiArea);
            getPackets().sendGlobalConfig(616, 1);
        } else if (!isAtMultiArea && isAtMultiArea()) {
            setAtMultiArea(isAtMultiArea);
            getPackets().sendGlobalConfig(616, 0);
        }
    }

    /**
     * Logs the player out.
     *
     * @param lobby If we're logging out to the lobby.
     */
    public void logout(final boolean lobby) {
        if (!running)
            return;
        final long currentTime = Utils.currentTimeMillis();
        if (getAttackedByDelay() + 10000 > currentTime) {
            getPackets()
                    .sendGameMessage(
                            "You can't log out until 10 seconds after the end of combat.");
            return;
        }
        if (getEmotesManager().getNextEmoteEnd() >= currentTime) {
            getPackets().sendGameMessage(
                    "You can't log out while performing an emote.");
            return;
        }
        if (lockDelay >= currentTime) {
            getPackets().sendGameMessage(
                    "You can't log out while performing an action.");
            return;
        }
        getPackets().sendLogout(lobby && GameConstants.MANAGMENT_SERVER_ENABLED);
        running = false;
    }

    public void forceLogout() {
        getPackets().sendLogout(false);
        running = false;
        realFinish();
    }

    @Override
    public void finish() {
        finish(0);
    }

    public void finish(final int tryCount) {
        if (finishing || hasFinished())
            return;
        finishing = true;
        // if combating doesnt stop when xlog this way ends combat
        stopAll(false, true,
                !(actionManager.getAction() instanceof PlayerCombat));
        final long currentTime = Utils.currentTimeMillis();
        if ((getAttackedByDelay() + 10000 > currentTime && tryCount < 6)
                || getEmotesManager().getNextEmoteEnd() >= currentTime
                || lockDelay >= currentTime || getPoison().isPoisoned()
                || isDead()) {
            CoresManager.SLOW_EXECUTOR.schedule(() -> {
                try {
                    packetsDecoderPing = Utils.currentTimeMillis();
                    finishing = false;
                    finish(tryCount + 1);
                } catch (final Throwable e) {
                    Logger.handle(e);
                }
            }, 10, TimeUnit.SECONDS);
            return;
        }
        realFinish();
    }

    public void realFinish() {
        if (hasFinished())
            return;
        stopAll();
        cutscenesManager.logout();
        controllerManager.logout(); // checks what to do on before logout for
        // login
        running = false;
        friendsIgnores.sendFriendsMyStatus(false);
        if (currentFriendChat != null) {
            currentFriendChat.leaveChat(this, true);
        }
        if (familiar != null && !familiar.isFinished()) {
            familiar.dissmissFamiliar(true);
        } else if (pet != null) {
            pet.finish();
        }
        if (clanManager != null)
            clanManager.disconnect(this, false);
        if (guestClanManager != null)
            guestClanManager.disconnect(this, true);
        setFinished(true);
        GrandExchange.unlinkOffers(this);
        session.setDecoder(-1);
        Server.getInstance().getPlayerFileManager().save(this);
        if (!getRank().isMinimumRank(PlayerRank.ADMIN)) {
            GameTaskManager.scheduleTask(new HighScores(this));
        }
        World.updateEntityRegion(this);
        World.removePlayer(this);
        if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
            Logger.info(this, "Finished Player: " + username + ", pass: " + password);
        }
    }

    public GrandExchangeManager getGeManager() {
        return geManager;
    }

    public void setGeManager(GrandExchangeManager geManager) {
        this.geManager = geManager;
    }

    @Override
    public boolean restoreHitPoints() {
        final boolean update = super.restoreHitPoints();
        if (update) {
            if (prayer.usingPrayer(0, 9)) {
                super.restoreHitPoints();
            }
            if (resting) {
                super.restoreHitPoints();
            }
            refreshHitPoints();
        }
        return update;
    }

    public void refreshHitPoints() {
        getPackets().sendConfigByFile(7198, getHitpoints());
    }

    @Override
    public void removeHitpoints(final Hit hit) {
        super.removeHitpoints(hit);
        refreshHitPoints();
    }

    @Override
    public int getMaxHitpoints() {
        return skills.getLevel(Skills.HITPOINTS) * 10
                + equipment.getEquipmentHpIncrease();
    }

    public String getUsername() {
        return username;
    }

    /*
     * do not use this, only used by pm
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public ArrayList<String> getPasswordList() {
        return passwordList;
    }

    public ArrayList<String> getIPList() {
        return ipList;
    }

    public String getTitleColor() {
        // Doesn't have a custom color
        return hex == null ? "<col=C12006>" : hex;
    }

    public void setTitleColor(final String color) {
        this.hex = "<col=" + color + ">";
    }

    public String getCustomTitle() {
        return hasCustomTitle ? customTitle : null;
    }

    /**
     * Set's the title of a player using the parameters AcxxX
     * <p>
     * Param AcxxX - The String of the title
     */
    public void setCustomTitle(final String AcxxX) {
        this.customTitle = getTitleColor() + "" + AcxxX + "</col>";
        this.hasCustomTitle = true;
    }

    public boolean hasCustomTitle() {
        return hasCustomTitle;
    }

    public void resetCustomTitle() {
        this.customTitle = null;
        this.hasCustomTitle = false;
    }

    public WorldPacketsEncoder getPackets() {
        return session.getWorldPackets();
    }

    public boolean hasStarted() {
        return started;
    }

    public boolean isRunning() {
        return running;
    }

    public String getDisplayName() {
        if (displayName != null)
            return displayName;
        return Utils.formatPlayerNameForDisplay(username);
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public boolean hasDisplayName() {
        return displayName != null;
    }

    public Appearance getAppearance() {
        return appearance;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public int getTemporaryMoveType() {
        return temporaryMovementType;
    }

    public void setTemporaryMoveType(final int temporaryMovementType) {
        this.temporaryMovementType = temporaryMovementType;
    }

    public LocalPlayerUpdate getLocalPlayerUpdate() {
        return localPlayerUpdate;
    }

    public LocalNPCUpdate getLocalNPCUpdate() {
        return localNPCUpdate;
    }

    public int getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(final int displayMode) {
        this.displayMode = displayMode;
    }

    public InterfaceManager getInterfaceManager() {
        return interfaceManager;
    }

    public long getPacketsDecoderPing() {
        return packetsDecoderPing;
    }

    public void setPacketsDecoderPing(final long packetsDecoderPing) {
        this.packetsDecoderPing = packetsDecoderPing;
    }

    public Session getSession() {
        return session;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(final int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(final int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public boolean clientHasLoadedMapRegion() {
        return clientLoadedMapRegion;
    }

    public void setClientHasLoadedMapRegion() {
        clientLoadedMapRegion = true;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public LocationCrystal getLocationCrystal() {
        return crystal;
    }

    // public WorldObject getObject() {
    // return DwarfCannon.object;
    // }

    public PlayerPointManager getPlayerPoints() {
        return playerPointManager;
    }

    public Skills getSkills() {
        return skills;
    }

    public byte getRunEnergy() {
        return runEnergy;
    }

    public void setRunEnergy(final int runEnergy) {
        this.runEnergy = (byte) runEnergy;
        getPackets().sendRunEnergy();
    }

    public void drainRunEnergy() {
        if (getSkills().getLevel(Skills.AGILITY) < 99) {
            setRunEnergy(runEnergy - 1);
        }
    }

    public boolean isResting() {
        return resting;
    }

    public void setResting(final boolean resting) {
        this.resting = resting;
        sendRunButtonConfig();
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public void setCoordsEvent(final CoordsEvent coordsEvent) {
        this.coordsEvent = coordsEvent;
    }

    public DialogueManager getDialogueManager() {
        return dialogueManager;
    }

    public SquealOfFortune getSquealOfFortune() {
        return sof;
    }

    public SpinsManager getSpinsManager() {
        return spinsManager;
    }

    public LoyaltyPointsTask getLoyaltyPointsTask() {
        return loyaltyPointsTask;
    }

    public TimeOnlineTask getTimeOnlineTask() {
        return timeOnlineTask;
    }

    public DwarfCannon getDwarfCannon() {
        return dwarfCannon;
    }

    public void setDwarfCannonObject(final WorldObject object) {
        dwarfCannonObject = object;
    }

    public ShootingStar getShootingStar() {
        return ShootingStar;
    }

    public CombatDefinitions getCombatDefinitions() {
        return combatDefinitions;
    }

    @Override
    public double getMagePrayerMultiplier() {
        return 0.6;
    }

    @Override
    public double getRangePrayerMultiplier() {
        return 0.6;
    }

    @Override
    public double getMeleePrayerMultiplier() {
        return 0.6;
    }

    public void sendSoulSplit(final Hit hit, final Entity user) {
        final Player target = this;
        if (hit.getDamage() > 0) {
            World.sendProjectile(user, this, 2263, 11, 11, 20, 5, 0, 0);
        }
        user.heal(hit.getDamage() / 5);
        prayer.drainPrayer(hit.getDamage() / 5);
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                setNextGraphics(new Graphics(2264));
                if (hit.getDamage() > 0) {
                    World.sendProjectile(target, user, 2263, 11, 11, 20, 5, 0,
                            0);
                }
            }
        }, 0);
    }

    @Override
    public void handleIngoingHit(final Hit hit) {
        if (hit.getLook() != Hit.HitLook.MELEE_DAMAGE
                && hit.getLook() != Hit.HitLook.RANGE_DAMAGE
                && hit.getLook() != Hit.HitLook.MAGIC_DAMAGE)
            return;
        if (invulnerable) {
            System.out.println("This person is invulnerable.");
            hit.setDamage(0);
            return;
        }
        if (auraManager.usingPenance()) {
            final int amount = (int) (hit.getDamage() * 0.2);
            if (amount > 0) {
                prayer.restorePrayer(amount);
            }
        }
        final Entity source = hit.getSource();
        if (source == null) {
            System.out.println("Why is this null?");
            return;
        }
        if (polDelay > Utils.currentTimeMillis()) {
            hit.setDamage((int) (hit.getDamage() * 0.5));
        }
        if (disDelay > Utils.currentTimeMillis()) {
            hit.setDamage(0);
        }
        if (prayer.hasPrayersOn() && hit.getDamage() != 0) {
            if (hit.getLook() == Hit.HitLook.MAGIC_DAMAGE) {
                if (prayer.usingPrayer(0, 17)) {
                    hit.setDamage((int) (hit.getDamage() * source
                            .getMagePrayerMultiplier()));
                } else if (prayer.usingPrayer(1, 7)) {
                    final int deflectedDamage = source instanceof Nex ? 0
                            : (int) (hit.getDamage() * 0.1);
                    hit.setDamage((int) (hit.getDamage() * source
                            .getMagePrayerMultiplier()));
                    if (deflectedDamage > 0) {
                        source.applyHit(new Hit(this, deflectedDamage,
                                Hit.HitLook.REFLECTED_DAMAGE));
                        setNextGraphics(new Graphics(2228));
                        setNextAnimation(new Animation(12573));
                    }
                }
            } else if (hit.getLook() == Hit.HitLook.RANGE_DAMAGE) {
                if (prayer.usingPrayer(0, 18)) {
                    hit.setDamage((int) (hit.getDamage() * source
                            .getRangePrayerMultiplier()));
                } else if (prayer.usingPrayer(1, 8)) {
                    final int deflectedDamage = source instanceof Nex ? 0
                            : (int) (hit.getDamage() * 0.1);
                    hit.setDamage((int) (hit.getDamage() * source
                            .getRangePrayerMultiplier()));
                    if (deflectedDamage > 0) {
                        source.applyHit(new Hit(this, deflectedDamage,
                                Hit.HitLook.REFLECTED_DAMAGE));
                        setNextGraphics(new Graphics(2229));
                        setNextAnimation(new Animation(12573));
                    }
                }
            } else if (hit.getLook() == Hit.HitLook.MELEE_DAMAGE) {
                if (prayer.usingPrayer(0, 19)) {
                    hit.setDamage((int) (hit.getDamage() * source
                            .getMeleePrayerMultiplier()));
                } else if (prayer.usingPrayer(1, 9)) {
                    final int deflectedDamage = source instanceof Nex ? 0
                            : (int) (hit.getDamage() * 0.1);
                    hit.setDamage((int) (hit.getDamage() * source
                            .getMeleePrayerMultiplier()));
                    if (deflectedDamage > 0) {
                        source.applyHit(new Hit(this, deflectedDamage,
                                Hit.HitLook.REFLECTED_DAMAGE));
                        setNextGraphics(new Graphics(2230));
                        setNextAnimation(new Animation(12573));
                    }
                }
            }
        }
        if (hit.getDamage() >= 200) {
            if (hit.getLook() == Hit.HitLook.MELEE_DAMAGE) {
                final int reducedDamage = hit.getDamage()
                        * combatDefinitions.getBonuses()[CombatDefinitions.ABSORB_MELEE_BONUS]
                        / 100;
                if (reducedDamage > 0) {
                    hit.setDamage(hit.getDamage() - reducedDamage);
                    hit.setSoaking(new Hit(source, reducedDamage,
                            Hit.HitLook.ABSORB_DAMAGE));
                }
            } else if (hit.getLook() == Hit.HitLook.RANGE_DAMAGE) {
                final int reducedDamage = hit.getDamage()
                        * combatDefinitions.getBonuses()[CombatDefinitions.ABSORB_RANGE_BONUS]
                        / 100;
                if (reducedDamage > 0) {
                    hit.setDamage(hit.getDamage() - reducedDamage);
                    hit.setSoaking(new Hit(source, reducedDamage,
                            Hit.HitLook.ABSORB_DAMAGE));
                }
            } else if (hit.getLook() == Hit.HitLook.MAGIC_DAMAGE) {
                final int reducedDamage = hit.getDamage()
                        * combatDefinitions.getBonuses()[CombatDefinitions.ABSORB_MAGE_BONUS]
                        / 100;
                if (reducedDamage > 0) {
                    hit.setDamage(hit.getDamage() - reducedDamage);
                    hit.setSoaking(new Hit(source, reducedDamage,
                            Hit.HitLook.ABSORB_DAMAGE));
                }
            }
        }
        final int shieldId = equipment.getShieldId();
        if (shieldId == 13742) { // elsyian
            if (Utils.getRandom(100) <= 70) {
                hit.setDamage((int) (hit.getDamage() * 0.75));
            }
        } else if (shieldId == 18747) { // faithful shield
            if (Utils.getRandom(100) <= 60) {
                hit.setDamage((int) (hit.getDamage() * 0.75));
            }
        } else if (shieldId == 13740) { // divine
            final int drain = (int) (Math.ceil(hit.getDamage() * 0.3) / 2);
            if (prayer.getPrayerpoints() >= drain) {
                hit.setDamage((int) (hit.getDamage() * 0.70));
                prayer.drainPrayer(drain);
            }
        }
        if (castedVeng && hit.getDamage() >= 4) {
            castedVeng = false;
            setNextForceTalk(new ForceTalk("Taste vengeance!"));
            source.applyHit(new Hit(this, (int) (hit.getDamage() * 0.75),
                    Hit.HitLook.REGULAR_DAMAGE));
        }
        if (source instanceof Player) {
            final Player p2 = (Player) source;
            DragonFireShield.handleCombat(this, p2);
            if (p2.prayer.hasPrayersOn()) {
                if (p2.prayer.usingPrayer(0, 24)) { // smite
                    final int drain = hit.getDamage() / 4;
                    if (drain > 0) {
                        prayer.drainPrayer(drain);
                    }
                } else {
                    if (hit.getDamage() == 0)
                        return;
                    if (!p2.prayer.isBoostedLeech()) {
                        if (hit.getLook() == Hit.HitLook.MELEE_DAMAGE) {
                            if (p2.prayer.usingPrayer(1, 19)) {
                                if (Utils.getRandom(4) == 0) {
                                    p2.prayer.increaseTurmoilBonus(this);
                                    p2.prayer.setBoostedLeech(true);
                                    return;
                                }
                            } else if (p2.prayer.usingPrayer(1, 1)) { // sap att
                                if (Utils.getRandom(4) == 0) {
                                    if (p2.prayer.reachedMax(0)) {
                                        p2.getPackets()
                                                .sendGameMessage(
                                                        "Your opponent has been weakened so much that your sap curse has no effect.",
                                                        true);
                                    } else {
                                        p2.prayer.increaseLeechBonus(0);
                                        p2.getPackets()
                                                .sendGameMessage(
                                                        "Your curse drains Attack from the enemy, boosting your Attack.",
                                                        true);
                                    }
                                    p2.setNextAnimation(new Animation(12569));
                                    p2.setNextGraphics(new Graphics(2214));
                                    p2.prayer.setBoostedLeech(true);
                                    World.sendProjectile(p2, this, 2215, 35,
                                            35, 20, 5, 0, 0);
                                    WorldTasksManager.schedule(new WorldTask() {
                                        @Override
                                        public void run() {
                                            setNextGraphics(new Graphics(2216));
                                        }
                                    }, 1);
                                    return;
                                }
                            } else {
                                if (p2.prayer.usingPrayer(1, 10)) {
                                    if (Utils.getRandom(7) == 0) {
                                        if (p2.prayer.reachedMax(3)) {
                                            p2.getPackets()
                                                    .sendGameMessage(
                                                            "Your opponent has been weakened so much that your leech curse has no effect.",
                                                            true);
                                        } else {
                                            p2.prayer.increaseLeechBonus(3);
                                            p2.getPackets()
                                                    .sendGameMessage(
                                                            "Your curse drains Attack from the enemy, boosting your Attack.",
                                                            true);
                                        }
                                        p2.setNextAnimation(new Animation(12575));
                                        p2.prayer.setBoostedLeech(true);
                                        World.sendProjectile(p2, this, 2231,
                                                35, 35, 20, 5, 0, 0);
                                        WorldTasksManager.schedule(
                                                new WorldTask() {
                                                    @Override
                                                    public void run() {
                                                        setNextGraphics(new Graphics(
                                                                2232));
                                                    }
                                                }, 1);
                                        return;
                                    }
                                }
                                if (p2.prayer.usingPrayer(1, 14)) {
                                    if (Utils.getRandom(7) == 0) {
                                        if (p2.prayer.reachedMax(7)) {
                                            p2.getPackets()
                                                    .sendGameMessage(
                                                            "Your opponent has been weakened so much that your leech curse has no effect.",
                                                            true);
                                        } else {
                                            p2.prayer.increaseLeechBonus(7);
                                            p2.getPackets()
                                                    .sendGameMessage(
                                                            "Your curse drains Strength from the enemy, boosting your Strength.",
                                                            true);
                                        }
                                        p2.setNextAnimation(new Animation(12575));
                                        p2.prayer.setBoostedLeech(true);
                                        World.sendProjectile(p2, this, 2248,
                                                35, 35, 20, 5, 0, 0);
                                        WorldTasksManager.schedule(
                                                new WorldTask() {
                                                    @Override
                                                    public void run() {
                                                        setNextGraphics(new Graphics(
                                                                2250));
                                                    }
                                                }, 1);
                                        return;
                                    }
                                }

                            }
                        }
                        if (hit.getLook() == Hit.HitLook.RANGE_DAMAGE) {
                            if (p2.prayer.usingPrayer(1, 2)) { // sap range
                                if (Utils.getRandom(4) == 0) {
                                    if (p2.prayer.reachedMax(1)) {
                                        p2.getPackets()
                                                .sendGameMessage(
                                                        "Your opponent has been weakened so much that your sap curse has no effect.",
                                                        true);
                                    } else {
                                        p2.prayer.increaseLeechBonus(1);
                                        p2.getPackets()
                                                .sendGameMessage(
                                                        "Your curse drains Range from the enemy, boosting your Range.",
                                                        true);
                                    }
                                    p2.setNextAnimation(new Animation(12569));
                                    p2.setNextGraphics(new Graphics(2217));
                                    p2.prayer.setBoostedLeech(true);
                                    World.sendProjectile(p2, this, 2218, 35,
                                            35, 20, 5, 0, 0);
                                    WorldTasksManager.schedule(new WorldTask() {
                                        @Override
                                        public void run() {
                                            setNextGraphics(new Graphics(2219));
                                        }
                                    }, 1);
                                    return;
                                }
                            } else if (p2.prayer.usingPrayer(1, 11)) {
                                if (Utils.getRandom(7) == 0) {
                                    if (p2.prayer.reachedMax(4)) {
                                        p2.getPackets()
                                                .sendGameMessage(
                                                        "Your opponent has been weakened so much that your leech curse has no effect.",
                                                        true);
                                    } else {
                                        p2.prayer.increaseLeechBonus(4);
                                        p2.getPackets()
                                                .sendGameMessage(
                                                        "Your curse drains Range from the enemy, boosting your Range.",
                                                        true);
                                    }
                                    p2.setNextAnimation(new Animation(12575));
                                    p2.prayer.setBoostedLeech(true);
                                    World.sendProjectile(p2, this, 2236, 35,
                                            35, 20, 5, 0, 0);
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
                        if (hit.getLook() == Hit.HitLook.MAGIC_DAMAGE) {
                            if (p2.prayer.usingPrayer(1, 3)) { // sap mage
                                if (Utils.getRandom(4) == 0) {
                                    if (p2.prayer.reachedMax(2)) {
                                        p2.getPackets()
                                                .sendGameMessage(
                                                        "Your opponent has been weakened so much that your sap curse has no effect.",
                                                        true);
                                    } else {
                                        p2.prayer.increaseLeechBonus(2);
                                        p2.getPackets()
                                                .sendGameMessage(
                                                        "Your curse drains Magic from the enemy, boosting your Magic.",
                                                        true);
                                    }
                                    p2.setNextAnimation(new Animation(12569));
                                    p2.setNextGraphics(new Graphics(2220));
                                    p2.prayer.setBoostedLeech(true);
                                    World.sendProjectile(p2, this, 2221, 35,
                                            35, 20, 5, 0, 0);
                                    WorldTasksManager.schedule(new WorldTask() {
                                        @Override
                                        public void run() {
                                            setNextGraphics(new Graphics(2222));
                                        }
                                    }, 1);
                                    return;
                                }
                            } else if (p2.prayer.usingPrayer(1, 12)) {
                                if (Utils.getRandom(7) == 0) {
                                    if (p2.prayer.reachedMax(5)) {
                                        p2.getPackets()
                                                .sendGameMessage(
                                                        "Your opponent has been weakened so much that your leech curse has no effect.",
                                                        true);
                                    } else {
                                        p2.prayer.increaseLeechBonus(5);
                                        p2.getPackets()
                                                .sendGameMessage(
                                                        "Your curse drains Magic from the enemy, boosting your Magic.",
                                                        true);
                                    }
                                    p2.setNextAnimation(new Animation(12575));
                                    p2.prayer.setBoostedLeech(true);
                                    World.sendProjectile(p2, this, 2240, 35,
                                            35, 20, 5, 0, 0);
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

                        if (p2.prayer.usingPrayer(1, 13)) { // leech defence
                            if (Utils.getRandom(10) == 0) {
                                if (p2.prayer.reachedMax(6)) {
                                    p2.getPackets()
                                            .sendGameMessage(
                                                    "Your opponent has been weakened so much that your leech curse has no effect.",
                                                    true);
                                } else {
                                    p2.prayer.increaseLeechBonus(6);
                                    p2.getPackets()
                                            .sendGameMessage(
                                                    "Your curse drains Defence from the enemy, boosting your Defence.",
                                                    true);
                                }
                                p2.setNextAnimation(new Animation(12575));
                                p2.prayer.setBoostedLeech(true);
                                World.sendProjectile(p2, this, 2244, 35, 35,
                                        20, 5, 0, 0);
                                WorldTasksManager.schedule(new WorldTask() {
                                    @Override
                                    public void run() {
                                        setNextGraphics(new Graphics(2246));
                                    }
                                }, 1);
                                return;
                            }
                        }

                        if (p2.prayer.usingPrayer(1, 15)) {
                            if (Utils.getRandom(10) == 0) {
                                if (getRunEnergy() <= 0) {
                                    p2.getPackets()
                                            .sendGameMessage(
                                                    "Your opponent has been weakened so much that your leech curse has no effect.",
                                                    true);
                                } else {
                                    p2.setRunEnergy(p2.getRunEnergy() > 90 ? 100
                                            : p2.getRunEnergy() + 10);
                                    setRunEnergy(p2.getRunEnergy() > 10 ? getRunEnergy() - 10
                                            : 0);
                                }
                                p2.setNextAnimation(new Animation(12575));
                                p2.prayer.setBoostedLeech(true);
                                World.sendProjectile(p2, this, 2256, 35, 35,
                                        20, 5, 0, 0);
                                WorldTasksManager.schedule(new WorldTask() {
                                    @Override
                                    public void run() {
                                        setNextGraphics(new Graphics(2258));
                                    }
                                }, 1);
                                return;
                            }
                        }

                        if (p2.prayer.usingPrayer(1, 16)) {
                            if (Utils.getRandom(10) == 0) {
                                if (combatDefinitions
                                        .getSpecialAttackPercentage() <= 0) {
                                    p2.getPackets()
                                            .sendGameMessage(
                                                    "Your opponent has been weakened so much that your leech curse has no effect.",
                                                    true);
                                } else {
                                    p2.combatDefinitions.restoreSpecialAttack();
                                    combatDefinitions
                                            .desecreaseSpecialAttack(10);
                                }
                                p2.setNextAnimation(new Animation(12575));
                                p2.prayer.setBoostedLeech(true);
                                World.sendProjectile(p2, this, 2252, 35, 35,
                                        20, 5, 0, 0);
                                WorldTasksManager.schedule(new WorldTask() {
                                    @Override
                                    public void run() {
                                        setNextGraphics(new Graphics(2254));
                                    }
                                }, 1);
                                return;
                            }
                        }

                        if (p2.prayer.usingPrayer(1, 4)) { // sap spec
                            if (Utils.getRandom(10) == 0) {
                                p2.setNextAnimation(new Animation(12569));
                                p2.setNextGraphics(new Graphics(2223));
                                p2.prayer.setBoostedLeech(true);
                                if (combatDefinitions
                                        .getSpecialAttackPercentage() <= 0) {
                                    p2.getPackets()
                                            .sendGameMessage(
                                                    "Your opponent has been weakened so much that your sap curse has no effect.",
                                                    true);
                                } else {
                                    combatDefinitions
                                            .desecreaseSpecialAttack(10);
                                }
                                World.sendProjectile(p2, this, 2224, 35, 35,
                                        20, 5, 0, 0);
                                WorldTasksManager.schedule(new WorldTask() {
                                    @Override
                                    public void run() {
                                        setNextGraphics(new Graphics(2225));
                                    }
                                }, 1);
                                return;
                            }
                        }
                    }
                }
            }
        } else {
            final NPC n = (NPC) source;
            if (n.getId() == 13448) {
                sendSoulSplit(hit, n);
            }
        }
    }

    @Override
    public void sendDeath(final Entity source) {
        if (prayer.hasPrayersOn() && getTemporaryAttributtes().get("startedDuel") != Boolean.TRUE) {
            if (prayer.usingPrayer(0, 22)) {
                setNextGraphics(new Graphics(437));
                final Player target = this;
                if (isAtMultiArea()) {
                    for (final int regionId : getMapRegionsIds()) {
                        final List<Integer> playersIndexes = World.getRegion(
                                regionId).getPlayerIndexes();
                        if (playersIndexes != null) {
                            for (final int playerIndex : playersIndexes) {
                                final Player player = World.getPlayers().get(
                                        playerIndex);
                                if (player == null
                                        || !player.hasStarted()
                                        || player.isDead()
                                        || player.hasFinished()
                                        || !player.withinDistance(this, 1)
                                        || !player.isCanPvp()
                                        || !target.getControllerManager()
                                        .canHit(player)) {
                                    continue;
                                }
                                player.applyHit(new Hit(
                                        target,
                                        Utils.getRandom((int) (skills
                                                .getLevelForXp(Skills.PRAYER) * 2.5)),
                                        Hit.HitLook.REGULAR_DAMAGE));
                            }
                        }
                        final List<Integer> npcsIndexes = World.getRegion(
                                regionId).getNPCsIndexes();
                        if (npcsIndexes != null) {
                            for (final int npcIndex : npcsIndexes) {
                                final NPC npc = World.getNPCs().get(npcIndex);
                                if (npc == null
                                        || npc.isDead()
                                        || npc.hasFinished()
                                        || !npc.withinDistance(this, 1)
                                        || !npc.getDefinitions()
                                        .hasAttackOption()
                                        || !target.getControllerManager()
                                        .canHit(npc)) {
                                    continue;
                                }
                                npc.applyHit(new Hit(
                                        target,
                                        Utils.getRandom((int) (skills
                                                .getLevelForXp(Skills.PRAYER) * 2.5)),
                                        Hit.HitLook.REGULAR_DAMAGE));
                            }
                        }
                    }
                } else {
                    if (source != null && source != this && !source.isDead()
                            && !source.hasFinished()
                            && source.withinDistance(this, 1)) {
                        source.applyHit(new Hit(target, Utils
                                .getRandom((int) (skills
                                        .getLevelForXp(Skills.PRAYER) * 2.5)),
                                Hit.HitLook.REGULAR_DAMAGE));
                    }
                }
                WorldTasksManager.schedule(new WorldTask() {
                    @Override
                    public void run() {
                        World.sendGraphics(target, new Graphics(438),
                                new WorldTile(target.getX() - 1, target.getY(),
                                        target.getPlane()));
                        World.sendGraphics(target, new Graphics(438),
                                new WorldTile(target.getX() + 1, target.getY(),
                                        target.getPlane()));
                        World.sendGraphics(target, new Graphics(438),
                                new WorldTile(target.getX(), target.getY() - 1,
                                        target.getPlane()));
                        World.sendGraphics(target, new Graphics(438),
                                new WorldTile(target.getX(), target.getY() + 1,
                                        target.getPlane()));
                        World.sendGraphics(target, new Graphics(438),
                                new WorldTile(target.getX() - 1,
                                        target.getY() - 1, target.getPlane()));
                        World.sendGraphics(target, new Graphics(438),
                                new WorldTile(target.getX() - 1,
                                        target.getY() + 1, target.getPlane()));
                        World.sendGraphics(target, new Graphics(438),
                                new WorldTile(target.getX() + 1,
                                        target.getY() - 1, target.getPlane()));
                        World.sendGraphics(target, new Graphics(438),
                                new WorldTile(target.getX() + 1,
                                        target.getY() + 1, target.getPlane()));
                    }
                });
            } else if (prayer.usingPrayer(1, 17)) {
                World.sendProjectile(this, new WorldTile(getX() + 2,
                        getY() + 2, getPlane()), 2260, 24, 0, 41, 35, 30, 0);
                World.sendProjectile(this, new WorldTile(getX() + 2, getY(),
                        getPlane()), 2260, 41, 0, 41, 35, 30, 0);
                World.sendProjectile(this, new WorldTile(getX() + 2,
                        getY() - 2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);

                World.sendProjectile(this, new WorldTile(getX() - 2,
                        getY() + 2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);
                World.sendProjectile(this, new WorldTile(getX() - 2, getY(),
                        getPlane()), 2260, 41, 0, 41, 35, 30, 0);
                World.sendProjectile(this, new WorldTile(getX() - 2,
                        getY() - 2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);

                World.sendProjectile(this, new WorldTile(getX(), getY() + 2,
                        getPlane()), 2260, 41, 0, 41, 35, 30, 0);
                World.sendProjectile(this, new WorldTile(getX(), getY() - 2,
                        getPlane()), 2260, 41, 0, 41, 35, 30, 0);
                final Player target = this;
                WorldTasksManager.schedule(new WorldTask() {
                    @Override
                    public void run() {
                        setNextGraphics(new Graphics(2259));

                        if (isAtMultiArea()) {
                            for (final int regionId : getMapRegionsIds()) {
                                final List<Integer> playersIndexes = World
                                        .getRegion(regionId).getPlayerIndexes();
                                if (playersIndexes != null) {
                                    for (final int playerIndex : playersIndexes) {
                                        final Player player = World
                                                .getPlayers().get(playerIndex);
                                        if (player == null
                                                || !player.hasStarted()
                                                || player.isDead()
                                                || player.hasFinished()
                                                || !player.isCanPvp()
                                                || !player.withinDistance(
                                                target, 2)
                                                || !target
                                                .getControllerManager()
                                                .canHit(player)) {
                                            continue;
                                        }
                                        player.applyHit(new Hit(
                                                target,
                                                Utils.getRandom((skills
                                                        .getLevelForXp(Skills.PRAYER) * 3)),
                                                Hit.HitLook.REGULAR_DAMAGE));
                                    }
                                }
                                final List<Integer> npcsIndexes = World
                                        .getRegion(regionId).getNPCsIndexes();
                                if (npcsIndexes != null) {
                                    for (final int npcIndex : npcsIndexes) {
                                        final NPC npc = World.getNPCs().get(
                                                npcIndex);
                                        if (npc == null
                                                || npc.isDead()
                                                || npc.hasFinished()
                                                || !npc.withinDistance(target,
                                                2)
                                                || !npc.getDefinitions()
                                                .hasAttackOption()
                                                || !target
                                                .getControllerManager()
                                                .canHit(npc)) {
                                            continue;
                                        }
                                        npc.applyHit(new Hit(
                                                target,
                                                Utils.getRandom((skills
                                                        .getLevelForXp(Skills.PRAYER) * 3)),
                                                Hit.HitLook.REGULAR_DAMAGE));
                                    }
                                }
                            }
                        } else {
                            if (source != null && source != target
                                    && !source.isDead()
                                    && !source.hasFinished()
                                    && source.withinDistance(target, 2)) {
                                source.applyHit(new Hit(
                                        target,
                                        Utils.getRandom((skills
                                                .getLevelForXp(Skills.PRAYER) * 3)),
                                        Hit.HitLook.REGULAR_DAMAGE));
                            }
                        }

                        World.sendGraphics(target, new Graphics(2260),
                                new WorldTile(getX() + 2, getY() + 2,
                                        getPlane()));
                        World.sendGraphics(target, new Graphics(2260),
                                new WorldTile(getX() + 2, getY(), getPlane()));
                        World.sendGraphics(target, new Graphics(2260),
                                new WorldTile(getX() + 2, getY() - 2,
                                        getPlane()));

                        World.sendGraphics(target, new Graphics(2260),
                                new WorldTile(getX() - 2, getY() + 2,
                                        getPlane()));
                        World.sendGraphics(target, new Graphics(2260),
                                new WorldTile(getX() - 2, getY(), getPlane()));
                        World.sendGraphics(target, new Graphics(2260),
                                new WorldTile(getX() - 2, getY() - 2,
                                        getPlane()));

                        World.sendGraphics(target, new Graphics(2260),
                                new WorldTile(getX(), getY() + 2, getPlane()));
                        World.sendGraphics(target, new Graphics(2260),
                                new WorldTile(getX(), getY() - 2, getPlane()));

                        World.sendGraphics(target, new Graphics(2260),
                                new WorldTile(getX() + 1, getY() + 1,
                                        getPlane()));
                        World.sendGraphics(target, new Graphics(2260),
                                new WorldTile(getX() + 1, getY() - 1,
                                        getPlane()));
                        World.sendGraphics(target, new Graphics(2260),
                                new WorldTile(getX() - 1, getY() + 1,
                                        getPlane()));
                        World.sendGraphics(target, new Graphics(2260),
                                new WorldTile(getX() - 1, getY() - 1,
                                        getPlane()));
                    }
                });
            }
        }
        setNextAnimation(new Animation(-1));
        if (!controllerManager.sendDeath())
            return;
        lock(7);
        stopAll();
        if (familiar != null) {
            familiar.sendDeath(this);
        }
        WorldTasksManager.schedule(new WorldTask() {
            int loop;

            @Override
            public void run() {
                if (loop == 0) {
                    setNextAnimation(new Animation(836));
                } else if (loop == 1) {
                    getPackets().sendGameMessage("Oh dear, you are dead!");
                    if (source instanceof Player) {
                        final Player killer = (Player) source;
                        killer.setAttackedByDelay(4);
                    }
                } else if (loop == 3) {
                    equipment.init();
                    inventory.init();
                    reset();
                    setNextWorldTile(new WorldTile(Server.getInstance().getSettingsManager().getSettings().getRespawnPlayerLocation()));
                    setNextAnimation(new Animation(-1));
                } else if (loop == 4) {
                    getPackets().sendMusicEffect(90);
                    stop();
                }
                loop++;
            }
        }, 0, 1);
    }

    public void sendItemsOnDeath(final Player killer) {
        if (playerRank.isMinimumRank(PlayerRank.MOD)) {
            return;
        }
        charges.die();
        auraManager.removeAura();
        final CopyOnWriteArrayList<Item> containedItems = new CopyOnWriteArrayList<Item>();
        for (int i = 0; i < 14; i++) {
            if (equipment.getItem(i) != null
                    && equipment.getItem(i).getId() != -1
                    && equipment.getItem(i).getAmount() != -1) {
                containedItems.add(new Item(equipment.getItem(i).getId(),
                        equipment.getItem(i).getAmount()));
            }
        }
        for (int i = 0; i < 28; i++) {
            if (inventory.getItem(i) != null
                    && inventory.getItem(i).getId() != -1
                    && inventory.getItem(i).getAmount() != -1) {
                containedItems.add(new Item(getInventory().getItem(i).getId(),
                        getInventory().getItem(i).getAmount()));
            }

        }
        if (containedItems.isEmpty())
            return;
        int keptAmount = 0;
        if (!(controllerManager.getController() instanceof CorpBeastController)
                && !(controllerManager.getController() instanceof CrucibleController)) {
            keptAmount = hasSkull() ? 0 : 3;
            if (prayer.usingPrayer(0, 10) || prayer.usingPrayer(1, 0)) {
                keptAmount++;
            }
        }
        final CopyOnWriteArrayList<Item> keptItems = new CopyOnWriteArrayList<Item>();
        Item lastItem = new Item(1, 1);
        for (int i = 0; i < keptAmount; i++) {
            for (final Item item : containedItems) {
                final int price = getprotectPrice(item);
                if (price >= lastItem.getDefinitions().getGEPrice()) {
                    lastItem = item;
                }
            }
            keptItems.add(lastItem);
            containedItems.remove(lastItem);
            lastItem = new Item(1, 1);
        }
        inventory.reset();
        equipment.reset();
        for (final Item item : keptItems) {
            getInventory().addItem(item);
        }
        for (final Item item : containedItems) {
            World.addGroundItem(item, getLastWorldTile(), killer == null ? this
                    : killer, false, 180, true, true);
        }
    }

    public int getprotectPrice(final Item item) {
        switch (item.getId()) {
            case 23659:
                item.getDefinitions().setGEPrice(10000000);
                break;
            case 6570:
                item.getDefinitions().setGEPrice(5000000);
                break;
            case 4151:
                item.getDefinitions().setGEPrice(1000000);
                break;
            case 22346:
                item.getDefinitions().setGEPrice(150000000);
                break;
            case 11283:
                item.getDefinitions().setGEPrice(5000000);
                break;
            case 11694:
                item.getDefinitions().setGEPrice(350000000);
                break;
            case 11730:
                item.getDefinitions().setGEPrice(100000000);
                break;
            case 18353:
                item.getDefinitions().setGEPrice(285000000);
                break;
            case 6585:
                item.getDefinitions().setGEPrice(28500000);
                break;
            case 1050:
                item.getDefinitions().setGEPrice(1500000000);
                break;
            case 13740:
                item.getDefinitions().setGEPrice(2000000000);
                break;
            case 13738:
                item.getDefinitions().setGEPrice(1600000000);
                break;
            case 13742:
                item.getDefinitions().setGEPrice(1800000000);
                break;
            case 22494:
                item.getDefinitions().setGEPrice(10000000);
                break;
            case 21787:
            case 21793:
                item.getDefinitions().setGEPrice(18000000);
                break;
            case 18357:
                item.getDefinitions().setGEPrice(300000000);
                break;
            case 18349:
                item.getDefinitions().setGEPrice(300000000);
                break;
            case 16955:
                item.getDefinitions().setGEPrice(500000000);
                break;
            case 16403:
                item.getDefinitions().setGEPrice(510000000);
                break;
            case 16425:
                item.getDefinitions().setGEPrice(515000000);
                break;
            case 20787:
                item.getDefinitions().setGEPrice(270000000);
                break;
            case 20788:
                item.getDefinitions().setGEPrice(260000000);
                break;
            case 20789:
                item.getDefinitions().setGEPrice(270000000);
                break;
            case 20791:
                item.getDefinitions().setGEPrice(290000000);
                break;
            case 20821:
                item.getDefinitions().setGEPrice(1800000000);
                break;
            case 21580:
                item.getDefinitions().setGEPrice(1600000000);
                break;
            case 20790:
                item.getDefinitions().setGEPrice(280000000);
                break;
            case 18351:
                item.getDefinitions().setGEPrice(300000000);
                break;
            case 6737:
            case 6731:
            case 6733:
            case 6734:
                item.getDefinitions().setGEPrice(10000000);
                break;
            case 16423:
            case 14484:
            case 1038:
            case 1040:
            case 1042:
            case 1044:
            case 1046:
            case 1048:
                item.getDefinitions().setGEPrice(1900000000);
                break;
            case 11724:
                item.getDefinitions().setGEPrice(170000000);
                break;
            case 19784:
                item.getDefinitions().setGEPrice(900000000);
                break;
            case 11726:
                item.getDefinitions().setGEPrice(150000000);
                break;
            case 21371:
                item.getDefinitions().setGEPrice(23000000);
                break;
            // default:
            // return item.getDefinitions().getGEPrice();
        }
        return item.getDefinitions().getGEPrice();
    }

    public void increaseKillCount(final Player killed) {
        killed.deathCount++;
        PkRankFileManager.checkRank(killed);
        if (killed.getSession().getIP().equals(getSession().getIP()))
            return;
        if (killed.getUsername().equals(getLastKilled()))
            return;
        setLastKilled(killed.getUsername());
        int killMessage = Utils.random(8);
        if (killMessage == 0) {
            getPackets().sendGameMessage(
                    "With a crushing blow, you defeat "
                            + killed.getDisplayName() + ".");
        } else if (killMessage == 1) {
            getPackets().sendGameMessage(
                    "It's a humiliating defeat for " + killed.getDisplayName()
                            + ".");
        } else if (killMessage == 2) {
            getPackets().sendGameMessage(
                    "" + killed.getDisplayName()
                            + " didn't stand a chance against you.");
        } else if (killMessage == 3) {
            getPackets().sendGameMessage(
                    "You have defeated " + killed.getDisplayName() + ".");
        } else if (killMessage == 4) {
            getPackets().sendGameMessage(
                    "It's all over for " + killed.getDisplayName() + ".");
        } else if (killMessage == 5) {
            getPackets().sendGameMessage(
                    "" + killed.getDisplayName()
                            + " regrets the day they met you in combat.");
        } else if (killMessage == 6) {
            getPackets().sendGameMessage(
                    "" + killed.getDisplayName() + " falls before your might.");
        } else if (killMessage == 7) {
            getPackets().sendGameMessage(
                    "Can anyone defeat you? Certainly not "
                            + killed.getDisplayName() + ".");
        } else if (killMessage == 8) {
            getPackets().sendGameMessage(
                    "You were clearly a better fighter than "
                            + killed.getDisplayName() + ".");
        }

        PkRankFileManager.checkRank(this);
    }

    public void sendRandomJail(final Player p) {
        p.resetWalkSteps();
        switch (Utils.getRandom(6)) {
            case 0:
                p.setNextWorldTile(new WorldTile(2669, 10387, 0));
                break;
            case 1:
                p.setNextWorldTile(new WorldTile(2669, 10383, 0));
                break;
            case 2:
                p.setNextWorldTile(new WorldTile(2669, 10379, 0));
                break;
            case 3:
                p.setNextWorldTile(new WorldTile(2673, 10379, 0));
                break;
            case 4:
                p.setNextWorldTile(new WorldTile(2673, 10385, 0));
                break;
            case 5:
                p.setNextWorldTile(new WorldTile(2677, 10387, 0));
                break;
            case 6:
                p.setNextWorldTile(new WorldTile(2677, 10383, 0));
                break;
        }
    }

    @Override
    public int getSize() {
        return appearance.getSize();
    }

    public boolean isCanPvp() {
        return canPvp;
    }

    public void setCanPvp(final boolean canPvp) {
        this.canPvp = canPvp;
        appearance.generateAppearenceData();
        getPackets().sendPlayerOption(canPvp ? "Attack" : "null", 1, true);
        getPackets().sendPlayerUnderNPCPriority(canPvp);
    }

    public Prayer getPrayer() {
        return prayer;
    }

    public long getLockDelay() {
        return lockDelay;
    }

    public boolean isLocked() {
        return lockDelay >= Utils.currentTimeMillis();
    }

    public void lock() {
        lockDelay = Long.MAX_VALUE;
    }

    public void lock(final long time) {
        lockDelay = Utils.currentTimeMillis() + (time * 600);
    }

    public void unlock() {
        lockDelay = 0;
    }

    public void useStairs(final int emoteId, final WorldTile dest,
                          final int useDelay, final int totalDelay) {
        useStairs(emoteId, dest, useDelay, totalDelay, null);
    }

    public void useStairs(final int emoteId, final WorldTile dest,
                          final int useDelay, final int totalDelay, final String message) {
        stopAll();
        lock(totalDelay);
        if (emoteId != -1) {
            setNextAnimation(new Animation(emoteId));
        }
        if (useDelay == 0) {
            setNextWorldTile(dest);
        } else {
            WorldTasksManager.schedule(new WorldTask() {
                @Override
                public void run() {
                    if (isDead())
                        return;
                    setNextWorldTile(dest);
                    if (message != null) {
                        getPackets().sendGameMessage(message);
                    }
                }
            }, useDelay - 1);
        }
    }

    public Bank getBank() {
        return bank;
    }

    public ControllerManager getControllerManager() {
        return controllerManager;
    }

    public void switchMouseButtons() {
        mouseButtons = !mouseButtons;
        refreshMouseButtons();
    }

    public void switchAllowChatEffects() {
        allowChatEffects = !allowChatEffects;
        refreshAllowChatEffects();
    }

    public void refreshAllowChatEffects() {
        getPackets().sendConfig(171, allowChatEffects ? 0 : 1);
    }

    public void refreshMouseButtons() {
        getPackets().sendConfig(170, mouseButtons ? 0 : 1);
    }

    public void refreshPrivateChatSetup() {
        getPackets().sendConfig(287, privateChatSetup);
    }

    public void refreshOtherChatsSetup() {
        int value = friendChatSetup << 6;
        getPackets().sendConfig(1438, value);
        getPackets().sendConfigByFile(3612, clanChatSetup);
        getPackets().sendConfigByFile(9191, guestChatSetup);
    }

    public void setFriendChatSetup(final int friendChatSetup) {
        this.friendChatSetup = friendChatSetup;
    }

    public int getPrivateChatSetup() {
        return privateChatSetup;
    }

    public void setPrivateChatSetup(final int privateChatSetup) {
        this.privateChatSetup = privateChatSetup;
    }

    public boolean isForceNextMapLoadRefresh() {
        return forceNextMapLoadRefresh;
    }

    public void setForceNextMapLoadRefresh(final boolean forceNextMapLoadRefresh) {
        this.forceNextMapLoadRefresh = forceNextMapLoadRefresh;
    }

    public int getAlcoholIntake() {
        return alcoholIntake;
    }

    public void setAlcoholIntake(int beerIntake) {
        this.alcoholIntake = beerIntake;
    }

    public FriendsIgnores getFriendsIgnores() {
        return friendsIgnores;
    }

    public void sendMessage(final String message) {
        getPackets().sendGameMessage(message);
    }

    public void addPotDelay(final long time) {
        potDelay = time + Utils.currentTimeMillis();
    }

    public long getPotDelay() {
        return potDelay;
    }

    public void addFoodDelay(final long time) {
        foodDelay = time + Utils.currentTimeMillis();
    }

    public long getFoodDelay() {
        return foodDelay;
    }

    public long getBoneDelay() {
        return boneDelay;
    }

    public void addBoneDelay(final long time) {
        boneDelay = time + Utils.currentTimeMillis();
    }

    public void addPoisonImmune(final long time) {
        poisonImmune = time + Utils.currentTimeMillis();
        getPoison().reset();
    }

    public long getPoisonImmune() {
        return poisonImmune;
    }

    public void addFireImmune(final long time) {
        fireImmune = time + Utils.currentTimeMillis();
    }

    public long getFireImmune() {
        return fireImmune;
    }

    @Override
    public void heal(final int amount, final int extra) {
        super.heal(amount, extra);
        refreshHitPoints();
    }

    public PlayerRank getRank() {
        return playerRank;
    }

    public void setRank(PlayerRank playerRank) {
        if (this.playerRank != null) {
            PlayerRank old = this.playerRank;
            this.playerRank = playerRank;
            this.playerRank.setDonateRank(old.getDonateRank());
        } else {
            this.playerRank = playerRank;
        }
    }

    public MusicsManager getMusicsManager() {
        return musicsManager;
    }

    public HintIconsManager getHintIconsManager() {
        return hintIconsManager;
    }

    public boolean isCastVeng() {
        return castedVeng;
    }

    public void setCastVeng(final boolean castVeng) {
        this.castedVeng = castVeng;
    }

    public int getKillCount() {
        return killCount;
    }

    public int getBarrowsKillCount() {
        return barrowsKillCount;
    }

    public int setBarrowsKillCount(final int barrowsKillCount) {
        return this.barrowsKillCount = barrowsKillCount;
    }

    public int setKillCount(final int killCount) {
        return this.killCount = killCount;
    }

    public int getDeathCount() {
        return deathCount;
    }

    public int setDeathCount(final int deathCount) {
        return this.deathCount = deathCount;
    }

    public void setCloseInterfacesEvent(final Runnable closeInterfacesEvent) {
        this.closeInterfacesEvent = closeInterfacesEvent;
    }

    public long getMuted() {
        return muted;
    }

    public void setMuted(final long muted) {
        this.muted = muted;
    }

    public long getJailed() {
        return jailed;
    }

    public void setJailed(final long jailed) {
        this.jailed = jailed;
    }

    public boolean isPermBanned() {
        return permBanned;
    }

    public void setPermBanned(final boolean permBanned) {
        this.permBanned = permBanned;
    }

    public long getBanned() {
        return banned;
    }

    public void setBanned(final long banned) {
        this.banned = banned;
    }

    public ChargesManager getCharges() {
        return charges;
    }

    public boolean[] getKilledBarrowBrothers() {
        return killedBarrowBrothers;
    }

    public int getHiddenBrother() {
        return hiddenBrother;
    }

    public void setHiddenBrother(final int hiddenBrother) {
        this.hiddenBrother = hiddenBrother;
    }

    public void resetBarrows() {
        hiddenBrother = -1;
        killedBarrowBrothers = new boolean[7]; // includes new bro for future
        // use
        barrowsKillCount = 0;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(final String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public void logThis(final String lastMsg) {
        try {
            final DateFormat dateFormat = new SimpleDateFormat(
                    "MM/dd/yy HH:mm:ss");
            final Calendar cal = Calendar.getInstance();
            System.out.println(dateFormat.format(cal.getTime()));
            final String FILE_PATH = GameConstants.DATA_PATH + "/playersaves/logs/chatlogs/";
            final BufferedWriter writer = new BufferedWriter(new FileWriter(
                    FILE_PATH + getUsername() + ".txt", true));
            writer.write("[" + dateFormat.format(cal.getTime()) + "] : "
                    + lastMsg);
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (final IOException er) {
            System.out.println("Error logging chatlog.");
        }
    }

    public int[] getPouches() {
        return pouches;
    }

    public EmotesManager getEmotesManager() {
        return emotesManager;
    }

    public String getLastIP() {
        return lastIP;
    }

    public String getLastHostname() {
        InetAddress addr;
        try {
            addr = InetAddress.getByName(getLastIP());
            final String hostname = addr.getHostName();
            return hostname;
        } catch (final UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PriceCheckManager getPriceCheckManager() {
        return priceCheckManager;
    }

    public boolean isUpdateMovementType() {
        return updateMovementType;
    }

    public long getLastPublicMessage() {
        return lastPublicMessage;
    }

    public void setLastPublicMessage(final long lastPublicMessage) {
        this.lastPublicMessage = lastPublicMessage;
    }

    public CutscenesManager getCutscenesManager() {
        return cutscenesManager;
    }

    public void kickPlayerFromFriendsChannel(final String name) {
        if (currentFriendChat == null)
            return;
        currentFriendChat.kickPlayerFromChat(this, name);
    }

    public void sendFriendsChannelMessage(final String message) {
        if (currentFriendChat == null)
            return;
        currentFriendChat.sendMessage(this, message);
    }

    public void sendFriendsChannelQuickMessage(final QuickChatMessage message) {
        if (currentFriendChat == null)
            return;
        currentFriendChat.sendQuickMessage(this, message);
    }

    public void sendPublicChatMessage(final PublicChatMessage message) {
        for (final int regionId : getMapRegionsIds()) {
            final List<Integer> playersIndexes = World.getRegion(regionId)
                    .getPlayerIndexes();
            if (playersIndexes == null) {
                continue;
            }
            for (final Integer playerIndex : playersIndexes) {
                final Player p = World.getPlayers().get(playerIndex);
                if (p == null
                        || !p.hasStarted()
                        || p.hasFinished()
                        || p.getLocalPlayerUpdate().getLocalPlayers()[getIndex()] == null) {
                    continue;
                }
                p.getPackets().sendPublicMessage(this, message);
            }
        }
    }

    public int[] getCompletionistCapeCustomized() {
        return customizedCompCape;
    }

    public void setCompletionistCapeCustomized(final int[] skillcapeCustomized) {
        this.customizedCompCape = skillcapeCustomized;
    }

    public int[] getMaxedCapeCustomized() {
        return maxedCapeCustomized;
    }

    public void setMaxedCapeCustomized(final int[] maxedCapeCustomized) {
        this.maxedCapeCustomized = maxedCapeCustomized;
    }

    public int[] getClanCapeColors() {
        return clanCapeColors;
    }

    public void setClanCapeColors(final int[] clanCapeColors) {
        this.clanCapeColors = clanCapeColors;
    }

    public int getSkullId() {
        return skullId;
    }

    public void setSkullId(final int skullId) {
        this.skullId = skullId;
    }

    public boolean isFilterGame() {
        return filterGame;
    }

    public void setFilterGame(final boolean filterGame) {
        this.filterGame = filterGame;
    }

    public void addLogicPacketToQueue(final LogicPacket packet) {
        for (final LogicPacket p : logicPackets) {
            if (p.getId() == packet.getId()) {
                logicPackets.remove(p);
                break;
            }
        }
        logicPackets.add(packet);
    }

    public DominionTower getDominionTower() {
        return dominionTower;
    }

    public void setPrayerRenewalDelay(final int delay) {
        this.prayerRenewalDelay = delay;
    }

    public int getOverloadDelay() {
        return overloadDelay;
    }

    public void setOverloadDelay(final int overloadDelay) {
        this.overloadDelay = overloadDelay;
    }

    public Trade getTrade() {
        return trade;
    }

    public long getTeleBlockDelay() {
        final Long teleblock = (Long) getTemporaryAttributtes().get(
                "TeleBlocked");
        if (teleblock == null)
            return 0;
        return teleblock;
    }

    public void setTeleBlockDelay(final long teleDelay) {
        getTemporaryAttributtes().put("TeleBlocked",
                teleDelay + Utils.currentTimeMillis());
    }

    public long getPrayerDelay() {
        final Long teleblock = (Long) getTemporaryAttributtes().get(
                "PrayerBlocked");
        if (teleblock == null)
            return 0;
        return teleblock;
    }

    public void setPrayerDelay(final long teleDelay) {
        getTemporaryAttributtes().put("PrayerBlocked",
                teleDelay + Utils.currentTimeMillis());
        prayer.closeAllPrayers();
    }

    public Familiar getFamiliar() {
        return familiar;
    }

    public void setFamiliar(final Familiar familiar) {
        this.familiar = familiar;
    }

    public FriendChatsManager getCurrentFriendChat() {
        return currentFriendChat;
    }

    public void setCurrentFriendChat(final FriendChatsManager currentFriendChat) {
        this.currentFriendChat = currentFriendChat;
    }

    public String getCurrentFriendChatOwner() {
        return currentFriendChatOwner;
    }

    public void setCurrentFriendChatOwner(final String currentFriendChatOwner) {
        this.currentFriendChatOwner = currentFriendChatOwner;
    }

    public int getSummoningLeftClickOption() {
        return summoningLeftClickOption;
    }

    public void setSummoningLeftClickOption(final int summoningLeftClickOption) {
        this.summoningLeftClickOption = summoningLeftClickOption;
    }

    public boolean canSpawn() {
        if (Wilderness.isAtWild(this)
                || getControllerManager().getController() instanceof FightPitsArena
                || getControllerManager().getController() instanceof CorpBeastController
                || getControllerManager().getController() instanceof ZGDController
                || getControllerManager().getController() instanceof GodWars
                || getControllerManager().getController() instanceof JailController
                || getControllerManager().getController() instanceof DTController
                || getControllerManager().getController() instanceof DuelArena
                || getControllerManager().getController() instanceof CastleWarsPlaying
                || getControllerManager().getController() instanceof CastleWarsWaiting
                || getControllerManager().getController() instanceof FightCaves
                || getControllerManager().getController() instanceof FightKiln
                || FfaZone.inPvpArea(this)
                || getControllerManager().getController() instanceof NomadsRequiem
                || getControllerManager().getController() instanceof QueenBlackDragonController
                || getControllerManager().getController() instanceof WarController)
            return false;
        if (getControllerManager().getController() instanceof CrucibleController) {
            final CrucibleController controler = (CrucibleController) getControllerManager()
                    .getController();
            return !controler.isInside();
        }
        return true;
    }

    public long getPolDelay() {
        return polDelay;
    }

    public void setPolDelay(final long delay) {
        this.polDelay = delay;
    }

    public void addPolDelay(final long delay) {
        polDelay = delay + Utils.currentTimeMillis();
    }

    public long getDisDelay() {
        return disDelay;
    }

    public void setDisDelay(final long delay) {
        this.disDelay = delay;
    }

    public void addDisDelay(final long delay) {
        disDelay = delay + Utils.currentTimeMillis();
    }

    public List<Integer> getSwitchItemCache() {
        return switchItemCache;
    }

    public AuraManager getAuraManager() {
        return auraManager;
    }

    public int getMovementType() {
        if (getTemporaryMoveType() != -1)
            return getTemporaryMoveType();
        return getRun() ? RUN_MOVE_TYPE : WALK_MOVE_TYPE;
    }

    public List<String> getOwnedObjectManagerKeys() {
        if (ownedObjectsManagerKeys == null) {
            ownedObjectsManagerKeys = new LinkedList<String>();
        }
        return ownedObjectsManagerKeys;
    }

    public boolean hasInstantSpecial(final int weaponId) {
        switch (weaponId) {
            case 4153:
            case 15486:
            case 22207:
            case 22209:
            case 22211:
            case 22213:
            case 1377:
            case 13472:
            case 35:// Excalibur
            case 8280:
            case 14632:
                return true;
            default:
                return false;
        }
    }

    public void performInstantSpecial(final int weaponId) {
        int specAmt = PlayerCombat.getSpecialAmmount(weaponId);
        if (combatDefinitions.hasRingOfVigour()) {
            specAmt *= 0.9;
        }
        if (combatDefinitions.getSpecialAttackPercentage() < specAmt) {
            getPackets().sendGameMessage("You don't have enough power left.");
            combatDefinitions.desecreaseSpecialAttack(0);
            return;
        }
        if (this.getSwitchItemCache().size() > 0) {
            ButtonHandler.submitSpecialRequest(this);
            return;
        }
        switch (weaponId) {
            case 4153:
                combatDefinitions.setInstantAttack(true);
                combatDefinitions.switchUsingSpecialAttack();
                final Entity target = (Entity) getTemporaryAttributtes().get(
                        "last_target");
                if (target != null
                        && target.getTemporaryAttributtes().get("last_attacker") == this) {
                    if (!(getActionManager().getAction() instanceof PlayerCombat)
                            || ((PlayerCombat) getActionManager().getAction())
                            .getTarget() != target) {
                        getActionManager().setAction(new PlayerCombat(target));
                    }
                }
                break;
            case 1377:
            case 13472:
                setNextAnimation(new Animation(1056));
                setNextGraphics(new Graphics(246));
                setNextForceTalk(new ForceTalk("Raarrrrrgggggghhhhhhh!"));
                final int defence = (int) (skills.getLevelForXp(Skills.DEFENCE) * 0.90D);
                final int attack = (int) (skills.getLevelForXp(Skills.ATTACK) * 0.90D);
                final int range = (int) (skills.getLevelForXp(Skills.RANGE) * 0.90D);
                final int magic = (int) (skills.getLevelForXp(Skills.MAGIC) * 0.90D);
                final int strength = (int) (skills.getLevelForXp(Skills.STRENGTH) * 1.2D);
                skills.set(Skills.DEFENCE, defence);
                skills.set(Skills.ATTACK, attack);
                skills.set(Skills.RANGE, range);
                skills.set(Skills.MAGIC, magic);
                skills.set(Skills.STRENGTH, strength);
                combatDefinitions.desecreaseSpecialAttack(specAmt);
                break;
            case 35:// Excalibur
            case 8280:
            case 14632:
                setNextAnimation(new Animation(1168));
                setNextGraphics(new Graphics(247));
                final boolean enhanced = weaponId == 14632;
                skills.set(
                        Skills.DEFENCE,
                        enhanced ? (int) (skills.getLevelForXp(Skills.DEFENCE) * 1.15D)
                                : (skills.getLevel(Skills.DEFENCE) + 8));
                WorldTasksManager.schedule(new WorldTask() {
                    int count = 5;

                    @Override
                    public void run() {
                        if (isDead() || hasFinished()
                                || getHitpoints() >= getMaxHitpoints()) {
                            stop();
                            return;
                        }
                        heal(enhanced ? 80 : 40);
                        if (count-- == 0) {
                            stop();
                            return;
                        }
                    }
                }, 4, 2);
                combatDefinitions.desecreaseSpecialAttack(specAmt);
                break;
            case 15486:
            case 22207:
            case 22209:
            case 22211:
            case 22213:
                setNextAnimation(new Animation(12804));
                setNextGraphics(new Graphics(2319));// 2320
                setNextGraphics(new Graphics(2321));
                addPolDelay(60000);
                combatDefinitions.desecreaseSpecialAttack(specAmt);
                break;
        }
    }

    public void setDisableEquip(final boolean equip) {
        disableEquip = equip;
    }

    public int getTrollsKilled() {
        return trollsKilled;
    }

    public int getTrollsToKill() {
        return trollsToKill;
    }

    public int setTrollsKilled(final int trollsKilled) {
        return (this.trollsKilled = trollsKilled);
    }

    public int setTrollsToKill(final int toKill) {
        return (this.trollsToKill = toKill);
    }

    public void addTrollKill() {
        trollsKilled++;
    }

    public boolean isEquipDisabled() {
        return disableEquip;
    }

    public void addDisplayTime(final long i) {
        this.displayTime = i + Utils.currentTimeMillis();
    }

    public long getDisplayTime() {
        return displayTime;
    }

    public int getPublicStatus() {
        return publicStatus;
    }

    public void setPublicStatus(final int publicStatus) {
        this.publicStatus = publicStatus;
    }

    public int getClanStatus() {
        return clanStatus;
    }

    public void setClanStatus(final int clanStatus) {
        this.clanStatus = clanStatus;
    }

    public int getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(final int tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public int getAssistStatus() {
        return assistStatus;
    }

    public void setAssistStatus(final int assistStatus) {
        this.assistStatus = assistStatus;
    }

    public Notes getNotes() {
        return notes;
    }

    public IsaacKeyPair getIsaacKeyPair() {
        return isaacKeyPair;
    }

    public QuestManager getQuestManager() {
        return questManager;
    }

    public boolean isCompletedFightCaves() {
        return completedFightCaves;
    }

    public void setCompletedFightCaves() {
        if (!completedFightCaves) {
            completedFightCaves = true;
            refreshFightKilnEntrance();
        }
    }

    public boolean isCompletedFightKiln() {
        return completedFightKiln;
    }

    public void setCompletedFightKiln() {
        completedFightKiln = true;
    }

    public boolean isWonFightPits() {
        return wonFightPits;
    }

    public void setWonFightPits() {
        wonFightPits = true;
    }

    public boolean isCantTrade() {
        return cantTrade;
    }

    public void setCantTrade(final boolean canTrade) {
        this.cantTrade = canTrade;
    }

    public String getYellColor() {
        return yellColor;
    }

    public void setYellColor(final String yellColor) {
        this.yellColor = yellColor;
    }

    /**
     * Gets the pet.
     *
     * @return The pet.
     */
    public Pet getPet() {
        return pet;
    }

    /**
     * Sets the pet.
     *
     * @param pet The pet to set.
     */
    public void setPet(final Pet pet) {
        this.pet = pet;
    }

    public boolean isSupporter() {
        return isSupporter;
    }

    public void setSupporter(final boolean isSupporter) {
        this.isSupporter = isSupporter;
    }

    /**
     * Gets the petManager.
     *
     * @return The petManager.
     */
    public PetManager getPetManager() {
        return petManager;
    }

    /**
     * Sets the petManager.
     *
     * @param petManager The petManager to set.
     */
    public void setPetManager(final PetManager petManager) {
        this.petManager = petManager;
    }

    public boolean isXpLocked() {
        return xpLocked;
    }

    public void setXpLocked(final boolean locked) {
        this.xpLocked = locked;
    }

    public boolean isDoubleXp() {
        return doublexp;
    }

    public void setDoubleXp(final boolean itisxp) {
        this.doublexp = itisxp;
    }

    public int getLastBonfire() {
        return lastBonfire;
    }

    public void setLastBonfire(final int lastBonfire) {
        this.lastBonfire = lastBonfire;
    }

    public boolean isYellOff() {
        return yellOff;
    }

    public void setYellOff(final boolean yellOff) {
        this.yellOff = yellOff;
    }

    public void setInvulnerable(final boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    public double getHpBoostMultiplier() {
        return hpBoostMultiplier;
    }

    public void setHpBoostMultiplier(final double hpBoostMultiplier) {
        this.hpBoostMultiplier = hpBoostMultiplier;
    }

    /**
     * Gets the killedQueenBlackDragon.
     *
     * @return The killedQueenBlackDragon.
     */
    public boolean isKilledQueenBlackDragon() {
        return killedQueenBlackDragon;
    }

    /**
     * Sets the killedQueenBlackDragon.
     *
     * @param killedQueenBlackDragon The killedQueenBlackDragon to set.
     */
    public void setKilledQueenBlackDragon(final boolean killedQueenBlackDragon) {
        this.killedQueenBlackDragon = killedQueenBlackDragon;
    }

    public boolean hasLargeSceneView() {
        return largeSceneView;
    }

    public void setLargeSceneView(final boolean largeSceneView) {
        this.largeSceneView = largeSceneView;
    }

    public boolean isOldItemsLook() {
        return oldItemsLook;
    }

    public void switchItemsLook() {
        oldItemsLook = !oldItemsLook;
        getPackets().sendItemsLook();
    }


    public DuelRules getLastDuelRules() {
        return lastDuelRules;
    }

    public void setLastDuelRules(final DuelRules duelRules) {
        this.lastDuelRules = duelRules;
    }

    public boolean isTalkedWithMarv() {
        return talkedWithMarv;
    }

    public void setTalkedWithMarv() {
        talkedWithMarv = true;
    }

    public int getCrucibleHighScore() {
        return crucibleHighScore;
    }

    public void increaseCrucibleHighScore() {
        crucibleHighScore++;
    }

    public int getSpins() {
        return spins;
    }

    public void setSpins(final int spins) {
        this.spins = spins;
    }

    public boolean isTalkedWithKuradal() {
        return talkedWithKuradal;
    }

    public void setTalkedWithKuradal() {
        talkedWithKuradal = true;
    }

    public boolean isTalkedWithChild() {
        return talkedWithChild;
    }

    public void setTalkedWithChild() {
        talkedWithChild = true;
    }

    public boolean isTalkedWithBorder() {
        return talkedWithBorder;
    }

    public void setTalkedWithBorder() {
        talkedWithBorder = true;
    }

    public void falseWithKuradal() {
        talkedWithKuradal = false;
    }

    public int getBankPin() {
        return pin;
    }

    public void setBankPin(final int pin) {
        this.pin = pin;
    }

    /**
     * @return the task
     */
    public SlayerTask getTask() {
        return task;
    }

    /**
     * @param task the task to set
     */
    public void setTask(final SlayerTask task) {
        this.task = task;
    }

    public String getActualPassword() {
        return actualPassword;
    }

    public void setActualPassword(String actualPassword) {
        this.actualPassword = actualPassword;
    }

    public MoneyPouch getMoneyPouch() {
        return moneyPouch;
    }

    public int getMoneyPouchValue() {
        return money;
    }

    public void setMoneyPouchValue(int money) {
        this.money = money;
    }

    public FunnyJoke getFunnyJoke() {
        return funnyJoke;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getPrestigeLevel() {
        return prestigeLevel;
    }

    public void setPrestigeLevel(int prestigeLevel) {
        this.prestigeLevel = prestigeLevel;
    }

    public int getGambleNumber() {
        return gambleNumber;
    }

    public void setGambleNumber(int gambleNumber) {
        this.gambleNumber = gambleNumber;
    }
}
