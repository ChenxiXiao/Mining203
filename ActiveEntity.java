import processing.core.PImage;

import java.util.List;
import java.util.Random;

public abstract class ActiveEntity extends Entity{
    private int actionPeriod;
    protected final Random rand = new Random();

    public ActiveEntity(String id, Point position, int actionPeriod, List<PImage> images) {
        super(id, position, images, 0,0);
        this.actionPeriod = actionPeriod;
    }
    public int getActionPeriod() {
        return actionPeriod;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent( this, new Activity( this, world, imageStore ), actionPeriod );
    }

    abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
}
