package fr.bretzel.minestom.utils.block.shape.box;

import fr.bretzel.minestom.utils.block.shape.Box;
import fr.bretzel.minestom.utils.block.shape.bounding.DragonEggBox;
import fr.bretzel.minestom.utils.block.shape.shapes.Shape;
import fr.bretzel.minestom.utils.block.shape.visual.DragonEggVisual;

public class DragonEgg extends Box {

    private final Shape box = new DragonEggVisual();

    public DragonEgg() {
        setDefaultBox(new DragonEggBox());
    }

    @Override
    public Shape getVisualShape() {
        return box;
    }
}
