package com.dawidweiss.ipipan.corpus;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests parsing of all unique tags from the
 * IPI PAN corpora (korpus wstepny).
 * 
 * @author Dawid Weiss
 */
public class IpiWstepnyTagsTest {

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

            for (int i = 0; i< tags.length; i++) {
                assertEquals("Tag not deserialized identically from code (" + tag + " -- " + tags[i].toString() + ")",
                        tag, tags[i].toString());
            }
        }
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(IpiWstepnyTagsTest.class.getName());

        try {
            LineNumberReader reader =
                new LineNumberReader(
                    new InputStreamReader(
                            IpiWstepnyTagsTest.class.getResourceAsStream("tokens.unique.ipi-wstepny.txt")));
            try {
                do {
                    String line = reader.readLine();
                    if (line == null) break;
                    suite.addTest(new TagParser("Parsing: '" + line + "'", line));
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
