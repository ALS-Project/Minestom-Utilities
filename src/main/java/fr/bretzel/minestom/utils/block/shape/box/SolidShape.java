package fr.bretzel.minestom.utils.block.shape.box;

import fr.bretzel.minestom.utils.block.shape.Box;
import fr.bretzel.minestom.utils.block.shape.bounding.CubeBox;

public class SolidShape extends Box {
    public SolidShape() {
        setDefaultBox(new CubeBox());
    }
}
