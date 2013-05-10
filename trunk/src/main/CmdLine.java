package main;

import data.ParsedSource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;
import latexparser.javacc.LatexLexer;
import latexparser.javacc.ParseException;
import visitors.CollectCorrectAnswers;
import visitors.OutputLatex;
import visitors.Randomizer;
import visitors.ReplaceInHeader;
import visitors.DivideBigQuestions;
import visitors.SelectQuestions;

/**
 * Main entry point in the software. 
 * @author visoft
 */
public class CmdLine {
    private static Logger log=Logger.getLogger(CmdLine.class.getName());
    
    /**
     * Does the actual processing.
     * Loads f1, parses it, 
     * selects, randomize, etc the parsed source
     * Writes it back to f2.
     * @param f1
     * @param f2 
     */
    private static void ProcessFiles(File f1,File f2,File ansFile,String toReplace){
        log.info("Starting to parse: "+f1.getPath());
        ParsedSource ps;
        DivideBigQuestions SelAns=new DivideBigQuestions();
        SelectQuestions SelQ=new SelectQuestions();
        Randomizer Rand=new Randomizer();
        OutputLatex OutLat=new OutputLatex();
        ReplaceInHeader HeadRep=new ReplaceInHeader("@Rep", toReplace);
        CollectCorrectAnswers OutAns=new CollectCorrectAnswers();
        StringBuilder rezult;
        String AnswersListed;
        try {
            LatexLexer lexer=new LatexLexer(new FileInputStream(f1));
            ps=lexer.getParsedSource();
        } catch (ParseException pex) {
            log.severe("Parsing error: "+pex.toString());
            return;
        } catch (FileNotFoundException ex){
            log.severe("File not found "+f1.getPath());
            return;
        }
        try {
            
        log.info("Running SelectAnswers:");
            ps=SelAns.ApplyTo(ps);
        log.info("Running SelectQuestions:");
            ps=SelQ.ApplyTo(ps);
        log.info("Running Randomizer:");
            ps=Rand.ApplyTo(ps);
        log.info("Replacing "+HeadRep.getToSearch()+" with \""+HeadRep.getToReplace()+"\"");
            ps=HeadRep.ApplyTo(ps);
        log.info("Running OutputLatex:");
            OutLat.ApplyTo(ps);
            rezult=OutLat.getContent();
        log.info("Generating answer file");
            ps=OutAns.ApplyTo(ps);
            AnswersListed=OutAns.getContent();
        } catch (Exception e) {
            log.severe("There was an error:"+e.toString());
            return;
        }
        
        log.info("Writting the out file");
        try {
            BufferedWriter  fos=new BufferedWriter(new FileWriter(f2));
            fos.append(rezult);
            fos.close();
            BufferedWriter fans=new BufferedWriter(new FileWriter(ansFile));
            fans.append(AnswersListed);
            fans.close();
            
        } catch (IOException e) {
            log.severe("Cannot open "+f2.getPath()+" for writting. Error:"+e.toString());
            return;
        }
        log.info("All done!");
                
    }
    
    /**
     * Print the instructions
     */
    private static void PrintInstructions(){
        System.out.println(
                  "Subject generator\n"
                + "Usage:\n"
                + "  ExamGenerator source_file.tex dest_file.tex answers.txt [\"tag\"]\n"
                + "Watch out for information messages, warnings and errors during generation process!\n"
                + "The tag will replace the @Ref marker in the file header. Always place the \"\" ");
    }
  
    
    /**
     * Main class
     * @param Args 
     */
    public static void main(String[] Args){
       if(Args.length<2){
           PrintInstructions();
           return;
       }
       File f1,f2,f3;
       f1=new File(Args[0]);
       if(f1.canRead()==false){
           log.severe("Cannot open "+Args[0]+" for reading. Application will exit");
           return;
       }
       f2=new File(Args[1]);
       if(f2.exists()==true &&  f2.canWrite()==false){
           log.severe("Cannot open "+Args[1]+" for writting. Application will exit");
           return;
       }
       f3=new File(Args[2]);
       if(f3.exists()==true &&  f3.canWrite()==false){
           log.severe("Cannot open "+Args[2]+" for writting. Application will exit");
           return;
       }
       String rep="";
       if(Args.length>=4){
           rep=Args[3];
           rep=rep.replace("\"", "");
       }
           
       ProcessFiles(f1,f2,f3,rep);
       
    }
}
