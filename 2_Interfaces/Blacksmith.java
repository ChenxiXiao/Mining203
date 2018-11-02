import processing.core.PImage;

import java.util.List;

public class Blacksmith implements Entity {
    //instantiation


    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod;

    //constructor
    public Blacksmith(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = 0;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public PImage getCurrentImage() {
        return images.get( imageIndex );
    }

    public List<PImage> getImages() {
        return images;
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

