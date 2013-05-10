package visitors;

import data.Answer;
import data.Directive;
import data.ParsedSource;
import data.Question;
import data.QuestionBlock;
import data.Section;
import data.SourceItem;

/**
 * Visitor that takes the ParsedSource and writes it into a latex file. 
 * Adds latex tags for sections, questions, answers, etc.
 * @author visoft
 */
public class OutputLatex implements Visitor{

    private StringBuilder content;
    private boolean outputAns;

    public OutputLatex() {
        content=new StringBuilder();
        outputAns=false;
    }
    
    /**
     * Call ApplyTo to get the file in the output format.
     * After calling ApplyTo call getContent() to get the actual content
     * The method returns source parameter.
     * @param source
     * @return 
     */
    @Override
    public ParsedSource ApplyTo(ParsedSource source){
       Directive dir=source.getParameters();
       String wa=dir.getMap().get("WriteAnswers");
       if("1".equalsIgnoreCase(wa) || "yes".equalsIgnoreCase(wa) || "true".equalsIgnoreCase(wa) )
           outputAns=true;

       //decide if there are sections or sectionless structure in the source
       boolean addEnumerate=false;
       if(source.getSubitems().size()>0)
           if(!source.getSubitems().get(0).getClass().equals(Section.class))
               addEnumerate=true;
       
       content.append(source.getHeader());
       
       if(addEnumerate)
            content.append("\\begin{enumerate}\n");
       
       for(SourceItem item:source.getSubitems())     
           item.accept(this);

       if(addEnumerate) 
            content.append("\\end{enumerate}\n");       
       
       content.append(source.getFooter());
       return source;
    }

    public StringBuilder getContent() {
        return content;
    }
    
    /**
     * Lists a question block. Outputs the text and then the actual questions.
     * @param item 
     */
    @Override
    public void visit(QuestionBlock item) {
        content.append(item.getTextContent()).append("\n");
        for(SourceItem quest:item.getSubitems())     
            quest.accept(this);
    }
    /**
     * Lists a question. The question text is preceded by \item.  Encloses the answers in begin/end{enumerate} latex tags.
     * @param item 
     */
    @Override
    public void visit(Question item) {
        content.append("\\item ");
        content.append(item.getTextContent()).append("\n");
        content.append("\\begin{enumerate}\n");
        for(SourceItem ans:item.getSubitems())     
           ans.accept(this);
        content.append("\\end{enumerate}\n");
    }
    /**
     * Outputs a specific answer.
     * @param item 
     */
    @Override
    public void visit(Answer item) {
        content.append("\\item ");
        content.append(item.getTextContent());
        if(outputAns)
            content.append(" Answer:"+item.getCorrect().toString());
    }

    @Override
    public void visit(Section item) {
        content.append(item.getTextContent());
        content.append("\\begin{enumerate}"+item.getEnumerateOptions()+"\n");
        for(SourceItem subitem:item.getSubitems())     
            subitem.accept(this);
        content.append("\\end{enumerate}\n");
        
    }
}
