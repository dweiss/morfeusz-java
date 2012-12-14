package com.dawidweiss.morfeusz;

import junit.framework.TestCase;

import java.io.UnsupportedEncodingException;


/**
 * Tests the low-level analyzer.
 */
public class LowLevelAnalyzerTest extends TestCase {
    /**
     * Required by JUnit.
     */
    public LowLevelAnalyzerTest(String s) {
        super(s);
    }

    /**
     * Test a single-word analysis and availability of
     * the morphological analysis table and properties.
     */
    public void testBasicAnalysis()
        throws UnsupportedEncodingException, SecurityException, 
            UnsatisfiedLinkError {
        Analyzer analyzer = Morfeusz.getInstance().getAnalyzer();
        InterpMorf[] analysis = analyzer.analyze("ja zostałem".getBytes(
                    "iso8859-2"));

        assertTrue("analysis should not be null.", analysis != null);

        assertTrue("there should be tokens in the analysis.",
            analyzer.getTokensNumber() > 0);

        assertTrue("token should be available.", analysis[0].getToken() != null);
        assertTrue("lemma should be available.", analysis[0].getLemma() != null);
        assertTrue("tag should be available.", analysis[0].getTag() != null);
    }

    
    public void testAnalysisOfSelectedExpressions()
        throws UnsupportedEncodingException, SecurityException, 
            UnsatisfiedLinkError {
        Analyzer analyzer = Morfeusz.getInstance().getAnalyzer();

        String [] words = {
            "ja zostałem",
            "wziąć",
            "kominiarz",
            "komin",
            "kominiarka",
            "jak",
            "żółtodzioby"
        };
        
        for (int i=0;i<words.length;i++) {
            InterpMorf[] analysis = 
                analyzer.analyze(words[i].getBytes("iso8859-2"));            

            assertTrue("analysis should not be null.", analysis != null);
            assertTrue("there should be tokens in the analysis.",
                analyzer.getTokensNumber() > 0);

            System.out.println("Analysis of: " + words[i]);
            for (int j=0;j<analyzer.getTokensNumber();j++) {
                String token = new String(analysis[j].getToken(), 0,
                    analysis[j].getTokenLength(), "iso8859-2");
                String lemma = new String(analysis[j].getLemma(), 0,
                    analysis[j].getLemmaLength(), "iso8859-2");
                String tag = new String(analysis[j].getTag(), 0,
                    analysis[j].getTagLength(), "iso8859-2");
                    
                System.out.println("\t"
                    + analysis[j].getNodeStart()
                    + "-"
                    + analysis[j].getNodeEnd()
                    + " : "
                    + token + " [" + lemma + " : " + tag + "]");
            }
        }
    }
}
