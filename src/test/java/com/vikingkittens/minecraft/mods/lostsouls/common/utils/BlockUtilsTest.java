package com.vikingkittens.minecraft.mods.lostsouls.common.utils;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import com.vikingkittens.minecraft.mods.lostsouls.MCTest;

public class BlockUtilsTest extends MCTest {
    private static final Logger LOGGER = LogManager.getLogger();

    private class BlocksLayout {
        protected String pattern;

        protected Map<BlockPos, BlockState> blockStatesNS = new HashMap<>();
        protected Map<BlockPos, BlockState> blockStatesEW = new HashMap<>();
        protected Map<BlockPos, BlockState> blockStatesFloor = new HashMap<>();

        public BlocksLayout(String pattern) {
            this.pattern = pattern;

            String[] rows = this.pattern.split("\\R");
            for (int rowNum = 0; rowNum < rows.length; rowNum++) {
                for (int colNum = 0; colNum < rows[rowNum].length(); colNum++) {
                    char c = rows[rowNum].charAt(colNum);
                    BlockState blockState;
                    switch (c) {
                        case 'S': blockState = stone; break;
                        case 'D': blockState = dirt; break;
                        case 'X': blockState = barrier; break;
                        default: blockState = air; break;
                    }
                    // NS
                    blockStatesNS.put(new BlockPos(
                            0,
                            rows.length / 2 - 1 - rowNum,
                            colNum - rows[rowNum].length() / 2
                    ), blockState);
                    // EW
                    blockStatesEW.put(new BlockPos(
                            colNum - rows[rowNum].length() / 2,
                            rows.length / 2 - 1 - rowNum,
                            0
                    ), blockState);
                    // Floor
                    blockStatesFloor.put(new BlockPos(
                            colNum - rows[rowNum].length() / 2,
                            0,
                            rows.length / 2 - 1 - rowNum
                    ), blockState);
                }
            }
        }

        public String getPattern() {
            return pattern;
        }

        public Map<BlockPos, BlockState> getBlockStatesNS() {
            return blockStatesNS;
        }

        public Map<BlockPos, BlockState> getBlockStatesEW() {
            return blockStatesEW;
        }

        public Map<BlockPos, BlockState> getBlockStatesFloor() {
            return blockStatesFloor;
        }

        private BlockState getBlockState(Map<BlockPos, BlockState> blockStates, BlockPos pos) {
            if (blockStates.containsKey(pos)) {
                return blockStates.get(pos);
            }
            return air;
        }

        public BlockState getBlockStateNS(BlockPos pos) {
            return getBlockState(blockStatesNS, pos);
        }

        public BlockState getBlockStateEW(BlockPos pos) {
            return getBlockState(blockStatesEW, pos);
        }

        public BlockState getBlockStateFloor(BlockPos pos) {
            return getBlockState(blockStatesFloor, pos);
        }
    }

    private class FloodFindTest extends BlocksLayout {
        protected String expected;

        protected Set<BlockPos> expectedBlockPositionsNS = null;
        protected Set<BlockPos> expectedBlockPositionsEW = null;
        protected Set<BlockPos> expectedBlockPositionsFloor = null;

        public FloodFindTest(String pattern, String expected) {
            super(pattern);
            this.expected = expected;
            if (this.expected != null) {
                BlocksLayout layout = new BlocksLayout(expected);
                expectedBlockPositionsNS = new HashSet<>();
                populateExpected(expectedBlockPositionsNS, layout.getBlockStatesNS());
                expectedBlockPositionsEW = new HashSet<>();
                populateExpected(expectedBlockPositionsEW, layout.getBlockStatesEW());
                expectedBlockPositionsFloor = new HashSet<>();
                populateExpected(expectedBlockPositionsFloor, layout.getBlockStatesFloor());
            }
        }

        private void populateExpected(Set<BlockPos> expectedBlockPositions, Map<BlockPos, BlockState> blockStates) {
            blockStates.forEach((pos, blockState) -> {
                if (blockState.getBlock() == barrier.getBlock()) {
                    expectedBlockPositions.add(pos);
                }
            });
        }

        public Set<BlockPos> getExpectedBlockPositionsNS() {
            return expectedBlockPositionsNS;
        }

        public Set<BlockPos> getExpectedBlockPositionsEW() {
            return expectedBlockPositionsEW;
        }

