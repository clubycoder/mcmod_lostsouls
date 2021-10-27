package com.vikingkittens.minecraft.mods.lostsouls.common.dimensions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.eventbus.api.IEventBus;

public class InitDimensions {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void init(IEventBus modEventBus) {
        LOGGER.info("Registering Dimensions");

        DimensionSoulValley.register();
    }
}
