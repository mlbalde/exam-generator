
package visitors;

import data.Answer;
import data.ParsedSource;
import data.Question;
import data.QuestionBlock;
import data.Section;
import data.SourceItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *Selects the desiredNoQ number of questions. 
 * The total number of q is specified by the <b>TotalCount</b> parameter.
 * The questions are randomly selected from the question blocks, and simple questions.
 * The questions have the same probability of being selected, regardless if they
 * are inside a question block or at the top level. 
 * The visitor recognize a special parameter, AlwaysSelectArr. This is applied for a 
 * question block and it means that the questions inside that block are selected by
 * default.
 * The applyTo creates a shallow copy of the questions and question blocks
 * 
 * In the end, all sections that are empty (either is a section or a questionblock) are removed
 * 
 * @author visoft
 */
public class SelectQuestions implements Visitor{
    
    private int[] MarkedForSelection;  //1 if select the i'th question
    private int crtQuestionNumber;       //current question counter.
    private ParsedSource dst;
    private SourceItem currentContainer;
    
    /**
     * Helper class that extracts questions included in a questionblock or section marked AlwaysSelect
     * This class also collects the total no of questions to be selected
     */
    public static class BuildQuestionList implements Visitor{
        
        public ArrayList<Integer> AlwaysSelectArr;
        public ArrayList<Integer> AvailableForSelection;
        public Integer AlwaysSelect;
        public Integer totalAlwaysSel;
        
        @Override
        public ParsedSource ApplyTo(ParsedSource src) {
            AlwaysSelectArr=new ArrayList<>();
            AvailableForSelection=new ArrayList<>();
            AlwaysSelect=0;
            totalAlwaysSel=0;
            for(SourceItem item:src.getSubitems())
                item.accept(this);
            return null;
        }
        
        public void SelAndQBVisit(SourceItem item){
            if(IsAlwaysSel(item))
                AlwaysSelect=1;
            int FixedNumberOfQuest=-1;
            int StartIndex=AvailableForSelection.size();
            int EndIndex;
            if(AlwaysSelect==0)
                FixedNumberOfQuest=IsQuestionsNumberSet(item);
            
            for(SourceItem subitem:item.getSubitems())
                subitem.accept(this);

            if(FixedNumberOfQuest>0){
                EndIndex=AvailableForSelection.size();
                totalAlwaysSel+=MarkFixedNumberOnArray(AvailableForSelection, AlwaysSelectArr, StartIndex, EndIndex, FixedNumberOfQuest);
            }
            
            if(IsAlwaysSel(item)) //Was set by me?
                AlwaysSelect=0;            
        }

        @Override
        public void visit(Section item) {
            try{
                SelAndQBVisit(item);
            }catch(RuntimeException ex){
                throw new RuntimeException("The current section cannot provide the desired number of questions");
            }
        }

        @Override
        public void visit(QuestionBlock item) {
            try{
                SelAndQBVisit(item);
            }catch(RuntimeException ex){
                throw new RuntimeException("The current question block cannot provide the desired number of questions");
            }

        }

        @Override
        public void visit(Question item) {
            AlwaysSelectArr.add(AlwaysSelect);
            totalAlwaysSel+=AlwaysSelect;
            AvailableForSelection.add((1+AlwaysSelect)%2);
        }

        @Override
        public void visit(Answer item) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    
    }
    /**
     * Marks with 0 the desiredNumber of items, starting with startIndex and ending with endIndex(exclusively)
     * Takes into account already marked items.
     * It also marks the elements in AlwaysSel array
     * @param array
     * @param startIndex
     * @param endIndex
     * @param desiredNumber 
     */
    private static int MarkFixedNumberOnArray(ArrayList<Integer> AvailableArray, ArrayList<Integer> AlwaysSelArray,int startIndex, int endIndex, int desiredNumber){
        if(desiredNumber==0)
            return 0;
        ArrayList<Integer> forSel=new ArrayList<>();
        int alreadySelectedCount=0;
        for(int i=startIndex;i<endIndex;i++){
            if(AvailableArray.get(i)==1)
                forSel.add(i);
            AvailableArray.set(i, 0);
            alreadySelectedCount+=AlwaysSelArray.get(i);
        }
        
        int avalilabeForSelCount=forSel.size();
        
        int diffToMark=desiredNumber-alreadySelectedCount;
        
        if(diffToMark<0)
            throw new RuntimeException("Values already filled over the desired number");
        if(diffToMark>avalilabeForSelCount)
            throw new RuntimeException("Not enough values");
        Collections.shuffle(forSel);
        int selected=0;
        for(int i=0;i<diffToMark;i++){
            AlwaysSelArray.set(forSel.get(i), 1);
            selected++;
        }
        return selected;
    }
    
    
    /**
     * Test the current QuestionBlock to see if directive AlwaysSelectArr is present
     * @param qb
     * @return 
     */
    private static boolean IsAlwaysSel(SourceItem item){
        try{
            String val=null;
            val=item.getParameters().getMap().get("AlwaysSelect");
            if(val==null)
                return false;
            if("NO".compareToIgnoreCase(val)==0)
                return false;
            if("0".compareToIgnoreCase(val)==0)
                return false;
        }
        catch(Exception ex){return false;};
        
        return true;
    }
    /**
     * Returns the nubmer of questions fixed by the parameters.
     * If the number is not set, or is auto, will return -1
     * @param item
     * @return 
     */
    private static int IsQuestionsNumberSet(SourceItem item){
        try{
            String val=null;
            val=item.getParameters().getMap().get("questions");
            if(val==null)
                return -1;
            int i=Integer.parseInt(val);
            if(i>0)
                return i;
        }
        catch(Exception ex){return -1;};
        
        return -1;
        
    }
    
