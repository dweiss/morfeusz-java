package com.dawidweiss.morfeusz;

import junit.framework.TestCase;

import java.io.UnsupportedEncodingException;


/**
 * Tests the basic Morfeusz JNI binding functionality.
 */
public class MorfeuszTest extends TestCase {
    /**
     * Required by the TestCase class.
     */
    public MorfeuszTest(String s) {
        super(s);
    }

    /**
     * Test whether Morfeusz singleton can be acquired at all.
     */
    public void testSingletonAvailability()
        throws UnsupportedEncodingException, SecurityException, 
            UnsatisfiedLinkError {
        Morfeusz.getInstance();
    }

    /**
     * Test whether the about info string is returned.
     */
    public void testAboutInfoMethod()
        throws UnsupportedEncodingException, SecurityException, 
            UnsatisfiedLinkError {
        assertTrue("About info string should not be null.",
            Morfeusz.getInstance().about() != null);
    }
    
}
