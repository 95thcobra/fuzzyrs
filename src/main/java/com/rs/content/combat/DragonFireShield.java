package com.rs.content.combat;

import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.player.combat.Combat;
import com.rs.player.combat.PlayerCombat;
import com.rs.world.Animation;
import com.rs.world.Graphics;
import com.rs.world.Hit;
import com.rs.world.Hit.HitLook;
import com.rs.world.World;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

/**
 * @author FuzzyAvacado
 */
public class DragonFireShield {

    public static void activate(Player player) {
        if (player.getEquipment().getShieldId() == 11283) {
            Long dfsCharge = (Long) player.getTemporaryAttributtes().get("LAST_DFS");
            if (dfsCharge != null && dfsCharge + 30000 > Utils.currentTimeMillis()) {
                player.sendMessage("Players may only use the Dragonfire shield special once every 30 seconds.");
                return;
            }
            if (player.getActionManager().getAction() instanceof PlayerCombat) {
                player.dfsActivate = true;
            } else {
                player.sendMessage("You must be under combat to do this.");
            }
        }
    }

    public static void handleCombat(Player player, Player target) {
        if (player.dfsActivate) {
            int damage = 0;
            player.dfsActivate = false;
            player.getTemporaryAttributtes().put("LAST_DFS", Utils.currentTimeMillis());
            player.setNextGraphics(new Graphics(1165));
            player.setNextAnimation(new Animation(6696));
            if (target.getFireImmune() >= Utils.currentTimeMillis()) {
                damage = Utils.random(100);
                target.sendMessage("Your antifire potion absorbs most of the fiery attack!");
            } else if (Combat.hasAntiDragProtection(target)) {
                damage = Utils.random(100);
                target.sendMessage("Your Dragonfire shield absorbs most of the fiery attack!");
            } else if (target.getFireImmune() >= Utils.currentTimeMillis() && Combat.hasAntiDragProtection(target)) {
                damage = 0;
                target.sendMessage("Due to the nature of your shield and potion you take no damage.");
            } else {
                damage = 200 + Utils.random(150);
                target.sendMessage("You are horribly burnt by the fiery attack!");
            }
            World.sendProjectile(player, target, 1166, 41, 16, 31, 35, 16, 0);
            final int finalDamage = damage;
            WorldTasksManager.schedule(new WorldTask() {
                @Override
                public void run() {
                    target.applyHit(new Hit(player, finalDamage, HitLook.REGULAR_DAMAGE));
                }
            });
        }
    }

}
