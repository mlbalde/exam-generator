/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
import visitors.Visitor;
import resources.LatexRes;

/**
 *
 * @author visoft
 */
public class QuestionBlockTest {
    
    public QuestionBlockTest() {
    }

    @Test
    public void testDeepClone() {
        QuestionBlock qb1,qb2,qb3;
        ArrayList<SourceItem> qlst=new ArrayList<>();
        qlst.add(LatexRes.GenerateBigQuestion(10, 5));
        qlst.add(LatexRes.GenerateBigQuestion(3, 2));
        qlst.add(LatexRes.GenerateBigQuestion(4, 4));
        
        qb1=new QuestionBlock("Some text", qlst, null);
        qb1.getParameters().getMap().put("Key", "Value");
        qb2=qb1.DeepClone();
        qb3=qb1.DeepClone();
        
        assertEquals("The cloning was not done correctly. Not all fields are identical", qb1,qb2);
        qb1.setTextContent("Bla");
        qb1.getParameters().getMap().remove("Key");
        assertEquals("The cloning was not done correctly. Some data were shallow copied", qb2,qb3);
        
        ((Answer)(qb2.getSubitems().get(2).getSubitems().get(2))).setCorrect(Boolean.FALSE);        
        assertFalse("This objects should be different", qb2.equals(qb3));

        qb2=qb2.DeepClone();
        qb2.directives.put("22", "33");
        assertFalse("This objects should be different", qb2.equals(qb3));
        
    }
}
