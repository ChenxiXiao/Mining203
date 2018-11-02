public class Activity implements Action {
    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;

    public Activity(Entity entity, WorldModel world, ImageStore imageStore) {
        //this.kind = kind;
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    @Override
    public void executeAction(EventScheduler scheduler) {
        //     switch (entity.getKind()) {
        //        case MINER_FULL:
        if (entity instanceof Movable) {
            if (entity instanceof Movable) {
                Movable e = (Movable) entity;
                e.executeActivity( world, imageStore, scheduler );
            }
        }
    }
}