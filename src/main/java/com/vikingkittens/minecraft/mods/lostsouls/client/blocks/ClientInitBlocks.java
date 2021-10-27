package com.vikingkittens.minecraft.mods.lostsouls.client.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

import com.vikingkittens.minecraft.mods.lostsouls.common.blocks.ModBlocks;

public class ClientInitBlocks {
    public static void init(Minecraft minecraft) {
        RenderTypeLookup.setRenderLayer(ModBlocks.SOUL_VALLEY_PORTAL, RenderType.translucent());
    }
}
