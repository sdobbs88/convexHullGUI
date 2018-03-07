
/**
 * Class: CSCI2410 Data Structures and Algorithms
 * Instructor: Y. Daniel Liang
 * Description: Creates a convex hull using a GUI and mouse clicks within the GUI. Right clicks removes 
 * a point and left clicks adds a point. Each time a point is added or removed, a new convex 
 * hull is created when appropriate. 
 * Due: 09/28/2016
 *
 * @author Shaun C. Dobbs
 * @version 1.0
 *
 * I pledge by honor that I have completed the programming assignment
 * independently. I have not copied the code from a student or any source. I
 * have not given my code to any student. * Sign here: Shaun C. Dobbs
 */
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Exercise22_13 extends Application {

    public Pane pane = new Pane();
    int numberOfPoints = 0;

    ArrayList<Double> list = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(pane, 350, 200);
        
        Label instructions = new Label("Left click to add and right click to remove");
        pane.getChildren().add(instructions);

        primaryStage.setTitle("Exercise22_13");
        primaryStage.setScene(scene);
        primaryStage.show();

        pane.setOnMouseClicked(e -> {
            EventTarget delCircEvent;
            if (e.getButton() == MouseButton.PRIMARY) {
                javafx.scene.shape.Circle circle = addCircle(e.getX(), e.getY());

                list.add(e.getX());
                list.add(e.getY());

                pane.getChildren().removeIf(node -> node instanceof Polygon);

                Polygon polygon = new Polygon();

                numberOfPoints++;
                pane.getChildren().add(circle);

                double[][] p = new double[numberOfPoints][2];

                //Remove rounding stuff before submitting
                for (int i = 0; i < p.length; i++) {
                    for (int j = 0; j < p[i].length; j++) {
                        if (j == 0) {
                            p[i][j] = list.get(i * 2);
                        } else {
                            p[i][j] = list.get((i * 2) + 1);
                        }
                    }
                }
                polygon.setStroke(Color.ORANGE);
                polygon.setStrokeWidth(2);
                polygon.setFill(Color.TRANSPARENT);

                ArrayList<MyPoint> hullList = getConvexHull(p);

                double[] xPoints = new double[hullList.size()];
                double[] yPoints = new double[hullList.size()];

                if (hullList.size() > 2) {
                    for (int i = 0; i < hullList.size(); i++) {
                        xPoints[i] = hullList.get(i).x;
                        yPoints[i] = hullList.get(i).y;
                    }
                    for (int i = 0; i < hullList.size(); i++) {
                        polygon.getPoints().addAll(xPoints[i]);
                        polygon.getPoints().addAll(yPoints[i]);
                    }
                }

                pane.getChildren().addAll(polygon);
                polygon.toBack();

            } else if (e.getButton() == MouseButton.SECONDARY) {

                double removeX = 0.0;
                double removeY = 0.0;
                
                if (e.getTarget() instanceof javafx.scene.shape.Circle) {

                    delCircEvent = e.getTarget();
                    String circleString = delCircEvent.toString();

                    
                    String removeXString = circleString.substring(14, 21);
                    removeXString = removeXString.replaceAll("[^\\d.]", "");
                    String removeYString = circleString.substring(28, 35);
                    removeYString = removeYString.replaceAll("[^\\d.]", "");
                    removeX = Double.parseDouble(removeXString);
                    removeY = Double.parseDouble(removeYString);

                    
                    deleteCircle(removeX, removeY);
                    list.remove(removeX);
                    list.remove(removeY);
                    numberOfPoints--;
                    
                    Polygon polygon = new Polygon();

                    double[][] p = new double[numberOfPoints][2];

                    for (int i = 0; i < p.length; i++) {
                        for (int j = 0; j < p[i].length; j++) {
                            if (j == 0) {
                                p[i][j] = list.get(i * 2);
                            } else {
                                p[i][j] = list.get((i * 2) + 1);
                            }
                        }
                    }
                    polygon.setStroke(Color.ORANGE);
                    polygon.setStrokeWidth(2);
                    polygon.setFill(Color.TRANSPARENT);

                    ArrayList<MyPoint> hullList = getConvexHull(p);

                    double[] xPoints = new double[hullList.size()];
                    double[] yPoints = new double[hullList.size()];

                    if (hullList.size() > 2) {
                        for (int i = 0; i < hullList.size(); i++) {
                            xPoints[i] = hullList.get(i).x;
                            yPoints[i] = hullList.get(i).y;
                        }
                        for (int i = 0; i < hullList.size(); i++) {
                            polygon.getPoints().addAll(xPoints[i]);
                            polygon.getPoints().addAll(yPoints[i]);
                        }
                    }

                    pane.getChildren().removeIf(node -> node instanceof Polygon);
                    pane.getChildren().addAll(polygon);
                    polygon.toBack();
                }
            }
        });
    }

    private javafx.scene.shape.Circle addCircle(double xLocation, double yLocation) {
        javafx.scene.shape.Circle circle = new javafx.scene.shape.Circle(xLocation, yLocation, 5, Color.BLUE);// create the circle and its properties(color: coral to see it better)
        circle.setStroke(Color.BLUE);
        return circle;
    }
    
    private void deleteCircle(double deleteX, double deleteY) {
        //Circles to be added to the pane using a list
        ObservableList<Node> list = pane.getChildren();
        for (int i = list.size() - 1; i >= 0; i--) {
            Node circle = list.get(i);
            // which circle to delete
            if (circle instanceof javafx.scene.shape.Circle && circle.contains(deleteX, deleteY)) {                
                pane.getChildren().remove(circle); 
                break;
            }
        }
    }
    static class MyPoint {

        double x, y;

        MyPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
    /**
     * Return the points that form a convex hull
     */
    public static ArrayList<MyPoint> getConvexHull(double[][] s) {
        // For efficiency, create an array of objects 
        MyPoint[] myPoints = new MyPoint[s.length];
        for (int i = 0; i < myPoints.length; i++) {
            myPoints[i] = new MyPoint(s[i][0], s[i][1]);
        }
        // Step 1
        MyPoint h0 = getRightmostLowestPoint(myPoints);
        ArrayList<MyPoint> H = new ArrayList<MyPoint>();
        H.add(h0);

        MyPoint t0 = h0;

        // Step 2 and Step 3
        while (true) {
            MyPoint t1 = myPoints[0];
            for (int i = 1; i < myPoints.length; i++) {
                double status = whichSide(t0.x, t0.y, t1.x, t1.y, myPoints[i].x, myPoints[i].y);

                if (status > 0) // Right side of the line. Please note we are using the Java coordinate system. y increases downward
                {
                    t1 = myPoints[i];
                } else if (status == 0) {
                    if (distance(s[i][0], s[i][1], t0.x, t0.y) > distance(t1.x, t1.y, t0.x, t0.y)) {
                        t1 = myPoints[i];
                    }
                }
            }
            if (t1.x == h0.x && t1.y == h0.y) {
                break; // A convex hull is found
            } else {
                H.add(t1);
                t0 = t1;
            }
        }
        return H;
    }
    /**
     * Return the rightmost lowest point in S
     */
    private static MyPoint getRightmostLowestPoint(MyPoint[] p) {
        int rightMostIndex = 0;
        double rightMostX = p[0].x;
        double rightMostY = p[0].y;

        for (int i = 1; i < p.length; i++) {
            if (rightMostY < p[i].y) {
                rightMostY = p[i].y;
                rightMostX = p[i].x;
                rightMostIndex = i;
            } else if (rightMostY == p[i].y && rightMostX < p[i].x) {
                rightMostX = p[i].x;
                rightMostIndex = i;
            }
        }

        return p[rightMostIndex];
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    static double whichSide(double x0, double y0, double x1, double y1, double x2, double y2) {
        return (x1 - x0) * (y2 - y0) - (x2 - x0) * (y1 - y0);
    }

    public static void main(String[] args) {
        launch(args);

    }
}
