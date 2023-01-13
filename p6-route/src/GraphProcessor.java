import java.security.InvalidAlgorithmParameterException;
import java.util.List;
import java.io.FileInputStream;
import java.util.Scanner;
import java.util.*;

/**
 * Models a weighted graph of latitude-longitude points
 * and supports various distance and routing operations.
 * To do: Add your name(s) as additional authors
 * @author Brandon Fain
 *
 */

 

public class GraphProcessor {

    /**
     * Creates and initializes a graph from a source data
     * file in the .graph format. Should be called
     * before any other methods work.
     * @param file a FileInputStream of the .graph file
     * @throws Exception if file not found or error reading
     */

    private int num_vertices;
    private int num_edges;
    private HashMap<Point, HashSet<Point>> map;
    private ArrayList<Point> list = new ArrayList<>();


    public void initialize(FileInputStream file) throws Exception {
        // TODO: Implement initialize

        map = new HashMap<>();

        Scanner s = new Scanner(file);
        num_vertices = s.nextInt();
        num_edges = s.nextInt();

        s.nextLine();

        for (int i = 0; i< num_vertices;i++){
            String[] ar = s.nextLine().split(" ");
            double lat = Double.parseDouble(ar[1]);
            double lon = Double.parseDouble(ar[2]);
            
            map.put(new Point(lat, lon), new HashSet<>());
            list.add(new Point(lat, lon));
        }

        for(int i = 0; i < num_edges; i++){
            int v1 = s.nextInt();
            int v2 = s.nextInt();

            Point p1 = list.get(v1);
            Point p2 = list.get(v2);
            if(!s.hasNextInt()){
                s.nextLine();
            }
            map.get(p1).add(p2);
            map.get(p2).add(p1);
        }
        s.close();
    }

    /**
     * Searches for the point in the graph that is closest in
     * straight-line distance to the parameter point p
     * @param p A point, not necessarily in the graph
     * @return The closest point in the graph to p
     */
    public Point nearestPoint(Point p) {
        // TODO: Implement nearestPoint
        Point min_point = list.get(0);
    
        for (Point pt: list){
            if (pt != min_point && p.distance(pt) < p.distance(min_point)){
                min_point  = pt;
            }
        }
        return min_point;
    }


    /**
     * Calculates the total distance along the route, summing
     * the distance between the first and the second Points, 
     * the second and the third, ..., the second to last and
     * the last. Distance returned in miles.
     * @param start Beginning point. May or may not be in the graph.
     * @param end Destination point May or may not be in the graph.
     * @return The distance to get from start to end
     */
    public double routeDistance(List<Point> route) {
        // TODO Implement routeDistance
        double distance = 0.0;
        Point first = route.get(0);

        for (int i = 1; i< route.size(); i++){
            distance += first.distance(route.get(i));
            if (i < route.size()-1){
                first = route.get(i);
            }
        }
        return distance;
    }
    

    /**
     * Checks if input points are part of a connected component
     * in the graph, that is, can one get from one to the other
     * only traversing edges in the graph
     * @param p1 one point
     * @param p2 another point
     * @return true if p2 is reachable from p1 (and vice versa)
     */
    public boolean connected(Point p1, Point p2) {
        // TODO: Implement connected
        Stack<Point> toExplore = new Stack<>();
        Point current = p1;
        HashSet<Point> visited = new HashSet<>();
        visited.add(current);
        toExplore.push(current);

        while(!toExplore.isEmpty()){
            current = toExplore.pop();
            for (Point neighbor:map.get(current)){ 
           
                if (!visited.contains(neighbor)){
                    if(neighbor.equals(p2)){
                        return true;
                    }
                    visited.add(neighbor);
                    toExplore.push(neighbor);
                }
            }
        }
        /* 
        if(visited.contains(p2)){
            return true;
        }
        */
        return false;
        
    }


    /**
     * Returns the shortest path, traversing the graph, that begins at start
     * and terminates at end, including start and end as the first and last
     * points in the returned list. If there is no such route, either because
     * start is not connected to end or because start equals end, throws an
     * exception.
     * @param start Beginning point.
     * @param end Destination point.
     * @return The shortest path [start, ..., end].
     * @throws InvalidAlgorithmParameterException if there is no such route, 
     * either because start is not connected to end or because start equals end.
     */
    
    public List<Point> route(Point start, Point end) throws InvalidAlgorithmParameterException {
        // TODO: Implement route
        if(!connected(start,end)){
            throw new InvalidAlgorithmParameterException("No path between start and end"); 
        }
        if(start.equals(end)){
            throw new InvalidAlgorithmParameterException("Start and End are same point"); 
        }

        Map<Point, Double> distance = new HashMap<>();
        Comparator<Point> comp = (a,b) -> Double.compare(distance.get(a), distance.get(b));
        PriorityQueue<Point> toExplore = new PriorityQueue<>(comp);
        HashMap<Point,Point> previous = new HashMap<>();

        List<Point> ret = new ArrayList<>();

        Point current = start;
        distance.put(current, 0.0);
        toExplore.add(current);


        while (!toExplore.isEmpty()){
            current = toExplore.remove();
            for (Point neighbor: map.get(current)){
                double weight = current.distance(neighbor);
                if (!distance.containsKey(neighbor) 
                || distance.get(neighbor) > distance.get(current) + weight){
                    distance.put(neighbor, distance.get(current) + weight);
                    previous.put(neighbor, current);
                    toExplore.add(neighbor);

                }
            }
        }

        Point prev = end;
        while(prev.compareTo(start) != 0){
            
            ret.add(prev);
            prev = previous.get(prev);
        }
        Collections.reverse(ret);
        return ret;
    }  
}
