package com.vikingkittens.minecraft.mods.lostsouls.common.dimensions.biomes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import com.vikingkittens.minecraft.mods.lostsouls.ModLostSouls;

public class InitBiomes {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final DeferredRegister<Biome> register = DeferredRegister.create(ForgeRegistries.BIOMES, ModLostSouls.ID);

    public static void init(IEventBus modEventBus) {
        LOGGER.info("Registering Biomes");
        register.register(modEventBus);

        BiomeSoulValleyDunes.register(register);
    }
}
