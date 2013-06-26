package rungekutta;

public class RungeKutta {
    private MathExpression f_expr;
    private double a, b;
    private double z, t, y;
    private double h;
    //private double z0, t0, y0;
    
    private double f(double t)
    {
        return f_expr.getValue(t);
    }
    
    private double phi(double z, double t, double y)
    {
        return z;
    }
    
    private double theta(double z, double t, double y)
    {
        return f(t) - a*z - b*y;
    }
    
    public RungeKutta(double a, double b, String ft, double t0, double y0, double z0, double h)
    {
        this.a = a;
        this.b = b;
        z = z0;
        y = y0;
        t = t0;
        this.h = h;
        ft = ft.replace(" ", "");
        f_expr = MathExpression.parseString(ft);
    }
	
    public void setPoint(double t, double y, double z)
    {
        this.z = z;
        this.y = y;
        this.t = t;
    }
    
    public Point getNextPoint()
    {
        double k1, k2, k3, k4;
        double l1, l2, l3, l4;
        
        k1 = phi(z, t, y);
        l1 = theta(z, t, y);
        
        k2 = phi(z + h*l1/2, t, y);
        l2 = theta(z + h*l1/2, t + h/2, y + h*k1/2);
        
        k3 = phi(z + h*l2/2, t, y);
        l3 = theta(z + h*l2/2, t + h/2, y + h*k2/2);
        
        k4 = phi(z + h*l3, t, y);
        l4 = theta(z + h*l3, t + h, y + h*k3);
        
        //System.out.println(Math.abs((k2-k3)/(k1-k2)));
        
        t = t + h;
        y = y + h*(k1 + 2*k2 + 2*k3 + k4)/6;
        z = z + h*(l1 + 2*l2 + 2*l3 + l4)/6;
        
        if(Double.isInfinite(y) || Double.isInfinite(z))
            throw new IllegalArgumentException("В точке " + (t - h) + 
                    " функция или производная принимает значение бесконечность.");
        if(Double.isNaN(y) || Double.isNaN(z))
            throw new IllegalArgumentException("В точке " + (t - h) + 
                    " значение функции или производной не определено.");
        
        return new Point(z, t, y);
    }
    
    public Point getPreviousPoint()
    {
        double k1, k2, k3, k4;
        double l1, l2, l3, l4;
        
        k1 = phi(z, t, y);
        l1 = theta(z, t, y);
        
        k2 = phi(z - h*l1/2, t, y);
        l2 = theta(z - h*l1/2, t - h/2, y - h*k1/2);
        
        k3 = phi(z - h*l2/2, t, y);
        l3 = theta(z - h*l2/2, t - h/2, y - h*k2/2);
        
        k4 = phi(z - h*l3, t, y);
        l4 = theta(z - h*l3, t - h, y - h*k3);
        
        //System.out.println(Math.abs((k2-k3)/(k1-k2)));
        
        t = t - h;
        y = y - h*(k1 + 2*k2 + 2*k3 + k4)/6;
        z = z - h*(l1 + 2*l2 + 2*l3 + l4)/6;
        
        if(Double.isInfinite(y) || Double.isInfinite(z))
            throw new IllegalArgumentException("В точке " + (t - h) + 
                    " функция или производная принимает значение бесконечность.");
        if(Double.isNaN(y) || Double.isNaN(z))
            throw new IllegalArgumentException("В точке " + (t - h) + 
                    " значение функции или производной не определено.");
		
        return new Point(z, t, y);
    }
}
