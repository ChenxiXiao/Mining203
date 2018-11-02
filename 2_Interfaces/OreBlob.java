
import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OreBlob implements Animated {

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;


    public OreBlob(String id, Point position,
                   int actionPeriod, int animationPeriod, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = 0;
        this.resourceCount = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
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

    public void nextImage() {
        imageIndex = (imageIndex + 1) % images.size();
    }

    public Point getPosition() {
        return position;
    }

    public int getAnimationPeriod() {
        return animationPeriod;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {

        scheduler.scheduleEvent( this, new Activity( this, world, imageStore ), actionPeriod );
        scheduler.scheduleEvent( this, new Animation( this, 0 ), getAnimationPeriod() );
    }

    public void executeActivity(WorldModel world, ImageStore imageStore,
                                EventScheduler scheduler) {
        final String QUAKE_KEY = "quake";
        Optional<Entity> blobTarget = world.findNearest( position, new Vein( id, position, actionPeriod, images ) ); // ????
        long nextPeriod = actionPeriod;

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().getPosition();

            if (moveTo( world, blobTarget.get(), scheduler )) {
                Quake quake = new Quake( tgtPos, imageStore.getImageList( QUAKE_KEY ) );

                world.addEntity( quake );
                nextPeriod += actionPeriod;
                quake.scheduleActions( scheduler, world, imageStore );
            }
        }
        scheduler.scheduleEvent( this, new Activity( this, world, imageStore ), nextPeriod );
    }

    public Point nextPosition(WorldModel world, Point destPos) {
        int horiz = Integer.signum( destPos.x - position.x );
        Point newPos = new Point( position.x + horiz, position.y );

        Optional<Entity> occupant = world.getOccupant( newPos );

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get() instanceof Ore))) //occupant.get().kind == EntityKind.ORE
        {
            int vert = Integer.signum( destPos.y - position.y );
            newPos = new Point( position.x, position.y + vert );
            occupant = world.getOccupant( newPos );

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get() instanceof Ore))) //occupant.get().kind == EntityKind.ORE
            {
                newPos = position;
            }
        }

        return newPos;
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.position.adjacent( target.getPosition() )) {
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