        public Set<BlockPos> getExpectedBlockPositionsFloor() {
            return expectedBlockPositionsFloor;
        }

        public String getPatternFromBlockPositions(Set<BlockPos> blockPositions, Direction.Axis axis) {
            if (blockPositions != null && !blockPositions.isEmpty()) {
                Integer minSourceRow = null; Integer maxSourceRow = null;
                Integer minSourceCol = null; Integer maxSourceCol = null;
                for (BlockPos pos : blockPositions) {
                    if (axis == Direction.Axis.Y) {
                        if (minSourceRow == null || pos.getZ() < minSourceRow) { minSourceRow = pos.getZ(); }
                        if (maxSourceRow == null || pos.getZ() > maxSourceRow) { maxSourceRow = pos.getZ(); }
                        if (minSourceCol == null || pos.getX() < minSourceCol) { minSourceCol = pos.getX(); }
                        if (maxSourceCol == null || pos.getX() > maxSourceCol) { maxSourceCol = pos.getX(); }
                    } else {
                        if (minSourceRow == null || pos.getY() < minSourceRow) { minSourceRow = pos.getY(); }
                        if (maxSourceRow == null || pos.getY() > maxSourceRow) { maxSourceRow = pos.getY(); }
                        if (axis == Direction.Axis.Z) {
                            if (minSourceCol == null || pos.getZ() < minSourceCol) { minSourceCol = pos.getZ(); }
                            if (maxSourceCol == null || pos.getZ() > maxSourceCol) { maxSourceCol = pos.getZ(); }
                        } else {
                            if (minSourceCol == null || pos.getX() < minSourceCol) { minSourceCol = pos.getX(); }
                            if (maxSourceCol == null || pos.getX() > maxSourceCol) { maxSourceCol = pos.getX(); }
                        }
                    }
                }

                int numRows = maxSourceRow - minSourceRow + 1;
                int numCols = maxSourceCol - minSourceCol + 1;
                char[][] blocks = new char[numRows][numCols];
                for (int rowNum = 0; rowNum < numRows; rowNum++) {
                    for (int colNum = 0; colNum < numCols; colNum++) {
                        blocks[rowNum][colNum] = ' ';
                    }
                }

                for (BlockPos pos : blockPositions) {
                    int rowNum;
                    int colNum;
                    if (axis == Direction.Axis.Y) {
                        rowNum = pos.getZ() - minSourceRow;
                        colNum = pos.getX() - minSourceCol;
                    } else {
                        rowNum = pos.getY() - minSourceRow;
                        if (axis == Direction.Axis.Z) {
                            colNum = pos.getZ() - minSourceCol;
                        } else {
                            colNum = pos.getX() - minSourceCol;
                        }
                    }
                    blocks[rowNum][colNum] = 'X';
                }

                StringBuilder pattern = new StringBuilder();
                for (int rowNum = numRows - 1; rowNum >= 0; rowNum--) {
                    pattern.append(blocks[rowNum]);
                    pattern.append('\n');
                }
                return pattern.toString();
            }
            return "(empty)";
        }
    }

