import processing.core.PImage;

import java.util.List;

public class Obstacle implements Entity {
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    public Obstacle(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.resourceLimit = 0;
        this.resourceCount = 0;
        this.actionPeriod = 0;
        this.animationPeriod = 0;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public PImage getCurrentImage() {
        return images.get( imageIndex );
    }

    @Override
    public List<PImage> getImages() {
        return null;
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

    public void nextImage() {
        imageIndex = (imageIndex + 1) % images.size();
    }


}