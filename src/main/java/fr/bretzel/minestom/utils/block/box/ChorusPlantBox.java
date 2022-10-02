package fr.bretzel.minestom.utils.block.box;

import fr.bretzel.minestom.utils.block.outline.OutlineChorusPlant;
import fr.bretzel.minestom.utils.block.BoxState;
import fr.bretzel.minestom.utils.block.shapes.Shape;
import fr.bretzel.minestom.utils.block.visual.ChorusPlantVisual;
import fr.bretzel.minestom.states.BlockState;
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
