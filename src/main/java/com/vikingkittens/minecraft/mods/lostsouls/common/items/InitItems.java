package com.vikingkittens.minecraft.mods.lostsouls.common.items;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import com.vikingkittens.minecraft.mods.lostsouls.ModLostSouls;

public class InitItems {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final DeferredRegister<Item> register = DeferredRegister.create(ForgeRegistries.ITEMS, ModLostSouls.ID);

    public static void init(IEventBus modEventBus) {
        LOGGER.info("Registering Items");
        register.register(modEventBus);

        ItemCorruptedEmerald.register(register);
    }
}
