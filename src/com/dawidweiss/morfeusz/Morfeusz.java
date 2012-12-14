package com.dawidweiss.morfeusz;

import java.io.*;
import java.util.Properties;


/**
 * This class provides public access to Morfeusz. The class
 * contains a singleton object, available by a call to
 * <code>getInstance</code>.
 */
public final class Morfeusz {
    /** UTF-8 encoding name */
    final static String ENCODING_UTF8 = "UTF-8";
    final static String ENCODING_ISO8859_2 = "iso8859-2";
    final static String ENCODING_CP1250 = "Cp1250";
    final static String ENCODING_CP852 = "Cp852";

    /** Option type for calling {@link #morfeusz_set_option(int, int)}. */
    public final static int MORFOPT_ENCODING = 1;

    /** A value for an option {@link #MORFOPT_ENCODING}, corresponding to <code>iso-8859-2</code> */
    public final static int MORFEUSZ_ISO8859_2 = 88592;

    /** A value for an option {@link #MORFOPT_ENCODING}, corresponding to <code>windows-1250</code> */
    public final static int MORFEUSZ_CP1250 = 1250;

    /** A value for an option {@link #MORFOPT_ENCODING}, corresponding to <code>windows-852</code> */
    public final static int MORFEUSZ_CP852 = 852;

    /** A value for an option {@link #MORFOPT_ENCODING}, corresponding to <code>utf-8</code> */
    public final static int MORFEUSZ_UTF8 = 8;

    /** Default encoding used by Morfeusz to convert bytes to characters. */
    private final static int MORFEUSZ_DEFAULT_ENCODING = MORFEUSZ_UTF8;

    /**
     * The singleton instance of Morfeusz class.
     */
    private static Morfeusz instance;
    
    /**
     * About information string.
     */
    private String aboutInfo;

    /** Codepage used for converting bytes to characters. */
    private final String encoding;

    /**
     * No instantiation outside of the class scope.
     */
    private Morfeusz(final int morfeuszEncoding) {
        this.encoding = encodingToCodePage(morfeuszEncoding);

        if (morfeusz_set_option(MORFOPT_ENCODING, morfeuszEncoding) != 1) {
            throw new RuntimeException("Morfeusz option MORFOPT_ENCODING could not be set to the default" +
                    " encoding: " + instance.encoding);
        }
    }

    /**
     * @return Returns the singleton object that can be used to acquire
     * new <code>Analyzer</code> instances.
     * 
     * @throws SecurityException If the native library cannot be accessed.
     * @throws UnsatisfiedLinkError If Morfeusz library cannot be linked or found.
     *         The search path depends on the operating system's shared object
     *         binding method.
     * @throws UnsupportedEncodingException If the required character encoding is not supported.
     */
    public static Morfeusz getInstance()
        throws UnsupportedEncodingException, SecurityException, UnsatisfiedLinkError
    {
        synchronized (Morfeusz.class) {
            if (instance != null) {
                return instance;
            }

            Runtime.getRuntime().loadLibrary("morfeusz-java");

            Morfeusz instance = new Morfeusz(MORFEUSZ_DEFAULT_ENCODING);
            instance.aboutInfo = new String(instance.aboutJniNative(), ENCODING_ISO8859_2) + "\n\n";
            InputStream is = instance.getClass().getResourceAsStream("/res/version.txt");
            if (is == null) {
	            instance.aboutInfo += "Java JNI binding code (c) Dawid Weiss\n(no version info)";
            } else {
	            try {
		            Properties p = new Properties();
		            p.load(is);

    	            instance.aboutInfo += p.getProperty("copyclause") + "\n";
    	            instance.aboutInfo += "Version: " + p.getProperty("version") + "\n";
    	            instance.aboutInfo += "SVN-Id: " + p.getProperty("svnid") + "\n";
	            } catch (IOException e) {
		            instance.aboutInfo += "Java JNI binding code (c) Dawid Weiss\n(exception reading version info)";
	            } finally {
		            try {
			            is.close();
		            } catch (IOException e) {
			            // ignore
		            }
	            }
            }
            Morfeusz.instance = instance;
            return instance;
        }
    }

    /**
     * @return Returns an information string about the version of Morfeusz
     *         used and about the Java binding version.
     */
    public String about() {
        return aboutInfo;
    }

    /**
     * @return Returns an instance of the low-level Morfeusz
     *         binding class. The returned
     *         analyzer instance is <b>not</b> thread-safe and reuses
     *         data structures after subsequent analyses.
     */
    public Analyzer getAnalyzer() {
        return new Analyzer(encoding);
    }

    /**
     * Returns <code>iso8859-2</code>-encoded copyright info from Morfeusz.
     */
    private native byte[] aboutJniNative();

    /**
     * A native method for setting options declared in Morfeusz.
     * See constants defined in this class for options and 
     * their values.
     * 
     * <b>Do not change {@link #MORFOPT_ENCODING} option after Morfeusz
     * has been initialized.</b>
     */
    public native int morfeusz_set_option(int option, int value);
    
    /**
     * Converts Morfeusz encoding option to a codepage string.
     * 
     * @throws IllegalArgumentException If the input value is unrecognized.
     */
    private static String encodingToCodePage(final int morfeuszEncoding) {
        switch (morfeuszEncoding) {
            case MORFEUSZ_CP1250: 
                return ENCODING_CP1250;
            case MORFEUSZ_ISO8859_2: 
                return ENCODING_ISO8859_2;
            case MORFEUSZ_CP852:
                return ENCODING_CP852;
            case MORFEUSZ_UTF8:
                return ENCODING_UTF8;
            default:
                throw new IllegalArgumentException("Unknown encoding: " + morfeuszEncoding);
        }
    }
}
