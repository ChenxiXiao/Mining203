import processing.core.PImage;
import java.util.Optional;
import java.util.List;
import java.lang.*;

public class MinerNotFull extends AnimatedEntity {

    public MinerNotFull(String id, int resourceLimit, Point position, int actionPeriod, int animationPeriod,
                        List<PImage> images) {
        super( id, position, images, resourceLimit, 0, actionPeriod, animationPeriod );
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> notFullTarget = world.findNearest( getPosition(), new Ore( getId(), getPosition(), getActionPeriod(), getImages() ) );

        if (!notFullTarget.isPresent() ||

                !moveTo( world, notFullTarget.get(), scheduler ) ||
                !transform( world, scheduler, imageStore )) {
            scheduler.scheduleEvent( this, new Activity( this, world, imageStore ), getActionPeriod() );
        }
    }

    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (resourceCount >= resourceCount) {
            MinerFull miner = new MinerFull( getId(), resourceLimit, getPosition(), getActionPeriod(), animationPeriod, getImages() );
            world.removeEntity( this );
            scheduler.unscheduleAllEvents( this );
            world.addEntity( miner );
            miner.scheduleActions( scheduler, world, imageStore );
            return true;
        }

        return false;
    }

    public Point nextPosition(WorldModel world, Point destPos) {
        int horiz = Integer.signum( destPos.x - getPosition().x );
        Point newPos = new Point( getPosition().x + horiz,
                getPosition().y );

        if (horiz == 0 || world.isOccupied( newPos )) {
            int vert = Integer.signum( destPos.y - getPosition().y );
            newPos = new Point( getPosition().x,
                    getPosition().y + vert );

            if (vert == 0 || world.isOccupied( newPos )) {
                newPos = getPosition();
            }
        }

        return newPos;
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (getPosition().adjacent( target.getPosition() )) {
            resourceCount += 1;
            world.removeEntity( target );
            scheduler.unscheduleAllEvents( target );
            return true;
        } else {
            Point nextPos = nextPosition( world, target.getPosition() );

            if (!this.getPosition().equals( nextPos )) {
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