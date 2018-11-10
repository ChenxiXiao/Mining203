import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.lang.String;

public class MinerFull extends AnimatedEntity {

    public MinerFull(String id, int resourceLimit, Point position, int actionPeriod, int animationPeriod, List<PImage> images) {
        super( id, position, images,
                resourceLimit, resourceLimit, actionPeriod, animationPeriod );
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest( getPosition(), new Blacksmith( getId(), getPosition(), getImages() ) ); // new blacksmith ().getClass().getname() is a string
        if (fullTarget.isPresent() &&
                moveTo( world, fullTarget.get(), scheduler )) {
            transform( world, scheduler, imageStore );
        } else {
            scheduler.scheduleEvent( this,
                    new Activity( this, world, imageStore ),
                    getActionPeriod() );
        }
    }

    public void transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        MinerNotFull miner = new MinerNotFull( getId(), resourceLimit, getPosition(), getActionPeriod(), animationPeriod, getImages() );
        world.removeEntity( this );
        scheduler.unscheduleAllEvents( this );

        world.addEntity( miner );
        miner.scheduleActions( scheduler, world, imageStore );
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
        if (this.getPosition().adjacent( target.getPosition() )) {
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