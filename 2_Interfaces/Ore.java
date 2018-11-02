import processing.core.PImage;

import java.util.List;
import java.util.Random;


public class Ore implements Movable {
    private String id;
    private Point position;
    private int actionPeriod;
    private List<PImage> images;
    private static final Random rand = new Random();
    private int imageIndex;

    public static final String BLOB_KEY = "blob";
    public static final String BLOB_ID_SUFFIX = " -- blob";
    public static final int BLOB_PERIOD_SCALE = 4;
    public static final int BLOB_ANIMATION_MIN = 50;
    public static final int BLOB_ANIMATION_MAX = 150;

    public Ore(String id, Point position, int actionPeriod, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.actionPeriod = actionPeriod;
        this.images = images;
        this.imageIndex = 0;

    }

    public PImage getCurrentImage() {
        return images.get( imageIndex );
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public List<PImage> getImages() {
        return images;
    }


    public void setPosition(Point position) {
        this.position = position;
    }

    public Point getPosition() {
        return position;
    }

    public void nextImage() {
        imageIndex = (imageIndex + 1) % images.size();
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent( this, new Activity( this, world, imageStore ), actionPeriod );
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        Point pos = position;  // store current position before removing
        world.removeEntity( this );
        scheduler.unscheduleAllEvents( this );

        OreBlob blob = new OreBlob( id + BLOB_ID_SUFFIX, pos, actionPeriod / BLOB_PERIOD_SCALE, BLOB_ANIMATION_MIN +
                rand.nextInt( BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN ),
                imageStore.getImageList( BLOB_KEY ) );

        world.addEntity( blob );
        blob.scheduleActions( scheduler, world, imageStore );

    }
}