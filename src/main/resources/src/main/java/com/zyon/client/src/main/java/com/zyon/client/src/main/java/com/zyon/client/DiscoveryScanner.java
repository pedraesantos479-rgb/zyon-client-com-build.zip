package com.zyon.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Reads only chunks already present in the client's chunk cache.
 * It deliberately never requests a chunk and never sends packets.
 */
public final class DiscoveryScanner {
    public record Finding(String label, BlockPos pos, int count, String clue) {}
    private final Map<String, Finding> findings = new LinkedHashMap<>();
    private int cooldown;

    public Map<String, Finding> findings() { return Map.copyOf(findings); }

    public void tick(MinecraftClient client) {
        if (cooldown-- > 0) return;
        cooldown = 40;
        if (client.world == null || client.player == null) return;

        findings.clear();
        int centerX = client.player.getChunkPos().x;
        int centerZ = client.player.getChunkPos().z;
        int r = Math.max(1, Math.min(16, ZyonClient.SETTINGS.rangeChunks));

        for (int cx = centerX - r; cx <= centerX + r; cx++) {
            for (int cz = centerZ - r; cz <= centerZ + r; cz++) {
                WorldChunk chunk = client.world.getChunkManager().getWorldChunk(cx, cz);
                if (chunk == null) continue; // no chunk loading
                inspectChunk(chunk);
            }
        }
    }

    private void inspectChunk(WorldChunk chunk) {
        int containers = 0, hoppers = 0, redstone = 0, amethyst = 0, machinery = 0;
        BlockPos.Mutable pos = new BlockPos.Mutable();
        for (int y = chunk.getBottomY(); y < chunk.getTopY(); y += 1) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    pos.set(chunk.getPos().getStartX() + x, y, chunk.getPos().getStartZ() + z);
                    BlockState state = chunk.getBlockState(pos);
                    Block b = state.getBlock();
                    if (isContainer(b)) containers++;
                    if (b == Blocks.HOPPER) hoppers++;
                    if (isRedstone(b)) redstone++;
                    if (isAmethyst(b)) amethyst++;
                    if (isMachine(b)) machinery++;
                }
            }
        }
        BlockPos center = new BlockPos(chunk.getPos().getStartX() + 8, chunk.getBottomY(),
                chunk.getPos().getStartZ() + 8);
        add("Containers", center, containers, "Baús e barris encontrados");
        add("Hoppers", center, hoppers, "Funis podem indicar armazenamento automático");
        add("Redstone", center, redstone, "Componentes de redstone");
        add("Amethyst", center, amethyst, "Blocos e cristais de ametista");
        add("Machinery", center, machinery, "Fornos, dispensers e blocos de trabalho");
    }

    private void add(String key, BlockPos pos, int count, String clue) {
        if (count > 0) findings.put(key + pos.getX() + ":" + pos.getZ(),
                new Finding(key, pos.toImmutable(), count, clue));
    }

    private boolean isContainer(Block b) {
        return b == Blocks.CHEST || b == Blocks.TRAPPED_CHEST || b == Blocks.BARREL ||
                b == Blocks.ENDER_CHEST;
    }
    private boolean isRedstone(Block b) {
        return b == Blocks.REDSTONE_WIRE || b == Blocks.REPEATER || b == Blocks.COMPARATOR ||
                b == Blocks.REDSTONE_TORCH || b == Blocks.PISTON || b == Blocks.STICKY_PISTON ||
                b == Blocks.OBSERVER;
    }
    private boolean isAmethyst(Block b) {
        return b == Blocks.AMETHYST_BLOCK || b == Blocks.BUDDING_AMETHYST ||
                b == Blocks.AMETHYST_CLUSTER || b == Blocks.LARGE_AMETHYST_BUD ||
                b == Blocks.MEDIUM_AMETHYST_BUD || b == Blocks.SMALL_AMETHYST_BUD;
    }
    private boolean isMachine(Block b) {
        return b == Blocks.FURNACE || b == Blocks.BLAST_FURNACE || b == Blocks.SMOKER ||
                b == Blocks.DISPENSER || b == Blocks.DROPPER || b == Blocks.CRAFTER ||
                b == Blocks.BREWING_STAND;
    }
}
