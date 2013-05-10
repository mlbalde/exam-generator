package visitors;

import data.Answer;
import data.Directive;
import data.ParsedSource;
import data.Question;
import data.QuestionBlock;
import data.Section;
import data.SourceItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;
import main.RandomSource;

/**
 * Class that selects the answers in the questions according to specifications.
 * The parameters are read from the specs field of the ParsedSource.
 * AnswersPerQestion == how many answers each question will have
 * MaxTrue,MinTrue == max and min number of true answers that each question will have. <br/>
 * 
 * 
 * It generates a new ParsedSource object with the new content. Shallow copies where 
 * it can. New questions are added and the original ones are removed (where is the case)
 * It takes every question and tries to generate the max number of questions respecting
 * the restrictions.
 * It ensures that the MaxTrue and MinTrue conditions are fulfilled. The selection
 * is performed at random.
 *  <p/>For a question, the algorithm is following:
 * -select true and false questions into different arrays <br/> 
 * -shuffle the arrays. <br/>
 * -generate a random number a between min and max <br/>
 * -select first true a answers <br/>
 * -select first false AnswersPerQuestion-a answers <br/>
 * If this cannot be accomplished, a log entry should be added <br/>
 * If the current question respects all the criteria (exact AnswersPerQuestion answers,
 * MaxTrue and MinTure fulfilled) the question is added without processing
 * Continue to the next question. 
 * 
 * <p/>
 * Implements the visitor pattern.
 * 
 * @author visoft
 */
public class DivideBigQuestions implements Visitor{

    private ParsedSource dest;
    private int AnswersPerQuestion,MaxTrue,MinTrue;
    private static Logger log=Logger.getLogger(DivideBigQuestions.class.getName());
    
    private SourceItem currentContainer;//reference to the current container, where the generated items will be inserted
    private int TotNoQuest;
    
    private int CrtFlatQuestNo;//current "flat" question number. That is, the current question no, regardless of questionblocks.
     
    /**
     *  Generate desired number of questions from source question Src.<br/>
     * -Collects the indexes of true and false answers <br/>
     * -Distributes for each question minimum number of true and false questions <br/>
     * -The rest of the answers are collected and are randomly distributed among the questions.<br/>
     * 
     * The algorithm is described in the class documentation.
     * @param Src - source question, containing the answers.
     * @param NoAns - No of answers per question
     * @param minT - min nr of Ture answers for each question
     * @param maxT - max nr of true answers for each question
     * @return 
     */
    protected static ArrayList<Question> generateQuestions(Question Src, int NoAns, int minT, int maxT, int CrtFlatQuestNo){
        int N,i,j;
        int crtT,crtF,crtR;
        Question q;
        N=Src.getSubitems().size();
        ArrayList<Integer> trueidx,falseidx,restidx;
        //Collect the true and false indexex
        trueidx=new ArrayList<>(N);
        falseidx=new ArrayList<>(N);
        restidx=new ArrayList<>(N);
        for(i=0;i<Src.getSubitems().size();i++){
            if(((Answer)Src.getSubitems().get(i)).getCorrect())
                trueidx.add(i);
            else
                falseidx.add(i);
        }
        //Shuffle the arrays
        Collections.shuffle(trueidx,RandomSource.get());
        Collections.shuffle(falseidx,RandomSource.get());
        
        int nrComputableQuestions;//max number of questions that can be computed
        nrComputableQuestions=getMaxNoOfQuestions(Src, NoAns, minT, maxT);
            
        ArrayList<Question> questList=new ArrayList<>(nrComputableQuestions);
        crtT=0;
        crtF=0;
        for(i=0;i<nrComputableQuestions;i++){
            q=new Question();
            q.setTextContent(Src.getTextContent());
            questList.add(q);
            for(j=0;j<minT;j++)
                q.getSubitems().add(Src.getSubitems().get(trueidx.get(crtT++)));
            for(j=0;j<NoAns-maxT;j++)
                q.getSubitems().add(Src.getSubitems().get(falseidx.get(crtF++)));
        }
        //Add the rest of the answers to restidx and shuflle it
        for(j=crtT;j<trueidx.size();j++)
            restidx.add(trueidx.get(j));
        for(j=crtF;j<falseidx.size();j++)
            restidx.add(falseidx.get(j));
        Collections.shuffle(restidx,RandomSource.get());
        //Put the rest of the answers in the questions
        crtR=0;
        for(i=0;i<questList.size();i++){
            q=questList.get(i);
            for(j=q.getSubitems().size();j<NoAns;j++)
                q.getSubitems().add(Src.getSubitems().get(restidx.get(crtR++)));
        }
        
        return questList;
    }
    /**
     * Computes the maximum number of questions that can be generated for this src question.
     * Takes into account the number of true, false answers and the min/max true answers per question
     * @param Src
     * @param NoAns
     * @param minT
     * @param maxT
     * @return 
     */
    protected static int getMaxNoOfQuestions(Question Src, int NoAns, int minT, int maxT){
        int m1,m2,m3;
        int N,ta,fa;

        m1=0;m2=0;m3=0;
        ta=0;fa=0;//true and false number of answers
        N=Src.getSubitems().size();
        for(SourceItem ans:Src.getSubitems())
            if(((Answer)ans).getCorrect())ta++;
                else    fa++;

        //Testing the parameters to get rid of the nonsense
        if(NoAns<=0)
            NoAns=1;
        m1=N/NoAns; //how many questions can be built with these answers?
        if(minT>0)
            m2=ta/minT;//how many quest can be built with the number of true answers?
        else
            m2=N;
        if(NoAns-maxT>0)
            m3=fa/(NoAns-maxT);//how many quest can be built with the number of false answers?
        else
            m3=N;
        
        m1=Math.min(m1, m2);
        m1=Math.min(m1, m3);
        
        return m1;
    }

              
    
