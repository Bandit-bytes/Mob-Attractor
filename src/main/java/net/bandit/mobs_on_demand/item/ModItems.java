package net.bandit.mobs_on_demand.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import static net.minecraft.core.registries.BuiltInRegistries.CREATIVE_MODE_TAB;
import static net.minecraft.core.registries.BuiltInRegistries.ITEM;

public class ModItems {
    public static final Item CHICKEN_ATTRACTOR = register("chicken_attractor",
            new MobAttractorItem(new FabricItemSettings().stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item COW_ATTRACTOR = register("cow_attractor",
            new MobAttractorItem(new FabricItemSettings().stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item SHEEP_ATTRACTOR = register("sheep_attractor",
            new MobAttractorItem(new FabricItemSettings().stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item PIG_ATTRACTOR = register("pig_attractor",
            new MobAttractorItem(new FabricItemSettings().stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item VILLAGER_ATTRACTOR = register("villager_attractor",
            new VillagerAttractorItem(new FabricItemSettings().stacksTo(1).rarity(Rarity.EPIC)));

    public static final CreativeModeTab MOB_ATTRACTOR_GROUP = Registry.register(
            CREATIVE_MODE_TAB,
            new ResourceLocation("mobs_on_demand", "mob_attractor_group"),
            FabricItemGroup.builder().title(Component.translatable("itemgroup.mob_attractor"))
                    .icon(() -> new ItemStack(ModItems.CHICKEN_ATTRACTOR))
                    .displayItems((displayContext, entries) -> {
                        entries.accept(ModItems.CHICKEN_ATTRACTOR);
                        entries.accept(ModItems.COW_ATTRACTOR);
                        entries.accept(ModItems.SHEEP_ATTRACTOR);
                        entries.accept(ModItems.PIG_ATTRACTOR);
                        entries.accept(ModItems.VILLAGER_ATTRACTOR);
                    }).build());

    private static Item register(String name, Item item) {
        return Registry.register(ITEM, new ResourceLocation("mobs_on_demand", name), item);
    }

    public static void registerItems() {
        // Call this method in your main mod initializer to register the items
    }
}
