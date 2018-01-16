package com.rs.core;

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
import com.rs.core.cache.Cache;
import com.rs.core.cache.loaders.ItemsEquipIds;
import com.rs.core.file.data.map.MapArchiveKeys;
import com.rs.core.file.data.map.MapAreas;
import com.rs.core.file.data.map.ObjectSpawnsFileManager;
import com.rs.core.file.data.npc.*;
import com.rs.core.file.managers.DTRankFileManager;
import com.rs.core.file.managers.DisplayNamesFileManager;
import com.rs.core.file.managers.IPBanFileManager;
import com.rs.core.file.managers.PkRankFileManager;
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

import java.io.IOException;

/**
 * @author John (FuzzyAvacado) on 12/22/2015.
 */
public class RS2Loader {

    public static void init() {
        try {
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
            if (SettingsManager.getSettings().CONTROL_PANEL) {
                final Panel frame = new Panel();
                frame.setVisible(true);
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IOException e) {
            Logger.handle(e);
        }
    }
}
