package com.rs.content.web.impl;

import com.rs.content.web.Database;
import com.rs.utils.Logger;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.task.gametask.GameTask;
import com.rs.task.gametask.GameTaskManager;
import com.rs.task.gametask.GameTaskType;

import java.sql.PreparedStatement;

/**
 * @author John (FuzzyAvacado) on 12/12/2015.
 */
@GameTaskType(GameTaskManager.GameTaskType.FAST)
public class HighScores extends GameTask {

    private Player player;

    public HighScores(Player player) {
        super(ExecutionType.SUBMIT);
        this.player = player;
    }

    public static String generateQuery() {
        return "INSERT INTO hs_users (" +
                "username, " +
                "rights, " +
                "overall_xp, " +
                "attack_xp, " +
                "defence_xp, " +
                "strength_xp, " +
                "constitution_xp, " +
                "ranged_xp, " +
                "prayer_xp, " +
                "magic_xp, " +
                "cooking_xp, " +
                "woodcutting_xp, " +
                "fletching_xp, " +
                "fishing_xp, " +
                "firemaking_xp, " +
                "crafting_xp, " +
                "smithing_xp, " +
                "mining_xp, " +
                "herblore_xp, " +
                "agility_xp, " +
                "thieving_xp, " +
                "slayer_xp, " +
                "farming_xp, " +
                "runecrafting_xp, " +
                "hunter_xp, " +
                "construction_xp, " +
                "summoning_xp, " +
                "dungeoneering_xp) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    public void run() {
        try {
            //"arravsculd159.mysql.db", "arravsculd159", "KmFXd2cHeEVN", "arravsculd159", "hs_users");
            Database db = new Database("arravscurse.com", "arravsculd159", "KmFXd2cHeEVN", "hs_users");
            String name = Utils.formatPlayerNameForDisplay(player.getUsername());
            if (!db.init()) {
                Logger.info(this.getClass(), "Failing to update " + name + " highscores. Database could not connect.");
                return;
            }
            PreparedStatement stmt1 = db.prepare("DELETE FROM hs_users WHERE username=?");
            stmt1.setString(1, name);
            stmt1.execute();

            PreparedStatement stmt2 = db.prepare(generateQuery());
            stmt2.setString(1, name);
            stmt2.setInt(2, player.getRank().getMessageIcon());
            stmt2.setLong(3, player.getSkills().getTotalXp());

            for (int i = 0; i < 25; i++) {
                stmt2.setInt(4 + i, (int) player.getSkills().getXp()[i]);
            }
            stmt2.execute();
            db.destroyAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
