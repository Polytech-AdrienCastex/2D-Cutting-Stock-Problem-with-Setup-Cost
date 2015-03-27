/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem.solver.cutpacking;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import problem.solver.patternplacement.ImageLocation;

/**
 *
 * @author Arnaud
 */
public class Ptrn {
    
private static final int HEIGHT = 60;
private static final int WIDTH = 40;
private static Map<Integer, Elmt> dico;

private byte[][] template;


//private int[] items;

private ArrayList<Elmt> blocks;
private Node root;
private int[] nbElmts = new int[4];  


public Ptrn(int[] wanted){
    
    /* initialize the global pattern model */
    root = new Node( 0, 0, WIDTH, HEIGHT);
    nbElmts = wanted;
    initPattern();
    initElmts();
    
}
    
private void initElmts(){
    blocks = new ArrayList<>();
    for(int i = 0; i < nbElmts.length; i++){
        //System.out.println(Integer.toString(nbElmts[i]));
        if(nbElmts[i] > 0)
        {
            blocks.add(new Elmt(i, Ptrn.getDico().get(i).getW(), Ptrn.getDico().get(i).getH(), nbElmts[i]));
        }
    }
    
    /* we insert blocks according to their height (by grouping each Elmts with same Id into the same block) */
    Collections.sort(blocks, new Comparator<Elmt>() {
        @Override
      public int compare(Elmt e1, Elmt e2) {
          if (e1.getH() < e2.getH()) // (e1.getW() < e2.getW())
              return 1;
          else if (e1.getH() > e2.getH()) // (e1.getW() > e2.getW())
              return -1;        	
          else
              return 0;
      }
        /*
      @Override
      public int compare(Elmt e1, Elmt e2) {
          if (e1.getH() < e2.getH()) // (e1.getW() < e2.getW())
              return 1;
          else if (e1.getH() > e2.getH()) // (e1.getW() > e2.getW())
              return -1;        	
          else
              return 0;
      }
        */
    });
}

private void initPattern(){
    /*
    template = new byte[HEIGHT][WIDTH];
    for(int i = 0; i < HEIGHT; i++)
        for(int j = 0; j < WIDTH; j++)
            template[i][j] = -1;*/
    template = new byte[WIDTH][HEIGHT];
    for(int i = 0; i < WIDTH; i++)
        for(int j = 0; j < HEIGHT; j++)
            template[i][j] = -1;
}

public void fillPattern() throws Exception{
    for(Elmt e : blocks)
    {
        //System.out.println("elmt : h = " + ( e.getFit().getY() + e.getH()) + " w = "  + (e.getFit().getX() + e.getW()));
        for(int i = 0; i <e.getW() ; i++)
        {
            for(int j = 0; j <e.getH() ; j++) 
                if(e.getFit() != null &&
                //( (e.getFit().getX() + i) < WIDTH && (e.getFit().getY() + j) < HEIGHT ) &&
                template[e.getFit().getX() + i][e.getFit().getY() + j] == -1)
                {/*
                    if(i == 0 && j == 0)
                        System.out.println(" id = " + (e.getId()) + " x = " + (e.getFit().getX() + i) + " y = " + (e.getFit().getY() + j));
                */
                    template[e.getFit().getX() + i][e.getFit().getY() + j] = (byte)e.getId();
                }
                //else
                    //throw new Exception("an item has already been set to this place");
        }
    }
}

  
  public void fit2() { //ArrayList<Elmt> blocks
    ArrayList<Elmt> packed = new ArrayList<Elmt>();
    Node node;
    Elmt block;
    for (int n = 0; n < blocks.size(); n++) {
        block = blocks.get(n);
        if ((node = this.findNode(this.root, null, block)) != null)
            block.setFit(this.splitNode(node, block));
        //System.out.println(block.getFit());
        packed.add(block);
    }
    blocks = packed;
  }
  
  
  /* look for the first (from top-left to bottow-right) suitable empty area in 
    which the current block fits */
  
