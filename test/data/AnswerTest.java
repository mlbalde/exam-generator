/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import org.junit.Test;
import static org.junit.Assert.*;
import visitors.Visitor;

/**
 *
 * @author visoft
 */
public class AnswerTest {
    
    public AnswerTest() {
    }

    @Test
    public void testDeepClone() {
        Answer ans=new Answer("Some text content", Boolean.TRUE,null);  
        ans.getParameters().put("Abc", "def");
        Answer ans2=ans.DeepClone();
        assertEquals("The cloning was not done correctly. Not all fields are identical", ans, ans2);
        Answer ans3=ans.DeepClone();
        ans.setCorrect(Boolean.FALSE);
        ans.setTextContent("Something else");
        ans.getParameters().put("Abc", "Def");
        assertEquals("The cloning was not done correctly. Some data were shallow copied", ans2, ans3);
    }
}
