# Introduction #

The Exam Generator software generates exam or test sheets starting from a
larger question file written in LaTeX. More precisely, if you have a list of 100 questions the software can generate a paper containing randomly selected 10 questions. The process of selection, randomization, placement of the questions can be tuned by options inserted in the LaTeX file.

The philosoply is that you have a large "database" LaTeX file, that you created during some years of activity and you want to randomly and automatically generate an exam sheet out of it.



# How to run #

Download the latest .jar file somewhere on your hard disk.

Make sure that you have java installed and it runs (i.e. you have the correct path, etc.)

Run the command:

```
java -jar ExamGenerator source_file.tex dest_file.tex answers.txt "variant"
```

Only the first three parameters are mandatory.

  * **source\_file.tex** is the source latex file that will be processed. This file will not be changed
  * **dest\_file.tex** Output file
  * **answers.txt** Because the generation is a random process you might want to know which choices are correct and which are not. The software will output the answers that are tagged as being correct in the source file.
  * **"variant"** See the pdf documentation.

# Instructions and demo files #

For a larger user guide please download the latest manual from [Downloads](https://code.google.com/p/exam-generator/downloads/list) section.

There is one demo directory in the source repository. There you find some files that are a "live" documents, that is, can be used as a starting point for an actual quiz source file.

Please examine the two pdf's, source pdf and dest pdf to see how a typical sheet looks like. Then take a peek to the source code for these pdf's.

The link to demo files is: [trunk/demo](https://code.google.com/p/exam-generator/source/browse/#svn%2Ftrunk%2Fdemo)

The command line used to generate the dest\_1.tex file is:
```
java -jar ExamGenerator source_1.tex dest_1.tex answers.txt "Replaced!"
```

# Final remarks #

Feel free to take a look in the java code for more details. Comments are welcome!

Good luck to you and your students :) !