package problem.solver.cutpacking;

import java.util.HashMap;
import java.util.Map;
import problem.solver.parameters.ImageKind;
import problem.solver.Pattern;
import problem.solver.parameters.PatternKind;
import problem.solver.patternplacement.ImageLocation;
import problem.solver.patternplacement.PatternPlacement;

public class CutInterfacer extends PatternPlacement
{
    public CutInterfacer(PatternKind patternKind)
    {
        super(patternKind);
        
        Map<Integer, Elmt> dico = new HashMap<>();
        patternKind.getImageKinds().stream().forEach((ik) -> {
            dico.put(ik.getPatternIndex(), new Elmt(ik.getPatternIndex(), ik.getWidth(), ik.getHeight()));
        });
        Ptrn.setDico(dico);
    }
    
    @Override
    public ImageLocation[] getLocations(Pattern pattern)
    {
        int[] e = new int[pattern.getImageNumber().length];
        for(int i = 0; i < e.length; i++)
            e[i] = (int)pattern.getImageNumber()[i];
        
        Ptrn ptrn = new Ptrn(e);
        
        if(ptrn.fit())
            return ptrn.getPositions().stream().toArray(ImageLocation[]::new);
        else
            return null;
    }
}
