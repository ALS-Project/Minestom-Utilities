package fr.bretzel.minestom.utils.block.shape.box;

import fr.bretzel.minestom.utils.block.shape.Box;
import fr.bretzel.minestom.utils.block.shape.bounding.VoidShape;

public class VoidBox extends Box {
    public static final VoidBox VOID_BOX = new VoidBox();


    protected VoidBox() {
        setDefaultBox(VoidShape.VOID_SHAPE);
    }
}
