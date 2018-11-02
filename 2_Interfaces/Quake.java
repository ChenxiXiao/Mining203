import processing.core.PImage;

import java.util.List;


public class Quake implements Animated {
    private String id;
    private Point position;
    private int actionPeriod;
    private List<PImage> images;
    private int resourceLimit;
    private int animationPeriod;
    private int resourceCount;
    private int imageIndex;
    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;

    public Quake(Point position, List<PImage> images) {
        this.id = QUAKE_ID;
        this.position = position;
        this.actionPeriod = QUAKE_ACTION_PERIOD;
        this.images = images;
        this.resourceCount = 0;
        this.resourceLimit = 0;
        this.imageIndex = 0;
        this.animationPeriod = QUAKE_ANIMATION_PERIOD;
    }

    //    return new Entity(EntityKind.QUAKE, QUAKE_ID, position, images,
    //         0, 0, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);

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

    public int getAnimationPeriod() {
        return animationPeriod;
    }

    public void nextImage() {
        imageIndex = (imageIndex + 1) % images.size();
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.unscheduleAllEvents( this );
        world.removeEntity( this );
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        int QUAKE_ANIMATION_REPEAT_COUNT = 10;
        scheduler.scheduleEvent( this, new Activity( this, world, imageStore ), actionPeriod );
        scheduler.scheduleEvent( this, new Animation( this, QUAKE_ANIMATION_REPEAT_COUNT ), getAnimationPeriod() );
    }
}