package com.vikingkittens.minecraft.mods.lostsouls.common.blocks;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.DeferredRegister;

public class BlockSoulValleyPortal extends NetherPortalBlock {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String ID = "soul_valley_portal";

    private static Properties props = Properties.of(Material.PORTAL)
            .noCollission()
            .randomTicks()
            .strength(-1.0F)
            .sound(SoundType.SOUL_SAND)
            .lightLevel((lvl) -> 15);

    public BlockSoulValleyPortal(Properties props) {
        super(props);
    }

    public static void register(DeferredRegister<Block> registry) {
        LOGGER.info("Registering Block " + ID);
        registry.register(ID, () -> new BlockSoulValleyPortal(props));
    }

    @Override
    public void entityInside(BlockState blockState, World world, BlockPos pos, Entity entity) {
        if (world instanceof ServerWorld && entity instanceof ServerPlayerEntity) {
            MinecraftServer server = ((ServerWorld)world).getServer();
            ServerPlayerEntity player = (ServerPlayerEntity)entity;

            if (!player.isOnPortalCooldown()) {
                boolean isOverworld = world.dimension().equals(World.OVERWORLD);
                if (isOverworld) {
                    LOGGER.info("Player " + player.getGameProfile().getName() + " teleporting to Soul Valley");
                } else {
                    LOGGER.info("Player " + player.getGameProfile().getName() + " teleporting to Overworld");
                }
                player.setPortalCooldown();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState blockState, World world, BlockPos pos, Random rand) {
        for (int particleNum = 0; particleNum < 4; particleNum++) {
            double x = (double)pos.getX() + rand.nextDouble();
            double y = (double)pos.getY() + rand.nextDouble();
            double z = (double)pos.getZ() + rand.nextDouble();
            double xd = ((double)rand.nextFloat() - 0.5D) * 0.5D;
            double yd = ((double)rand.nextFloat() - 0.5D) * 0.5D;
            double zd = ((double)rand.nextFloat() - 0.5D) * 0.5D;
            int j = rand.nextInt(2) * 2 - 1;
            if (!world.getBlockState(pos.west()).is(this) && !world.getBlockState(pos.east()).is(this)) {
                x = (double)pos.getX() + 0.5D + 0.25D * (double)j;
                xd = (rand.nextFloat() * 2.0F * (float)j);
            } else {
                z = (double)pos.getZ() + 0.5D + 0.25D * (double)j;
                zd = (rand.nextFloat() * 2.0F * (float)j);
            }

            world.addParticle(ParticleTypes.PORTAL, x, y, z, xd, yd, zd);
        }

    }
}
