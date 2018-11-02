import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Vein implements Movable {
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;

    private static final Random rand = new Random();


    public Vein(String id, Point position, int actionPeriod, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.actionPeriod = actionPeriod;
        this.imageIndex = 0;
        this.resourceLimit = 0;
        this.resourceCount = 0;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public List<PImage> getImages() {
        return images;
    }

    public PImage getCurrentImage() {
        return images.get( imageIndex );
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
        int ORE_CORRUPT_MIN = 20000;
        int ORE_CORRUPT_MAX = 30000;
        Optional<Point> openPt = position.findOpenAround( world );
        String ORE_KEY = "ore";
        String ORE_ID_PREFIX = "ore -- ";
        if (openPt.isPresent()) {
            Ore ore = new Ore( ORE_ID_PREFIX + id, openPt.get(), ORE_CORRUPT_MIN + rand.nextInt( ORE_CORRUPT_MAX - ORE_CORRUPT_MIN ), imageStore.getImageList( ORE_KEY ) );
            world.addEntity( ore );
            ore.scheduleActions( scheduler, world, imageStore );
        }
        scheduler.scheduleEvent( this, new Activity( this, world, imageStore ), actionPeriod );
    }
}