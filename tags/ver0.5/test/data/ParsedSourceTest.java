/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.ArrayList;
import latexparser.LexerTest;
import org.junit.Test;
import static org.junit.Assert.*;
import resources.LatexRes;

/**
 *
 * @author visoft
 */
public class ParsedSourceTest {
    
    public ParsedSourceTest() {
    }


    @Test
    public void testDeepClone() {
        ParsedSource ps1,ps2,ps3;
        ps1=LatexRes.GenSimpleParsedSrc();
        
        ps2=ps1.DeepClone();
        ps3=ps1.DeepClone();
        
        assertEquals("The cloning was not done correctly. Not all fields are identical", ps1,ps2);
        ps1.footer="asdfasdf";
        ps1.getParameters().getMap().put("Key","Val");
        assertEquals("The cloning was not done correctly. Some data were shallow copied", ps2,ps3);
    }
}
