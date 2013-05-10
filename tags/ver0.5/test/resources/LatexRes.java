/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import data.Answer;
import data.ParsedSource;
import data.Question;
import data.QuestionBlock;
import data.Section;
import data.SourceItem;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Resources regarding Latex
 * @author visoft
 */
public class LatexRes {
    /**
     * Generate a ParsedSource consisting of a simple header, two questions and one block with two questions.
     * The footer is just a document{end} tag.
     * @return 
     */
    public static ParsedSource GenSimpleParsedSrc()
    {
        ParsedSource ps=new ParsedSource();
        
        Answer[] aa={new Answer("Answer 1", false,null),new Answer("Answer 2", true,null),new Answer("Answer 3", false,null)};
        ArrayList<SourceItem> ans=new ArrayList<SourceItem>(Arrays.asList(aa));
        
        Question q1=new Question("Question 1",ans,null);
        ans.add(new Answer("Answer 4", Boolean.TRUE,null));
        Question q2=new Question("Question 2",ans,null);
        
        QuestionBlock qb=new QuestionBlock();
        qb.setTextContent("Quest Block");
        qb.getParameters().getMap().put("questions", "1");
        
        
        ps.setHeader("\\documentclass{article}\n\\usepackage{amsmath}\n\\usepackage{amsfonts}\n\\usepackage{amssymb}\n\\usepackage{graphicx}\n\\begin{document}\n"
                + "Some sample document\n"
                + "\\begin{enumerate}\n");
        ps.getSubitems().add(q1);
        ps.getSubitems().add(q2);
        
        q1=new Question("block Question 1",ans,null);
        q2=new Question("block Question 2",ans,null);
        qb.getSubitems().add(q1);
        qb.getSubitems().add(q2);
        
        ps.getSubitems().add(qb);
        ps.setFooter("\\end{enumerate}\n\\end{document}");
        
        return ps;
    }
    /**
     * Generates a big question having N answers from which T are true, rest are false.
     * 
     * @param N
     * @param T
     
     */
    public static Question GenerateBigQuestion(int N, int T) {
        Question q=new Question();
        int F,i;
        F=N-T;
        int k=0;
        for(i=0;i<T;i++)
            q.getSubitems().add(new Answer("Answer no "+(k++), Boolean.TRUE,null));
        for(i=0;i<F;i++)
            q.getSubitems().add(new Answer("Answer no "+(k++), Boolean.FALSE,null));
        return q;
    }

    public static Question GenerateBigQuestion(int N, int T,String text) {
        Question q=GenerateBigQuestion(N, T);
        q.setTextContent(text);
        return q;
    }

    
    public static Reader ReadResource(String ResourceName){
        try {
            InputStream istr=LatexRes.class.getResourceAsStream(ResourceName);
            return new InputStreamReader(istr);
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }
    
    
    public static QuestionBlock generateQuestionBlock(int NrQuestions, int N, int T,String qbText){
        QuestionBlock qb;
        qb=new QuestionBlock();
        qb.setTextContent(qbText);
        
        for(int i=0;i<NrQuestions;i++){
            Question q=GenerateBigQuestion(N, T);
            qb.getSubitems().add(q);
        }
        
        return qb;
    }
    
    public static ParsedSource generateBareParsedSr(){
        ParsedSource src;
        src=new ParsedSource();
        src.setHeader("Some header");
        src.setFooter("Some footer");
        
        return src;
    }
    public static ParsedSource generateSomeSectionSrc(){
        ParsedSource src=generateBareParsedSr();
        Section sec;
        sec=new Section("{Section 1}", "[SomeEnumParams]", null, null);
        sec.getSubitems().add(GenerateBigQuestion(5, 3,"1"));
        sec.getSubitems().add(GenerateBigQuestion(5, 3,"2"));
        sec.getSubitems().add(GenerateBigQuestion(5, 3,"3"));
        sec.getSubitems().add(GenerateBigQuestion(5, 3,"4"));
        sec.getSubitems().add(GenerateBigQuestion(5, 3,"5"));
        src.getSubitems().add(sec);
        sec=new Section("{Section 2}", "[SomeEnum]", null, null);
        sec.getSubitems().add(GenerateBigQuestion(5, 3,"6"));
        sec.getSubitems().add(GenerateBigQuestion(5, 3,"7"));
        sec.getSubitems().add(GenerateBigQuestion(5, 3,"8"));
        sec.getSubitems().add(GenerateBigQuestion(5, 3,"9"));
        sec.getSubitems().add(GenerateBigQuestion(5, 3,"10"));
        src.getSubitems().add(sec);
        
        return src;
    }
    
    public static ParsedSource generate4SectionSrc(){
         ParsedSource src=generateBareParsedSr();
         Section sec=new Section();
         sec.setTextContent("{Section 1}\nand some content\n");
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
         sec.getSubitems().add(qb);         
         
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         
         src.getSubitems().add(sec);
         
         sec=new Section();
         sec.setTextContent("{Section 2}\nand some content\n");
         sec.setEnumerateOptions("[enum options]\nand some text after that\n");
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         src.getSubitems().add(sec);
         
         sec=new Section();
         sec.setTextContent("{Section 3}\nand some content\n");
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         sec.getSubitems().add(GenerateBigQuestion(5, 3));
         src.getSubitems().add(sec);

         sec=new Section();
         sec.setTextContent("{Section 4}\nand some content\n");
         sec.getSubitems().add(generateQuestionBlock(9, 5, 3, "\nQB 1\n"));
         sec.getSubitems().add(generateQuestionBlock(3, 5, 3, "\nQB 2\n"));
         sec.getSubitems().add(generateQuestionBlock(7, 5, 3, "\nQB 3\n"));
         src.getSubitems().add(sec);
         
         return src;
    }
    
}


