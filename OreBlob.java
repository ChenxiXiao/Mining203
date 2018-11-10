import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public class OreBlob extends AnimatedEntity {

    private static final String QUAKE_KEY = "quake";


    public OreBlob(String id, Point position,
                   int actionPeriod, int animationPeriod, List<PImage> images) {
        super( id, position, images, 0, 0, actionPeriod, animationPeriod );
    }

    public void executeActivity(WorldModel world, ImageStore imageStore,
                                EventScheduler scheduler) {
        Optional<Entity> blobTarget = world.findNearest( getPosition(), new Vein( getId(), getPosition(), getActionPeriod(), getImages() ) ); // ????
        long nextPeriod = getActionPeriod();

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().getPosition();

            if (moveTo( world, blobTarget.get(), scheduler )) {
                Quake quake = new Quake( tgtPos, imageStore.getImageList( QUAKE_KEY ) );

                world.addEntity( quake );
                nextPeriod += getActionPeriod();
                quake.scheduleActions( scheduler, world, imageStore );
            }
        }
        scheduler.scheduleEvent( this, new Activity( this, world, imageStore ), nextPeriod );
    }

    public Point nextPosition(WorldModel world, Point destPos) {
        int horiz = Integer.signum( destPos.x - getPosition().x );
        Point newPos = new Point( getPosition().x + horiz, getPosition().y );

        Optional<Entity> occupant = world.getOccupant( newPos );

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get() instanceof Ore))) //occupant.get().kind == EntityKind.ORE
        {
            int vert = Integer.signum( destPos.y - getPosition().y );
            newPos = new Point( getPosition().x, getPosition().y + vert );
            occupant = world.getOccupant( newPos );

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get() instanceof Ore))) //occupant.get().kind == EntityKind.ORE
            {
                newPos = getPosition();
            }
        }

        return newPos;
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (getPosition().adjacent( target.getPosition() )) {
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