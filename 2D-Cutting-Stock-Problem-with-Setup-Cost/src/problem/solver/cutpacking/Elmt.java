/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem.solver.cutpacking;

import java.util.logging.Level;
import java.util.logging.Logger;
import problem.solver.parameters.ImageKind;
import problem.solver.parameters.PatternKind;
import problem.solver.patternplacement.ImageLocation;


/**
 *
 * @author Arnaud
 */
public class Elmt implements Cloneable {
    private static int nbTypes;
    
    private int id;
    private int w;
    private int h;
    private int pieces = 1;
    private Node fit;
    
    private boolean isReversed = false;

    public Elmt(int _w, int _h){
        w = _w;
        h = _h;
    }
     
    public Elmt(int _id, int _w, int _h){
        this(_w, _h);
        id = _id;
    } 
    
    public Elmt(int _id, int _w, int _h, int _pcs){
        this(_id, _w, _h);
        if(_pcs > 0)
            pieces = _pcs;
    } 
    
    /* operates a 90° rotation of the element */
    public void reverse(){
        isReversed = !isReversed;
    }
    
    /* returns the size of a 1-piece block of this element
    public int[] getUnity() {
        int[] unitysize = new int[2];
        unitysize[0] = w;
        unitysize[1] = h;
        return unitysize;
    }
    */
    
    public int getSingleW() {
        return w;
    }
    
    public int getSingleH() {
        return h;
    }
    
    public boolean isReversed() {
        return isReversed;
    }
    
    @Override
    public Elmt clone(){
        Elmt elmt = null;
        try {
            elmt = (Elmt) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Elmt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return elmt;
    }
    
    /* makes the element 1 piece smaller*/
    public void reduce(){
        if(pieces > 1)
            pieces --;
    }
    
    /* check wether there's a possibility to reduce the gap between element extremity
    and template border (right & bottom) by splitting the block*/
    public boolean isBetter(Node node){
        
        int ndh = node.getH();
        int ndw = node.getW();
        int goal = -1;
        int limit = -1;
        int min = ndh + ndw;
        int nbp = this.getNbPieces();
        int temp;
        Elmt seeker = this.clone();
       
        if(Localisation.FULL.equals(node.getLocal()))
            return true;
        if(Localisation.VERTICAL.equals(node.getLocal()))
        {
            goal = ndh;
            limit = ndw;
            min = goal - this.getH();
        }
        if(Localisation.HORIZONTAL.equals(node.getLocal()))
        {
            goal = ndw;
            limit = ndh;
            min = goal - this.getW();
        }
            
        while(seeker.getNbPieces() > 1)// && min > 0)
        {
            seeker.reduce();
            if(seeker.getH() <= limit && ( (temp = goal - seeker.getW()) >= 0 && temp < min))
                return false;
            if(seeker.getW() <= limit && ( (temp = goal - seeker.getH()) >= 0 && temp < min))
                return false;
        }
        return true;        
    }
    
    public int maxByPattern(){
        int cmpt = 0;
        Ptrn pat;
        int nbElmts = Ptrn.getDico().size();
        int[] test = new int[nbElmts];
        for(int i = 0; i< nbElmts; i++)
            test[i] = 0;
        do{
            test[this.id]++;
            pat = new Ptrn(test);
            cmpt++;
        }
        while(pat.fit());
        
        return cmpt;
    }
    
    public int maxPerPattern(){
        int cmpt = 1;
        Ptrn pat;
        int nbElmts = Ptrn.getDico().size();
        int[] test = new int[nbElmts];
        for(int i = 0; i< nbElmts; i++)
            test[i] = 0;
        do{
            test[this.id]++;
            pat = new Ptrn(test);
            cmpt++;
        }
        while(pat.fit());
        
        return cmpt;
    }
    
    public int getId() {
        return id;
    }
    
    public int getW() {
        int aggwidth = w * pieces;
        if(!isReversed)
        {            
            if(aggwidth > h)
                return h;
            else
                return aggwidth;
        }
        else
        {
            if(aggwidth > h)
                return aggwidth;
            else
                return h; 
        }
    }
    /*
    public void setW(int w) {
        this.w = w;
    }
    */
    public int getH() {
        int aggwidth = w * pieces;
        if(!isReversed)
        {
            if(aggwidth > h)
                return aggwidth;
            else
                return h;            
        }
        else
        {
            if(aggwidth > h)
                return h;
            else
                return aggwidth;
        }
    }
    
    
    public ImageLocation[] getElmtPositions(){ //(int X, int Y)
        
        ImageKind ik = new ImageKind(this.getSingleW(), this.getSingleH(), -1, new PatternKind(Ptrn.getWidth(), Ptrn.getHeight()));
        ImageLocation.Direction dir;
        int nbPieces = this.getNbPieces();
        //Double[] pos = new Double[nbPieces];
        //int[][] pos = new int[nbPieces][2];
        ImageLocation[] items = new ImageLocation[nbPieces];
        if(fit == null)
            return new ImageLocation[0];
        int x = fit.getX(), y = fit.getY();
        int i = 0;
        int coord[] = new int[2];
        //for(int i = 0; i < nbPieces; i++)
        do{
            if(this.isReversed())
            {
                dir = ImageLocation.Direction.Vertical;
                if(i > 0)
                    x += this.getSingleW();
            }
            else
            {
                dir = ImageLocation.Direction.Horizontal;
                if(i > 0)
                    y += this.getSingleW();//H
            }
            /* coord[0] = x;
            coord[1] = x;
            pos[i] = coord; */
            items[i] = new ImageLocation(x, y, dir, ik);
            i++;
        }
        while(i < nbPieces);
        //System.out.println("coord:"+ x + " | "+ y);
        return items;
    }
    /*
    public void setH(int h) {
        this.h = h;
    }
    */
    public int getNbPieces() {
        return pieces;
    }
    
    public Node getFit() {
        return fit;
    }
    
    public int getX() {//throws UnplacedException
        if(fit == null)
            return -1;//throw new UnplacedException("Element " + this.id + "hasn't been set.");
        return fit.getX();        
    }
    
    public int getY() {//throws UnplacedException
        if(fit == null)
            return -1;//throw new UnplacedException("Element " + this.id + "hasn't been set.");
        return fit.getY();        
    }

    public void setFit(Node fit) {
        this.fit = fit;
    }
}
