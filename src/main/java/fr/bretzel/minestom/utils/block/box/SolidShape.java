package fr.bretzel.minestom.utils.block.box;

import fr.bretzel.minestom.utils.block.bounding.CubeBox;
import fr.bretzel.minestom.utils.block.Box;

public class SolidShape extends Box {
    public SolidShape() {
        setDefaultBox(new CubeBox());
    }
}
