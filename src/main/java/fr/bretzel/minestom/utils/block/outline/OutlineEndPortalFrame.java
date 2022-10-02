package fr.bretzel.minestom.utils.block.outline;

import fr.bretzel.minestom.utils.block.shapes.BlockStateShapes;
import fr.bretzel.minestom.utils.block.shapes.Shape;
import fr.bretzel.minestom.utils.raytrace.RayBlockResult;
import fr.bretzel.minestom.utils.raytrace.RayTraceContext;
import fr.bretzel.minestom.states.BlockState;
import fr.bretzel.minestom.states.state.BooleanState;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.Nullable;

public class OutlineEndPortalFrame extends BlockStateShapes<BlockState> {

    private final boolean hasEye;
    private Shape simpleShape;

    public OutlineEndPortalFrame(Block alternative) {
        super(alternative);

        hasEye = states().get(BooleanState.Of("eye"));

        if (hasEye) {
            add(0.0, 0.0, 0.0, 1.0, 0.8125, 1.0);
            add(0.25, 0.8125, 0.25, 0.75, 1.0, 0.75);
        } else {
            simpleShape = new Shape(0.0, 0.0, 0.0, 1.0, 0.8125, 1.0);
        }
    }

    @Nullable
    @Override
    public RayBlockResult rayTraceBlocks(RayTraceContext context, Point blockPosition, Point offset) {
        if (hasEye)
            return super.rayTraceBlocks(context, blockPosition, offset);
        else return simpleShape.rayTraceBlocks(context, blockPosition, offset);
    }

    @Override
    public boolean isInside(Vec position) {
        if (hasEye)
            return super.isInside(position);
        else return simpleShape.isInside(position);
    }
}
