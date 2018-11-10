import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Vein extends ActiveEntity {
    private static final int ORE_CORRUPT_MIN = 20000;
    private static final int ORE_CORRUPT_MAX = 30000;
    private static final String ORE_KEY = "ore";
    private static final String ORE_ID_PREFIX = "ore -- ";

    public Vein(String id, Point position, int actionPeriod, List<PImage> images) {
        super( id, position, actionPeriod, images );
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        Optional<Point> openPt = getPosition().findOpenAround( world );

        if (openPt.isPresent()) {
            Ore ore = new Ore( ORE_ID_PREFIX + getId(), openPt.get(), ORE_CORRUPT_MIN + rand.nextInt( ORE_CORRUPT_MAX - ORE_CORRUPT_MIN ), imageStore.getImageList( ORE_KEY ) );
            world.addEntity( ore );
            ore.scheduleActions( scheduler, world, imageStore );
        }
        scheduler.scheduleEvent( this, new Activity( this, world, imageStore ), getActionPeriod() );
    }
}