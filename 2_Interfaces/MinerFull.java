
import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.lang.String;

public class MinerFull implements Animated {

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;




    public MinerFull(String id, int resourceLimit, Point position, int actionPeriod, int animationPeriod,
                     List<PImage> images) {
        this.id = id;
        this.resourceLimit = resourceLimit;
        this.position = position;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.images = images;
        this.imageIndex = 0;
        this.resourceCount = resourceLimit;
    }

    public PImage getCurrentImage()
    {return images.get(imageIndex);}

    public int getImageIndex() {
        return imageIndex;
    }

    public List<PImage> getImages() {
        return images;
    }

    public void nextImage() {
        imageIndex = (imageIndex + 1) % images.size();
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Point getPosition() {
        return position;
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest( position, new Blacksmith( id, position, images ) ); // new blacksmith ().getClass().getname() is a string
        if (fullTarget.isPresent() &&
                moveTo( world, fullTarget.get(), scheduler )) {
            transform( world, scheduler, imageStore );
        } else {
            scheduler.scheduleEvent( this,
                    new Activity( this, world, imageStore ),
                    actionPeriod );
        }
    }

    public int getAnimationPeriod() {
        return animationPeriod;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent( this, new Activity( this, world, imageStore ), actionPeriod );
        scheduler.scheduleEvent( this, new Animation( this, 0 ), getAnimationPeriod() );
    }

    public void transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        MinerNotFull miner = new MinerNotFull( id, resourceLimit, position, actionPeriod, animationPeriod, images );
        world.removeEntity( this );
        scheduler.unscheduleAllEvents( this );

        world.addEntity( miner );
        miner.scheduleActions( scheduler, world, imageStore );
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

//?????????????
    /*
    public static boolean parse(String [] properties, WorldModel world,
                                     ImageStore imageStore)
    {
        if (properties.length == MINER_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[MINER_COL]),
                    Integer.parseInt(properties[MINER_ROW]));
            MinerNotFull entity = new MinerNotFull(properties[MINER_ID],
                    Integer.parseInt(properties[MINER_LIMIT]),
                    pt,
                    Integer.parseInt(properties[MINER_ACTION_PERIOD]),
                    Integer.parseInt(properties[MINER_ANIMATION_PERIOD]),
                    imageStore.getImageList(MINER_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == MINER_NUM_PROPERTIES;
    }
*/
}
