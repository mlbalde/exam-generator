/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visitors;

import visitors.DivideBigQuestions;
import data.Answer;
import data.Directive;
import data.ParsedSource;
import data.Question;
import data.QuestionBlock;
import data.SourceItem;
import java.io.Reader;
import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import static resources.LatexRes.*;
import static visitors.DivideBigQuestions.*;
import java.util.Arrays;
import latexparser.javacc.LatexLexer;
import latexparser.javacc.ParseException;
import resources.LatexRes;

/**
 *
 * @author visoft
 */
public class DivideBigQuestionsTest {
    
    public DivideBigQuestionsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    
    @Before
    public void setUp() {
    }

    /**
     * Tests general workings of the ApplyTo method 
     */
    @Test
    public void testApplyTo1() {
        Question[] q={GenerateBigQuestion(20, 4),GenerateBigQuestion(5, 2)};
        ArrayList<Question> qarr=new ArrayList<>(Arrays.asList(q));
        ParsedSource ps=new ParsedSource();
        QuestionBlock qb=new QuestionBlock("",qarr , new Directive());
        ps.getSubitems().add(qb);
        ps.getSubitems().add(GenerateBigQuestion(5, 2));
        ps.getParameters().getMap().put("AnswersPerQestion", "5");
        
        DivideBigQuestions sc=new DivideBigQuestions();
        ParsedSource rez= sc.ApplyTo(ps);
        
        assertEquals("The number of generated items is wrong",2,rez.getSubitems().size());
        assertEquals("The type of first item is wrong",QuestionBlock.class,rez.getSubitems().get(0).getClass());
        assertEquals("The type of second item is wrong",Question.class,rez.getSubitems().get(1).getClass());
        assertEquals("The no of questions in questionblock is wrong",5,((QuestionBlock)rez.getSubitems().get(0)).getSubitems().size());
        
    }
    
    /**
     * Test the parameter identification to ApplyT0
     */
    @Test
    public void testApplyTo2() {
        Question[] q={GenerateBigQuestion(20, 4),GenerateBigQuestion(5, 2)};
        ArrayList<Question> qarr=new ArrayList<>(Arrays.asList(q));
        QuestionBlock qb=new QuestionBlock("",qarr ,null);
        
        ParsedSource ps=new ParsedSource();
        ps.getSubitems().add(qb);
        ps.getParameters().getMap().put("AnswersPerQestion", "5");
        ps.getParameters().getMap().put("MinTrue", "2");
        ps.getParameters().getMap().put("MaxTrue", "4");

        
        DivideBigQuestions sc=new DivideBigQuestions();
        ParsedSource rez= sc.ApplyTo(ps);
        
        assertEquals("The number of generated items is wrong",1,rez.getSubitems().size());
        assertEquals("The type of first item is wrong",QuestionBlock.class,rez.getSubitems().get(0).getClass());
        assertEquals("The no of questions in questionblock is wrong",3,((QuestionBlock)rez.getSubitems().get(0)).getSubitems().size());
        
    }
    
    
    
    /*
     * Cases to be tested:
     *  1)-5 answers, 1 question
     *  2)-k*A answers, k questions, A answer/quest
     *  3)-same as before, with min no of T
     *  4)-same, min no of F
     *
     *  -count to see if F and T are <= with original F and T
     * 
     */
    
    @Test
    public void testGenerateQuestions1() {
        Question q=GenerateBigQuestion(5, 3);
        ArrayList<Question> resp=DivideBigQuestions.generateQuestions(q, 5, 1, 4,-1);
        assertEquals("The no of generated questions is wrong",1, resp.size());
        assertEquals("The no of answers in the question is wrong", 5,resp.get(0).getSubitems().size());
    }

    @Test
    public void testGenerateQuestions2() {
        Question q=GenerateBigQuestion(20, 8);
        ArrayList<Question> resp=DivideBigQuestions.generateQuestions(q, 5, 1, 4,-1);
        assertEquals("The no of generated questions is wrong",4, resp.size());
        Question qr=null;
        for(int i=0;i<resp.size();i++){
            qr=resp.get(i);
            assertEquals("The no of answers in the"+i + "'th question is wrong",5, qr.getSubitems().size());
            int T=0,j;
            for(j=0;j<qr.getSubitems().size();j++)
                if(qr.getAnswerAt(j).getCorrect())
                    T++;
            assertTrue("The number of T answers is less than desired",T>=1);
            assertTrue("The number of F answers is less than desired",T<=4);
        }
    }
    @Test
    public void testGenerateQuestions3() {
        Question q=GenerateBigQuestion(20, 4);
        ArrayList<Question> resp=DivideBigQuestions.generateQuestions(q, 5, 1, 4,-1);
        assertEquals("The no of generated questions is wrong",4, resp.size());
        Question qr=null;
        for(int i=0;i<resp.size();i++){
            qr=resp.get(i);
            assertEquals("The no of answers in the"+i + "'th question is wrong",5, qr.getSubitems().size());
            int T=0,j;
            for(j=0;j<qr.getSubitems().size();j++)
                if(qr.getAnswerAt(j).getCorrect())
                    T++;
            assertTrue("The number of T answers is less than desired",T>=1);
            assertTrue("The number of F answers is less than desired",T<=4);
        }
    }

