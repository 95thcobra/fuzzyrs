package com.rs.world.task.gametask.impl;

import com.rs.content.actions.skills.Skills;
import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.World;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.GameTaskType;

import java.util.concurrent.TimeUnit;

/**
 * @author FuzzyAvacado
 */
@GameTaskType(GameTaskManager.GameTaskType.FAST)
public class RestoreSkillsTask extends GameTask {

    public RestoreSkillsTask() {
        super(GameTask.ExecutionType.FIXED_DELAY, 0, 30, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        for (final Player player : World.getPlayers()) {
            if (player == null || !player.isRunning()) {
                continue;
            }
            int amountTimes = player.getPrayer().usingPrayer(0, 8) ? 2
                    : 1;
            if (player.isResting()) {
                amountTimes += 1;
            }
            final boolean berserker = player.getPrayer()
                    .usingPrayer(1, 5);
            for (int skill = 0; skill < 25; skill++) {
                if (skill == Skills.SUMMONING) {
                    continue;
                }
                for (int time = 0; time < amountTimes; time++) {
                    final int currentLevel = player.getSkills()
                            .getLevel(skill);
                    final int normalLevel = player.getSkills()
                            .getLevelForXp(skill);
                    if (currentLevel > normalLevel) {
                        if (skill == Skills.ATTACK
                                || skill == Skills.STRENGTH
                                || skill == Skills.DEFENCE
                                || skill == Skills.RANGE
                                || skill == Skills.MAGIC) {
                            if (berserker
                                    && Utils.getRandom(100) <= 15) {
                                continue;
                            }
                        }
                        player.getSkills().set(skill,
                                currentLevel - 1);
                    } else if (currentLevel < normalLevel) {
                        player.getSkills().set(skill,
                                currentLevel + 1);
                    } else {
                        break;
                    }
                }
            }
        }
    }
}
