package fr.bretzel.minestom.utils.block.box;

import fr.bretzel.minestom.utils.block.bounding.VoidShape;
import fr.bretzel.minestom.utils.block.Box;
import fr.bretzel.minestom.utils.block.shapes.Shape;

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
