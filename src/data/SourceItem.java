package data;

import java.util.ArrayList;
import java.util.Objects;
import visitors.Visitor;

/**
 * Class that is the ancestor of all the components of an exam sheet.
 * 
 * Contains some text content and a list of subitems.
 * It also tracks a possible list of directives.
 * @author visoft
 */
public abstract class SourceItem implements Visitable{
    protected String textContent;
    protected ArrayList<SourceItem> subitems;
     protected Directive directives;
    
    
    public SourceItem(String textContent,ArrayList<? extends SourceItem> subitems,Directive parameters) {
        this.textContent = textContent;
        
        this.subitems=new ArrayList<>();
        if(subitems!=null)
            this.subitems.addAll(subitems);
        if(parameters==null)
            parameters=new Directive();        
        this.directives=parameters;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.textContent);
        hash = 37 * hash + Objects.hashCode(this.subitems);
        hash = 37 * hash + Objects.hashCode(this.directives);
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
        final SourceItem other = (SourceItem) obj;
        if (!Objects.equals(this.textContent, other.textContent)) {
            return false;
        }
        if (!Objects.equals(this.subitems, other.subitems)) {
            return false;
        }
        if (!Objects.equals(this.directives, other.directives)) {
            return false;
        }
        return true;
    }

  


    public SourceItem() {
        this("",null,null);
    }
    /**
     * Get the text from this item. This might be the question text, answer text etc.
     * @return 
     */
    public String getTextContent() {
        return textContent;
    }
    /**
     * Sets the text content of this item
     * @param textContent 
     */
    public void setTextContent(String textContent) {
        this.textContent  = textContent;
    }

    public ArrayList<SourceItem> getSubitems() {
        return subitems;
    }

    public Directive getParameters() {
        return directives;
    }

    
   
    /**
     * Part of the visitor pattern.
     * 
     * @param visitor
     */
    @Override
    public abstract void accept(Visitor visitor);
    
    /**
     * Force the elements to implement a deep copy operation
     * @return 
     */
    public abstract SourceItem DeepClone();
    
    public abstract SourceItem ShallowClone();

    protected static void copyContentDeep(SourceItem src,SourceItem dst){
        dst.textContent=src.textContent;
        dst.directives=src.directives.DeepClone();
        dst.subitems=new ArrayList<>(src.subitems.size());
        for(SourceItem  it:src.subitems){
            dst.subitems.add(it.DeepClone());
        }
    }
    protected static void copyContentShallow(SourceItem src,SourceItem dst){
        dst.textContent=src.textContent;
        dst.directives=src.directives.DeepClone();
        dst.subitems=new ArrayList<>(src.subitems.size());
        dst.subitems.addAll(src.subitems);
    }
}
