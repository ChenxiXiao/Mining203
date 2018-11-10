import java.util.List;

import processing.core.PImage;

public abstract class Entity {

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    protected int resourceLimit;
    protected int resourceCount;

    public Entity(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    public PImage getCurrentImage() {
        return images.get( imageIndex );
    }

    public List<PImage> getImages() {
        return images;
    }

    public void setImages(List<PImage> images) {
        this.images = images;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point point) {
        this.position = point;
    }
}