    /**
     * Visits a question block.
     * Creates a copy of the info in the block.
     * Adds it to the destination.
     * Visits each question.
     * If no questions were added during visits, the newly added
     * question block will be deleted.
     * 
     * 
     * @param item 
     */
    @Override
    public void visit(QuestionBlock item) {
        QuestionBlock newBlock=item.ShallowClone();
        newBlock.getSubitems().clear();
        SourceItem oldContainer=currentContainer;
        
        for(SourceItem subitem:item.getSubitems()){
            currentContainer=newBlock;
            subitem.accept(this);
       }       
       if(newBlock.getSubitems().size()>0){
           //Some elements were added, adding the block to the destination
           oldContainer.getSubitems().add(newBlock);
       }
           
    }

    @Override
    public void visit(Question item) {
        if(MarkedForSelection[crtQuestionNumber]==1){
            currentContainer.getSubitems().add(item.ShallowClone());
        }
        crtQuestionNumber++;
    }

    @Override
    public void visit(Answer item) {
        throw new UnsupportedOperationException("No business here!");
    }
    /**
     *Randomly selects questions, taking into account the AlwaysSelect parameter.
     * 
     * Performs a shallow copy of the source
     * 
     * @param src
     * @return 
     */
    @Override
    public ParsedSource ApplyTo(ParsedSource src) {
        int totalSelectableQuestions,desiredNoQ,totalMarkedAlwaysSel,i,finalNumberOfQuestions=0;
        int totalQuestionCount;
        
        
        BuildQuestionList questionList=new BuildQuestionList();
        questionList.ApplyTo(src);
        
        totalSelectableQuestions=0;
        totalMarkedAlwaysSel=questionList.totalAlwaysSel;
        totalQuestionCount=questionList.AlwaysSelectArr.size();
        
        for(i=0;i<questionList.AvailableForSelection.size();i++)
            if(questionList.AvailableForSelection.get(i)==1)
                totalSelectableQuestions++;
        
        
        String val;
        desiredNoQ=totalSelectableQuestions+totalMarkedAlwaysSel; //Simulate the "auto" effect
        try{
        val=src.getParameters().getMap().get("TotalCount");
        if(val!=null)
            desiredNoQ=Integer.parseInt(val);
        }catch(Exception ex){}

        MarkedForSelection=new int[totalQuestionCount];
            
        //see if it's possible to select desiredNoQ number of questions
        if(desiredNoQ<totalMarkedAlwaysSel)
            throw new RuntimeException("The desired no of questions is less than"
                    + "the no of questions that are marked to be always selected");

        finalNumberOfQuestions=desiredNoQ;
        if(totalSelectableQuestions+totalMarkedAlwaysSel<finalNumberOfQuestions)
            throw new RuntimeException("There are not enough questions in the "
                    + "source to generate desired no of questions");

        MarkFixedNumberOnArray(questionList.AvailableForSelection, questionList.AlwaysSelectArr, 0, questionList.AvailableForSelection.size(), finalNumberOfQuestions);

        for(i=0;i<totalQuestionCount;i++)
            MarkedForSelection[i]=questionList.AlwaysSelectArr.get(i);
        
       //Copy the source in dst
       dst=src.ShallowClone();
       dst.getSubitems().clear();
       
       crtQuestionNumber=0; //Current question is set to 0
       
       //Visit each item and set the questions
       //Remove questions that will not be selected
       for(SourceItem item:src.getSubitems()){
           currentContainer=dst;
           item.accept(this);
       }
       
       return dst;
        
    }

    @Override
    public void visit(Section item) {
        Section newsect=item.ShallowClone();
        newsect.getSubitems().clear();
        SourceItem oldContainer=currentContainer;
        
        for(SourceItem subitem:item.getSubitems()){
            currentContainer=newsect;
            subitem.accept(this);
       }
       if(newsect.getSubitems().size()>0){
           //There were some items added, adding to the parent container
          oldContainer.getSubitems().add(newsect);
       }
           
    }
    
}
