/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visitors;

import data.ParsedSource;
import visitors.OutputLatex;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;
import resources.LatexRes;

/**
 *
 * @author visoft
 */
public class OutputLatexTest {
    
    public OutputLatexTest() {
    }
    private static ParsedSource ps1;
    @BeforeClass
    public static void setUpClass() throws Exception {
        ps1=resources.LatexRes.GenSimpleParsedSrc();
    }

    /**
     * Tests the parsing of a ParsedSource into actual latex document.
     * The actual test must be done manual. Copy paste the output into a 
     * latex file and compile it. :)
     */
    @Test
    public void testApplyTo() {
       OutputLatex ltx=new OutputLatex();
       ps1.getParameters().getMap().put("WriteAnswers","1");
       ltx.ApplyTo(ps1);
       System.out.println(ltx.getContent().toString());
    }
    @Test
    public void testGenerateSomeSectionedLatex(){
        OutputLatex ltx=new OutputLatex();
        ParsedSource src=LatexRes.generate4SectionSrc();
        ltx.ApplyTo(src);
        String result=ltx.getContent().toString();
        
    }
}
