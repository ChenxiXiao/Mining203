import processing.core.PImage;

import java.util.List;


public abstract class AnimatedEntity extends ActiveEntity {
    protected int animationPeriod;

    public AnimatedEntity(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        super( id, position, actionPeriod, images );
        this.animationPeriod = animationPeriod;
    }

    public void nextImage() {
        setImageIndex( (getImageIndex() + 1) % getImages().size() );
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent( this, new Activity( this, world, imageStore ), animationPeriod );
        scheduler.scheduleEvent( this, new Animation( this, 0 ), animationPeriod );
    }
}
