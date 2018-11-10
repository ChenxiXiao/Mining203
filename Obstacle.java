import processing.core.PImage;

import java.util.List;

class Obstacle extends Entity {
    public Obstacle(String id, Point position, List<PImage> images) {
        super(id, position, images, 0, 0);
    }
}