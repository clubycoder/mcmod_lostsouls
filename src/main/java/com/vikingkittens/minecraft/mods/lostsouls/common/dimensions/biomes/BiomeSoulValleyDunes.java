package com.vikingkittens.minecraft.mods.lostsouls.common.dimensions.biomes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeMaker;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.registries.DeferredRegister;

import com.vikingkittens.minecraft.mods.lostsouls.ModLostSouls;

public class BiomeSoulValleyDunes {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String ID = "soul_valley_dunes";

    public static final RegistryKey<Biome> KEY = RegistryKey.create(Registry.BIOME_REGISTRY,
            new ResourceLocation(ModLostSouls.ID, ID));

    public static void register(DeferredRegister<Biome> registry) {
        LOGGER.info("Registering Biome " + ID);
        registry.register(ID, BiomeMaker::theVoidBiome);
        BiomeManager.addBiome(BiomeManager.BiomeType.DESERT, new BiomeManager.BiomeEntry(KEY, 10));
    }
}
