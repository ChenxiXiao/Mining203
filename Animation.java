public class Animation extends Action {
     private int repeatCount;
    // private AnimatedEntity entity;
     //takes action kind

   public Animation(AnimatedEntity entity, int repeatCount) {
       super(entity);
       this.repeatCount = repeatCount;
   }

   public void executeAction (EventScheduler scheduler){
       AnimatedEntity animatedEntity = (AnimatedEntity)entity;
       (animatedEntity).nextImage();
       if (repeatCount != 1) {
          scheduler.scheduleEvent(animatedEntity, new Animation(animatedEntity, Math.max( repeatCount - 1, 0 )), animatedEntity.animationPeriod);
           }
   }
}
