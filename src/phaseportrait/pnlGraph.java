package phaseportrait;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import rungekutta.Point;

public class pnlGraph extends javax.swing.JPanel {

    private ArrayList<double[]> xs;
    private ArrayList<double[]> ys;
    private double k;
    private double xmax, ymax;
    private AffineTransform tr;
    private int x0, y0;
    private float curX, curY;
    private double oldX, oldY;
    private float th;
    
    public enum GraphType{
        YT, ZY
    }
    
    public pnlGraph() {
        initComponents();
        th = 0.2f;
        xs = new ArrayList<>();
        ys = new ArrayList<>();
        curX = curY = 0;
        setPlane(500, 500);
    }
    
    public void setThickness(float t)
    {
        th = t;
        this.repaint();
    }
    
    public final void setPlane(double xMax, double yMax)
    {
        k = 5;
        xmax = xMax;
        ymax = yMax;
        x0 = 170;
        y0 = 170;
        tr = new AffineTransform(k, 0, 0, -k, x0, y0);
    }
    
    public void clear()
    {
        xs = new ArrayList<>();
        ys = new ArrayList<>();
    }
    
    public void addGraph(Point[] pts, GraphType t)
    {
        double[] x, y;
        x = new double[pts.length];
        y = new double[pts.length];
        if(t == GraphType.YT)
        {
            for(int i = 0; i < pts.length; i++)
            {
                x[i] = pts[i].t;
                y[i] = pts[i].y;
                //System.out.printf("%f %f\n", x[i], y[i]);
            }
        }
        else
        {
            for(int i = 0; i < pts.length; i++)
            {
                x[i] = pts[i].y;
                y[i] = pts[i].z;
                //System.out.printf("%f %f\n", x[i], y[i]);
            }
        }
        xs.add(x);
        ys.add(y);
        //graphs.add(pts);
    }
    
    private void drawAxis(Graphics2D g)
    {
        Line2D xAxis = new Line2D.Double(-xmax, 0, xmax, 0);
        Line2D yAxis = new Line2D.Double(0, -ymax, 0, ymax);
        
        g.setStroke(new BasicStroke(0.4f));
        Color tmp = g.getColor();
        g.setColor(Color.BLACK);
        g.draw(xAxis);
        g.draw(yAxis);
        g.setStroke(new BasicStroke(0.1f));
        
        g.setColor(new Color(0.7f, 0.7f, 0.7f, 0.5f));
        for(double cx = 1; cx <= xmax; cx += 1)
        {
            yAxis = new Line2D.Double(cx, -ymax, cx, ymax);
            g.draw(yAxis);
            yAxis = new Line2D.Double(-cx, -ymax, -cx, ymax);
            g.draw(yAxis);
        }
        for(double cy = 1; cy <= ymax; cy += 1)
        {
            xAxis = new Line2D.Double(-xmax, cy, xmax, cy);
            g.draw(xAxis);
            xAxis = new Line2D.Double(-xmax, -cy, xmax, -cy);
            g.draw(xAxis);
        }
        g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.8f));
        for(double cx = 10; cx <= xmax; cx += 10)
        {
            yAxis = new Line2D.Double(cx, -ymax, cx, ymax);
            g.draw(yAxis);
            yAxis = new Line2D.Double(-cx, -ymax, -cx, ymax);
            g.draw(yAxis);
        }
        for(double cy = 10; cy <= ymax; cy += 10)
        {
            xAxis = new Line2D.Double(-xmax, cy, xmax, cy);
            g.draw(xAxis);
            xAxis = new Line2D.Double(-xmax, -cy, xmax, -cy);
            g.draw(xAxis);
        }
        g.setColor(tmp);
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;
        Stroke stTmp = g2.getStroke();
        
        Dimension d = this.getSize();
        Color tmpC = g.getColor();
        g.setColor(Color.WHITE);
        g2.fillRect(0, 0, d.width, d.height);
        g.setColor(tmpC);
        
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        
        AffineTransform tmp = g2.getTransform();
        g2.transform(tr);
        drawAxis(g2);
        
        g2.setStroke(new BasicStroke(th));
        double[] x, y;
        for(int i = 0; i < xs.size(); i++)
        {
            x = xs.get(i);
            y = ys.get(i);
            GeneralPath gp = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xs.get(i).length);
            if(x[0] > 1e+6 || y[0] > 1e+6 || x[0] < -1e+6 || y[0] < -1e+6)
                x[0] = y[0] = 1e+6;
            gp.moveTo(x[0], y[0]);
            double lastX = x[0], lastY = y[0];
            for(int j = 1; j < x.length; j++)
            {
                if(x[j] > 1e+6 || y[j] > 1e+6 || x[j] < -1e+6 || y[j] < -1e+6)
                    gp.lineTo(lastX, lastY);
                else
                {
                    gp.lineTo(x[j], y[j]);
                    lastX = x[j];
                    lastY = y[j];
                }
            }
            g2.draw(gp);
        }
        g2.setTransform(tmp);
        g2.setStroke(stTmp);
    }
    
    public float getXCoor()
    {
        return curX;
    }
    
    public float getYCoor()
    {
        return curY;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                formMouseWheelMoved(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 340, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 340, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_formMouseWheelMoved
        k += 0.1*evt.getPreciseWheelRotation();
        if(k < 0.1)
        {
            k = 0.1;
            return;
        }
        tr = new AffineTransform(k, 0, 0, -k, x0, y0);
        this.repaint();
    }//GEN-LAST:event_formMouseWheelMoved

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        curX = (float)((evt.getX() - x0)/k);
        curY = (float)((y0 - evt.getY())/k);
    }//GEN-LAST:event_formMouseMoved

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        x0 += evt.getX() - oldX;
        oldX = evt.getX();
        
        y0 += evt.getY() - oldY;
        oldY = evt.getY();
        
        tr = new AffineTransform(k, 0, 0, -k, x0, y0);
        this.repaint();
    }//GEN-LAST:event_formMouseDragged

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        oldX = evt.getX();
        oldY = evt.getY();
    }//GEN-LAST:event_formMousePressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
