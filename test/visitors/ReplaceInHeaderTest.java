/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visitors;

import visitors.ReplaceInHeader;
import data.Answer;
import data.ParsedSource;
import data.Question;
import data.QuestionBlock;
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
public class ReplaceInHeaderTest {
    private static ParsedSource ps1;
    public ReplaceInHeaderTest() {
    }
    @BeforeClass
    public static void setup() throws ParseException{
        Reader file;
        file= LatexRes.ReadResource("paper3.tex");
        LatexLexer lexer=new LatexLexer(file);
        ps1=lexer.getParsedSource();
    }

    /**
     * Test no actual change
     */
    @Test
    public void testApplyTo() {
       ParsedSource ps;
       ReplaceInHeader head=new ReplaceInHeader("@Rer", "@Ref"); 
       ps=head.ApplyTo(ps1);
       assertEquals("Theere should be no change",ps1, ps);
    }
    /**
     * There must be some change
     */
    @Test
    public void testSomeChange(){
       ParsedSource ps;
       ReplaceInHeader head=new ReplaceInHeader("@Ref", "@Ref1"); 
       ps=head.ApplyTo(ps1);
       assertFalse("Theere should be change!",ps1.equals(ps));
    }
    /**
     * The change must be reversible
     */
    @Test
    public void testSomeChange1(){
       ParsedSource ps;
       ReplaceInHeader head=new ReplaceInHeader("@Ref", "@Ref1"); 
       ps=head.ApplyTo(ps1);
       assertFalse("Theere should be change!",ps1.equals(ps));
       ReplaceInHeader head1=new ReplaceInHeader("@Ref1", "@Ref"); 
       ps=head1.ApplyTo(ps);
       assertEquals("The change should be reversible",ps1, ps);
    }

    /**
     * There must be some change, but only in header
     */
    @Test
    public void testSomeChangeOnlyInHead(){
       ParsedSource ps;
       ReplaceInHeader head=new ReplaceInHeader("@Ref", "@Ref1"); 
       ps=head.ApplyTo(ps1);
       assertFalse("Theere should be change!",ps1.equals(ps));
       ps.setHeader(ps1.getHeader());
       assertEquals("Theere should be no change except in header",ps1, ps);
       
    }
    
}