    public DivideBigQuestions() {
    }
    
    /**
     * Apply the selection to source.
     * Returns the selected questions with desired number of answers.
     * The mechanism of selection is described in the class description
     * 
     * @param source
     * @return 
     */
    @Override
    public ParsedSource ApplyTo(ParsedSource source){
       Directive dir=source.getParameters();
       String param;
       param=dir.getMap().get("AnswersPerQestion");
       AnswersPerQuestion=5;//default
       try{
           AnswersPerQuestion=Integer.parseInt(param);
       }catch(Exception e){  };
       
       param=dir.getMap().get("MaxTrue");
       MaxTrue=4;//default
       try{
           MaxTrue=Integer.parseInt(param);
       }catch(Exception e){  };

       param=dir.getMap().get("MinTrue");
       MinTrue=1;//default
       try{
           MinTrue=Integer.parseInt(param);
       }catch(Exception e){  };
       
       //Copy some data from the source
       dest=source.ShallowClone();
       dest.getSubitems().clear();
       
       //Visit each item and set the questions
       TotNoQuest=0;
       CrtFlatQuestNo=0;
       
       for(SourceItem item:source.getSubitems()){
           currentContainer=dest;
           item.accept(this);
       }
       log.info("SelectAnswers generated a total of "+TotNoQuest+" questions");
       return dest;
    }        
    
    @Override
    public void visit(QuestionBlock item) {
        QuestionBlock newBlock=item.ShallowClone();
        newBlock.getSubitems().clear();
        currentContainer.getSubitems().add(newBlock);
        
        for(SourceItem subitem:item.getSubitems()){
            currentContainer=newBlock;
            subitem.accept(this);
       }
    }
    /**
     * Generate a question from a simple question.
     * Uses selection and the procedure described in the class description
     * @param item 
     */
    @Override
    public void visit(Question item) {
        ArrayList<Question> quest;
        //test to see if its possible
        int maxNoOfSpawnedQuestions=getMaxNoOfQuestions(item, AnswersPerQuestion, MinTrue, MaxTrue);
        
        if(maxNoOfSpawnedQuestions<1){
            log.info("Current simple question (0 based):" + CrtFlatQuestNo + " cannot generate the desired number of answers. Skipping");
            return;
        }
        if(maxNoOfSpawnedQuestions==1){
            currentContainer.getSubitems().add(item.ShallowClone());
            TotNoQuest++;
            CrtFlatQuestNo++;
            return;
        }
        
        quest=generateQuestions(item, AnswersPerQuestion, MinTrue, MaxTrue,CrtFlatQuestNo);
        currentContainer.getSubitems().addAll(quest);
        TotNoQuest+=quest.size();
        CrtFlatQuestNo+=quest.size();
    }

    @Override
    public void visit(Answer item) {
        throw new UnsupportedOperationException("You should not call this visitor on an answer!");
    }

    @Override
    public void visit(Section item) {
        Section newsect=item.ShallowClone();
        newsect.getSubitems().clear();
        currentContainer.getSubitems().add(newsect);
        
        for(SourceItem subitem:item.getSubitems()){
            currentContainer=newsect;
            subitem.accept(this);
       }
    }
}
