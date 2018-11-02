import processing.core.PImage;

import java.util.Optional;
import java.util.List;
import java.lang.*;

public class MinerNotFull implements Animated {

    private String id;
    private Point position;
    private int actionPeriod;
    private int resourceLimit;
    private int animationPeriod;
    private List<PImage> images;
    private int resourceCount;
    private int imageIndex;


    public MinerNotFull(String id, int resourceLimit, Point position, int actionPeriod, int animationPeriod,
                        List<PImage> images) {
        this.id = id;
        this.position = position;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.images = images;
        this.resourceLimit = resourceLimit;
        this.resourceCount = 0;
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


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> notFullTarget = world.findNearest( position, new Ore( id, position, actionPeriod, images ) );

        if (!notFullTarget.isPresent() ||

                !moveTo( world, notFullTarget.get(), scheduler ) ||
                !transform( world, scheduler, imageStore )) {
            scheduler.scheduleEvent( this, new Activity( this, world, imageStore ), actionPeriod );
        }
    }

    public int getAnimationPeriod() {
        return animationPeriod;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent( this, new Activity( this, world, imageStore ), actionPeriod );
        scheduler.scheduleEvent( this, new Animation( this, 0 ), getAnimationPeriod() );
    }

    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (resourceCount >= resourceLimit) {
            MinerFull miner = new MinerFull( id, resourceLimit, position, actionPeriod, animationPeriod, images );
            world.removeEntity( this );
            scheduler.unscheduleAllEvents( this );
            world.addEntity( miner );
            miner.scheduleActions( scheduler, world, imageStore );
            return true;
        }

        return false;
    }

    public Point nextPosition(WorldModel world, Point destPos) {
        int horiz = Integer.signum( destPos.x - position.x );
        Point newPos = new Point( position.x + horiz,
                position.y );

        if (horiz == 0 || world.isOccupied( newPos )) {
            int vert = Integer.signum( destPos.y - position.y );
            newPos = new Point( position.x,
                    position.y + vert );

            if (vert == 0 || world.isOccupied( newPos )) {
                newPos = position;
            }
        }

        return newPos;
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.position.adjacent( target.getPosition() )) {
            this.resourceCount += 1;
            world.removeEntity( target );
            scheduler.unscheduleAllEvents( target );
            return true;
        } else {
            Point nextPos = nextPosition( world, target.getPosition() );

            if (!this.position.equals( nextPos )) {
                Optional<Entity> occupant = world.getOccupant( nextPos );
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents( occupant.get() );
                }

                world.moveEntity( this, nextPos );
            }
            return false;
        }
    }
}