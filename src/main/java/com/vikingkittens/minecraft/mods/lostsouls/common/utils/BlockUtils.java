package com.vikingkittens.minecraft.mods.lostsouls.common.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockUtils {
    public static Set<BlockPos> floodFindBorderedBy(
            World world,
            Direction.Axis axis,
            int distanceLimit,
            Block find,
            Block border,
            BlockPos pos
    ) {
        Set<BlockPos> positions = new HashSet<>();
        Stack<BlockPos> scanPositions = new Stack<>();
        BlockPos[] neighborPositions = new BlockPos[4];

        if (world.getBlockState(pos).getBlock() == find) {
            scanPositions.push(pos);
        }

        while (!scanPositions.isEmpty()) {
            BlockPos scanPos = scanPositions.pop();
            if (
                    Math.abs(pos.getX() - scanPos.getX()) > distanceLimit ||
                    Math.abs(pos.getY() - scanPos.getY()) > distanceLimit ||
                    Math.abs(pos.getZ() - scanPos.getZ()) > distanceLimit
            ) {
                // Scanning has extended beyond the limit and/or the border has a leak
                return null;
            }

            if (!positions.contains(scanPos)) {
                positions.add(scanPos);
                if (axis == Direction.Axis.X) { // EW Wall - Up, Down, East, West
                    neighborPositions[0] = scanPos.above();
                    neighborPositions[1] = scanPos.below();
                    neighborPositions[2] = scanPos.east();
                    neighborPositions[3] = scanPos.west();
                } else if (axis == Direction.Axis.Z) { // NS Wall - Up, Down, North, South
                    neighborPositions[0] = scanPos.above();
                    neighborPositions[1] = scanPos.below();
                    neighborPositions[2] = scanPos.north();
                    neighborPositions[3] = scanPos.south();
                } else if (axis == Direction.Axis.Y) { // Floor - North, East, South, West
                    neighborPositions[0] = scanPos.north();
                    neighborPositions[1] = scanPos.south();
                    neighborPositions[2] = scanPos.east();
                    neighborPositions[3] = scanPos.west();
                }
                for (int neighborNum = 0; neighborNum < 4; neighborNum++) {
                    Block neighborBlock = world.getBlockState(neighborPositions[neighborNum]).getBlock();
                    if (neighborBlock == find) {
                        scanPositions.push(neighborPositions[neighborNum]);
                    } else if (neighborBlock != border) {
                        // Inconsistent border
                        return null;
                    }
                }
            }
        }

        return positions;
    }

    public static Set<BlockPos> floodFindSameBorderedBy(
            World world,
            Direction.Axis axis,
            int distanceLimit,
            Block border,
            BlockPos pos
    ) {
        return floodFindBorderedBy(
                world,
                axis,
                distanceLimit,
                world.getBlockState(pos).getBlock(),
                border,
                pos
        );
    }
}