  public boolean fit(){ //ArrayList<Elmt> blocks
    ArrayList<Elmt> packed = new ArrayList<Elmt>();
    Node node;
    Elmt block;
    int nbpcs;
    boolean ok = false;
    for (int n = 0; n < blocks.size(); n++) {
        block = blocks.get(n);
        /* we ensure that every pieces of a divided block has been set*/
        do{
            nbpcs = 0;
            ok = false;
            /* we try to set the Id-aggregated(nbpcs) block into an empty area */
            do{ /* A big enough area has actually been found for the Id-aggregated(pieces) block constisting in */
                if ((node = findNode(this.root, null, block)) != null)
                {
                    block.setFit(this.splitNode(node, block));
                   // System.out.println("break");
                    //System.out.println(block.getFit());
                    packed.add(block);
                    ok = true;
                    //System.out.println("added pcs: " + block.getNbPieces() + " w: " + block.getW() + " h: " + block.getH());
                    break;
                }
                /* No suitable area Id-aggregated(pieces) block, we have to divide 
                    it into 2 smaller blocks (sizes of (pieces - nbpcs) & nbpcs) */
                else
                {
                    block.reduce();
                    nbpcs++;
                    //System.out.println("test " + nbpcs + "  " + block.getNbPieces());
                }
                if(nbpcs > 25) // ToDo
                    return false;
            }
            while(block.getNbPieces() > 0);
            
            block = new Elmt(block.getId(), block.getWidth(), block.getHeight(), nbpcs);
        }
        while(nbpcs > 0 ); //&& !ok
    }
    blocks = packed;
    return true;
  }
  
  /*
    public Node findNode(Node root, Elmt block) {
    int w;
    int h;
    int nbpcs = 0;//block.getNbPieces();
    Node nd;
    if (root.isUsed())
        if((nd = findNode(root.getRight(), block)) != null)
            return nd;
        else
        {
            nd = findNode(root.getDown(), block);
            return nd;
        }
    else{
        do{
            w = block.getW();
            h = block.getH();
            if ((h <= root.getW()) && (w <= root.getH()))
            {
                block.reverse();
                return root;
            }
            else if ((w <= root.getW()) && (h <= root.getH()))
                return root;
            block.reduce();
            nbpcs++;
        }
    while(block.getNbPieces() > 1);
    }
    return null;
  }
  */
  
  public Node checkNode(Node root, Elmt block) {
      return findNode(root, null, block);
  }
  
  /*
  public void minimize2(ArrayList<Node> nds, Elmt block){
    int nbpcs;
    boolean ok;
    int minGap = Pattern.HEIGHT + Pattern.WIDTH;
    HashMap bst = new Hashmap
    do{
        nbpcs = 0;
        ok = false;
        do{ 
            if ((node = findNode(this.root, null, block)) != null)
            {
                block.setFit(this.splitNode(node, block));
               // System.out.println("break");
                System.out.println(block.getFit());
                packed.add(block);
                ok = true;
                System.out.println("added pcs: " + block.getNbPieces() + " w: " + block.getW() + " h: " + block.getH());
                break;
            }
            else
            {
                block.reduce();
                nbpcs++;
                //System.out.println("test " + nbpcs + "  " + block.getNbPieces());
            }
            if(nbpcs > 25) // ToDo
                return false;
        }
        while(block.getNbPieces() > 0);
        //System.out.println("Unity " + block.getUnity()[0] + "  " + block.getUnity()[1]);
        block = new Elmt(block.getId(), block.getUnity()[0], block.getUnity()[1], nbpcs);
    }
    while(nbpcs > 0 ); //&& !ok
  }
  */
  
