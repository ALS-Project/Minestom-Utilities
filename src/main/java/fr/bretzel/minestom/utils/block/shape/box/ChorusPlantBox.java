package fr.bretzel.minestom.utils.block.shape.box;

import fr.bretzel.minestom.utils.block.shape.BoxState;
import fr.bretzel.minestom.utils.block.shape.outline.OutlineChorusPlant;
import fr.bretzel.minestom.utils.block.shape.shapes.Shape;
import fr.bretzel.minestom.utils.block.shape.visual.ChorusPlantVisual;
import fr.bretzel.minestomstates.BlockState;
import net.minestom.server.instance.block.Block;

public class ChorusPlantBox extends BoxState<BlockState> {

    private final Shape visual;

    public ChorusPlantBox(Block blockAlternative) {
        super(blockAlternative);
        setDefaultBox(new OutlineChorusPlant(blockAlternative));
        visual = new ChorusPlantVisual(blockAlternative);
    }

    @Override
    public Shape getVisualShape() {
        return visual;
    }
}
