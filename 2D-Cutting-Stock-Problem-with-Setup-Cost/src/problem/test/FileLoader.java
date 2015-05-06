package problem.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import problem.solver.parameters.PatternKind;

public class FileLoader
{
    public static PatternKind loadFromFile(File f) throws IOException
    {
        List<String> lines = Files.readAllLines(f.toPath());
        
        PatternKind pk = null;
        int w = 0;
        int h = 0;
        
        for(String line : lines)
        {
            if(line.startsWith("LX"))
            { // LX
                w = Integer.parseInt(line.split("=")[1]);
            }
            else if(line.startsWith("LY"))
            {
                h = Integer.parseInt(line.split("=")[1]);
            }
            else if(line.startsWith("m"))
            { }
            else
            {
                if(pk == null)
                    pk = new PatternKind(w, h);
                
                String[] elements = line.split("\t");
                pk.addImageKind((int)Double.parseDouble(elements[0]), (int)Double.parseDouble(elements[1]), Integer.parseInt(elements[2]));
            }
        }
        
        return pk;
    }
}
