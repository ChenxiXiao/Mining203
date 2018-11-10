public abstract class Action {

    protected Entity entity;

    public Action (Entity entity){
        this.entity = entity;
    }

    abstract void executeAction(EventScheduler scheduler);
}
