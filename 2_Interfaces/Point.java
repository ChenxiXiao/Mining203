import processing.core.PImage;
import java.util.List;
import java.util.Optional;


final class Point {
   public final int x;
   public final int y;

   public Point(int x, int y) {
      this.x = x;
      this.y = y;
   }

   public String toString() {
      return "(" + x + "," + y + ")";
   }

   public boolean equals(Object other) {
      return other instanceof Point &&
              ((Point) other).x == this.x &&
              ((Point) other).y == this.y;
   }

   public int hashCode() {
      int result = 17;
      result = result * 31 + x;
      result = result * 31 + y;
      return result;
   }

   public Optional<Entity> nearestEntity(List<Entity> entities) {
      if (entities.isEmpty()) {
         return Optional.empty();
      } else {
         Entity nearest = entities.get( 0 );
         int nearestDistance = this.distanceSquared( nearest.getPosition() );

         for (Entity other : entities) {
            int otherDistance = this.distanceSquared( other.getPosition() );

            if (otherDistance < nearestDistance) {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of( nearest );
      }
   }

   public Optional<Point> findOpenAround(WorldModel world) {
      int ORE_REACH = 1;
      for (int dy = -ORE_REACH; dy <= ORE_REACH; dy++) {
         for (int dx = -ORE_REACH; dx <= ORE_REACH; dx++) {
            Point newPt = new Point( x + dx, y + dy );
            if (world.withinBounds(newPt ) &&
                    !world.isOccupied(newPt )) {
               return Optional.of( newPt );
            }
         }
      }
      return Optional.empty();
   }

   public int distanceSquared(Point p1) {
      int deltaX = p1.x - this.x;
      int deltaY = p1.y - this.y;

      return deltaX * deltaX + deltaY * deltaY;
   }


   public boolean adjacent(Point p2) {
      return (this.x == p2.x && Math.abs( this.y - p2.y ) == 1) ||
              (this.y == p2.y && Math.abs( this.x - p2.x ) == 1);
   }

}