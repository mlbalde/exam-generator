package data;

import java.util.ArrayList;
import visitors.Visitor;

/**
 * Keeps a question block. That is a block of text followed by several questions that must be kept together.
 * @author visoft
 */
public class QuestionBlock extends SourceItem{

    public QuestionBlock(String textContent, ArrayList<? extends SourceItem> subitems, Directive parameters) {
        super(textContent, subitems, parameters);
    }

    public QuestionBlock() {
        this("",null,null);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public QuestionBlock DeepClone() {
        QuestionBlock dst=new QuestionBlock();
        copyContentDeep(this, dst);
        return dst;
    }
    
    public Question getQuestionAt(int k){
        return (Question)subitems.get(k);
    }

    @Override
    public QuestionBlock ShallowClone() {
        QuestionBlock dst=new QuestionBlock();
        copyContentShallow(this, dst);
        return dst;
    }
    
}
