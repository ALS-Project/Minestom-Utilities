package fr.bretzel.minestom.utils.block.box;

import fr.bretzel.minestom.utils.block.bounding.VoidShape;
import fr.bretzel.minestom.utils.block.Box;

public class VoidBox extends Box {
    public static final VoidBox VOID_BOX = new VoidBox();


    protected VoidBox() {
        setDefaultBox(VoidShape.VOID_SHAPE);
    }
}
