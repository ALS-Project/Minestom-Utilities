package fr.bretzel.minestom.utils.block;

import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.BlockStateManager;
import fr.bretzel.minestom.states.state.Directional;
import fr.bretzel.minestom.states.state.Facing;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockUtils {
    private final Instance instance;
    private final Point position;
    private final BlockState blockState;

    public BlockUtils(Instance instance, Point position) {
        this.instance = instance;
        this.position = Pos.fromPoint(position);
        this.blockState = BlockStateManager.get(instance.getBlock(this.position));
    }


    public BlockUtils up() {
        return relative(Facing.UP);
    }

    public BlockUtils down() {
        return relative(Facing.DOWN);
    }

    public BlockUtils north() {
        return relative(Facing.NORTH);
    }

    public BlockUtils east() {
        return relative(Facing.EAST);
    }

    public BlockUtils south() {
        return relative(Facing.SOUTH);
    }

    public BlockUtils west() {
        return relative(Facing.WEST);
    }

    private BlockUtils relative(int x, int y, int z) {
        return new BlockUtils(instance, position.add(x, y, z));
    }

    public BlockUtils relative(Facing blockFace) {
        return relative(blockFace.getRelativeX(), blockFace.getRelativeY(), blockFace.getRelativeZ());
    }

    public BlockUtils relative(Directional directional) {
        return relative(directional.toBlockFacing());
    }

    public Block block() {
        return blockState.block();
    }

    public BlockState state() {
        return blockState;
    }

    public Instance instance() {
        return instance;
    }

    public Point position() {
        return position;
    }

    public boolean equals(Block block) {
        return block().compare(block, Block.Comparator.ID);
    }

    @Override
    public String toString() {
        return "BlockUtil{" +
                "instance=" + instance +
                ", position=" + position +
                ", blockState=" + blockState +
                '}';
    }

    @NotNull
    public static Block getOrDefault(@Nullable Block block, @NotNull Block defaults) {
        return block == null ? defaults : block;
    }
}
