package fr.bretzel.minestom.utils.block.visual;

import fr.bretzel.minestom.utils.block.shapes.BlockStateShape;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.SlabType;
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
