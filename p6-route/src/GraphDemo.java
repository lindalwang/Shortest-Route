import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * Demonstrates the calculation of shortest paths in the US Highway
 * network, showing the functionality of GraphProcessor and using
 * Visualize
 * To do: Add your name(s) as authors
 */
public class GraphDemo {
    public static void main(String[] args) throws Exception{
        Scanner s = new Scanner(System.in);
        GraphProcessor g = new GraphProcessor();
        g.initialize(new FileInputStream("data/usa.graph"));
        System.out.println("Where are you coming from?");
        System.out.println("Enter a city and state abbreviation (e.g., Durham NC)");
        String start = s.nextLine();
        String[] ar = start.split(" ");
        Point origin = new Point(0, 0);
        Point p1 = new Point(0, 0);
        Scanner sc = new Scanner(new FileInputStream("data/uscities.csv"));  
        while(sc.hasNextLine()){
            String[] array = sc.nextLine().split(",");
            if(ar[0].equals(array[0]) && ar[1].equals(array[1])){
                p1 = new Point(Double.parseDouble(array[2]), Double.parseDouble(array[3]));
                break;
            }
        }
        System.out.println("Where are you going to?");
        System.out.println("Enter a city and state abbreviation (e.g., Durham NC)");
        String end = s.nextLine();
        String[] ara = end.split(" ");
        Point p2 = new Point(0, 0);
        Scanner sc2 = new Scanner(new FileInputStream("data/uscities.csv"));  
        while(sc2.hasNextLine()){
            String[] array = sc2.nextLine().split(",");
            //System.out.println(array[0]);
            if(ara[0].equals(array[0]) && ara[1].equals(array[1])){
                p2 = new Point(Double.parseDouble(array[2]), Double.parseDouble(array[3]));
                break;
            }
        }
        if(p1.equals(origin) || p2.equals(origin)){
            throw new Exception("Invalid city.");
        }
        double startTime = System.nanoTime();
        Point nearestStart = g.nearestPoint(p1);
        Point nearestEnd = g.nearestPoint(p2);
        List<Point> route = g.route(nearestStart, nearestEnd);
        double dist = g.routeDistance(route);
        double endTime = System.nanoTime();
        double elapsedNanos = (endTime - startTime)/1e6;
        System.out.printf("Nearest point to %s is (%.6f,%.6f)%n is", start, g.nearestPoint(p1).getLat(),g.nearestPoint(p1).getLon());
        System.out.printf("Nearest point to %s is (%.6f,%.6f)%n is" , end, g.nearestPoint(p2).getLat(),g.nearestPoint(p2).getLon());
        System.out.println("Route beDtween " + start + " and " + end + " is " + dist + "miles.");
        System.out.printf("Total time to get nearest points, route, and distance: %2.3f s\n", elapsedNanos);
        Visualize visualize = new Visualize("data/usa.vis","images/usa.png");
        visualize.drawPoint(p1);
        visualize.drawPoint(p2);
        visualize.drawRoute(route);
    }
}