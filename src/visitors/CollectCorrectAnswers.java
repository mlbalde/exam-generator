package visitors;

import data.Answer;
import data.ParsedSource;
import data.Question;
import data.QuestionBlock;
import data.Section;
import data.SourceItem;
import java.util.ArrayList;

/**
 * Takes a ParsedSource object and extracts for each question the question number
 * and the correct answers. The answers will be written in a String in the
 * following manner:
 * Question No  Ans correct 1  Ans Correct 2 etc.
 * The questions and answers are 1 based.
 * Ex:
 * 1 1 3
 * 2 3 4
 * This means that the question 1 has correct answers A and C, the question 2 
 * has C and D correct answers.
 * @author visoft
 */
public class CollectCorrectAnswers implements Visitor {
    
    private ArrayList<Integer> crtAnswer;//Stores the correct answers to the current question
                                         //in the first position is the question number
    private int crtAnsNo;//current index of answer that is examined.  1 based!
    private ArrayList<ArrayList<Integer>> Answers; //table containing the question number and the answers
    private int crtQuestionNo;// id of the current question. 1 based!

    public CollectCorrectAnswers() {
        Answers=null;
        crtAnswer=null;
    }
        
    @Override
    public ParsedSource ApplyTo(ParsedSource src) {
        if(src==null)
            return null;
        //Initialize the arrays
        Answers=new ArrayList<>();
        crtQuestionNo=1;
        for(SourceItem item:src.getSubitems())     
                   item.accept(this);        
        return src;
    }

    @Override
    public void visit(QuestionBlock item) {
        for(SourceItem quest:item.getSubitems())     
            quest.accept(this);
    }

    @Override
    public void visit(Question item) {
        crtAnswer=new ArrayList<>();
        crtAnswer.add(crtQuestionNo);
        crtAnsNo=1;
        for(SourceItem ans:item.getSubitems())     
            ans.accept(this);
        Answers.add(crtAnswer);
        crtQuestionNo++;
    }

    @Override
    public void visit(Answer item) {
        if(item.getCorrect())
            crtAnswer.add(crtAnsNo);
        crtAnsNo++;
    }
    /**
     * Retrieves the content of the answer file
     * @return 
     */
    public String getContent(){
        if(Answers==null)
            return "";
        StringBuilder content=new StringBuilder();
        for(ArrayList<Integer> line:Answers){
            for(Integer i:line)
                content.append(i).append(" ");
            content.append("\n");
        }
        return content.toString();            
    }

    @Override
    public void visit(Section item) {
        for(SourceItem quest:item.getSubitems())     
            quest.accept(this);
    }
}
