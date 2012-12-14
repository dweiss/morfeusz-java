package com.dawidweiss.morfeusz;

import java.io.UnsupportedEncodingException;


/**
 * An analyzer class can parse a string into tokens and return their
 * morphological analysis.
 * 
 * This is a low-level, procedural-like, access to Morfeusz.
 *
 * Analyzer class is <b>not</b> thread-safe, and every invocation of
 * <code>analyze</code> will replace the internal structures with new data.
 */
public final class Analyzer {
    private static final int MAX_GRAPH_NODES = 50;

    /** Character-to-bytes encoding used by Morfeusz. */
    private final String encoding;

    private InterpMorf[] morphologicalAnalysis;
    private int tokensNumber;

    /**
     * Constructor accessible only from within package scope.
     */
    Analyzer(final String encoding) {
        this.encoding = encoding;
        morphologicalAnalysis = new InterpMorf[MAX_GRAPH_NODES];

        for (int i = 0; i < morphologicalAnalysis.length; i++) {
            morphologicalAnalysis[i] = new InterpMorf(encoding);
        }
    }

    /**
     * Analyzes a given term. 
     * 
     * The result is stored and returned as an array of <code>InterpMorf</code>
     * objects. <b>These objects are reused on subsequent calls.</b>
     * Their contents will change when <code>analyze</code> is invoked again.
     * 
     * @since 1.2
     */
    public InterpMorf[] analyze(String term) {
        try {
            tokensNumber = morfeusz_analyse(term.getBytes(encoding));
            return morphologicalAnalysis;
        } catch (UnsupportedEncodingException e) {
            // Practically unreachable.
            throw new RuntimeException();
        }
    }

    /**
     * Analyzes a term encoded with a codepage previously set by
     * passing {@link Morfeusz#MORFOPT_ENCODING} to {@link Morfeusz#morfeusz_set_option(int, int)}. 
     * 
     * The result is stored and returned as an array of <code>InterpMorf</code>
     * objects. <b>These objects are reused on subsequent calls.</b>
     * Their contents will change when <code>analyze</code> is invoked again.
     * 
     * @deprecated It is advised that you use the {@link #analyze(String)} methods instead of the 
     * low-level byte array version.
     */
    public InterpMorf[] analyze(byte[] term) {
        tokensNumber = morfeusz_analyse(term);
        return morphologicalAnalysis;
    }

    /**
     * Returns the number of tokens for the most recently analyzed string.
     */
    public int getTokensNumber() {
        return tokensNumber;
    }

    /**
     * A native binding to Morfeusz's morphological analysis. The binding
     * function in C fills <code>morphologicalAnalysis</code> array with
     * new data.
     * 
     * @param term The term to be analyzed.
     * @return Number of tokens in the analysis array.
     */
    private final native int morfeusz_analyse(byte[] term);
}
