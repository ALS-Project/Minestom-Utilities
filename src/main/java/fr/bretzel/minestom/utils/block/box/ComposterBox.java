package fr.bretzel.minestom.utils.block.box;


import fr.bretzel.minestom.utils.block.bounding.CubeBox;
import fr.bretzel.minestom.utils.block.Box;
import fr.bretzel.minestom.utils.block.shapes.Shape;
import fr.bretzel.minestom.utils.block.visual.ComposterVisual;

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
