package visitors;

import data.Answer;
import data.Directive;
import data.ParsedSource;
import data.Question;
import data.QuestionBlock;
import data.Section;
import data.SourceItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import main.RandomSource;

/**
 * This class implements a visitor that will randomize the questions.
 * The visitor interface is Visitor.
 * The questions will be randomized. The questionbocks will be also randomized.
 * Inside the questionblocks, the questions will be also randomized.
 * It will also randomize the answers. 
 * The "main" method, ApplyTo will return a ParsedSource object with the questions 
 * randomized. All the original questions will be found in the returned object.
 * There is a special directive that could be specified to a question block, 
 * DoNotRandomize=1. This directive will prevent this class from changing the position
 * of the question block.
 * The DoNotRandomize=all will also maintain the order of the questions inside the question block and
 * the answer order in the questions.
 * @author visoft
 */
public class Randomizer implements Visitor {
    
    private ParsedSource dst;
    
/**
     * Test the current directive to see if DoNotRandomize is present
     * @param dir
     * @return 
     */
    private static boolean IsDoNotRandom(Directive dir){
        try{
            String val=null;
            val=dir.getMap().get("DoNotRandomize");
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
    
    private static boolean IsDoNotRandomAll(Directive dir){
        try{
            String val=null;
            val=dir.getMap().get("DoNotRandomize");
            if("ALL".compareToIgnoreCase(val)==0)
                return true;
        }
        catch(Exception ex){};
        return false;        
    }
    /**
     * randomize list of items. Those items that are DoNotRandom are left in place
     * The randomization happens in place.
     * @param items 
     */
    protected static void ShuffleItemList(ArrayList<SourceItem> items){
        ArrayList<Integer> toShuffle=new ArrayList<>(items.size());
        int[] isRandomizable=new int[items.size()];
        SourceItem[] origList=new SourceItem[items.size()];
        items.toArray(origList);
        
        for(int i=0;i<items.size();i++){
            if(IsDoNotRandom(items.get(i).getParameters())==false){
                toShuffle.add(i);
                isRandomizable[i]=1;
            }else
                isRandomizable[i]=0;
        }
        
        Collections.shuffle(toShuffle,RandomSource.get());
        
        int crtPos=0;
        //Apply randomization
        for(int i=0;i<items.size();i++){
            if(isRandomizable[i]==1){
                items.set(i, origList[toShuffle.get(crtPos++)]);
            }
        }
    }
    
    
    /**
     * Applies the randomization to a parsed source.
     * It will return the randomized source. 
     * @param src
     * @return 
     */
    public ParsedSource ApplyTo(ParsedSource src){
        int i,N;
        SourceItem item;
        
        dst=src.ShallowClone();

        ShuffleItemList(dst.getSubitems());
        for(SourceItem subitem:dst.getSubitems()){
            if(!IsDoNotRandomAll(subitem.getParameters()))
                subitem.accept(this);
        }
        
        return dst;
    }
    
    /**
     * Visitor for the question block. It will randomize the questions in place
     * It doesn't create a copy of the question block
     * @param item 
     */
    @Override
    public void visit(QuestionBlock item) {
        ShuffleItemList(item.getSubitems());
        for(SourceItem subitem:item.getSubitems()){
            if(!IsDoNotRandomAll(subitem.getParameters()))
                subitem.accept(this);
        }
    }
    /**
     * Randomizes the order of the answers in the question in place
     * Doesn't make a copy of the question
     * @param item 
     */
    @Override
    public void visit(Question item) {
        ShuffleItemList(item.getSubitems());
    }

    /**
     * Nothing to do with individual answer
     * @param item 
     */
    @Override
    public void visit(Answer item) {
        throw new UnsupportedOperationException("You should not call this visitor on an answer!");
    }

    @Override
    public void visit(Section item) {
        ShuffleItemList(item.getSubitems());
        for(SourceItem subitem:item.getSubitems()){
            if(!IsDoNotRandomAll(subitem.getParameters()))
                subitem.accept(this);
        }
    }
    
}
