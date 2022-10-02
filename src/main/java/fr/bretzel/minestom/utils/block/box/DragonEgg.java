package fr.bretzel.minestom.utils.block.box;

import fr.bretzel.minestom.utils.block.bounding.DragonEggBox;
import fr.bretzel.minestom.utils.block.Box;
import fr.bretzel.minestom.utils.block.shapes.Shape;
import fr.bretzel.minestom.utils.block.visual.DragonEggVisual;

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
