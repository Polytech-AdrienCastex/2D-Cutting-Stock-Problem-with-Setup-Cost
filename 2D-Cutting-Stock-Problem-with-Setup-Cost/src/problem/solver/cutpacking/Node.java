package problem.solver.cutpacking;

import java.util.ArrayList;
import java.util.Collections;

public class Node
{
    private int x;
    private int y;
    private int w;
    private int h;
    
    private boolean used = false;
    /* keep in mind wether this node is set to the right or bottom of its parent*/
    private Localisation local;

    private Node right;
    private Node down;
    
    public Node(int _x, int _y, int _w, int _h, Localisation _loc)
    {
        x = _x;
        y = _y;
        w = _w;
        h = _h;
        local = _loc;
    }
    
    public Node(int _x, int _y, int _w, int _h)
    {
        this(_x, _y, _w, _h, Localisation.FULL);
    }
    
    @Override
    public String toString()
    {
        return "this: {x:" + this.x + " y:" + this.y + 
               "}, right: {x:" + this.right.x + " y:" + this.right.y + "; w:" + this.right.w + " h:" + this.right.h +
               "}, down: {x:" + this.down.x + " y:" + this.down.y + "; w:" + this.down.w + " h:" + this.down.h + "}";
    }
      
    public boolean isUsed()
    {
        return used;
    }

    public void setUsed(boolean used)
    {
        this.used = used;
    }

    public Node getDown()
    {
        return down;
    }

    public void setDown(Node down)
    {
        this.down = down;
    }


    public Node getRight()
    {
        return right;
    }

    public void setRight(Node right)
    {
        this.right = right;
    }
    
    

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getW()
    {
        return w;
    }

    public void setW(int w)
    {
        this.w = w;
    }

    public int getH()
    {
        return h;
    }

    public void setH(int h)
    {
        this.h = h;
    }
  
    public int getArea()
    {
        return w * h;
    }
    
    public ArrayList<Node> sortNodeByArea()
    {
        ArrayList<Node> nds = new ArrayList<>();
        nds.add(right);
        nds.add(down);
        
        Collections.sort(nds, (n1, n2) -> (n1.getArea() < n2.getArea() ? 1 : (n1.getArea() > n2.getArea() ? -1 : 0)));
        return nds;
    }
    
    public Localisation getLocal()
    {
        return local;
    }
}
