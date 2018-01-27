package com.rs.content.staff.actions;

import com.rs.content.staff.actions.impl.*;
import com.rs.utils.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author FuzzyAvacado
 */
public class StaffActionManager {

    private static final Map<Class<? extends StaffAction>, StaffAction> staffActions = new HashMap<>();

    public static void init() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        add(AddNewsAction.class);
        add(BanAction.class);
        add(ChangeDisplayName.class);
        add(CheckBankAction.class);
        add(CheckInventoryAction.class);
        add(EmptyBankAction.class);
        add(EmptyInventoryAction.class);
        add(GetInfoAction.class);
        add(InitializeServerSettings.class);
        add(InitializeShops.class);
        add(IPBanAction.class);
        add(JailAction.class);
        add(KickAction.class);
        add(MuteAction.class);
        add(RankManagementAction.class);
        add(ServerMessageAction.class);
        add(SpawnNpcAction.class);
        add(SpawnObjectAction.class);
        add(TeleportToAction.class);
        add(TeleportToMeAction.class);
        add(UnBanAction.class);
        add(UnIPBanAction.class);
        add(UnJailAction.class);
        add(UnMuteAction.class);
        Logger.info(StaffActionManager.class, "Loaded " + staffActions.values().size() + " Staff Actions.");
    }

    public static void add(Class<? extends StaffAction> c) throws IllegalAccessException, InstantiationException {
        staffActions.put(c, c.newInstance());
    }

    public static StaffAction getStaffAction(Class<? extends StaffAction> c) {
        return staffActions.get(c);
    }
}
