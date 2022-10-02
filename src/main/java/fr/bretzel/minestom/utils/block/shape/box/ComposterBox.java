package fr.bretzel.minestom.utils.block.shape.box;


import fr.bretzel.minestom.utils.block.shape.Box;
import fr.bretzel.minestom.utils.block.shape.bounding.CubeBox;
import fr.bretzel.minestom.utils.block.shape.shapes.Shape;
import fr.bretzel.minestom.utils.block.shape.visual.ComposterVisual;

public class ComposterBox extends Box {
    private final Shape visual = new ComposterVisual();

    public ComposterBox() {
        setDefaultBox(new CubeBox());
    }

    @Override
    public Shape getVisualShape() {
        return visual;
    }
}
