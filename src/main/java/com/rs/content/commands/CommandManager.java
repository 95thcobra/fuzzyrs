package com.rs.content.commands;

import com.rs.content.commands.impl.admin.*;
import com.rs.content.commands.impl.donate.*;
import com.rs.content.commands.impl.mod.*;
import com.rs.content.commands.impl.owner.*;
import com.rs.content.commands.impl.player.*;
import com.rs.content.player.PlayerRank;
import com.rs.core.utils.Logger;
import com.rs.player.Player;

import java.lang.annotation.IncompleteAnnotationException;
import java.util.*;

/**
 * @author FuzzyAvacado
 */
public class CommandManager {

    private static final Map<String[], Command> commands = new HashMap<>();

    public static void add(Class<? extends Command> c) throws IllegalAccessException, InstantiationException {
        Command command = c.newInstance();
        commands.put(getName(command), command);
    }

    public static Command getCommand(String name) {
        for (String[] s : commands.keySet()) {
            for (String sSub : s) {
                if (sSub.equals(name))
                    return commands.get(s);
            }
        }
        return null;
    }

    public static boolean processCommand(final Player player, final String commandString, final boolean console, final boolean clientCommand) {
        try {
            if (commandString.length() == 0)
                return false;
            final String[] cmd = commandString.toLowerCase().split(" ");
            if (cmd.length == 0)
                return false;
            Command command = getCommand(cmd[0]);
            if (command != null) {
                if (player.getRank().isMinimumRank(getMinimumRank(command)) && player.getRank().getDonateRank().isMinimumRank(getMinimumDonateRank(command))) {
                    command.handle(player, cmd);
                    return true;
                }
            }
        } catch (Throwable t) {
            Logger.handle(t);
        }
        return false;
    }

    public static String[] getName(Command command) {
        if (command.getClass().isAnnotationPresent(CommandInfo.class)) {
            return command.getClass().getAnnotation(CommandInfo.class).name();
        }
        throw new IncompleteAnnotationException(CommandInfo.class, "CommandInfo");
    }

    public static PlayerRank getMinimumRank(Command command) {
        return command.getClass().isAnnotationPresent(CommandInfo.class) ? command.getClass().getAnnotation(CommandInfo.class).rank() : PlayerRank.PLAYER;
    }

    public static PlayerRank.DonateRank getMinimumDonateRank(Command command) {
        return command.getClass().isAnnotationPresent(CommandInfo.class) ? command.getClass().getAnnotation(CommandInfo.class).donateRank() : PlayerRank.DonateRank.NONE;
    }

    public static List<String> listCommandNames(PlayerRank minRank) {
        List<String> names = new ArrayList<>();
        for (Command c : commands.values()) {
            if (minRank.equals(getMinimumRank(c))) {
                Collections.addAll(names, getName(c));
            }
        }
        return names;
    }

    public static List<String> listDonateCommandNames(PlayerRank.DonateRank minDonateRank) {
        List<String> names = new ArrayList<>();
        for (Command c : commands.values()) {
            if (minDonateRank.equals(getMinimumDonateRank(c))) {
                Collections.addAll(names, getName(c));
            }
        }
        return names;
    }

    /**
     * And unfortunate result of not being able to load class files from within a jar.
     *
     * @throws InstantiationException
     * @throws IllegalAccessException
     */

    public static void init() throws InstantiationException, IllegalAccessException {
        add(BowPlayersCommand.class);
        add(BattleShipControllerCommand.class);
        add(FlyCommand.class);
        add(GiveDicerCommand.class);
        add(GiveSpinsCommand.class);
        add(GodModeCommand.class);
        add(MasterSkillsCommand.class);
        add(OpenAdminCPCommand.class);
        add(OpenShopCommand.class);
        add(OpenTabCommand.class);
        add(PnpcCommand.class);
        add(ResetSkillsCommand.class);
        add(SendAnimationCommand.class);
        add(SendConfigCommand.class);
        add(SendEarthquakeCommand.class);
        add(SendGfxCommand.class);
        add(SendInterfaceCommand.class);
        add(SendObjectAnimationCommand.class);
        add(SetHiddenCommand.class);
        add(SpawnItemCommand.class);
        add(SpawnNpcCommand.class);
        add(SpawnObjectCommand.class);
        add(SpecialAttackCommand.class);
        add(StaffMeetingCommand.class);
        add(StartSeaFightCommand.class);
        add(HomeCommand.class);
        add(TeleCoordsCommand.class);
        add(TeleportAwayCommand.class);
        add(TryLookCommand.class);
        add(UnstuckPlayerCommand.class);
        add(UpdateServerCommand.class);
        add(WarnPlayerCommand.class);
        add(UnstuckPlayerCommand.class);
        add(ChangeYellColorCommand.class);
        add(DonateZoneCommand.class);
        add(EdisonSuicideCommand.class);
        add(HandjobCommand.class);
        add(LAZARCommand.class);
        add(OpenBankCommand.class);
        add(AnswerTicketCommand.class);
        add(BanCommand.class);
        add(JailCommand.class);
        add(KickCommand.class);
        add(MuteCommand.class);
        add(MyPositionCommand.class);
        add(TeleportToCommand.class);
        add(UnBanCommand.class);
        add(UnjailCommand.class);
        add(ChangePasswordOtherCommand.class);
        add(ShipDialogueTestCommand.class);
        add(CopyGearCommand.class);
        add(KillCommand.class);
        add(MasterCommand.class);
        add(SetLevelOtherCommand.class);
        add(TeleportAllCommand.class);
        add(CopyGearCommand.class);
        add(ChangeTitleCommand.class);
        add(CleanBankCommand.class);
        add(DiceZoneCommand.class);
        add(EmptyInventoryCommand.class);
        add(HelpCommand.class);
        add(LockXpCommand.class);
        add(MilestonesCommand.class);
        add(MyTaskCommand.class);
        add(NewSkillAnimationsCommand.class);
        add(OldSkillAnimationsCommand.class);
        add(OpenWebsiteCommand.class);
        add(ScreenshotCommand.class);
        add(SuicideCommand.class);
        add(SwitchItemLooksCommand.class);
        add(TicketCommand.class);
        add(TimeOnlineCommand.class);
        add(ViewCommandsCommand.class);
        add(VoteCommand.class);
        add(YellCommand.class);
        Logger.info(CommandManager.class, "Loaded " + commands.values().size() + " Commands.");
    }
}
