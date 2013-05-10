/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package latexparser;
import data.Answer;
import data.Directive;
import visitors.OutputLatex;
import data.ParsedSource;
import data.Question;
import data.QuestionBlock;
import data.Section;
import java.io.Reader;
import java.io.StringReader;
import latexparser.javacc.LatexLexer;
import latexparser.javacc.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;
import resources.LatexRes;

/**
 *
 * @author visoft
 */
public class LexerTest {
    
    @Test
    public void testLoadFile()
    {
        Reader file;
        file=LatexRes.ReadResource("paper1.tex");
        assertNotNull("The file paper1.tex cannot be opened", file);
    }
    /**
     * Test a well behaved file, but with convoluted comments and 
     * commands "hidden" in \verb and lstlisting blocks.
     * @throws ParseException 
     */
    @Test
    public void testParseRegularFile1() throws ParseException
    {
        Reader file;
        file=LatexRes.ReadResource("paper1.tex");
        LatexLexer lexer=new LatexLexer(file);
        ParsedSource ps;
        ps=lexer.getParsedSource();
    }

    /**
     * Test the parsing and the regeneration of the file. The output is in a stringbuffer
     * @throws ParseException 
     */
    @Test
    public void testParseRegularFile2() throws ParseException
    {
        Reader file;
        file=LatexRes.ReadResource("paper2.tex");
        LatexLexer lexer=new LatexLexer(file);
        ParsedSource ps;
        ps=lexer.getParsedSource();
        
        StringBuilder content;
        OutputLatex out=new OutputLatex();
        out.ApplyTo(ps);
        content=out.getContent();   //Take the content of this variable and 
                                    //compile it in Latex. The pdf should look identical
        assertNotNull("The output is empty",content); 
       
    }

    /** Roughly tests that everything that matters is parsed fromt the file
     */
    
    @Test
    public void testParseRegularFile3() throws ParseException
    {
        Reader file;
        file=LatexRes.ReadResource("paper2.tex");
        LatexLexer lexer=new LatexLexer(file);
        ParsedSource ps;
        ps=lexer.getParsedSource();
        //Assesing the directive part
        Directive dir=ps.getParameters();
        assertEquals("The specs part should contain 3 items",3,dir.getMap().values().size());
        assertNotNull("The key before head is missing",dir.getMap().get("BeforeHead"));
        assertNotNull("The key Inside Head is missing",dir.getMap().get("InsideHead"));
        assertNotNull("The key After Head is missing",dir.getMap().get("AfterHead"));
        assertTrue("The header should be longer",ps.getHeader().split("\n").length>75);
        assertTrue("The footer should be longer",ps.getHeader().split("\n").length>1);
        assertEquals("The no of items is wrong",4,ps.getSubitems().size());
        QuestionBlock bl;
        bl=(QuestionBlock)ps.getSubitems().get(0);
        assertEquals("The no of qb parameters are wrong",1,bl.getParameters().getMap().values().size());
        assertEquals("The text in parameter for the qb is wrong","auto", bl.getParameters().getMap().get("questions"));
        assertEquals("The no of subquestions is wrong",2,bl.getSubitems().size());
        Question q;
        q=(Question)ps.getSubitems().get(3);
        assertEquals("The no of answers in question 4 is wrong",5, q.getSubitems().size());
        assertEquals("The 4'th answer should be true",true, q.getAnswerAt(3).getCorrect());
        assertEquals("The 3'th answer should be false",false, q.getAnswerAt(2).getCorrect());
    }
    
    
    @Test
    public void testNoSectionDocument() throws ParseException{
        Reader file;
        file=LatexRes.ReadResource("paper6.tex");
        LatexLexer lexer=new LatexLexer(file);
        ParsedSource ps;
        ps=lexer.getParsedSource();
        assertEquals("No of items is wrong",2,ps.getSubitems().size());
        assertEquals("The first item must be a block", QuestionBlock.class,ps.getSubitems().get(0).getClass());
        assertEquals("The second item must be a question", Question.class,ps.getSubitems().get(1).getClass());
        QuestionBlock qb=(QuestionBlock)ps.getSubitems().get(0);
        assertEquals("The questionblock have wrong no of items",2,qb.getSubitems().size());
        Question q=qb.getQuestionAt(0);
        assertEquals("The question have wrong no of answers",3,q.getSubitems().size());
        assertEquals("The item class is wrong in the question",Answer.class,q.getAnswerAt(0).getClass());
        assertEquals("The item class is wrong in the question",Answer.class,q.getAnswerAt(1).getClass());
        assertEquals("The item class is wrong in the question",Answer.class,q.getAnswerAt(2).getClass());
        
    }
    
