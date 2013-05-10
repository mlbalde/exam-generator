package data;

import java.util.Objects;
import visitors.Visitor;

/**
 * A parsed list of questions. The file (latex for now) is broken down in various components
 * This is the main container, that holds the rest of the structures.
 * @author visoft
 */
public class ParsedSource extends SourceItem {
    protected String header, footer;
   
    
    public ParsedSource() {
        super("", null, null);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.header);
        hash = 61 * hash + Objects.hashCode(this.footer);
        hash = 61 * hash + super.hashCode();
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
        final ParsedSource other = (ParsedSource) obj;
        if (!Objects.equals(this.header, other.header)) {
            return false;
        }
        if (!Objects.equals(this.footer, other.footer)) {
            return false;
        }
        return super.equals(obj);
    }


    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Makes a deep clone of the current object.
     * @return 
     */
    public ParsedSource DeepClone(){
       ParsedSource dest=new ParsedSource();
       copyContentDeep(this, dest);
       dest.setHeader(header);
       dest.setFooter(footer);
       return dest;
    }

    @Override
    public void accept(Visitor visitor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ParsedSource ShallowClone() {
       ParsedSource dest=new ParsedSource();
       copyContentShallow(this, dest);
       dest.setHeader(header);
       dest.setFooter(footer);
       return dest;
    }

}
