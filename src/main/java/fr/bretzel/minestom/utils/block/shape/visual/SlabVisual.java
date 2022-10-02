package fr.bretzel.minestom.utils.block.shape.visual;

import fr.bretzel.minestom.utils.block.shape.shapes.BlockStateShape;
import fr.bretzel.minestomstates.BlockState;
import fr.bretzel.minestomstates.state.SlabType;
import net.minestom.server.instance.block.Block;

public class SlabVisual extends BlockStateShape<BlockState> {
    public SlabVisual(Block alternative) {
        super(alternative);

        SlabType slabType = states().get(SlabType.class);

        if (slabType == SlabType.BOTTOM) {
            set(0, 0, 0, 1, 0.5, 1);
        } else if (slabType == SlabType.TOP) {
            set(0, 0.5, 0, 1, 1, 1);
        } else {
            set(0, 0, 0, 1, 1, 1);
        }
    }
}
