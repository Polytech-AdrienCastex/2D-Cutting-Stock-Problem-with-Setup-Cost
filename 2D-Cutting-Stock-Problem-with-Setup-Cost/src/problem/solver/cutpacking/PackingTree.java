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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arnaud
 */
public class PackingTree {
      
private ArrayList<Elmt> blocks;
private Node root;
private int[] nbElmts = new int[4];  

final static String OUTPUT_FILE_NAME = "C:\\Temp\\output.txt";
final static Charset ENCODING = StandardCharsets.UTF_8;
  
public Ptrn pat;
public ArrayList<Ptrn> pats;
    
  public PackingTree(int w, int h){
      /* initialize the global pattern model */
      root = new Node( 0, 0, w, h );
      
      /* initialize the global pattern model */
      initDico();
      
      /* wished number of each block following its Id */
    //  new int[]{0, 1, 2, 4};
     // new int[]{0, 1, 2, 4}
      pat = new Ptrn(new int[]{1, 0, 2, 3});
      pats = new ArrayList<Ptrn>();
      checkAllCombi();
  }
  
  public void checkAllCombi(){
      Ptrn[][][][] checking = new Ptrn
                [Ptrn.getDico().get(0).maxPerPattern()]
                [Ptrn.getDico().get(1).maxPerPattern()]
                [Ptrn.getDico().get(2).maxPerPattern()]
                [Ptrn.getDico().get(3).maxPerPattern()];
        for(int i = 0; i< checking.length - 1; i++)
            for(int j =0; j < checking[0].length - 1 ; j++)
                for(int k = 0; k < checking[0][0].length - 1; k++)
                    for(int l = 0; l < checking[0][0][0].length - 1; l++)
                    {
                        pat = new Ptrn(new int[]{i, j, k, l});
                        pats.add(pat);
                    }
  }
   
  
  
private void initDico(){
    Map<Integer, Elmt> dico = new HashMap<Integer, Elmt>();
    dico.put(0, new Elmt(0, 24, 30));
    dico.put(1, new Elmt(1, 13, 56));
    dico.put(2, new Elmt(2, 14, 22));
    dico.put(3, new Elmt(3, 9, 23)); //9
    Ptrn.setDico(dico);
}


/*
private void initElmts(int[] nbElmts){
    blocks = new ArrayList<Elmt>();
    for(int i = 0; i < 4; i++){
        if(nbElmts[i] == 1)
            blocks.add(dico.get(i).clone());
        else if(nbElmts[i] > 1)
        {
            int width = 0, height = dico.get(i).clone().getH();
            for(int j = 0; j < nbElmts[i]; j++)
            {
                width += dico.get(i).clone().getW();
                if(j == nbElmts[i]-1)
                {
                    if(width > height)
                    {
                        int temp = height;
                        height = width;
                        width = temp;
                    }
                    System.out.println(i + " w= " + width + " h= " + height);
                    blocks.add(new Elmt(i, width, height));
                }
            }
        }
        
    }
        
}
*/

public void process(){
    try {
        pat.writer(OUTPUT_FILE_NAME);
    } catch (FileNotFoundException ex) {
        Logger.getLogger(PackingTree.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
        Logger.getLogger(PackingTree.class.getName()).log(Level.SEVERE, null, ex);
    }
}

  
  public static void main(String[] args) throws UnsupportedEncodingException, IOException {
      FileOutputStream outputStream = null;
      
    try {
        PackingTree pt = new PackingTree(40, 60);
        /* wished number of each block following its Id */
        //  new int[]{0, 1, 2, 4};
        // new int[]{0, 1, 2, 4}
        Ptrn pat;
        outputStream = new FileOutputStream("C:\\Temp\\global2.txt");
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-16");
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
        
        
        
        for(Ptrn p : pt.pats)
        {
            System.out.println("td");
            //if(p.fit())
            {
                int i = p.getNbElmts()[0];
                int j = p.getNbElmts()[1];
                int k = p.getNbElmts()[2];
                int l = p.getNbElmts()[3];
                //p.writer(OUTPUT_FILE_NAME);
                bufferedWriter.write("(" + i + ", "+ j + ", "+ k + ", "+ l + ") = "+ p.fit());
                bufferedWriter.newLine();
                
                String fileName = "C:\\Temp\\global(" + i + j + k + l + ").txt";
               /* outputStream2 = new FileOutputStream(fileName);
                OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(outputStream2, "UTF-16");
                BufferedWriter bufferedWriter2 = new BufferedWriter(outputStreamWriter2);*/
                //if(p.fit())
                    p.writer(fileName);
                
            }
            
        }
        /*
        boolean[][][][] checking = new boolean
                [Pattern.getDico().get(0).maxByPattern()]
                [Pattern.getDico().get(1).maxByPattern()]
                [Pattern.getDico().get(2).maxByPattern()]
                [Pattern.getDico().get(3).maxByPattern()];
        
            
        
        for(int i = checking.length - 1, a = 0; i > 0 && i >= a; i--)
            for(int j = checking[0].length - 1, b = 0; j > 0 && j >= b; j--)
                for(int k = checking[0][0].length - 1, c = 0; k > 0 && k >= c ; k--)
                    for(int l = checking[0][0][0].length - 1, d = 0; l > 0 && l >= d ; l--)
                    {
                        pat = new Pattern(new int[]{i, j, k, l});
                        checking[i][j][k][l] = pat.fit();
                       // System.out.println("(" + i + ", "+ j + ", "+ k + ", "+ l + ") = " + Boolean.toString(checking[i][j][k][l]));
                        pat.writer(OUTPUT_FILE_NAME);
                        bufferedWriter.write("(" + i + ", "+ j + ", "+ k + ", "+ l + ") = " + Boolean.toString(checking[i][j][k][l]));
                        bufferedWriter.newLine();
                        
                    }
        
        */
        bufferedWriter.close();
    } catch (FileNotFoundException ex) {
        Logger.getLogger(PackingTree.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        try {
            outputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(PackingTree.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }
}
