package com.vikingkittens.minecraft.mods.lostsouls;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

import com.vikingkittens.minecraft.mods.lostsouls.client.blocks.ClientInitBlocks;
import com.vikingkittens.minecraft.mods.lostsouls.common.blocks.InitBlocks;
import com.vikingkittens.minecraft.mods.lostsouls.common.blocks.ModBlocks;
import com.vikingkittens.minecraft.mods.lostsouls.common.entities.InitEntities;
import com.vikingkittens.minecraft.mods.lostsouls.common.items.InitItems;

@Mod(ModLostSouls.ID)
public class ModLostSouls {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String ID = "lostsouls";

    public ModLostSouls() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        GeckoLib.initialize();

        InitBlocks.init(modEventBus);
        InitItems.init(modEventBus);
        InitEntities.init(modEventBus);

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onSetup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void onSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Preinit:");
        LOGGER.info("COMPACTED_SOUL_SAND BLOCK: {}", ModBlocks.COMPACTED_SOUL_SAND.getRegistryName());
        LOGGER.info("SOUL_VALLEY_PORTAL BLOCK: {}", ModBlocks.SOUL_VALLEY_PORTAL.getRegistryName());
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        Minecraft minecraft = event.getMinecraftSupplier().get();

        LOGGER.info("Player Joined {}", minecraft.getUser().getName());

        ClientInitBlocks.init(minecraft);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
