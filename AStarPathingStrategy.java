import java.util.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.HashMap;
import java.lang.Math;

class AStarPathingStrategy
        implements PathingStrategy {
    public class Node {
        private Point p;
        public int g;
        public int h;
        public Node prev;

        public Node(Point p, Point end) {
            this.p = p;
            this.g = 0; //steps taken
            this.h = this.get_distance( end ); //from the end
            this.prev = null;
        }


        public int get_distance(Point y) {
            //int distance = Math.abs(x.getX() - y.getX()) + Math.abs(x.getY() - y.getY());
            return Math.abs( p.getX() - y.getX() ) + Math.abs( p.getY() - y.getY() );
        }

        @Override
        public boolean equals(Object other) {
            //null checker and class checker
            if (other == null || getClass() != other.getClass()) {
                return false;
            }
            // casting, otherwise it might not have pgh
            Node otherNode = (Node) other;
            if (p.getX() == otherNode.p.getX() && p.getY() == otherNode.p.getY()) {
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return "Node: " + "p= " + p + ", g= " + g + ", h= " + h;//+ ", prev Node= " + prev;
        }

        public int cost() {
            return h + g;
        }

        public int h() {
            return h;
        }

        public int g() {
            return g;
        }

        
/*
        public Node getPrev(){
            if (this.prev == null){
                return this;
            }
            return prev;
        }
        */
    }

    public boolean notIn(List<Node> explored, Point p) {
        if (explored == null) {
            return true;
        }
        for (Node n : explored) {
            if (n.p.equals( p )) {
                return false;
            }
        }
        return true;
    }

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {
        Comparator<Node> compl = Comparator.comparing( Node::cost ).thenComparing( Node::h );//.thenComparing(Node::cost);
        PriorityQueue<Node> openlist = new PriorityQueue<Node>( 5, compl ); //frontier or valid move
        List<Point> path = new LinkedList<Point>();
        List<Node> closelist = new LinkedList<Node>();
        List<Point> validPoints;
        Node startNode = new Node( start, end );
        // public PriorityQueue(int initialCapacity,Comparator<? super E> comparator)
        openlist.add( startNode );
        //while frontier is not Empty -- solution exists
        Map<Point, Integer> gMap = new HashMap<>();
        while (!openlist.isEmpty()) {
            Node current = openlist.poll(); // pop up the node with minimum cost, O (log(n))
            if (withinReach.test( current.p, end )) {
                //System.out.println( "path found" );
                while (true) {
                    path.add( 0, current.p );
                    current = current.prev;
                    if (current.equals( startNode )) {
                        return path;
                        //reconstruct path
                    }
                }
            }

            openlist.remove( current );
            closelist.add( current );

            validPoints = potentialNeighbors.apply( current.p ).filter( canPassThrough ).filter( pt -> notIn( closelist, pt ) && !pt.equals( start ) ).collect( Collectors.toList() );
            for (Point point : validPoints) {
                Node node = new Node( point, end );
                int temp_f = current.g + current.get_distance( point );
                if (!openlist.contains( node )) {
                    node.prev = current;
                    node.g = temp_f;
                    gMap.put( node.p, new Integer( temp_f ) );
                    openlist.add( node );
                } else if (temp_f > gMap.get( node.p )) {
                    continue;
                }
            }
        }
        return path;
    }
}
