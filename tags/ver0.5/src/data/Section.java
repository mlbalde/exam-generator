/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.ArrayList;
import java.util.Objects;
import visitors.Visitor;

/**
 * Keeps track of a "\section{}" part from latex.
 * 
 * In addition to the SourceItem data, there is an EnumerateOptions string parameter
 * that keeps track of eventual options following the first \begin{enumerate} in latex.
 * @author visoft
 */
public class Section extends SourceItem {

    public Section(String textContent,String EnumerateOptions, ArrayList<SourceItem> subitems, Directive parameters) {
        super(textContent, subitems, parameters);
        this.EnumerateOptions=EnumerateOptions;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 53 * hash + Objects.hashCode(this.EnumerateOptions);
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
        final Section other = (Section) obj;
        if (!Objects.equals(this.EnumerateOptions, other.EnumerateOptions)) {
            return false;
        }
        return super.equals(obj);
    }

    public Section() {
        this("","",null,null);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Section DeepClone() {
        Section dst=new Section();
        dst.EnumerateOptions=EnumerateOptions;
        copyContentDeep(this, dst);
        return dst;
    }

    @Override
    public Section ShallowClone() {
        Section dst=new Section();
        dst.EnumerateOptions=EnumerateOptions;
        copyContentShallow(this, dst);
        return dst;
    }

    public String getEnumerateOptions() {
        return EnumerateOptions;
    }

    public void setEnumerateOptions(String EnumerateOptions) {
        this.EnumerateOptions = EnumerateOptions;
    }
    
    private String EnumerateOptions;
}
