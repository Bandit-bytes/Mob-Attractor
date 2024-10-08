package net.bandit.mobs_on_demand.item;

import dev.ftb.mods.ftbchunks.api.ClaimedChunk;
import dev.ftb.mods.ftbchunks.api.FTBChunksAPI;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class MobAttractorItem extends Item {

    private static final boolean isFTBChunksLoaded = FabricLoader.getInstance().isModLoaded("ftbchunks");

    public MobAttractorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!world.isClientSide) {
            // Play a sound effect when the item is used
            world.playSound(null, player.blockPosition(), SoundEvents.BELL_RESONATE, SoundSource.PLAYERS, 1.0F, 1.0F);

            // Define the search radius (128 blocks in this case)
            double radius = 128.0;
            AABB searchBox = new AABB(
                    player.getX() - radius, player.getY() - radius, player.getZ() - radius,
                    player.getX() + radius, player.getY() + radius, player.getZ() + radius
            );

            // Find all animals within the radius
            List<Animal> animals = world.getEntitiesOfClass(Animal.class, searchBox, entity -> {
                if (this == ModItems.CHICKEN_ATTRACTOR) {
                    return entity instanceof Chicken;
                } else if (this == ModItems.COW_ATTRACTOR) {
                    return entity instanceof Cow;
                } else if (this == ModItems.SHEEP_ATTRACTOR) {
                    return entity instanceof Sheep;
                } else if (this == ModItems.PIG_ATTRACTOR) {
                    return entity instanceof Pig;
                }
                return false;
            });

            for (Animal animal : animals) {
                if (isFTBChunksLoaded && player instanceof ServerPlayer serverPlayer) {
                    int chunkX = animal.blockPosition().getX() >> 4;
                    int chunkZ = animal.blockPosition().getZ() >> 4;
                    ChunkDimPos chunkPos = new ChunkDimPos(animal.level().dimension(), chunkX, chunkZ);
                    ClaimedChunk claimedChunk = FTBChunksAPI.api().getManager().getChunk(chunkPos);
                    if (claimedChunk != null) {
                        // Check team ownership
                        Team chunkOwnerTeam = (Team) claimedChunk.getTeamData().getTeam();
                        Optional<Team> optionalPlayerTeam = FTBTeamsAPI.api().getManager().getTeamForPlayer(serverPlayer);
                        if (optionalPlayerTeam.isPresent()) {
                            Team playerTeam = optionalPlayerTeam.get();

                            if (!chunkOwnerTeam.equals(playerTeam)) {
                                continue; // Skip the animal in this claimed chunk
                            }
                        } else {
                            continue; // Player is not in a team, skip
                        }
                    }
                }
                animal.setTarget(player);
                animal.getNavigation().moveTo(player, 2.0);
            }
            player.getCooldowns().addCooldown(this, 100);
        }
        return InteractionResultHolder.success(itemStack);
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        if (this == ModItems.CHICKEN_ATTRACTOR) {
            tooltip.add(Component.translatable("item.mobs_on_demand.chicken_attractor.tooltip1").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("item.mobs_on_demand.chicken_attractor.tooltip2").withStyle(ChatFormatting.YELLOW));
        } else if (this == ModItems.COW_ATTRACTOR) {
            tooltip.add(Component.translatable("item.mobs_on_demand.cow_attractor.tooltip1").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("item.mobs_on_demand.cow_attractor.tooltip2").withStyle(ChatFormatting.YELLOW));
        } else if (this == ModItems.SHEEP_ATTRACTOR) {
            tooltip.add(Component.translatable("item.mobs_on_demand.sheep_attractor.tooltip1").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("item.mobs_on_demand.sheep_attractor.tooltip2").withStyle(ChatFormatting.YELLOW));
        } else if (this == ModItems.PIG_ATTRACTOR) {
            tooltip.add(Component.translatable("item.mobs_on_demand.pig_attractor.tooltip1").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("item.mobs_on_demand.pig_attractor.tooltip2").withStyle(ChatFormatting.YELLOW));
        }

        super.appendHoverText(stack, world, tooltip, context);
    }
}
