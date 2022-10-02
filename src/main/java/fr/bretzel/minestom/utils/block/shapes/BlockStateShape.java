package fr.bretzel.minestom.utils.block.shapes;

import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.BlockStateManager;
import net.minestom.server.instance.block.Block;

public abstract class BlockStateShape<S extends BlockState> extends Shape {
    private final S blockStates;

    public BlockStateShape(Block block) {
        this.blockStates = (S) BlockStateManager.get(block);
    }

    public BlockStateShape(BlockState blockState) {
        this.blockStates = (S) blockState;
    }

    public S states() {
        return blockStates;
    }

    public void set(double x, double y, double z, double x1, double y1, double z1) {
        setMax(Math.max(x, x1), Math.max(y, y1), Math.max(z, z1));
        setMin(Math.min(x, x1), Math.min(y, y1), Math.min(z, z1));
    }
}
