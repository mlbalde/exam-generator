/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
import visitors.Visitor;

/**
 *
 * @author visoft
 */
public class QuestionTest {
    
    public QuestionTest() {
    }

    @Test
    public void NullInConstructor(){
        Question q=new Question("Some text",null, null);
        //SHOULD NOT THROW ANY EXCEPTION
    }

    @Test
    public void testDeepClone() {
        
        Question q1,q2,q3;
        q1=new Question("Some text content",null, null);
        Answer ans=new Answer("Some text content", Boolean.TRUE,null);
        q1.getSubitems().add(ans);
        ans=ans.DeepClone();ans.setCorrect(Boolean.FALSE);
        q1.getSubitems().add(ans);
        ans=ans.DeepClone();ans.setTextContent("Sth else");
        q1.getSubitems().add(ans);
        
        q2=q1.DeepClone();
        assertTrue("The cloning was not done correctly. Not all fields are identical", q1.equals(q2));
        
        q3=q1.DeepClone();
        q1.setTextContent("Different");
        q1.getSubitems().get(2).setTextContent("No no no!");
        assertTrue("The cloning was not done correctly. Some data were shallow copied", q2.equals(q3));
        
        q2.getSubitems().get(2).setTextContent("BLA BLA BLA");
        assertFalse("This objects should be different", q2.equals(q3));
    }

    
}
