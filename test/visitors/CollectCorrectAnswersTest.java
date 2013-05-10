/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visitors;

import visitors.CollectCorrectAnswers;
import data.ParsedSource;
import java.io.Reader;
import latexparser.javacc.LatexLexer;
import latexparser.javacc.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import resources.LatexRes;

/**
 *
 * @author visoft
 */
public class CollectCorrectAnswersTest {
    private static ParsedSource ps1;
    
    public CollectCorrectAnswersTest() {
    }
    
    @BeforeClass
    public static void setup() throws ParseException{
        Reader file;
        file= LatexRes.ReadResource("paper3.tex");
        LatexLexer lexer=new LatexLexer(file);
        ps1=lexer.getParsedSource();
    }
    
    
    @Test
    public void testApplyTo() {
        System.out.println("ApplyTo");
        CollectCorrectAnswers instance = new CollectCorrectAnswers();
        String correct="1 4 8 \n2 4 8 \n3 4 8 \n4 4 8 \n5 4 \n6 4 \n7 \n";
        instance.ApplyTo(ps1);
        String dest;
        dest=instance.getContent();
        assertEquals("The answer output is wrong", correct,dest);
        
    }
}
