/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visitors;

import data.Answer;
import data.ParsedSource;
import data.Question;
import data.QuestionBlock;
import data.Section;
import data.SourceItem;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
import static resources.LatexRes.*;

/**
 * 
 * @author visoft
 */
public class RandomizerTest {
    
    public RandomizerTest() {
    }
    /**
     * Test to see if the questions that must be moved are moved,
     * and those who don't, don't.
     * 
     * There are 6! permutations.
     * The chances that this test fails is 30/720=0.04
     * The test fails when shuffle gives the identical permutation
     * 
     */
    @Test
    public void testRndQuest() {
        ParsedSource ps;
        ParsedSource rez;
        Randomizer rnd=new Randomizer();
        int i,j;
        
        QuestionBlock qb;
        Question q;
        for(int k=0;k<30;k++){  
            ps=new ParsedSource();
            q=GenerateBigQuestion(5,3);q.setTextContent("1");ps.getSubitems().add(q);
            q=GenerateBigQuestion(5,3);q.setTextContent("2");ps.getSubitems().add(q);
            q=GenerateBigQuestion(5,3);q.setTextContent("3");ps.getSubitems().add(q);

            qb=new QuestionBlock();
            qb.getParameters().getMap().put("DoNotRandomize", "all");
            q=GenerateBigQuestion(5,3);q.setTextContent("4");qb.getSubitems().add(q);
            q=GenerateBigQuestion(5,3);q.setTextContent("5");qb.getSubitems().add(q);
            ps.getSubitems().add(qb);
            
            q=GenerateBigQuestion(5,3);q.setTextContent("6");ps.getSubitems().add(q);
            q=GenerateBigQuestion(5,3);q.setTextContent("7");ps.getSubitems().add(q);
            qb=new QuestionBlock();
            q=GenerateBigQuestion(5,3);q.setTextContent("8");qb.getSubitems().add(q);
            q=GenerateBigQuestion(5,3);q.setTextContent("9");qb.getSubitems().add(q);
            ps.getSubitems().add(qb);
            
            rez=rnd.ApplyTo(ps);

            assertEquals("The no of selected questions is wrong",7,rez.getSubitems().size());

            qb=(QuestionBlock)rez.getSubitems().get(3);
            i=Integer.parseInt(qb.getSubitems().get(0).getTextContent());
            assertEquals("Question 4 is moved",4,i);
            i=Integer.parseInt(qb.getSubitems().get(1).getTextContent());
            assertEquals("Question 5 is moved",5,i);
            
            ArrayList<Integer> tab=new ArrayList<>();
            
            for(i=0;i<rez.getSubitems().size();i++){
                if(i==3)continue;//This should stay in place! It was tested earlier!
                SourceItem it=rez.getSubitems().get(i);
                if(it.getClass()==QuestionBlock.class){
                    for(SourceItem qq:((QuestionBlock)it).getSubitems())
                        tab.add(Integer.parseInt(qq.getTextContent()));
                    continue;
                }
                tab.add(Integer.parseInt(it.getTextContent()));
            }
            
            boolean moved=false;
            for(i=0;i<tab.size()-1;i++)
                if(tab.get(i)>tab.get(i+1)){
                    moved=true;
                    break;
                }
            assertTrue("No question has changed its place!",moved);
        
        }//end for k tests
    }
    /**
     * Test to see if the answers are moved.
     * Runned 10 times on 5 answers. It will fail with p=5/10!
     */
    @Test
    public void testRndAns() {
        ParsedSource ps=new ParsedSource();
        ParsedSource rez;
        Randomizer rnd=new Randomizer();
        int NoA=5;
        for(int k=0;k<10;k++){
            Question q=new Question();
            for(int i=0;i<NoA;i++)
                q.getSubitems().add(new Answer(Integer.toString(i), Boolean.TRUE,null));
            ps.getSubitems().add(q);

            rez=rnd.ApplyTo(ps);

            int[] tab=new int[NoA];
            q=(Question)rez.getSubitems().get(0);
            for(int i=0;i<q.getSubitems().size();i++)
                tab[i]=Integer.parseInt(q.getSubitems().get(i).getTextContent());
            int moved=0;
            for(int i=0;i<NoA-1;i++)
                if(tab[i]!=i){
                    moved++;
                }
            assertTrue("Not enough answers were moved: "+moved,moved>=1);
        }
    }
    /**
     * Do not randomize should prevent the answers of being randomized
     */
    
