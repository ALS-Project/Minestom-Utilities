package fr.bretzel.minestom.utils.block;

import fr.bretzel.minestom.utils.block.bounding.*;
import fr.bretzel.minestom.utils.block.box.*;
import fr.bretzel.minestom.utils.block.outline.*;
import fr.bretzel.minestom.utils.block.shapes.Shape;
import fr.bretzel.minestom.utils.block.visual.*;
import it.unimi.dsi.fastutil.shorts.Short2ObjectArrayMap;
import net.minestom.server.instance.block.Block;

public class BoxManager {

    private static final Short2ObjectArrayMap<Box> boxCache = new Short2ObjectArrayMap<>();

    public static Box get(Block block) {
        return boxCache.computeIfAbsent(block.stateId(), aShort -> getInternal(block));
    }

    private static Box getInternal(Block block) {
        if (block == Block.BAMBOO) return new BambooBoxState(block);
        else if (block == Block.BAMBOO_SAPLING) return new SimpleBox(new OutlineSaplings(), false);
        else if (block == Block.BELL) return new BellBoxState(block);
        else if (block == Block.BREWING_STAND) return new SimpleBox(new BrewingStandBox());
        else if (block == Block.CACTUS) return new SimpleBox(new CactusBounding());
        else if (block == Block.CAMPFIRE || block == Block.SOUL_CAMPFIRE) return new CampfireBoxState(block);
        else if (block.name().contains("carpet")) return new SimpleBox(new CarpetBox());
        else if (block == Block.CAULDRON) return new CauldronBox();
        else if (block == Block.CHAIN) return new SimpleBox(new ChainBox(block));
        else if (block == Block.COMPARATOR || block == Block.REPEATER) return new SimpleBox(new RedstoneTile());
        else if (block == Block.COMPOSTER) return new ComposterBox();
        else if (block == Block.CONDUIT) return new SimpleBox(new ConduitBox());
        else if (block == Block.DAYLIGHT_DETECTOR) return new SimpleBox(new DaylightDetectorBox());
        else if (block == Block.ENCHANTING_TABLE) return new SimpleBox(new EnchantingTableBox());
        else if (block.name().contains("chest")) return new SimpleBox(new ChestBox());
        else if (block == Block.FARMLAND) return new SimpleBox(new FarmlandBox());
        else if (block == Block.FLOWER_POT || block.name().contains("potted")) return new SimpleBox(new PottedBox());
        else if (block == Block.DIRT_PATH) return new SimpleBox(new GrassPathBox());
        else if (block != Block.PISTON_HEAD && block.name().endsWith("head")) return new SimpleBox(new HeadBox());
        else if (block == Block.LECTERN) return new LecternBoxState(block);
        else if (block == Block.LILY_PAD) return new SimpleBox(new LilyPadBox());
        else if (block == Block.RED_MUSHROOM || block == Block.BROWN_MUSHROOM) return new SimpleBox(new MushroomBox());
        else if (block == Block.PISTON_HEAD) return new SimpleBox(new PistonHeadBox(block));
        else if (block.name().contains("sapling") || block == Block.SEAGRASS)
            return new SimpleBox(new OutlineSaplings(), false);
        else if (block == Block.SCAFFOLDING) return new ScaffoldingBoxState(block);
        else if (block == Block.SOUL_SAND) return new SimpleBox(new SoulSandBox());
        else if (block.name().endsWith("stairs")) return new SimpleBox(new StairsVisual(block));
        else if (block == Block.STONECUTTER) return new SimpleBox(new StonecutterBox());
        else if (block.name().contains("slab")) return new SimpleBox(new SlabVisual(block));
        else if (block == Block.DRAGON_EGG) return new DragonEgg();
        else if (block == Block.LADDER) return new SimpleBox(new LadderVisual(block));
        else if (block.name().contains("button")) return new ButtonBoxState(block);
        else if (block == Block.END_ROD) return new SimpleBox(new EndRodVisual(block));
        else if (block.name().contains("torch")) return new TorchBoxState(block);
        else if (block == Block.SEA_PICKLE) return new SimpleBox(new OutlinePickle(block));
        else if (block == Block.COBWEB) return new SimpleBox(new CubeBox(), false);
        else if (block == Block.SUGAR_CANE)
            return new SimpleBox(new Shape(0.125, 0.0, 0.125, 0.875, 1.0, 0.875), false);
        else if (block == Block.KELP) return new SimpleBox(new Shape(0.0, 0.0, 0.0, 1.0, 0.5625, 1.0), false);
        else if (block == Block.KELP_PLANT) return new SimpleBox(new Shape(), false);
        else if (FlowerBox.flowers.contains(block)) return new FlowerBox();
        else if (block == Block.GRASS || block == Block.DEAD_BUSH || block == Block.FERN || block == Block.CRIMSON_ROOTS || block == Block.WARPED_ROOTS)
            return new SimpleBox(new Shape(0.125, 0.0, 0.125, 0.875, 0.8125, 0.875), false);
        else if (block == Block.NETHER_SPROUTS)
            return new SimpleBox(new Shape(0.125, 0.0, 0.125, 0.875, 0.1875, 0.875), false);
        else if (block == Block.TWISTING_VINES)
            return new SimpleBox(new Shape(0.25, 0.0, 0.25, 0.75, 0.9375, 0.75), false);
        else if (block == Block.TWISTING_VINES_PLANT)
            return new SimpleBox(new Shape(0.25, 0.0, 0.25, 0.75, 1.0, 0.75), false);
        else if (block == Block.WEEPING_VINES)
            return new SimpleBox(new Shape(0.28125, 0.625, 0.28125, 0.71875, 1.0, 0.71875), false);
        else if (block == Block.WEEPING_VINES_PLANT)
            return new SimpleBox(new Shape(0.0625, 0.0, 0.0625, 0.9375, 1.0, 0.9375), false);
        else if (block == Block.CRIMSON_FUNGUS || block == Block.WARPED_FUNGUS)
            return new SimpleBox(new Shape(0.25, 0.0, 0.25, 0.75, 0.5625, 0.75), false);
        else if (block == Block.VINE) return new SimpleBox(new OutlineVine(block), false);
        else if (block == Block.TURTLE_EGG) return new SimpleBox(new OutlineTurtleEgg(block));
        else if (block == Block.CAKE) return new SimpleBox(new CakeVisual(block));
        else if (block == Block.END_PORTAL_FRAME) return new SimpleBox(new OutlineEndPortalFrame(block));
        else if (block == Block.CHORUS_FLOWER) return new SimpleBox(new OutlineChorusFlower());
        else if (block == Block.CHORUS_PLANT) return new SimpleBox(new OutlineChorusPlant(block));
        else if (block.name().contains("fence") && !block.name().contains("gate"))
            return new SimpleBox(new FenceVisual(block));
        else if (block.name().contains("pane") || block == Block.IRON_BARS) return new SimpleBox(new PaneVisual(block));

        //Register a basic value if no box are present
        if (block.isSolid()) return new SolidShape();
        else return VoidBox.VOID_BOX;
    }
}
