/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem.solver.cutpacking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Arnaud
 */
public class Node {
    
    private int x;
    private int y;
    private int w;
    private int h;
    
    private boolean used = false;
    /* keep in mind wether this node is set to the right or bottom of its parent*/
    private Localisation local;

    private Node right;
    private Node down;
    
    private Node side;
    private Node bellow;
    
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
    public String toString(){
        return "this: {x:" + this.x + " y:" + this.y + 
               "}, right: {x:" + this.right.x + " y:" + this.right.y + "; w:" + this.right.w + " h:" + this.right.h +
               "}, down: {x:" + this.down.x + " y:" + this.down.y + "; w:" + this.down.w + " h:" + this.down.h + "}";
    }
      
    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public Node getDown() {
        return down;
    }

    public void setDown(Node down) {
        this.down = down;
    }


    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }
    
    
    public Node getBellow() {
        return bellow;
    }

    public void setBellow(Node bellow) {
        this.bellow = bellow;
    }

    public Node getSide() {
        return side;
    }

    public void setSide(Node side) {
        this.side = side;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }
  
    public int getArea() {
        return w * h;
    }
    
    public ArrayList<Node> sortNodeByArea() {
        ArrayList<Node> nds = new ArrayList<Node>();
        nds.add(right);
        nds.add(down);
      //  nds.add(side);
      //  nds.add(bellow);
        Collections.sort(nds, new Comparator<Node>() {
            @Override
          public int compare(Node n1, Node n2) {
              if (n1.getArea() < n2.getArea()) // (e1.getW() < e2.getW())
                  return 1;
              else if (n1.getArea() > n2.getArea()) // (e1.getW() > e2.getW())
                  return -1;        	
              else
                  return 0;
          }
        });
        return nds;
    }
    
    public Localisation getLocal() {
        return local;
    }
}
