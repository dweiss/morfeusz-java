package com.dawidweiss.ipipan.corpus;

import junit.framework.TestCase;

/**
 * Tests IPI PAN corpora tag parser (simple cases).
 * 
 * @author Dawid Weiss
 */
public class TagTest extends TestCase {

    public TagTest(String s) {
        super(s);
    }

    public void testMultipleCodes() {
        String tagCodes = "adj:sg:nom:m1.m2.m3:pos|adj:sg:acc:m3:pos";
        Tag[] tags = Tag.create(tagCodes);

        assertEquals(2, tags.length);
    }

    public void testEmptyCode() {
        String tagCodes = "";
        Tag[] tags = Tag.create(tagCodes);

        assertEquals(0, tags.length);
    }

    public void testNeutralGenders() {
        String tagCodes = "subst:sg:nom.acc.voc:n1.n2";
        Tag[] tags = Tag.create(tagCodes);
        assertEquals(1, tags.length);
        assertEquals("subst", tags[0].partOfSpeech);
        assertEquals("sg", tags[0].number);
        assertEquals("nom.acc.voc", tags[0]._case);
        assertEquals("n1.n2", tags[0].gender);
    }
    
    public void testContained() {
        // Positive examples
        assertTrue(
                Tag.contained(Tag.create("adj:sg:acc.inst:f:pos")[0].getCode(),
                Tag.create("adj:sg:acc:f:pos")[0].getCode()));
        // Positive examples
        assertTrue(
                Tag.contained(Tag.create("adj:sg:acc.inst:f.n.m1:pos")[0].getCode(),
                Tag.create("adj:sg:acc:m1:pos")[0].getCode()));

        // Negative examples
        assertFalse(
                Tag.contained(Tag.create("adj:sg:inst.loc.voc:f:pos")[0].getCode(),
                Tag.create("adj:sg:acc:f:pos")[0].getCode()));
        assertFalse(
                Tag.contained(Tag.create("subst:sg:inst:f")[0].getCode(),
                Tag.create("adj:sg:inst:f:pos")[0].getCode()));
    }
}
