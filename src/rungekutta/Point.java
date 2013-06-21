package rungekutta;

public class Point implements Comparable {
    
    public double z, t, y;
    
    public Point(double z, double t, double y)
    {
        this.z = z;
        this.y = y;
        this.t = t;
    }

    @Override
    public int compareTo(Object o) {
        Point p = (Point)o;
        if(t < p.t)
            return -1;
        else if(t == p.t)
            return 0;
        else
            return 1;
    }
    
}
