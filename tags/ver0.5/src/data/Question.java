package data;

import java.util.ArrayList;
import visitors.Visitor;


/**
 * Keeps track of a question. That means question text and the choices.
 * @author visoft
 */
public class Question extends SourceItem {

    public Question(String textContent, ArrayList<? extends SourceItem> subitems, Directive parameters) {
        super(textContent, subitems, parameters);
    }

    public Question() {
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
    public Question DeepClone() {
        Question dst=new Question();
        copyContentDeep(this, dst);
        return dst;        
    }
    
    public Answer getAnswerAt(int k){
        return (Answer) subitems.get(k);
    }

    @Override
    public Question ShallowClone() {
        Question dst=new Question();
        copyContentShallow(this, dst);
        return dst;        
    }
}