    @Test
    public void testSectionedDocument() throws ParseException{
        Reader file;
        file=LatexRes.ReadResource("paper7.tex");
        LatexLexer lexer=new LatexLexer(file);
        ParsedSource ps;
        ps=lexer.getParsedSource();
        assertEquals("No of items is wrong",3,ps.getSubitems().size());
        for(int i=0;i<ps.getSubitems().size();i++){
            assertEquals("The "+i+" subitem must be a section",Section.class,ps.getSubitems().get(i).getClass());
        }
        Section sec=(Section)ps.getSubitems().get(0);
        assertTrue("The description was not parsed competly",sec.getTextContent().contains("AAA_SEARCH_BBB"));
        assertEquals("The parameters were not parsed correctly",2,sec.getParameters().getMap().keySet().size());
        assertNotNull("The key 'questions' is not in the parameter set",sec.getParameters().get("questions"));
        assertNotNull("The key 'DoNotRadomize' is not in the parameter set",sec.getParameters().get("DoNotRadomize"));
    }
         
    /**
     * The text that doesn't appear right after \section part should be ignored
     * 
     * @throws ParseException 
     */
    @Test
    public void testNotIgnoringTextInSection() throws ParseException{
        String file="%@Header.begin \n... %@Header.end\n"
                + " \\section{s1} AAAA \n"
                + "\\begin{enumerate} BBB \n"
                + "\\item Question \\begin{enumerate} \\item Answer \\end{enumerate} CCC \n"
                + " \\end{enumerate} \n%@Footer.begin\n %@Footer.end \n";
         LatexLexer lexer=new LatexLexer(new StringReader(file));
         ParsedSource ps=lexer.getParsedSource();
         Section s1;
         s1=(Section)ps.getSubitems().get(0);
         assertFalse("The BBB was found in \\section textContent ",s1.getTextContent().contains("BBB"));
         assertTrue("The BBB was found in \\section enumerateOptions",s1.getEnumerateOptions().contains("BBB"));
         assertFalse("The CCC was found in \\section textContent ",s1.getTextContent().contains("CCC"));
         assertFalse("The CCC was found in \\section enumerateOptions",s1.getEnumerateOptions().contains("CCC"));
    }
    
    
    
    /**
     * The text that doesn't appear in \item  part should be ignored
     * 
     * @throws ParseException 
     */
    @Test
    public void testNotIgnoringTextBetweenEnvironments() throws ParseException{
        String file="%@Header.begin \n... %@Header.end\n"
                + "\\begin{enumerate} BBB \n"
                + "\\item Question \\begin{enumerate} \\item Answer \\end{enumerate} CCC \n"
                + " \\end{enumerate} \n%@Footer.begin\n %@Footer.end \n";
         LatexLexer lexer=new LatexLexer(new StringReader(file));
         ParsedSource ps=lexer.getParsedSource();
         OutputLatex outLatex=new OutputLatex();
         outLatex.ApplyTo(ps);
         String output=outLatex.getContent().toString();
         
         assertFalse("The BBB was found in parsed document outside \\item part ",output.contains("BBB"));
         assertFalse("The CCC was found in parsed document outside \\item part",output.contains("CCC"));
         
    }
    /**
     * The @Specs part cut off some more text than necessary.
     */
    @Test
    public void testSpecsCutTheText() throws ParseException{
        String file="%@Header.begin \n"
                + "AAAAAAAAAAAAAAAAAA"
                + "%@Specs(TotalCount=auto AnswersPerQestion=5 MaxTrue=5 MinTrue=0 WriteAnswers=0)\n"
                + "BBBBBBBBBBBBBBBBBB\n"
                + "CCCCCCCCCCCCCCCCCC\n %@Header.end\n"
                + "\\begin{enumerate} some text \n"
                + "\\item Question \\begin{enumerate} \\item Answer \\end{enumerate} some ignored text \n"
                + " \\end{enumerate} \n%@Footer.begin\n %@Footer.end \n";
    
         LatexLexer lexer=new LatexLexer(new StringReader(file));
         ParsedSource ps=lexer.getParsedSource();
         
         String head=ps.getHeader();
         assertTrue("The sequence before @Specs is missing from header ",head.contains("AAAAAAAAAAAAAAAAAA"));
         assertTrue("The sequence after @Specs is missing from header (BBB) ",head.contains("BBBBBBBBBBBBBBBBBB"));
         assertTrue("The sequence after @Specs is missing from header (AAA) ",head.contains("CCCCCCCCCCCCCCCCCC"));
    }
}

