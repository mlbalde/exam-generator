package visitors;

import data.Answer;
import data.ParsedSource;
import data.Question;
import data.QuestionBlock;
import data.Section;

/**
 * Searches for a string in the ParsedSource header and replaces it with the 
 * specified string.
 * 
 * For now, only the header is searched, and no special considerations are considered.
 * The default string to be searched is "@Ref".
 * 
 * The purpose of this class is that a user wants to generate several tests from one
 * source file. Each test must be identified by an ID. This class allows the user
 * to insert this ID into the title/preamble of the finished paper.
 * @author visoft
 */
public class ReplaceInHeader implements Visitor {

    private String toSearch;//The string to be searched
    private String toReplace; //The replacement

    public ReplaceInHeader(String toSearch, String toReplace) {
        this.toSearch = toSearch;
        this.toReplace = toReplace;
        if(toSearch.length()<=0)
            toSearch="@Ref";
    }

    public String getToSearch() {
        return toSearch;
    }

    public String getToReplace() {
        return toReplace;
    }
    
    @Override
    public ParsedSource ApplyTo(ParsedSource src) {
        ParsedSource dst;
        dst=src.ShallowClone();
        String head=dst.getHeader();
        head=head.replaceAll(toSearch,toReplace);
        dst.setHeader(head);
        return dst;
    }

    @Override
    public void visit(QuestionBlock item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void visit(Question item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void visit(Answer item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void visit(Section item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