    @Test
    public void testFloodFindBorderedBy() {
        FloodFindTest tests[] = {
                // All air
                new FloodFindTest(""
                        + "          \n"
                        + "          \n"
                        + "          \n"
                        + "          \n"
                        + "          \n"
                        + "          \n",
                        null
                ),
                // Stone with a hole
                new FloodFindTest(""
                        + "          \n"
                        + " SSSSSSSS \n"
                        + "        S \n"
                        + " S      S \n"
                        + " SSSSSSSS \n"
                        + "          \n",
                        null
                ),
                // Not all stone
                new FloodFindTest(""
                        + "          \n"
                        + " SSSSSSSS \n"
                        + " D      S \n"
                        + " S      S \n"
                        + " SSSSSSSS \n"
                        + "          \n",
                        null
                ),
                // Dirt
                new FloodFindTest(""
                        + "          \n"
                        + " DDDDDDDD \n"
                        + " D      D \n"
                        + " D      D \n"
                        + " DDDDDDDD \n"
                        + "          \n",
                        null
                ),
                new FloodFindTest(""
                        + "SSSSSSSSSS\n"
                        + "S        S\n"
                        + "S        S\n"
                        + "S        S\n"
                        + "S        S\n"
                        + "SSSSSSSSSS\n", ""
                        + "          \n"
                        + " XXXXXXXX \n"
                        + " XXXXXXXX \n"
                        + " XXXXXXXX \n"
                        + " XXXXXXXX \n"
                        + "          \n"
                ),
                new FloodFindTest(""
                        + "SSSSSSSSSS\n"
                        + "S S      S\n"
                        + "S S      S\n"
                        + "S      S S\n"
                        + "S      S S\n"
                        + "SSSSSSSSSS\n", ""
                        + "          \n"
                        + " X XXXXXX \n"
                        + " X XXXXXX \n"
                        + " XXXXXX X \n"
                        + " XXXXXX X \n"
                        + "          \n"
                ),
                new FloodFindTest(""
                        + "SSSSSSSSSS\n"
                        + "S        S\n"
                        + "SSS      S\n"
                        + "S      SSS\n"
                        + "S        S\n"
                        + "SSSSSSSSSS\n", ""
                        + "          \n"
                        + " XXXXXXXX \n"
                        + "   XXXXXX \n"
                        + " XXXXXX   \n"
                        + " XXXXXXXX \n"
                        + "          \n"
                ),
                new FloodFindTest(""
                        + "  SSSSSS  \n"
                        + "  S    S  \n"
                        + "SSS    SSS\n"
                        + "S        S\n"
                        + "SSS    SSS\n"
                        + "  SSSSSS  \n", ""
                        + "          \n"
                        + "   XXXX   \n"
                        + "   XXXX   \n"
                        + " XXXXXXXX \n"
                        + "   XXXX   \n"
                        + "          \n"
                )
        };

        for (FloodFindTest test : tests) {
            LOGGER.info("Pattern:\n" + test.getPattern());
            Set<BlockPos> blockPositions;

            // NS
            reset(world);
            when(world.getBlockState(any(BlockPos.class))).thenAnswer((Answer<BlockState>) invocation -> {
                BlockPos pos = invocation.getArgument(0);
                return test.getBlockStateNS(pos);
            });
            blockPositions = BlockUtils.floodFindSameBorderedBy(
                    world, Direction.Axis.Z, 15, stone.getBlock(), origin);
            LOGGER.info("Found NS:\n" + test.getPatternFromBlockPositions(blockPositions, Direction.Axis.Z));
            assertEquals(test.getExpectedBlockPositionsNS(), blockPositions);
            assertNull(BlockUtils.floodFindSameBorderedBy(
                    world, Direction.Axis.X, 15, stone.getBlock(), origin));
            assertNull(BlockUtils.floodFindSameBorderedBy(
                    world, Direction.Axis.Y, 15, stone.getBlock(), origin));

            // EW
            reset(world);
            when(world.getBlockState(any(BlockPos.class))).thenAnswer((Answer<BlockState>) invocation -> {
                BlockPos pos = invocation.getArgument(0);
                return test.getBlockStateEW(pos);
            });
            assertNull(BlockUtils.floodFindSameBorderedBy(
                    world, Direction.Axis.Z, 15, stone.getBlock(), origin));
            blockPositions = BlockUtils.floodFindSameBorderedBy(
                    world, Direction.Axis.X, 15, stone.getBlock(), origin);
            LOGGER.info("Found EW:\n" + test.getPatternFromBlockPositions(blockPositions, Direction.Axis.X));
            assertEquals(test.getExpectedBlockPositionsEW(), blockPositions);
            assertNull(BlockUtils.floodFindSameBorderedBy(
                    world, Direction.Axis.Y, 15, stone.getBlock(), origin));

            // Floor
            reset(world);
            when(world.getBlockState(any(BlockPos.class))).thenAnswer((Answer<BlockState>) invocation -> {
                BlockPos pos = invocation.getArgument(0);
                return test.getBlockStateFloor(pos);
            });
            assertNull(BlockUtils.floodFindSameBorderedBy(
                    world, Direction.Axis.Z, 15, stone.getBlock(), origin));
            assertNull(BlockUtils.floodFindSameBorderedBy(
                    world, Direction.Axis.X, 15, stone.getBlock(), origin));
            blockPositions = BlockUtils.floodFindSameBorderedBy(
                    world, Direction.Axis.Y, 15, stone.getBlock(), origin);
            LOGGER.info("Found Floor:\n" + test.getPatternFromBlockPositions(blockPositions, Direction.Axis.Y));
            assertEquals(test.getExpectedBlockPositionsFloor(), blockPositions);
        }
    }
}
