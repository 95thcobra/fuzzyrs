package com.rs;

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
import com.rs.core.GameEngine;
import com.rs.core.NetworkEngine;
import com.rs.core.cache.Cache;
import com.rs.core.cache.loaders.ItemsEquipIds;
import com.rs.core.cores.CoresManager;
import com.rs.core.file.GameFileManager;
import com.rs.core.file.JsonFileManager;
import com.rs.core.file.data.map.MapArchiveKeys;
import com.rs.core.file.data.map.MapAreas;
import com.rs.core.file.data.map.ObjectSpawnsFileManager;
import com.rs.core.file.data.npc.*;
import com.rs.core.file.managers.DTRankFileManager;
import com.rs.core.file.managers.DisplayNamesFileManager;
import com.rs.core.file.managers.IPBanFileManager;
import com.rs.core.file.managers.PkRankFileManager;
import com.rs.core.settings.GameConstants;
import com.rs.core.settings.SettingsManager;
import com.rs.core.utils.Logger;
import com.rs.core.utils.file.AutoBackup;
import com.rs.core.utils.file.MusicHints;
import com.rs.core.utils.huffman.Huffman;
import com.rs.core.utils.item.ItemBonuses;
import com.rs.core.utils.item.ItemExamines;
import com.rs.core.utils.tools.Panel;
import com.rs.world.RegionBuilder;
import com.rs.world.World;
import com.rs.world.npc.combat.CombatScriptsHandler;
import lombok.*;

import java.io.IOException;


/**
 * Created by Fuzzy 1/25/2018
 */
@RequiredArgsConstructor
@Getter
public final class Server {

    private final JsonFileManager jsonFileManager;
    private final GameEngine gameEngine;
    private final NetworkEngine networkEngine;
    private final SettingsManager settingsManager;

    @Setter(AccessLevel.PRIVATE)
    private long startTime;

    public void start() throws IllegalAccessException, InstantiationException, IOException, ClassNotFoundException {
        long currentTime = System.currentTimeMillis();
        getSettingsManager().init();
        CommandManager.init();
        Cache.init();
        ItemsEquipIds.init();
        Huffman.init();
        DisplayNamesFileManager.init();
        IPBanFileManager.init();
        PkRankFileManager.init();
        DTRankFileManager.init();
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
        GameFileManager.saveAll();
        getNetworkEngine().shutdown();
        getGameEngine().shutdown();
        CoresManager.shutdown();
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
            JsonFileManager jsonFileManager = JsonFileManager.create();
            GameEngine gameEngine = new GameEngine();
            NetworkEngine networkEngine = new NetworkEngine();
            SettingsManager settingsManager = new SettingsManager(GameConstants.SETTINGS_PATH, jsonFileManager);
            return new Server(jsonFileManager, gameEngine, networkEngine, settingsManager);
        }
    }
}
