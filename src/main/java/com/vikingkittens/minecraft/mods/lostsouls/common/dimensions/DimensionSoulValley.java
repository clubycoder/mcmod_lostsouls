package com.vikingkittens.minecraft.mods.lostsouls.common.dimensions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import com.vikingkittens.minecraft.mods.lostsouls.ModLostSouls;

public class DimensionSoulValley {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String ID = "soul_valley";

    public static final RegistryKey<World> KEY = RegistryKey.create(Registry.DIMENSION_REGISTRY,
            new ResourceLocation(ModLostSouls.ID, ID));

    public static void register() {
        LOGGER.info("Registering Dimension " + ID);
    }
}
