import java.util.List;

import processing.core.PImage;

interface Entity {

    void setPosition(Point position);

    Point getPosition();

    void nextImage();

    int getImageIndex();

    List<PImage> getImages();

    PImage getCurrentImage();

}
