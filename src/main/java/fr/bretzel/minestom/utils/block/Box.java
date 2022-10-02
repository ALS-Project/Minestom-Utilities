package fr.bretzel.minestom.utils.block;

import fr.bretzel.minestom.utils.block.shapes.Shape;

public abstract class Box {

    private Shape bounding = new Shape();
    private Shape visual = new Shape();
    private Shape outline = new Shape();

    public Shape getVisualShape() {
        return visual;
    }

    public Shape getBoundingBox() {
        return bounding;
    }

    public Shape getOutlineShape() {
        return outline;
    }

    public void setDefaultBox(Shape box) {
        this.bounding = box;
        this.visual = box;
        this.outline = box;
    }

    public void setOutline(Shape outline) {
        this.outline = outline;
    }

    public void setVisual(Shape visual) {
        this.visual = visual;
    }

    public void setBounding(Shape bounding) {
        this.bounding = bounding;
    }

    @Override
    public String toString() {
        return "Box{" +
                "bounding=" + bounding +
                ", visual=" + visual +
                ", outline=" + outline +
                '}';
    }
}