  public Node findNode(Node root, Node prev, Elmt block) {
    int w = block.getW();
    int h = block.getH();
    Node nd;
    /* we go through the tree till we find a welcoming leaf */
    /* the current node has already been filled, we go deeper inside (to right first, down then) */
    if (root.isUsed())
    {
        for(Node n : root.sortNodeByArea())
            if((nd = findNode(n, root, block)) != null)
                return nd;
    }/* we check whether we could fill the template in a more optimal way (saving 
        more space on pattern borders) by splitting Id-aggregated block */
    else if(block.isBetter(root))
    {
        //System.out.println("h node = "+ root.getH() +" w node = " + root.getW() + " h block = "+ h+" w block = " + w);
        if ((h <= root.getW()) && (w <= root.getH()))
        {
            /* correct the empty area size bellow or next to the previous set elmt, 
            by making it smaller at case the on-going added element uses a part of it*/
            if(Localisation.HORIZONTAL.equals(root.getLocal()) && h > HEIGHT - (prev.getDown().getY() + prev.getDown().getH()))
                prev.getDown().setW(prev.getDown().getW() - w);
            if(Localisation.VERTICAL.equals(root.getLocal()) && w > WIDTH - (prev.getRight().getX() + prev.getRight().getW()))
                prev.getRight().setH(prev.getRight().getH() - h);
            block.rotate();
            //System.out.println("widroot = "+ root.getW() + " heiroot = " + root.getH() + "wid = "+ block.getW() + " hei= " + block.getH());
            return root;
        }
        if ((w <= root.getW()) && (h <= root.getH()))
            return root;
        return null;
    }
    return null;
  }

    
  public Node splitNode(Node node, Elmt block) {
    int w = block.getW();
    int h = block.getH();
    node.setUsed(true);
    /* we split the current space into 2 (the one on the left, the other on bottom),
        before finally stocking the current node into the block (fit)*/
    node.setDown(new Node(node.getX(), node.getY() + h, w, node.getH() - h, Localisation.VERTICAL));
    node.setRight(new Node(node.getX() + w, node.getY(), node.getW() - w, h, Localisation.HORIZONTAL));
    
    node.setBellow(new Node(node.getX(), node.getY() + h, w, node.getH() - h, Localisation.VERTICAL));
    node.setSide(new Node(node.getX() + w, node.getY(), node.getW() - w, h, Localisation.HORIZONTAL));
    
    /* we extand area's spaces when possible*/
    node.getDown().setW(node.getDown().getW() + node.getRight().getW());
    node.getRight().setH(node.getRight().getH() + node.getDown().getH());
    return node;
  }
  
  public List<ImageLocation> getPositions(){
      ArrayList<ImageLocation> posList = new ArrayList<ImageLocation>();
      
      for(Elmt bl : blocks)
            for(ImageLocation il : bl.getElmtPositions())//bl.getX(), bl.getY()
            {
                posList.add(il);
            }
      return posList;
  }
  
public void writer(String outFileName) throws UnsupportedEncodingException, FileNotFoundException, IOException{
    byte temp;
    /*
    for(Elmt b : getBlocks())
        System.out.println(b.getId());*/
    try {
        /*if(!this.fit())
            return;*/
        fillPattern();
    } catch (Exception ex) {
        Logger.getLogger(PackingTree.class.getName()).log(Level.SEVERE, null, ex);
    }
    try {
        FileOutputStream outputStream = new FileOutputStream(outFileName);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-16");
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

        bufferedWriter.write("    ");
        for(int i = 0; i < Ptrn.getWidth(); i++)
        {
            bufferedWriter.write(Integer.toString(i%10)); // 
            //if(i < 10)
                bufferedWriter.write(" ");
        }

        bufferedWriter.newLine();
        bufferedWriter.newLine();

        for(int j = 0; j < Ptrn.getHeight(); j++)
        {
            for(int i = 0; i < Ptrn.getWidth(); i++)
            {
                if(i == 0)
                {
                    bufferedWriter.write(Integer.toString(j)+ "  ");
                    if(j<10)
                        bufferedWriter.write(" ");                    
                }

                temp = getTemplate()[i][j];
                if(temp >= 0)
                    bufferedWriter.write(Byte.toString(temp) + " ");
                else
                    bufferedWriter.write("- ");
            }
            bufferedWriter.newLine();
        }
        
        bufferedWriter.newLine();
        for(Elmt bl : blocks)
            for(int i = 0; i < bl.getElmtPositions().length; i++)//bl.getX(), bl.getY()
            {
                //bufferedWriter.write(il.toString());
                if( bl.getElmtPositions()[i] != null)
                    bufferedWriter.write("X : " + bl.getElmtPositions()[i].getX() + ",Y : " + bl.getElmtPositions()[i].getY());
                    bufferedWriter.newLine();
            }
        bufferedWriter.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    
    public static Map<Integer, Elmt> getDico() {
        return dico;
    }

    public static void setDico(Map<Integer, Elmt> _dico) {
        //dico = new HashMap<Integer, Elmt>();
        dico = _dico;
    }

    public ArrayList<Elmt> getBlocks() {
        return blocks;
    }

    public void setBlocks(ArrayList<Elmt> _blocks) {
        this.blocks = _blocks;
    }
    
    public int[] getNbElmts() {
        return nbElmts;
    }

    public byte[][] getTemplate() {
        return template;
    }

    public static int getHeight() {
        return HEIGHT;
    }

    public static int getWidth() {
        return WIDTH;
    }


}
