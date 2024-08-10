package net.bandit.mobs_on_demand;

import net.fabricmc.api.ModInitializer;
import net.bandit.mobs_on_demand.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobsOnDemandMod implements ModInitializer {
    public static final String MOD_ID = "mobs_on_demand";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.registerItems();
        LOGGER.info("Mob's on Demand Mod Initialized!");
    }
}
