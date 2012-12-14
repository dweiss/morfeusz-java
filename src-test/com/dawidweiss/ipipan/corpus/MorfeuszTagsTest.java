package com.dawidweiss.ipipan.corpus;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.dawidweiss.morfeusz.Analyzer;
import com.dawidweiss.morfeusz.InterpMorf;
import com.dawidweiss.morfeusz.Morfeusz;

/**
 * Tests parsing of a wide range of tags
 * emitted by Morfeusz. 
 *
 * The test is based on analysis of a bunch
 * of words extracted from the IPI PAN corpora.
 *
 * @author Dawid Weiss
 */
public class MorfeuszTagsTest {

    private final static class TagParser extends TestCase {
        private final String tag;

        public TagParser(String testName, String tag) {
            super(testName);
            this.tag = tag;
        }
        
        protected void runTest() {
            Tag [] tags = Tag.create(tag);
            assertNotNull(tags);
            assertTrue(tags.length > 0);

            StringBuffer buf = new StringBuffer();
            for (int i = 0; i< tags.length; i++) {
                if (i > 0) buf.append("|");
                buf.append(tags[i].toString());
            }
            if (tag.indexOf('_') == -1) {
	            assertEquals("Tag not deserialized identically from code (" + buf.toString() + ")",
	                    tag, buf.toString());
            }
        }
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(MorfeuszTagsTest.class.getName());

        try {
            LineNumberReader reader =
                new LineNumberReader(
                    new InputStreamReader(
                            MorfeuszTagsTest.class.getResourceAsStream(
                                    "tokens.samples.ipi-wstepny.txt"), "UTF-8"));
            try {
                Analyzer analyzer = Morfeusz.getInstance().getAnalyzer();

                do {
                    String ipiTag = reader.readLine();
                    if (ipiTag == null || ipiTag.trim().length() == 0)
                        break;

                    String word;
                    do {
                        word = reader.readLine();
                        if (word.trim().length() == 0)
                            break;
                        InterpMorf [] analysis = analyzer.analyze(word.getBytes("iso8859-2"));
                        for (int i = 0; i < analyzer.getTokensNumber(); i++) {
                            byte [] tagBytes = analysis[i].getTag();
                            if (tagBytes == null) continue;
                            String tag = new String(tagBytes, 0, analysis[i].getTagLength());
                            // System.out.println(ipiTag + ", " + word + ", " + tag);

                            if (tag.length() > 0) {
                                suite.addTest(new TagParser("Parsing: '" + tag + "', ipi: '" + ipiTag + "' (" + word + ")", tag));
                            }
                        }
                    } while (true);
                } while (true);
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read tags resource.");
        }
                
        return suite;
    }
}
