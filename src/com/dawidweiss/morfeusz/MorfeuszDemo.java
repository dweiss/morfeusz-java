package com.dawidweiss.morfeusz;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.Writer;

import com.dawidweiss.ipipan.corpus.Tag;


/**
 * Demonstrates Morfeusz-Java binding by taking
 * words from the input (or a file) and printing
 * the result of morphological analysis.
 * 
 * @author Dawid Weiss
 * @version $Revision$
 */
public class MorfeuszDemo {

	private boolean parseTags;

	public MorfeuszDemo() {
    }

    public void analyze(Reader input, Writer output) throws IOException {
        Analyzer analyzer = Morfeusz.getInstance().getAnalyzer();
        int words = 0;
        long start = System.currentTimeMillis();
        
        StreamTokenizer tokenizer = new StreamTokenizer(input);
        
        int token;
        while (StreamTokenizer.TT_EOF != (token = tokenizer.nextToken())) {
            if (token == StreamTokenizer.TT_WORD) {
                output.write(tokenizer.sval);
                output.write(" ");

                words++;
                InterpMorf[] analysis = 
                    analyzer.analyze(tokenizer.sval);
                if (analysis == null) {
                    output.write("?");
                } else {
                    for (int j=0; j<analyzer.getTokensNumber(); j++) {
                        String part = analysis[j].getTokenImage(); 
                        String lemma = analysis[j].getLemmaImage();
                        String tag = analysis[j].getTagImage();
                        
                        if (j>0) output.write("; ");
                        output.write(part);
                        output.write(",");
                        output.write(lemma);
                        output.write(",");
                        output.write(tag);
                        
                        if (parseTags) {
                            try {
                                Tag.create(tag);
                            } catch (RuntimeException e) {
                                System.err.println("Could not parse tag for: "
                                        + tokenizer.sval + " ('" + tag + "'); Error: "
                                        + e.toString());
                            }
                        }
                    }
                }

                output.write("\n");
            } else {
                // ignore other tokens
            }
        }
        input.close();
        output.close();
        
        long time = (System.currentTimeMillis() - start);
        System.err.println("Analyzed: " + words + " words in " +
                time + " milliseconds." );
        System.err.println( (int)(words / (time / 1000.0f)) + " words per second.");
    }
    
	public static void main(String[] args) {
        MorfeuszDemo demo = new MorfeuszDemo();
        
        Reader r = null;
        Writer w = null;
        String encoding = "utf-8";

        int i = 0;
        try {
            for (; i<args.length ; i++) {
                if (args[i].equals("-help")) {
                    System.err.println("Arguments: [-version] [-encoding input_stream_encoding] [-parsetags] [input file] [output file]");
                    return;
                } else if (args[i].equals("-encoding")) {
                    i++;
                    encoding = args[i];
                } else if (args[i].equals("-version")) {
	                System.out.println(Morfeusz.getInstance().about());
	                // exit immediately
	                return;
                } else if (args[i].equals("-parsetags")) {
                    demo.setParseTags(true);
                } else {
                    if (r == null) {
                        System.err.println("Using input characters encoding: " + encoding);
                        r = new InputStreamReader( 
                        		new BufferedInputStream( new FileInputStream(args[i])), encoding);
                    } else {
                        if (w == null) {
                            w = new OutputStreamWriter( 
                                    new BufferedOutputStream( new FileOutputStream(args[i])), "utf-8");
                        } else {
                            System.err.println("Too many parameters.");
                            return;
                        }
                    }
                }
            }

            if (r == null) {
                System.err.println("Using input characters encoding: " + encoding);
                System.err.println("Reading from standard input.");
                r = new InputStreamReader( System.in, encoding );
            }
            if (w == null) {
                w = new OutputStreamWriter( System.out, "utf-8");
            }

            demo.analyze(r, w);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error parsing parameters at: "
                    + args[i-1]);
            System.err.println("Type '-help' for help.");
        } catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        } finally {
            try {
	            if (r != null) r.close();
	            if (w != null) w.close();
            } catch (IOException e) {
            }
        }
	}
	
    private void setParseTags(boolean value) {
        this.parseTags = value;
    }
}