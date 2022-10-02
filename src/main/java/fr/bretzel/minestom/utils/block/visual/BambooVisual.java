package fr.bretzel.minestom.utils.block.visual;

import fr.bretzel.minestom.utils.block.shapes.BlockStateShape;
import fr.bretzel.minestom.utils.raytrace.OffsetType;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.IntegerState;
import net.minestom.server.instance.block.Block;

public class BambooVisual extends BlockStateShape<BlockState> {

    public static final IntegerState age = IntegerState.Of("age", 0, 0, 1);

    public BambooVisual(Block alternative) {
        super(alternative);

        if (states().get(age) == 0) {
            set(0.4375, 0.0, 0.4375, 0.5625, 1.0, 0.5625);
        } else {
            set(0.40625, 0.0, 0.40625, 0.59375, 1.0, 0.59375);
        }
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }
}
