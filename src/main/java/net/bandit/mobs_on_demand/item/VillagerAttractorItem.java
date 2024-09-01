package net.bandit.mobs_on_demand.item;

import dev.ftb.mods.ftbchunks.api.ClaimedChunk;
import dev.ftb.mods.ftbchunks.api.FTBChunksAPI;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class VillagerAttractorItem extends Item {

    private static final boolean isFTBChunksLoaded = FabricLoader.getInstance().isModLoaded("ftbchunks");

    public VillagerAttractorItem(Properties properties) {
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

            // Find all villagers within the radius
            List<Villager> villagers = world.getEntitiesOfClass(Villager.class, searchBox);

            for (Villager villager : villagers) {
                if (isFTBChunksLoaded && player instanceof ServerPlayer serverPlayer) {
                    int chunkX = villager.blockPosition().getX() >> 4;
                    int chunkZ = villager.blockPosition().getZ() >> 4;
                    ChunkDimPos chunkPos = new ChunkDimPos(villager.level().dimension(), chunkX, chunkZ);
                    ClaimedChunk claimedChunk = FTBChunksAPI.api().getManager().getChunk(chunkPos);
                    if (claimedChunk != null) {
                        Team chunkOwnerTeam = (Team) claimedChunk.getTeamData().getTeam();
                        Optional<Team> optionalPlayerTeam = FTBTeamsAPI.api().getManager().getTeamForPlayer(serverPlayer);
                        if (optionalPlayerTeam.isPresent()) {
                            Team playerTeam = optionalPlayerTeam.get();

                            if (!chunkOwnerTeam.equals(playerTeam)) {
                                continue; // Skip the villager in this claimed chunk
                            }
                        } else {
                            continue; // Player is not in a team, skip
                        }
                    }
                }
                // Get the player's position
                BlockPos targetPos = player.blockPosition();

                // Use the villager's brain to set the memory for the walk target, similar to how they respond to a bell
                villager.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(targetPos, 1F, 1));
            }

            player.getCooldowns().addCooldown(this, 100);
        }
        return InteractionResultHolder.success(itemStack);
    }

            @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("item.mobs_on_demand.villager_attractor.tooltip1").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.mobs_on_demand.villager_attractor.tooltip2").withStyle(ChatFormatting.YELLOW));
        super.appendHoverText(stack, world, tooltip, context);
    }
}
