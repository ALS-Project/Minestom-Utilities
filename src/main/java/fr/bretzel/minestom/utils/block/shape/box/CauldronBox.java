package fr.bretzel.minestom.utils.block.shape.box;

import fr.bretzel.minestom.utils.block.shape.Box;
import fr.bretzel.minestom.utils.block.shape.bounding.CubeBox;
import fr.bretzel.minestom.utils.block.shape.shapes.Shape;
import fr.bretzel.minestom.utils.block.shape.visual.CauldronVisual;

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
