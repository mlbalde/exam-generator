package visitors;

import data.Answer;
import data.ParsedSource;
import data.Question;
import data.QuestionBlock;
import data.Section;

/**
 * Visitor pattern part. Implemented by each 'algorithm' that needs to visit the
 * collection.
 * @author visoft
 */
public interface Visitor {
    public ParsedSource ApplyTo(ParsedSource src);
    public void visit(Section item);
    public void visit(QuestionBlock item);
    public void visit(Question item);
    public void visit(Answer item);
}
