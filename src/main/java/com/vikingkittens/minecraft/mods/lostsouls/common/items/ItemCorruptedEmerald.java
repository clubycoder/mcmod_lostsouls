package com.vikingkittens.minecraft.mods.lostsouls.common.items;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;

public class ItemCorruptedEmerald extends Item {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String ID = "corrupted_emerald";

    private static Properties props = new Item.Properties()
            .tab(ItemGroup.TAB_MATERIALS)
            .stacksTo(64);

    public ItemCorruptedEmerald(Properties props) {
        super(props);
    }

    public static void register(DeferredRegister<Item> registry) {
        LOGGER.info("Registering Item " + ID);
        registry.register(ID, () -> new ItemCorruptedEmerald(props));
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, PlayerEntity player) {
        return super.onDroppedByPlayer(item, player);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
        return super.onLeftClickEntity(stack, player, entity);
    }
}