    @Test
    public void testRndAnsNoRandomize() {
        ParsedSource ps=new ParsedSource();
        ParsedSource rez;
        Randomizer rnd=new Randomizer();
        int NoA=10;
        for(int k=0;k<10;k++){
            Question q=new Question();
            for(int i=0;i<NoA;i++)
                q.getSubitems().add(new Answer(Integer.toString(i), Boolean.TRUE,null));
            ps.getSubitems().add(q);
            
            QuestionBlock blk=new QuestionBlock();
            blk.getParameters().getMap().put("DoNotRandomize", "all");
            q=new Question();
            q.getSubitems().add(new Answer("1", Boolean.TRUE,null));
            q.getSubitems().add(new Answer("2", Boolean.TRUE,null));
            q.getSubitems().add(new Answer("3", Boolean.TRUE,null));
            q.getSubitems().add(new Answer("4", Boolean.TRUE,null));
            q.getSubitems().add(new Answer("5", Boolean.FALSE,null));
            blk.getSubitems().add(q);
           
            ps.getSubitems().add(blk);
            
            rez=rnd.ApplyTo(ps);

            int[] tab=new int[5];
            blk=(QuestionBlock)rez.getSubitems().get(1);
            q=blk.getQuestionAt(0);
            for(int i=0;i<q.getSubitems().size();i++)
                tab[i]=Integer.parseInt(q.getSubitems().get(i).getTextContent());
            boolean moved=false;
            for(int i=0;i<4;i++)
                if(tab[i]>tab[i+1]){
                    moved=true;
                    break;
                }
            assertTrue("The answers were moved despite DoNotRandomize",!moved);
        }
    }
    /**
     * TEst to see if inside a questionblock the answers are randomized
     */
    @Test
    public void testIfRndBlock(){
        ParsedSource ps=new ParsedSource();
        ParsedSource rez;
        Randomizer rnd=new Randomizer();
        int NoA=10;        
        for(int k=0;k<10;k++){
            QuestionBlock qb=new QuestionBlock();
            Question q=new Question();
            for(int i=0;i<NoA;i++)
                q.getSubitems().add(new Answer(Integer.toString(i), Boolean.TRUE,null));
            qb.getSubitems().add(q);
            ps.getSubitems().add(qb);
            
            rez=rnd.ApplyTo(ps);

            int[] tab=new int[NoA];
            q=((QuestionBlock)rez.getSubitems().get(0)).getQuestionAt(0);
            for(int i=0;i<q.getSubitems().size();i++)
                tab[i]=Integer.parseInt(q.getSubitems().get(i).getTextContent());
            int moved=0;
            for(int i=0;i<NoA-1;i++)
                if(tab[i]!=i){
                    moved++;
                }
            assertTrue("Not enough answers were moved: "+moved,moved>=2);
        }        
        
    }
    
