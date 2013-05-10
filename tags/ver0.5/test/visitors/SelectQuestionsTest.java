/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visitors;

import visitors.DivideBigQuestions;
import visitors.SelectQuestions;
import data.Answer;
import data.ParsedSource;
import data.Question;
import data.QuestionBlock;
import data.Section;
import data.SourceItem;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import static resources.LatexRes.*;

/**
 *
 * @author visoft
 */
public class SelectQuestionsTest {
    
    public SelectQuestionsTest() {
    }

    
    /**
     * Test that reveals a bug. When there are only questions, one has to select
     * a lesser number of questions
     */
    @Test
    public void testSelectWhenOnlyQuest(){
        ParsedSource ps=new ParsedSource();
        ParsedSource rez;
        DivideBigQuestions sel=new DivideBigQuestions();
        SelectQuestions selq=new SelectQuestions();
        
        ps.getParameters().getMap().put("AnswersPerQestion","5");
        ps.getParameters().getMap().put("TotalCount","3");
        ps.getSubitems().add(GenerateBigQuestion(5,3));
        ps.getSubitems().add(GenerateBigQuestion(5,3));
        ps.getSubitems().add(GenerateBigQuestion(5,3));
        ps.getSubitems().add(GenerateBigQuestion(5,3));
        ps.getSubitems().add(GenerateBigQuestion(5,3));
        ps.getSubitems().add(GenerateBigQuestion(5,3));
        ps.getSubitems().add(GenerateBigQuestion(5,3));
        
        rez=sel.ApplyTo(ps);
        rez=selq.ApplyTo(rez);
        
        assertEquals("The no of selected questions is wrong",3,rez.getSubitems().size());
        
    }
    /**
     * Select exactly TotalCount of questions.
     */
    @Test
    public void testSelectExact(){
        ParsedSource ps=new ParsedSource();
        ParsedSource rez;
        DivideBigQuestions sel=new DivideBigQuestions();
        SelectQuestions selq=new SelectQuestions();

        for(int i=1;i<10;i++){
            int no=(int) Math.round(Math.random()*10+2);


            ps.getParameters().getMap().put("AnswersPerQestion","5");
            ps.getParameters().getMap().put("TotalCount",Integer.toString(no));
            ps.getSubitems().add(GenerateBigQuestion(5,3));
            ps.getSubitems().add(GenerateBigQuestion(5,3));
            ps.getSubitems().add(GenerateBigQuestion(5,3));
            ps.getSubitems().add(GenerateBigQuestion(5,3));
            ps.getSubitems().add(GenerateBigQuestion(5,3));
            ps.getSubitems().add(GenerateBigQuestion(5,3));
            ps.getSubitems().add(GenerateBigQuestion(5,3));

            Question[] q={GenerateBigQuestion(20, 4),GenerateBigQuestion(5, 2)};
            ArrayList<Question> qarr=new ArrayList<>(Arrays.asList(q));
            QuestionBlock qb=new QuestionBlock("",qarr ,null);        

            ps.getSubitems().add(qb);
            
            rez=sel.ApplyTo(ps);
            rez=selq.ApplyTo(rez);

            int k=0;
            SourceItem sc;
            for(int j=0;j<rez.getSubitems().size();j++)
            {
                sc=rez.getSubitems().get(j);
                if(sc.getClass()==Question.class)
                    k++;
                else
                    k+=((QuestionBlock)sc).getSubitems().size();
            }
            
            assertEquals("The no of selected questions is wrong",no,k);
        }
    }
        /**
       * Test AlwaysSelect directive
       */
      @Test
      public void testSelectAlwaysSel(){
            ParsedSource ps=new ParsedSource();
            ParsedSource rez;
            DivideBigQuestions sel=new DivideBigQuestions();
            SelectQuestions selq=new SelectQuestions();
        
            ps.getParameters().getMap().put("AnswersPerQestion","5");
            ps.getParameters().getMap().put("TotalCount","1"); //Must select exactly one question
            ps.getSubitems().add(GenerateBigQuestion(5,3));
            ps.getSubitems().add(GenerateBigQuestion(5,3));
            ps.getSubitems().add(GenerateBigQuestion(5,3));
            ps.getSubitems().add(GenerateBigQuestion(5,3));
            ps.getSubitems().add(GenerateBigQuestion(5,3));
            ps.getSubitems().add(GenerateBigQuestion(5,3));
            ps.getSubitems().add(GenerateBigQuestion(5,3));

            Question[] q={GenerateBigQuestion(20, 4),GenerateBigQuestion(5, 2)};
            ArrayList<Question> qarr=new ArrayList<>(Arrays.asList(q));
            QuestionBlock qb=new QuestionBlock("",qarr ,null);
            ps.getSubitems().add(qb);
            
            q=new Question[]{GenerateBigQuestion(5, 2)};
            q[0].setTextContent("Right!");
            qarr=new ArrayList<>(Arrays.asList(q));
            qb=new QuestionBlock("", qarr, null);
            qb.getParameters().getMap().put("AlwaysSelect", "1"); //This is the questionblock that must always be selected
            ps.getSubitems().add(qb);
            
            rez=sel.ApplyTo(ps);
            rez=selq.ApplyTo(rez);

            int k=0;
            SourceItem sc;
            for(int j=0;j<rez.getSubitems().size();j++)
            {
                sc=rez.getSubitems().get(j);
                if(sc.getClass()==Question.class)
                    k++;
                else
                {
                    //assertTrue("The question block shouldn't be empty",((QuestionBlock)sc).getSubitems().size()>0);
                    k+=((QuestionBlock)sc).getSubitems().size();
                }
            }
            
            assertEquals("The no of selected questions is wrong",1,k);
            qb=(QuestionBlock)rez.getSubitems().get(0);
            Question qq=qb.getQuestionAt(0);
            assertEquals("The selected question is wrong","Right!", qq.getTextContent());
    }
    