    @Test
    public void testGenerateQuestions4() {
        Question q=GenerateBigQuestion(20, 16);
        ArrayList<Question> resp=DivideBigQuestions.generateQuestions(q,  5, 1, 4,-1);
        assertEquals("The no of generated questions is wrong",4, resp.size());
        Question qr=null;
        for(int i=0;i<resp.size();i++){
            qr=resp.get(i);
            assertEquals("The no of answers in the"+i + "'th question is wrong",5, qr.getSubitems().size());
            int T=0,j;
            for(j=0;j<qr.getSubitems().size();j++)
                if(qr.getAnswerAt(j).getCorrect())
                    T++;
            assertTrue("The number of T answers is less than desired",T>=1);
            assertTrue("The number of F answers is less than desired",T<=4);
        }
    }
    

    @Test
    public void testGetMaxNoOfQuestions() {
        Question q;
        Answer ans;
        int N,T,F,i;
        N=20; //Total no of answers
        T=5;// Total no of true answers
        q=GenerateBigQuestion(N, T);
        int nmax;
        nmax=DivideBigQuestions.getMaxNoOfQuestions(q, 5, 1,4);
        assertEquals("The max number of questions is computed wrong",4, nmax);

        nmax=DivideBigQuestions.getMaxNoOfQuestions(q, 5, 1,1);
        assertEquals("The max number of questions is computed wrong",3, nmax);
        
        nmax=DivideBigQuestions.getMaxNoOfQuestions(q, 10, 1,9);
        assertEquals("The max number of questions is computed wrong",2, nmax);
        
        nmax=DivideBigQuestions.getMaxNoOfQuestions(q, 5, 2,4);
        assertEquals("The max number of questions is computed wrong",2, nmax);
    }
    
    /**
     * Test getMaxNoOfQuestions when several values are zero
     */
    @Test
    public void testGetMaxNoQuestDivBy0(){
        Question q=GenerateBigQuestion(20, 5);
        int k;
        k=DivideBigQuestions.getMaxNoOfQuestions(q, 5, 0, 5);
        assertEquals("The no of max questions is wrong",4,k);       

    }
    
    /**
     * Test getMaxNoOfQuestions when several values are zero
     */
    @Test
    public void testGetMaxNoQuestDivBy1(){
        Question q=GenerateBigQuestion(20, 5);
        int k;
        k=DivideBigQuestions.getMaxNoOfQuestions(q, 5, 0, 0);
        assertEquals("The no of max questions is wrong",3,k);       
    }

     /**
     * Test getMaxNoOfQuestions when several values are zero
     */
    @Test
    public void testGetMaxNoQuestDivBy2(){
        Question q=GenerateBigQuestion(20, 5);
        int k;
        k=DivideBigQuestions.getMaxNoOfQuestions(q, 0, 0, 0); //Kind of a meaningless 
        //should not get an exception
    }
     /**
     * Test getMaxNoOfQuestions when several values are zero
     */
    @Test
    public void testGetMaxNoQuestDivBy3(){
        Question q=GenerateBigQuestion(20, 0);
        int k;
        k=DivideBigQuestions.getMaxNoOfQuestions(q, 5, 0, 5); //Kind of a meaningless 
        assertEquals("The no of max questions is wrong",4,k);       

    }
    
    /**
     * A bug in the exam file.
     */
    @Test
    public void testTheExamSheet() throws ParseException{
        Reader file;
        file= LatexRes.ReadResource("paper4.tex");
        LatexLexer lexer=new LatexLexer(file);
        ParsedSource ps=lexer.getParsedSource();
        
        DivideBigQuestions sel=new DivideBigQuestions();
        ParsedSource rez;
        rez=sel.ApplyTo(ps);
        assertEquals("There must be one question",1,rez.getSubitems().size());
        }
}