    @Test
    public void testIfRndIsOne(){
        ParsedSource ps;
        ParsedSource rez;
        Randomizer rnd=new Randomizer();
        int i,j,k;
        
        QuestionBlock qb;
        Question q;
        for(int kiter=0;kiter<5;kiter++){  
            ps=new ParsedSource();
            q=GenerateBigQuestion(5,3);q.setTextContent("1");ps.getSubitems().add(q);
            q=GenerateBigQuestion(5,3);q.setTextContent("2");ps.getSubitems().add(q);
            q=GenerateBigQuestion(5,3);q.setTextContent("3");ps.getSubitems().add(q);

            qb=new QuestionBlock();
            qb.getParameters().getMap().put("DoNotRandomize", "1");
            qb.setTextContent("BQ3");
            q=GenerateBigQuestion(5,3);q.setTextContent("4");qb.getSubitems().add(q);
            q=GenerateBigQuestion(5,3);q.setTextContent("5");qb.getSubitems().add(q);
            q=GenerateBigQuestion(5,3);q.setTextContent("6");qb.getSubitems().add(q);
            q=GenerateBigQuestion(5,3);q.setTextContent("7");qb.getSubitems().add(q);
            ps.getSubitems().add(qb);
            
            qb=new QuestionBlock();
            q=GenerateBigQuestion(5,3);q.setTextContent("8");qb.getSubitems().add(q);
            q=GenerateBigQuestion(5,3);q.setTextContent("9");qb.getSubitems().add(q);
            q=GenerateBigQuestion(5,3);q.setTextContent("10");qb.getSubitems().add(q);
            q=GenerateBigQuestion(5,3);q.setTextContent("11");qb.getSubitems().add(q);
            q=GenerateBigQuestion(5,3);q.setTextContent("12");qb.getSubitems().add(q);
            ps.getSubitems().add(qb);
            
            rez=rnd.ApplyTo(ps);

            assertEquals("The no of selected items is wrong",5,rez.getSubitems().size());

            qb=(QuestionBlock)rez.getSubitems().get(3);
            boolean moved=false;
            for(i=0;i<qb.getSubitems().size();i++){
                k=Integer.parseInt(qb.getSubitems().get(i).getTextContent());
                if(k!=i+4){
                    moved=true;
                    break;
                }
            }
            assertTrue("At least one question in questionblock 3 should be moved!",moved);
            
            ArrayList<Integer> tab=new ArrayList<>();
            
            for(i=0;i<rez.getSubitems().size();i++){
                SourceItem it=rez.getSubitems().get(i);
                if(it.getClass()==QuestionBlock.class){
                    for(SourceItem qq:((QuestionBlock)it).getSubitems())
                        tab.add(Integer.parseInt(qq.getTextContent()));
                    continue;
                }
                tab.add(Integer.parseInt(it.getTextContent()));
            }
            
            moved=false;
            for(i=0;i<tab.size()-1;i++)
                if(tab.get(i)>tab.get(i+1)){
                    moved=true;
                    break;
                }
            
            assertTrue("No question has changed its place!",moved);
        
        }//end for k tests        
        
        
        
    }
    
    @Test
    public void testDoNotRandSection_1(){
        ParsedSource src=generateSomeSectionSrc();
        ParsedSource dest;
        Section sec=(Section)src.getSubitems().get(0);
        sec.getParameters().put("DoNotRandomize", "1");
        
        Randomizer rnd=new Randomizer();
        dest=rnd.ApplyTo(src);
        //Check that the section 1 is always in the same place
        sec=(Section)dest.getSubitems().get(0);
        assertTrue("The first section was randomized",sec.getTextContent().contains("Section 1"));
        
        boolean moved=false;
        
        //Test the order of the questions and the answers inside them. See if they are all the same
        for(int i=0;i<sec.getSubitems().size();i++){
            Question q=(Question)sec.getSubitems().get(i);
            if(!q.getTextContent().contains(""+(i+1))){
                moved=true;
                break;
            }
            for(int j=0;j<q.getSubitems().size();j++){
                Answer a=q.getAnswerAt(j);
                if(!a.getTextContent().contains(""+j)){
                    moved=true;
                    break;
                }
            }
        }        
        assertTrue("No questions were randomized!",moved);
    }
    
    @Test
    public void testDoNotRandSection_all(){
        ParsedSource src=generateSomeSectionSrc();
        ParsedSource dest;
        Section sec=(Section)src.getSubitems().get(0);
        sec.getParameters().put("DoNotRandomize", "All");
        
        Randomizer rnd=new Randomizer();
        dest=rnd.ApplyTo(src);
        //Check that the section 1 is always in the same place
        sec=(Section)dest.getSubitems().get(0);
        assertTrue("The first section was randomized",sec.getTextContent().contains("Section 1"));
        
        //Test the order of the questions and the answers inside them
        for(int i=0;i<sec.getSubitems().size();i++){
            Question q=(Question)sec.getSubitems().get(i);
            assertTrue("The question "+i+" was randomized",q.getTextContent().contains(""+(i+1)));
            for(int j=0;j<q.getSubitems().size();j++){
                Answer a=q.getAnswerAt(j);
                assertTrue("The answer at question "+i+" was randomized",a.getTextContent().contains(""+j));
            }
        }
        
    }
    
}

