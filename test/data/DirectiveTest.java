/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author visoft
 */
public class DirectiveTest {
    
    public DirectiveTest() {
    }

    @Test
    public void testDeepClone() {
        Directive dir=new Directive();
        dir.put("k1", "v1");
        Directive d1,d2;
        d1=dir.DeepClone();
        d2=d1.DeepClone();
        assertEquals("The data was not copied correctly",dir,d2);
        d1.put("k2", "v2");
        d1.put("k1", "vx");
        assertEquals("Some data was shallow copied",dir,d2);
        assertFalse("The objects should be different",dir.equals(d1));
    }
}
