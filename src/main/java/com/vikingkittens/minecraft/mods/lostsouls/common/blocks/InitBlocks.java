package com.vikingkittens.minecraft.mods.lostsouls.common.blocks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import com.vikingkittens.minecraft.mods.lostsouls.ModLostSouls;

@Mod.EventBusSubscriber(modid = ModLostSouls.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class InitBlocks {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final DeferredRegister<Block> register = DeferredRegister.create(ForgeRegistries.BLOCKS, ModLostSouls.ID);

    public static void init(IEventBus modEventBus) {
        LOGGER.info("Registering Blocks");
        register.register(modEventBus);

        BlockCompactedSoulSand.register(register);
        BlockSoulValleyPortal.register(register);
    }

    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
        register.getEntries().stream().map(RegistryObject::get).forEach(block -> event.getRegistry()
                .register(new BlockItem(
                    block,
                    new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)
                ).setRegistryName(block.getRegistryName()))
        );
    }
}
