import java.util.*;

import processing.core.PImage;

final class ImageStore {
    private Map<String, List<PImage>> images;
    private List<PImage> defaultImages;

    public ImageStore(PImage defaultImage) {
        images = new HashMap<>();
        defaultImages = new LinkedList<>();
        defaultImages.add( defaultImage );
    }

    public List<PImage> getImageList(String key) {
        return images.getOrDefault( key, defaultImages );
    }

    public Map<String, List<PImage>> images() {
        return images;
    }

}
