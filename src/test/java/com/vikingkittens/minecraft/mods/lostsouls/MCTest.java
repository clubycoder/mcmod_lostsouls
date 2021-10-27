package com.vikingkittens.minecraft.mods.lostsouls;

import org.junit.Before;
import static org.mockito.Mockito.*;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Bootstrap;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;

public class MCTest {
    protected World world;
    protected BlockState air;
    protected BlockState stone;
    protected BlockState dirt;
    protected BlockState barrier;

    protected BlockPos origin = new BlockPos(0, 0, 0);

    @Before
    public void baseSetup() {
        Bootstrap.bootStrap();
        ModContainer modContainer = mock(ModContainer.class);
        ModLoadingContext.get().setActiveContainer(modContainer, null);

        world = mock(World.class);

        air = mock(BlockState.class);
        when(air.getBlock()).thenReturn(Blocks.AIR);
        stone = mock(BlockState.class);
        when(stone.getBlock()).thenReturn(Blocks.STONE);
        dirt = mock(BlockState.class);
        when(dirt.getBlock()).thenReturn(Blocks.DIRT);
        barrier = mock(BlockState.class);
        when(barrier.getBlock()).thenReturn(Blocks.BARRIER);
    }
}
