/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package latexparser.javacc;

import data.Answer;
import data.ParsedSource;
import data.Question;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;
import resources.LatexRes;
import static org.junit.Assert.*;

/**
 *
 * @author visoft
 */
public class LatexLexerTest {
    
    @Test
    public void readAFile()throws ParseException{
        Reader file;
        file= LatexRes.ReadResource("paper4.tex");
        LatexLexer lexer=new LatexLexer(file);
        ParsedSource ps=lexer.getParsedSource();
        assertEquals("The no of items is wrong",1,ps.getSubitems().size());
        Question q=(Question)ps.getSubitems().get(0);
        assertEquals("The no of answers in the question is wrong",5,q.getSubitems().size());
        assertTrue("Ans 1 should be false",!q.getAnswerAt(0).getCorrect());
        assertTrue("Ans 2 should be false",!q.getAnswerAt(1).getCorrect());
        assertTrue("Ans 3 should be false",!q.getAnswerAt(2).getCorrect());
        assertTrue("Ans 4 should be false",!q.getAnswerAt(3).getCorrect());
        assertTrue("Ans 5 should be true",q.getAnswerAt(4).getCorrect());
        
    }
    
    /**
     * Doesn't parse the \\lstinline!xxx! correct.
     */
    @Test
    public void ParseLstInline()throws ParseException{
        String Example="%@Header.begin\n%@Header.end\n\\begin{enumerate}\\item Question: \n\\begin{enumerate}\n"
                + "\\item Item  \\lstinline!true!. %@T\n\\end{enumerate}"
                + "\n\\end{enumerate}\\n%@Footer.begin\n%@Footer.end";
        StringReader src=new StringReader(Example);
        LatexLexer lexer=new LatexLexer(src);
        ParsedSource ps=lexer.getParsedSource();
        assertEquals("The no of items is wrong",1,ps.getSubitems().size());
        Question q=(Question)ps.getSubitems().get(0);
        Answer ans=q.getAnswerAt(0);
        assertEquals("The text is different"," Item  \\lstinline!true!. %\n", ans.getTextContent());
    }
    /**
     * Doesn't parse the \\verb!xxx! correct.
     */
    @Test
    public void ParseLstVerb()throws ParseException{
        String Example="%@Header.begin\n%@Header.end\n\\begin{enumerate}\\item Question: \n\\begin{enumerate}\n"
                + "\\item Item  \\verb!true!. %@T\n\\end{enumerate}"
                + "\n\\end{enumerate}\\n%@Footer.begin\n%@Footer.end";
        StringReader src=new StringReader(Example);
        LatexLexer lexer=new LatexLexer(src);
        ParsedSource ps=lexer.getParsedSource();
        assertEquals("The no of items is wrong",1,ps.getSubitems().size());
        Question q=(Question)ps.getSubitems().get(0);
        Answer ans=q.getAnswerAt(0);
        assertEquals("The text is different"," Item  \\verb!true!. %\n", ans.getTextContent());
    }

    
}