     @Test
     public void selectBlockNoOfQuestions(){
         ParsedSource src=generateBareParsedSr();
         QuestionBlock qb=new QuestionBlock();
         qb.getSubitems().add(GenerateBigQuestion(5, 3));
         qb.getSubitems().add(GenerateBigQuestion(5, 3));
         qb.getSubitems().add(GenerateBigQuestion(5, 3));
         qb.getSubitems().add(GenerateBigQuestion(5, 3));
         qb.getSubitems().add(GenerateBigQuestion(5, 3));
         qb.getSubitems().add(GenerateBigQuestion(5, 3));
         qb.getSubitems().add(GenerateBigQuestion(5, 3));
         qb.getParameters().put("questions", "3");
         src.getSubitems().add(qb);
         
         DivideBigQuestions sel=new DivideBigQuestions();
         SelectQuestions selq=new SelectQuestions();
         
         ParsedSource rez1,rez2;
         rez2=selq.ApplyTo(src);
         
         qb=(QuestionBlock)rez2.getSubitems().get(0);
         assertEquals("The nr of questions is wrong",3,qb.getSubitems().size());
         
         
     }

     @Test
     public void selectSectionNoOfQuestions(){
         ParsedSource src=generateBareParsedSr();
         Section sec=new Section();
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getParameters().put("questions", "3");
         src.getSubitems().add(sec);
         
         DivideBigQuestions sel=new DivideBigQuestions();
         SelectQuestions selq=new SelectQuestions();
         
         ParsedSource rez1,rez2;
         rez2=selq.ApplyTo(src);
         
         sec=(Section)rez2.getSubitems().get(0);
         assertEquals("The nr of questions is wrong",3,sec.getSubitems().size());
     }
     
     
     /**
      * Complex test that tests the compliance of questions=n option at different levels
      * 
      * */
     @Test
     public void selectComplexNoOfQuestions(){
         ParsedSource src=generateBareParsedSr();
         Section sec=new Section();
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         
         QuestionBlock qb=new QuestionBlock();
         qb.getSubitems().add(GenerateBigQuestion(5, 3));
         qb.getSubitems().add(GenerateBigQuestion(5, 3));
         qb.getSubitems().add(GenerateBigQuestion(5, 3));
         qb.getSubitems().add(GenerateBigQuestion(5, 3));
         qb.getSubitems().add(GenerateBigQuestion(5, 3));
         qb.getSubitems().add(GenerateBigQuestion(5, 3));
         qb.getSubitems().add(GenerateBigQuestion(5, 3));
         qb.getParameters().put("questions", "3");
         sec.getSubitems().add(qb);         
         
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getParameters().put("questions", "5");
         src.getSubitems().add(sec);
         
         sec=new Section();
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         src.getSubitems().add(sec);
         
         DivideBigQuestions sel=new DivideBigQuestions();
         SelectQuestions selq=new SelectQuestions();

         sec=new Section();
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getParameters().put("AlwaysSelect", "1");
         src.getSubitems().add(sec);
         
         src.getParameters().put("TotalCount", "8");
         
         ParsedSource rez1,rez2;
         rez2=selq.ApplyTo(src);
         
         int totNoQ=0,totNoSec=0,totNoBlock=0;
         for(int i=0;i<rez2.getSubitems().size();i++){
             sec=(Section)rez2.getSubitems().get(i);
             totNoSec=0;
             for(int j=0;j<sec.getSubitems().size();j++){
                 SourceItem item;
                 item=sec.getSubitems().get(j);
                 if(item.getClass()==Question.class){
                     totNoSec++;
                     totNoQ++;
                 }
                 if(item.getClass().equals(QuestionBlock.class)){
                     totNoBlock=item.getSubitems().size();
                     totNoQ+=totNoBlock;
                     totNoSec+=totNoBlock;
                 }
             }
             if(i==0)
               assertEquals("The first section has the wrong number of questions",5,totNoSec);
         }

         assertEquals("The questionBlock has the wrong number of questions",3,totNoBlock);
         assertEquals("The nr of questions in the dest file is wrong",8,totNoQ);
     }
     
}
