package com.dawidweiss.ipipan.corpus;

/**
 * Thrown from the tag parser when the POS code is not among any
 * of the known codes.
 * 
 * @author Dawid Weiss
 */
public class TagParserException extends RuntimeException {
    public TagParserException(String message) {
        super(message);
    }
}
