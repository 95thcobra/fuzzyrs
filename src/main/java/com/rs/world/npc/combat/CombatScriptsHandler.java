package com.rs.world.npc.combat;

import com.rs.core.utils.Logger;
import com.rs.world.Entity;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.impl.*;

import java.util.HashMap;

public class CombatScriptsHandler {

    private static final HashMap<Object, CombatScript> cachedCombatScripts = new HashMap<>();
    private static final CombatScript DEFAULT_SCRIPT = new Default();

    public static void init() throws InstantiationException, IllegalAccessException {
        add(AbbysalTitanCombat.class);
        add(AhrimCombat.class);
        add(AkrisCombat.class);
        add(AvatarCombat.class);
        add(BarrelchestCombat.class);
        add(BarricadeCombat.class);
        add(BlinkCombat.class);
        add(BorkCombat.class);
        add(CommanderZilyanaCombat.class);
        add(CorporealBeastCombat.class);
        add(DharokCombat.class);
        add(DreadFowlCombat.class);
        add(DreadNipCombat.class);
        add(EvilChickenCombat.class);
        add(FakeNomadCombat.class);
        add(FrostDragonCombat.class);
        add(GanodermicCombat.class);
        add(GeneralGraardorCombat.class);
        add(GeyserTitanCombat.class);
        add(GiantMoleCombat.class);
        add(GlacorCombat.class);
        add(GuthanCombat.class);
        add(HaarlakCombat.class);
        add(HarAkenTentacleCombat.class);
        add(HatiCombat.class);
        add(IceKingCombat.class);
        add(IronTitanCombat.class);
        add(JadCombat.class);
        add(KalphiteQueenCombat.class);
        add(KarilCombat.class);
        add(KetZekCombat.class);
        add(KingBlackDragonCombat.class);
        add(Kreearra.class);
        add(KrilTsutsaroth.class);
        add(LavaTitanCombat.class);
        add(LeatherDragonCombat.class);
        add(LivingRockStrickerCombat.class);
        add(LucienCombat.class);
        add(MercenaryMageCombat.class);
        add(MetalDragonCombat.class);
        add(MinotaurCombat.class);
        add(MossTitanCombat.class);
        add(NexCombat.class);
        add(NomadCombat.class);
        add(OrkLegionCombat.class);
        add(PestQueenCombat.class);
        add(PkerCombat.class);
        add(PrimeCombat.class);
        add(RefugeOfFearSOLMageMininionCombat.class);
        add(RevenantCombat.class);
        add(RocCombat.class);
        add(SeaCombat.class);
        add(SkollCombat.class);
        add(SpiritKalphiteCombat.class);
        add(SpiritWolfCombat.class);
        add(SteelTitanCombat.class);
        add(StrykewyrmCombat.class);
        add(SunfreetCombat.class);
        add(ThornySnailCombat.class);
        add(ThunderCombat.class);
        add(TokHaarKetDillCombat.class);
        add(TokHaarMej.class);
        add(TokXilCombat.class);
        add(ToragCombat.class);
        add(TormentedDemonCombat.class);
        add(TzKihCombat.class);
        add(WildyWyrmCombat.class);
        add(WolverineCombat.class);
        add(YtMejKotCombat.class);
        Logger.info(CombatScriptsHandler.class, "Loaded " + cachedCombatScripts.size() + " NPC Combat Scripts.");
    }

    public static void add(Class<? extends CombatScript> c) throws IllegalAccessException, InstantiationException {
        CombatScript combatScript = c.newInstance();
        cachedCombatScripts.put(combatScript.getKeys(), combatScript);
    }

    public static int specialAttack(final NPC npc, final Entity target) {
        CombatScript script = cachedCombatScripts.get(npc.getId());
        if (script == null) {
            script = cachedCombatScripts.get(npc.getDefinitions().name);
            if (script == null) {
                script = DEFAULT_SCRIPT;
            }
        }
        return script.attack(npc, target);
    }
}
