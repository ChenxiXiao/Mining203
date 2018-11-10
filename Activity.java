public class Activity extends Action {
    private WorldModel world;
    private ImageStore imageStore;
 //   private ActiveEntity entity;

    public Activity(ActiveEntity entity, WorldModel world, ImageStore imageStore) {
        //this.kind = kind;
        super(entity);
     //   this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }


    //execute actions??

        public void executeAction(EventScheduler scheduler) {
            //     switch (entity.getKind()) {
            //        case MINER_FULL:
            ActiveEntity activeEntity = (ActiveEntity)entity;
            activeEntity.executeActivity( world, imageStore, scheduler );
        }
}