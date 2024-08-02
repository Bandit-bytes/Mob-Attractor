package net.bandit.mob_attractor;

import net.fabricmc.api.ModInitializer;
import net.bandit.mob_attractor.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobAttractorMod implements ModInitializer {
    public static final String MOD_ID = "mob_attractor";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.registerItems();
        LOGGER.info("Mob Attractor Mod Initialized!");
    }
}
