package data;

import java.util.ArrayList;
import visitors.Visitor;
import java.util.Objects;

/**
 * Class that keeps track of an individual choice. Keeps track of the text and if
 * this is a true or false answer
 * @author visoft
 */
public class Answer extends SourceItem{
    protected Boolean correct;

    public Answer(String textContent, Boolean correct, Directive parameters) {
        super(textContent, null, parameters);
        this.correct = correct;
    }

    public Answer() {
        this("", false,null);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.correct);
        hash = 17 * hash + Objects.hashCode(this.textContent);
        hash = 17 * hash + Objects.hashCode(this.directives);        
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Answer other = (Answer) obj;
        if (!Objects.equals(this.correct, other.correct)) {
            return false;
        }
        if (!Objects.equals(this.textContent, other.textContent)) {
            return false;
        }
        if (!Objects.equals(this.directives, other.directives)) {
            return false;
        }        
        return true;
    }

    /**
     * Returns true if the answer is supposed to be a correct answer
     * @return 
     */
    public Boolean getCorrect() {
        return correct;
    }
    /**
     * Sets if this answer is correct or not
     * @param correct 
     */
    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }
    
    /**
     * Accept visit, from visitor pattern
     * @param visitor 
     */
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
    /**
     * Deep clone this object
     * @return 
     */
    @Override
    public Answer DeepClone() {
        Answer ans=new Answer(textContent, correct, directives);
        return ans;
    }

    @Override
    public Answer ShallowClone() {
        return DeepClone();
    }
  }
