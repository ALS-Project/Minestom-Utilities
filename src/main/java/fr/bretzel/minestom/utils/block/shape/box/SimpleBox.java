package fr.bretzel.minestom.utils.block.shape.box;

import fr.bretzel.minestom.utils.block.shape.Box;
import fr.bretzel.minestom.utils.block.shape.bounding.VoidShape;
import fr.bretzel.minestom.utils.block.shape.shapes.Shape;

public class SimpleBox extends Box {

    public SimpleBox(Shape box) {
        setDefaultBox(box);
    }


    public SimpleBox(Shape box, boolean hasBounding) {
        setDefaultBox(box);

        if (!hasBounding)
            setBounding(VoidShape.VOID_SHAPE);
    }
}
