package com.vikingkittens.minecraft.mods.lostsouls.common.blocks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.DeferredRegister;

public class BlockCompactedSoulSand extends Block {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String ID = "compacted_soul_sand";

    private static Block.Properties props = AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY)
            .harvestTool(ToolType.PICKAXE)
            .harvestLevel(2)
            .sound(SoundType.SOUL_SAND);

    public BlockCompactedSoulSand(Properties props) {
        super(props);
    }

    public static void register(DeferredRegister<Block> registry) {
        LOGGER.info("Registering Block " + ID);
        registry.register(ID, () -> new BlockCompactedSoulSand(props));
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(
            BlockState state,
            World world,
            BlockPos pos,
            PlayerEntity player,
            Hand hand,
            BlockRayTraceResult hit
    ) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();
        if (
                item == Items.FLINT_AND_STEEL &&
                !player.isCrouching() &&
                hit.getDirection() == Direction.UP &&
                world.getBlockState(pos.above()).getBlock() == Blocks.AIR
        ) {
            LOGGER.info("Attempting to light " + ID + " at " + pos);
            return ActionResultType.CONSUME;
        }
        return super.use(state, world, pos, player, hand, hit);
    }
}
