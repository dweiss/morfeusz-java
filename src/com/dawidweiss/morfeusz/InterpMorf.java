package com.dawidweiss.morfeusz;

import java.io.UnsupportedEncodingException;


/**
 * This class represents the InterpMorf structure, returned by Morfeusz:
 * <pre>
 * struct _InterpMorf {
 *   int p, k;  // number of start node and end node
 *   char *forma,  // segment (token)
 *        *haslo,  // lemma
 *        *interp; // morphosyntactic tag
 * };
 * </pre>
 *
 * Fields in this class are in package scope to speed up copying from native data structures 
 * in Morfeusz.
 */
public final class InterpMorf {
    /** Maximum length of a single token. */
    private static final int MAX_TOKEN_LENGTH = 200;
    
    /** Maximum length of a lemma. */
    private static final int MAX_LEMMA_LENGTH = 200;
    
    /** Maximum length of a tag. */
    private static final int MAX_TAG_LENGTH = 600;
    
    /**
     * Check if the required encoding is available (further exceptions
     * have empty blocks).
     */
    static {
        try {
            new String("abc".getBytes(), Morfeusz.ENCODING_UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Required encoding unsupported: " + Morfeusz.ENCODING_UTF8);
        }
    }
    
    /**
     * Byte-to-character encoding used in Morfeusz.
     */
    private final String morfeuszCharEncoding;

    /** Number of start node. */
    int p;

    /** Number of end node. */
    int k;

    /** Token, verbatim as returned by Morfeusz */
    final byte[] token = new byte[MAX_TOKEN_LENGTH];

    /** Lenght of the most recently analyzed token. */
    int tokenLength;

    /** Lemma, verbatim as returned by Morfeusz */
    final byte[] lemma = new byte[MAX_LEMMA_LENGTH];

    /** Lenght of the most recently analyzed token's lemma. */
    int lemmaLength;

    /** Morphosyntactic tag, verbatim as returned by Morfeusz */
    final byte[] tag = new byte[MAX_TAG_LENGTH];

    /** Lenght of the most recently analyzed token's tag. */
    int tagLength;

    /** Constructor only accessible from within package */
    InterpMorf(String byteToCharEncoding) {
        this.morfeuszCharEncoding = byteToCharEncoding;
    }

    /**
     * Returns the token as a byte array.
     * The length of data in the array is variable and has to be retrieved
     * using separate method <code>getTokenLength</code>
     * 
     * @deprecated Consider using {@link #getTokenImage()}, this method will be inaccessible in the future.
     */
    public final byte[] getToken() {
        return token;
    }

    /**
     * Returns the lemma as a byte array.
     * The length of data in the array is variable and has to be retrieved
     * using separate method <code>getLemmaLength</code>
     * 
     * @deprecated Consider using {@link #getLemmaImage()}, this method will be inaccessible in the future.
     */
    public final byte[] getLemma() {
        return lemma;
    }

    /**
     * Returns the tag as a byte array.
     * The length of data in the array is variable and has to be retrieved
     * using separate method <code>getTagLength</code>
     * 
     * @deprecated Consider using {@link #getTagImage()}, this method will be inaccessible in the future.
     */
    public final byte[] getTag() {
        return tag;
    }

    /**
     * Returns the number of the start node.
     * 
     * @deprecated Consider using {@link #getNodeImage()}, this method will be inaccessible in the future.
     */
    public final int getNodeStart() {
        return p;
    }

    /**
     * Returns the number of the end node.
     * 
     * @deprecated Consider using {@link #getNodeImage()}, this method will be inaccessible in the future.
     */
    public final int getNodeEnd() {
        return k;
    }

    /**
     * Returns the most recently analyzed token's lemma length.
     * 
     * @deprecated Consider using {@link #getLemmaImage()}, this method will be inaccessible in the future.
     */
    public final int getLemmaLength() {
        return lemmaLength;
    }

    /**
     * Returns the most recently analyzed token's tag length.
     * 
     * @deprecated Consider using {@link #getTagImage()}, this method will be inaccessible in the future.
     */
    public final int getTagLength() {
        return tagLength;
    }

    /**
     * Returns the most recently analyzed token's length.
     * 
     * @deprecated Consider using {@link #getTokenImage()}, this method will be inaccessible in the future.
     */
    public final int getTokenLength() {
        return tokenLength;
    }
    
    /**
     * Returns the tag image as a string.
     */
    public final String getTagImage() {
        try {
            return new String(getTag(), 0, getTagLength(), morfeuszCharEncoding);
        } catch (UnsupportedEncodingException e) {
            /* Checked in a static block. */
            throw new RuntimeException();
        }
    }

    /**
     * Returns the lemma image as a string.
     */
    public final String getLemmaImage() {
        try {
            return new String(getLemma(), 0, getLemmaLength(), morfeuszCharEncoding);
        } catch (UnsupportedEncodingException e) {
            /* Checked in a static block. */
            throw new RuntimeException();
        }
    }

    /**
     * Returns the lemma image as a string.
     */
    public final String getTokenImage() {
        try {
            return new String(getToken(), 0, getTokenLength(), morfeuszCharEncoding);
        } catch (UnsupportedEncodingException e) {
            /* Checked in a static block. */
            throw new RuntimeException();
        }
    }

    /**
     * Returns a visual representation of the token structure. 
     */
    public String toString() {
        return getNodeStart() + "-" + getNodeEnd() + " : " +
        getTokenImage() + " : " +
        getLemmaImage() + " : " +
        getTagImage();
    }
}
