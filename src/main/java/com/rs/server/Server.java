package com.rs.server;

import com.rs.content.actions.skills.fishing.FishingSpotsHandler;
import com.rs.content.christmas.funnyjoke.FunnyJokeHandler;
import com.rs.content.commands.CommandManager;
import com.rs.content.cutscenes.CutsceneHandler;
import com.rs.content.economy.exchange.GrandExchange;
import com.rs.content.economy.exchange.GrandExchangePriceManager;
import com.rs.content.economy.exchange.GrandExchangeUnlimitedItems;
import com.rs.content.economy.shops.ShopsManager;
import com.rs.content.items.WeightManager;
import com.rs.content.staff.actions.StaffActionManager;
import com.rs.server.engine.GameEngine;
import com.rs.server.engine.NetworkEngine;
import com.rs.core.cache.Cache;
import com.rs.core.cache.loaders.ItemsEquipIds;
import com.rs.core.cores.CoresManager;
import com.rs.core.file.data.map.MapArchiveKeys;
import com.rs.core.file.data.map.MapAreas;
import com.rs.core.file.data.map.ObjectSpawnsFileManager;
import com.rs.core.file.data.npc.*;
import com.rs.player.Player;
import com.rs.server.file.impl.*;
import com.rs.server.file.impl.PkRankFileManager;
import com.rs.utils.Logger;
import com.rs.utils.file.AutoBackup;
import com.rs.utils.file.MusicHints;
import com.rs.utils.huffman.Huffman;
import com.rs.utils.item.ItemBonuses;
import com.rs.utils.item.ItemExamines;
import com.rs.utils.tools.Panel;
import com.rs.world.region.RegionBuilder;
import com.rs.world.World;
import com.rs.world.npc.combat.CombatScriptsHandler;
import com.rs.task.gametask.GameTaskManager;
import lombok.*;

import java.io.IOException;


/**
 * Created by Fuzzy 1/25/2018
 */
@RequiredArgsConstructor
@Getter
public final class Server {

    private final SettingsManager settingsManager;
    private final GameEngine gameEngine;
    private final NetworkEngine networkEngine;
    private final PlayerFileManager playerFileManager;
    private final DTRankFileManager dtRankFileManager;
    private final IPBanFileManager ipBanFileManager;
    private final DisplayNamesFileManager displayNamesFileManager;
    private final ClanFilesManager clanFilesManager;
    private final GrandExchangeFileManager grandExchangeFileManager;
    private final PkRankFileManager pkRankFileManager;
    private final GameTaskManager gameTaskManager;
    private final CommandManager commandManager;

    @Setter(AccessLevel.PRIVATE)
    private long startTime;

    public void start() throws IllegalAccessException, InstantiationException, IOException, ClassNotFoundException {
        long currentTime = System.currentTimeMillis();
        getSettingsManager().init();
        getCommandManager().init();
        Cache.init();
        ItemsEquipIds.init();
        Huffman.init();
        getDisplayNamesFileManager().init();
        getIpBanFileManager().init();
        getPkRankFileManager().init();
        getDtRankFileManager().init();
        MapArchiveKeys.init();
        MapAreas.init();
        ObjectSpawnsFileManager.init();
        GrandExchangePriceManager.init();
        GrandExchangeUnlimitedItems.init();
        GrandExchange.init();
        NPCSpawnsFileManager.init();
        NPCExaminesFileManager.loadExamines();
        NPCCombatDefinitionsFileManager.loadNPCCombatDefinitions();
        NPCDropsFileManager.init();
        NPCBonusesFileManager.loadNpcBonuses();
        NPCNameFileManager.loadNpcNames();
        FunnyJokeHandler.init();
        ItemExamines.init();
        WeightManager.init();
        ItemBonuses.init();
        MusicHints.init();
        ShopsManager.init();
        StaffActionManager.init();
        AutoBackup.init();
        FishingSpotsHandler.init();
        CombatScriptsHandler.init();
        CutsceneHandler.init();
        World.init();
        RegionBuilder.init();
        if (getSettingsManager().getSettings().isControlPanel()) {
            final Panel frame = new Panel();
            frame.setVisible(true);
        }
        getGameEngine().init();
        getNetworkEngine().init(getSettingsManager().getSettings().getMaxConnections());
        setStartTime(System.currentTimeMillis());
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
        Logger.info(Server.class, "Server started in " + (getStartTime() - currentTime) + "ms.");
    }

    public void stop() {
        saveAll();
        getNetworkEngine().shutdown();
        getGameEngine().shutdown();
        CoresManager.shutdown();
    }

    public void saveAll() {
        for (final Player player : World.getPlayers()) {
            if (player == null || !player.hasStarted() || player.hasFinished()) {
                continue;
            }
            getPlayerFileManager().save(player);
        }
        getDisplayNamesFileManager().save();
        GrandExchange.save();
        getIpBanFileManager().save();
        getPkRankFileManager().save();
        getDtRankFileManager().save();
    }

    public void restart() {
        //TODO restart method
    }

    private static Server instance;

    public static Server getInstance() {
        return instance;
    }

    public static Server setInstance(Server instance) {
        return Server.instance = instance;
    }

    public static final class Builder {

        public Server build() {
            SettingsManager settingsManager = new SettingsManager(GameFileConstants.SETTINGS_FILE);
            GameEngine gameEngine = new GameEngine();
            NetworkEngine networkEngine = new NetworkEngine();
            PlayerFileManager playerFileManager = new PlayerFileManager(GameFileConstants.PLAYERS_DIR);
            DTRankFileManager dtRankFileManager = new DTRankFileManager(GameFileConstants.DT_RANKS_FILE);
            IPBanFileManager ipBanFileManager = new IPBanFileManager(GameFileConstants.IP_BANS_FILE);
            DisplayNamesFileManager displayNamesFileManager = new DisplayNamesFileManager(GameFileConstants.DISPLAY_NAMES_FILE);
            ClanFilesManager clanFilesManager = new ClanFilesManager();
            GrandExchangeFileManager grandExchangeFileManager = new GrandExchangeFileManager();
            PkRankFileManager pkRankFileManager = new PkRankFileManager(GameFileConstants.PK_RANKS_FILE);
            GameTaskManager gameTaskManager = new GameTaskManager();
            CommandManager commandManager = new CommandManager();
            return new Server(settingsManager, gameEngine, networkEngine, playerFileManager, dtRankFileManager,
                    ipBanFileManager, displayNamesFileManager, clanFilesManager, grandExchangeFileManager, pkRankFileManager,
                    gameTaskManager, commandManager);
        }
    }
}
