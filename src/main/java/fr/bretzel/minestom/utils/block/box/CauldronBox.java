package fr.bretzel.minestom.utils.block.box;

import fr.bretzel.minestom.utils.block.bounding.CubeBox;
import fr.bretzel.minestom.utils.block.Box;
import fr.bretzel.minestom.utils.block.shapes.Shape;
import fr.bretzel.minestom.utils.block.visual.CauldronVisual;

public class CauldronBox extends Box {

    private final Shape visual = new CauldronVisual();

    public CauldronBox() {
        setDefaultBox(new CubeBox());
    }

    @Override
    public Shape getOutlineShape() {
        return getVisualShape();
    }

    @Override
    public Shape getVisualShape() {
        return visual;
    }
}
