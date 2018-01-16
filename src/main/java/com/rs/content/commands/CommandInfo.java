package com.rs.content.commands;

import com.rs.content.player.PlayerRank;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author FuzzyAvacado
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {

    String[] name();
    PlayerRank rank() default PlayerRank.PLAYER;
    PlayerRank.DonateRank donateRank() default PlayerRank.DonateRank.NONE;
}
