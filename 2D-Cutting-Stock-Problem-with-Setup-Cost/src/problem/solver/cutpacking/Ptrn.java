package problem.solver.cutpacking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import problem.solver.parameters.ImageKind;
import problem.solver.parameters.PatternKind;
import problem.solver.patternplacement.ImageLocation;

public class Ptrn
{
    private final PatternKind pk;
    
    private ArrayList<Elmt> blocks;
    private final Node root;

    public Ptrn(int[] wanted, PatternKind pk)
    {
        this.root = new Node(0, 0, pk.getWidth(), pk.getHeight());
        this.pk = pk;
        
        initElmts(wanted);
    }

    private void initElmts(int[] nbElmts)
    {
        blocks = new ArrayList<>();
        
        for(ImageKind ik : pk.getImageKinds())
            if(nbElmts.length > ik.getPatternIndex() && nbElmts[ik.getPatternIndex()] > 0)
                blocks.add(new Elmt(ik, nbElmts[ik.getPatternIndex()]));

        /* we insert blocks according to their height (by grouping each Elmts with same Id into the same block) */
        Collections.sort(blocks, (e1, e2) -> (e1.getH() < e2.getH() ? 1 : (e1.getH() > e2.getH() ? -1 : 0)));
    }

    /* look for the first (from top-left to bottow-right) suitable empty area in 
     which the current block fits */
    public boolean fit()
    {
        ArrayList<Elmt> packed = new ArrayList<>();
        int nbpcs;
        
        for (Elmt block1 : blocks)
        {
            /* we ensure that every pieces of a divided block has been set*/
            do
            {
                nbpcs = 0;
                /* we try to set the Id-aggregated(nbpcs) block into an empty area */
                do
                { /* A big enough area has actually been found for the Id-aggregated(pieces) block constisting in */
                    Node node = findNode(this.root, null, block1);
                    if (node != null)
                    {
                        block1.setFit(this.splitNode(node, block1));
                        
                        packed.add(block1);
                        break;
                    } /* No suitable area Id-aggregated(pieces) block, we have to divide it into 2 smaller blocks (sizes of (pieces - nbpcs) & nbpcs) */
                    else
                    {
                        block1.reduce();
                        nbpcs++;
                    }
                    
                    if (nbpcs > 25)
                        return false;
                    
                } while (block1.getNbPieces() > 0);

                block1 = new Elmt(block1.getImageKind(), nbpcs);
            } while (nbpcs > 0);
        }
        
        blocks = packed;
        return true;
    }

    private Node findNode(Node root, Node prev, Elmt block)
    {
        int w = block.getW();
        int h = block.getH();
        Node nd;
        /* we go through the tree till we find a welcoming leaf */
        /* the current node has already been filled, we go deeper inside (to right first, down then) */
        if (root.isUsed())
        {
            for (Node n : root.sortNodeByArea())
                if ((nd = findNode(n, root, block)) != null)
                    return nd;
                    
        } /* we check whether we could fill the template in a more optimal way (saving more space on pattern borders) by splitting Id-aggregated block */
        else if (block.isBetter(root))
        {
            //System.out.println("h node = "+ root.getH() +" w node = " + root.getW() + " h block = "+ h+" w block = " + w);
            if ((h <= root.getW()) && (w <= root.getH()))
            {
                /* correct the empty area size bellow or next to the previous set elmt, 
                 by making it smaller at case the on-going added element uses a part of it*/
                if (Localisation.HORIZONTAL.equals(root.getLocal()) && h > pk.getHeight() - (prev.getDown().getY() + prev.getDown().getH()))
                    prev.getDown().setW(prev.getDown().getW() - w);
                    
                if (Localisation.VERTICAL.equals(root.getLocal()) && w > pk.getWidth() - (prev.getRight().getX() + prev.getRight().getW()))
                    prev.getRight().setH(prev.getRight().getH() - h);
                
                block.rotate();
                return root;
            }
            if ((w <= root.getW()) && (h <= root.getH()))
                return root;
            
            return null;
        }
        return null;
    }

    private Node splitNode(Node node, Elmt block)
    {
        int w = block.getW();
        int h = block.getH();
        node.setUsed(true);
        /* we split the current space into 2 (the one on the left, the other on bottom),
         before finally stocking the current node into the block (fit)*/
        node.setDown(new Node(node.getX(), node.getY() + h, w, node.getH() - h, Localisation.VERTICAL));
        node.setRight(new Node(node.getX() + w, node.getY(), node.getW() - w, h, Localisation.HORIZONTAL));

        /* we extand area's spaces when possible*/
        node.getDown().setW(node.getDown().getW() + node.getRight().getW());
        node.getRight().setH(node.getRight().getH() + node.getDown().getH());
        return node;
    }

    public List<ImageLocation> getPositions()
    {
        //ArrayList<ImageLocation> posList = new ArrayList<>();
        
        return blocks.parallelStream().flatMap(b -> Stream.of(b.getElmtPositions())).collect(Collectors.toList());
/*
        blocks.stream().forEach((bl) -> {
            posList.addAll(Arrays.asList(bl.getElmtPositions()));
        });
        
        return posList;*/
    }
}
