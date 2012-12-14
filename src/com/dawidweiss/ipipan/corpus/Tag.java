package com.dawidweiss.ipipan.corpus;

import java.util.*;

/**
 * Splits a single morphosyntactic code ("tag")
 * into separate fields and a numeric code represented
 * as a <code>long</code> number.
 *
 * This code is somewhat experimental, but may be useful
 * so I decided to release it early.
 *
 * @author Dawid Weiss
 */
public class Tag {
    public String partOfSpeech;

    private long tagCode;

    public String number;
    public String _case;
    public String gender;
    public String person;
    public String degree;
    public String aspect;
    public String negation;
    public String accommodability;
    public String accentability;
    public String postPrepositionality;
    public String agglutination;
    public String vocalicity;

    /**
     * If enabled, the tag's representation reconstructed from a numeric
     * code may contain underscores where all values of a given category are
     * present (it is a "wildcard" character).
     * 
     * This is by default disabled, but I keep it to be compatible with
     * what Morfeusz sometimes returns. 
     */
    private final static boolean EMIT_UNDERSCORE = false;

    /* Numeric codes of categories and their attributes */
    
    public final static long NOUN           = (1  << 0);
    public final static long NOUN_SUBST     = (1  << 0) + (1  << 4);
    public final static long NOUN_DEPR      = (1  << 0) + (2  << 4);

    public final static long ADJ            = (2  << 0);
    public final static long ADJ_           = (2  << 0) + (1  << 4);
    public final static long ADJ_A          = (2  << 0) + (2  << 4);
    public final static long ADJ_P          = (2  << 0) + (3  << 4);

    public final static long ADV            = (3  << 0);
    public final static long NUM            = (4  << 0);

    public final static long PPRON          = (5  << 0);
    public final static long PPRON_12       = (5  << 0) + (1  << 4);
    public final static long PPRON_3        = (5  << 0) + (2  << 4);
    public final static long PPRON_SIEBIE   = (5  << 0) + (3  << 4);

    public final static long VERB           = (6  << 0);
    public final static long VERB_FIN       = (6  << 0) + (1  << 4);
    public final static long VERB_BEDZIE    = (6  << 0) + (2  << 4);
    public final static long VERB_AGLT      = (6  << 0) + (3  << 4);
    public final static long VERB_PRAET     = (6  << 0) + (4  << 4);
    public final static long VERB_IMPT      = (6  << 0) + (5  << 4);
    public final static long VERB_IMPS      = (6  << 0) + (6  << 4);
    public final static long VERB_INF       = (6  << 0) + (7  << 4);
    public final static long VERB_PCON      = (6  << 0) + (8  << 4);
    public final static long VERB_PANT      = (6  << 0) + (9  << 4);
    public final static long VERB_GER       = (6  << 0) + (10 << 4);
    public final static long VERB_PACT      = (6  << 0) + (11 << 4);
    public final static long VERB_PPAS      = (6  << 0) + (12 << 4);

    public final static long WINIEN         = (7  << 0);
    public final static long PRED           = (8  << 0);
    public final static long PREP           = (9  << 0);
    public final static long CONJ           = (10 << 0);
    public final static long QUB            = (11 << 0);
    public final static long XXS            = (12 << 0);
    public final static long XXX            = (13 << 0);
    public final static long INTERP         = (14 << 0);
    public final static long IGN            = (15 << 0);
    public final static long UNKNOWN        = (0  << 0);

    public final static long MASK_POS_MAIN  = 0xf;
    public final static long MASK_POS_SUB   = 0xf0;
    public final static long MASK_POS       = MASK_POS_MAIN | MASK_POS_SUB;

    public final static long NUMBER_SG      = (1l << 9);
    public final static long NUMBER_PL      = (1l << 10);
    public final static long MASK_NUMBER    = NUMBER_SG | NUMBER_PL; 

    public final static long CASE_NOM       = (1l << 11);
    public final static long CASE_GEN       = (1l << 12);
    public final static long CASE_DAT       = (1l << 13);
    public final static long CASE_ACC       = (1l << 14);
    public final static long CASE_INST      = (1l << 15);
    public final static long CASE_LOC       = (1l << 16);
    public final static long CASE_VOC       = (1l << 17);
    public final static long MASK_CASE      = CASE_NOM | CASE_GEN | CASE_DAT | CASE_ACC | CASE_INST | CASE_LOC | CASE_VOC;

    public final static long GENDER_M       = (1l << 18);
    public final static long GENDER_M1      = GENDER_M | (1l << 19);
    public final static long GENDER_M2      = GENDER_M | (1l << 20);
    public final static long GENDER_M3      = GENDER_M | (1l << 21);
    public final static long GENDER_F       = (1l << 22);
    public final static long GENDER_N       = (1l << 23);
    public final static long GENDER_N1      = GENDER_N | (1l << 24);
    public final static long GENDER_N2      = GENDER_N | (1l << 25);
    public final static long GENDER_P       = (1l << 26);
    public final static long GENDER_P1      = GENDER_P | (1l << 27);
    public final static long GENDER_P2      = GENDER_P | (1l << 28);
    public final static long GENDER_P3      = GENDER_P | (1l << 29);
    public final static long MASK_GENDER    = GENDER_M1 | GENDER_M2 | GENDER_M3 | GENDER_F | GENDER_N1 | GENDER_N2 | GENDER_P1 | GENDER_P2 | GENDER_P3;

    public final static long PERSON_PRI     = (1l << 30);
    public final static long PERSON_SEC     = (1l << 31);
    public final static long PERSON_TER     = (1l << 32);
    public final static long MASK_PERSON    = PERSON_PRI | PERSON_SEC | PERSON_TER;
    
    public final static long DEGREE_POS     = (1l << 33);
    public final static long DEGREE_COMP    = (1l << 34);
    public final static long DEGREE_SUP     = (1l << 35);
    public final static long MASK_DEGREE    = DEGREE_POS | DEGREE_COMP | DEGREE_SUP;

    public final static long ASPECT_PERF 	= (1l << 36);
    public final static long ASPECT_IMPERF  = (1l << 37);
    public final static long MASK_ASPECT    = ASPECT_PERF | ASPECT_IMPERF;

    public final static long VOCALITY_WOK   = (1l << 48);
    public final static long VOCALITY_NWOK  = (1l << 49);

    public final static long AGLUT_AGL      = (1l << 48);
    public final static long AGLUT_NAGL     = (1l << 49);
    
    public final static long ACCOM_CONGR    = (1l << 44);
    public final static long ACCOM_REC      = (1l << 45);
    
    public final static long PPRAEP_PRAEP   = (1l << 42);
    public final static long PPRAEP_NPRAEP  = (1l << 43);
    
    public final static long ACC_ACC        = (1l << 40);
    public final static long ACC_NACC       = (1l << 41);
    
    public final static long NEG_AFF        = (1l << 38);
    public final static long NEG_NEG        = (1l << 39);

    private static final int EMIT_DEGREE   = 1 << 0;
    private static final int EMIT_VOCALITY = 1 << 1;
    private static final int EMIT_ASPECT   = 1 << 2;
    private static final int EMIT_CASE     = 1 << 3;
    private static final int EMIT_NUMBER   = 1 << 4;
    private static final int EMIT_GENDER   = 1 << 5;
    private static final int EMIT_NEGATION = 1 << 6;
    private static final int EMIT_PERSON   = 1 << 7;
    private static final int EMIT_POST_PREPOSITIONALITY = 1 << 8;
    private static final int EMIT_ACCENTABILITY = 1 << 9;
    private static final int EMIT_ACCOMMODABILITY = 1 << 10;
    private static final int EMIT_AGGLUTINATION = 1 << 11;


    private static final HashMap posCodes = new HashMap();
    static {
        posCodes.put("subst", new Long(NOUN_SUBST));
        posCodes.put("depr", new Long(NOUN_DEPR));
        posCodes.put("adj", new Long(ADJ_));
        posCodes.put("adja", new Long(ADJ_A));
        posCodes.put("adjp", new Long(ADJ_P));
        posCodes.put("adv", new Long(ADV));
        posCodes.put("num", new Long(NUM));
        posCodes.put("ppron12", new Long(PPRON_12));
        posCodes.put("ppron3", new Long(PPRON_3));
        posCodes.put("siebie", new Long(PPRON_SIEBIE));
        posCodes.put("fin", new Long(VERB_FIN));
        posCodes.put("bedzie", new Long(VERB_BEDZIE));
        posCodes.put("aglt", new Long(VERB_AGLT));
        posCodes.put("praet", new Long(VERB_PRAET));
        posCodes.put("impt", new Long(VERB_IMPT));
        posCodes.put("imps", new Long(VERB_IMPS));
        posCodes.put("inf", new Long(VERB_INF));
        posCodes.put("pcon", new Long(VERB_PCON));
        posCodes.put("pant", new Long(VERB_PANT));
        posCodes.put("ger", new Long(VERB_GER));
        posCodes.put("pact", new Long(VERB_PACT));
        posCodes.put("ppas", new Long(VERB_PPAS));
        posCodes.put("winien", new Long(WINIEN));
        posCodes.put("pred", new Long(PRED));
        posCodes.put("prep", new Long(PREP));
        posCodes.put("conj", new Long(CONJ));
        posCodes.put("qub", new Long(QUB));
        posCodes.put("xxs", new Long(XXS));
        posCodes.put("xxx", new Long(XXX));
        posCodes.put("interp", new Long(INTERP));
        posCodes.put("ign", new Long(IGN));
    }

    /**
     * Creates a new tag from its string notation.
     */
    private Tag(String tagCode) {
        parse(tagCode);
    }
    
    private void assertTrue(boolean condition, String message) {
        if (condition == false) throw new RuntimeException(message);
    }

    private long assertValidReturnCode(HashMap allowed, String values) {
        long code = 0;
        if (values.equals("_")) {
            for (Iterator i = allowed.values().iterator(); i.hasNext(); ) {
                code |= ((Long) i.next()).longValue();
            }
        } else {
            if (values.indexOf('.') >= 0) {
                String [] v = values.split("\\.");
                for (int i = 0; i < v.length; i++) {
                    Long l = (Long) allowed.get(v[i]);
                    if (l == null) {
                        throw new RuntimeException("Illegal attribute value: " + v[i]);
                    }
                    code |= l.longValue();
                }
            } else {
                Long l = (Long) allowed.get(values);
                if (l == null) {
                    throw new RuntimeException("Illegal attribute value: " + values);
                }
                code |= l.longValue();
            }
        }
        return code;
    }


    
    private final static HashMap ASPECT_VALUES = new HashMap();
    private final static String [] ASPECT_VALUES_ORDER = {"imperf", "perf"};
    static {
        ASPECT_VALUES.put("imperf", new Long(ASPECT_IMPERF));
        ASPECT_VALUES.put("perf", new Long(ASPECT_PERF));
    }
    private long parseAspect(String [] codes, int position) {
        assertTrue(position < codes.length, "Aspect not present in tag data.");
        this.aspect = codes[position];
        return assertValidReturnCode(ASPECT_VALUES, aspect);
    }



    private final static HashMap DEGREE_VALUES = new HashMap();
	private final static String [] DEGREE_VALUES_ORDER = {"pos", "comp", "sup"};
    static {
        DEGREE_VALUES.put("pos", new Long(DEGREE_POS));
        DEGREE_VALUES.put("comp", new Long(DEGREE_COMP));
        DEGREE_VALUES.put("sup", new Long(DEGREE_SUP));
    }
    private long parseDegree(String [] codes, int position) {
        assertTrue(position < codes.length, "Degree not present in tag data.");
        this.degree = codes[position];
        return assertValidReturnCode(DEGREE_VALUES, codes[position]);
    }



    private final static HashMap VOCALITY_VALUES = new HashMap();
    private final static String [] VOCALITY_VALUES_ORDER = {"wok", "nwok"};
    static {
        VOCALITY_VALUES.put("nwok", new Long(VOCALITY_NWOK));
        VOCALITY_VALUES.put("wok", new Long(VOCALITY_WOK));
    }
    private long parseVocality(String [] codes, int position) {
        assertTrue(position < codes.length, "Vocality not present in tag data.");
        this.vocalicity = codes[position];
        return assertValidReturnCode(VOCALITY_VALUES, codes[position]);
    }
    
    

    private final static HashMap CASE_VALUES = new HashMap();
    private final static String [] CASE_VALUES_ORDER = {"nom", "gen", "dat", "acc", "inst", "loc", "voc"};
    static {
        CASE_VALUES.put("nom", new Long(CASE_NOM));
        CASE_VALUES.put("gen", new Long(CASE_GEN));
        CASE_VALUES.put("dat", new Long(CASE_DAT));
        CASE_VALUES.put("acc", new Long(CASE_ACC));
        CASE_VALUES.put("inst", new Long(CASE_INST));
        CASE_VALUES.put("loc", new Long(CASE_LOC));
        CASE_VALUES.put("voc", new Long(CASE_VOC));
    }
    private long parseCase(String [] codes, int position) {
        assertTrue(position < codes.length, "Case not present in tag data.");
        this._case = codes[position];
        return assertValidReturnCode(CASE_VALUES, codes[position]);
    }

    
    
    private final static HashMap NUMBER_VALUES = new HashMap();
    private final static String [] NUMBER_VALUES_ORDER = {"sg", "pl"};
    static {
        NUMBER_VALUES.put("sg", new Long(NUMBER_SG));
        NUMBER_VALUES.put("pl", new Long(NUMBER_PL));
    }
    private long parseNumber(String [] codes, int position) {
        assertTrue(position < codes.length, "Number not present in tag data.");
        this.number = codes[position];
        return assertValidReturnCode(NUMBER_VALUES, codes[position]);
    }



    private final static HashMap GENDER_VALUES = new HashMap();
    private final static String[] GENDER_VALUES_ORDER = {"m1", "m2", "m3", "f", "n1", "n2", "p1", "p2", "p3"};
    private final static String[] GENDER_VALUES_ORDER_IPI_WSTEPNY = {"m1", "m2", "m3", "f", "n", "p1", "p2", "p3"};
    static {
        GENDER_VALUES.put("m1", new Long(GENDER_M1));
        GENDER_VALUES.put("m2", new Long(GENDER_M2));
        GENDER_VALUES.put("m3", new Long(GENDER_M3));
        GENDER_VALUES.put("f", new Long(GENDER_F));
        GENDER_VALUES.put("n1", new Long(GENDER_N1));
        GENDER_VALUES.put("n2", new Long(GENDER_N2));
        GENDER_VALUES.put("n", new Long(GENDER_N)); // IPI-WSTEPNY
        GENDER_VALUES.put("p1", new Long(GENDER_P1));
        GENDER_VALUES.put("p2", new Long(GENDER_P2));
        GENDER_VALUES.put("p3", new Long(GENDER_P3));
    }
    private long parseGender(String [] codes, int position) {
        assertTrue(position < codes.length, "Gender not present in tag data.");
        this.gender = codes[position];
        return assertValidReturnCode(GENDER_VALUES, codes[position]);
    }

    
    
    private final static HashMap NEGATION_VALUES = new HashMap();
    private final static String [] NEGATION_VALUES_ORDER = {"aff", "neg"};
    static {
        NEGATION_VALUES.put("aff", new Long(NEG_AFF));
        NEGATION_VALUES.put("neg", new Long(NEG_NEG));
    }
    private long parseNegation(String [] codes, int position) {
        assertTrue(position < codes.length, "Negation not present in tag data.");
        this.negation = codes[position];
        return assertValidReturnCode(NEGATION_VALUES, codes[position]);
    }



    private final static HashMap PERSON_VALUES = new HashMap();
    private final static String [] PERSON_VALUES_ORDER = {"pri", "sec", "ter"};
    static {
        PERSON_VALUES.put("pri", new Long(PERSON_PRI));
        PERSON_VALUES.put("sec", new Long(PERSON_SEC));
        PERSON_VALUES.put("ter", new Long(PERSON_TER));
    }
    private long parsePerson(String [] codes, int position) {
        assertTrue(position < codes.length, "Person not present in tag data.");
        this.person = codes[position];
        return assertValidReturnCode(PERSON_VALUES, codes[position]);
    }


    
    private final static HashMap ACCENTABILITY_VALUES = new HashMap();
    private final static String [] ACCENTABILITY_VALUES_ORDER = {"akc", "nakc"};
    static {
        ACCENTABILITY_VALUES.put("akc", new Long(ACC_ACC));
        ACCENTABILITY_VALUES.put("nakc", new Long(ACC_NACC));
    }
    private long parseAccentability(String [] codes, int position) {
        assertTrue(position < codes.length, "Accentability not present in tag data.");
        this.accentability = codes[position];
        return assertValidReturnCode(ACCENTABILITY_VALUES, codes[position]);
    }

    
    
    private final static HashMap POST_PREPOSITIONALITY_VALUES = new HashMap();
    private final static String [] POST_PREPOSITIONALITY_VALUES_ORDER = {"praep", "npraep"};
    static {
        POST_PREPOSITIONALITY_VALUES.put("npraep", new Long(PPRAEP_NPRAEP));
        POST_PREPOSITIONALITY_VALUES.put("praep", new Long(PPRAEP_PRAEP));
    }
    private long parsePostPrepositionality(String [] codes, int position) {
        assertTrue(position < codes.length, "Post-prepositionality not present in tag data.");
        this.postPrepositionality = codes[position];
        return assertValidReturnCode(POST_PREPOSITIONALITY_VALUES, codes[position]);
    }


    
    private final static HashMap ACCOMMODABILITY_VALUES = new HashMap();
    private final static String [] ACCOMMODABILITY_VALUES_ORDER = {"congr", "rec"};
    static {
        ACCOMMODABILITY_VALUES.put("congr", new Long(ACCOM_CONGR));
        ACCOMMODABILITY_VALUES.put("rec", new Long(ACCOM_REC));
    }
    private long parseAccommodability(String [] codes, int position) {
        assertTrue(position < codes.length, "Accommodability not present in tag data.");
        this.accommodability = codes[position];
        return assertValidReturnCode(ACCOMMODABILITY_VALUES, codes[position]);
    }

    
    
    private final static HashMap AGGLUTINATION_VALUES = new HashMap();
    private final static String [] AGGLUTINATION_VALUES_ORDER = {"agl", "nagl"};
    static {
        AGGLUTINATION_VALUES.put("agl", new Long(AGLUT_AGL));
        AGGLUTINATION_VALUES.put("nagl", new Long(AGLUT_NAGL));
    }
    private long parseAgglutination(String [] codes, int position) {
        assertTrue(position < codes.length, "Agglutination not present in tag data.");
        this.agglutination = codes[position];
        return assertValidReturnCode(AGGLUTINATION_VALUES, codes[position]);
    }

    
    
    private void parse(String tagCode) {
        String [] codes = tagCode.split(":");

        this.partOfSpeech = codes[0];
        long tagLongCode = 0;

        if ("adja".equals(partOfSpeech)
                || "adjp".equals(partOfSpeech)
                || "conj".equals(partOfSpeech)
                || "interp".equals(partOfSpeech)
                || "pred".equals(partOfSpeech)
                || "xxx".equals(partOfSpeech)
                || "ign".equals(partOfSpeech)) {
            assertTrue(codes.length == 1,
                    "Unknown extra data associated with POS tag: " + partOfSpeech);
        } else if ("adv".equals(partOfSpeech)) {
            tagLongCode |= parseDegree(codes, 1);
            assertTrue(codes.length == 2, "Incorrect extra tag data: " + tagCode);
        } else if ("imps".equals(partOfSpeech)
                || "inf".equals(partOfSpeech)
                || "pant".equals(partOfSpeech)
                || "pcon".equals(partOfSpeech)) {
            tagLongCode |= parseAspect(codes, 1);
            assertTrue(codes.length == 2, "Incorrect extra tag data: " + tagCode);
        } else if ("qub".equals(partOfSpeech)) {
            if (codes.length > 1) {
                tagLongCode |= parseVocality(codes, 1);
                assertTrue(codes.length == 2, "Incorrect extra tag data: " + tagCode);
            } else {
                assertTrue(codes.length == 1, "Incorrect extra tag data: " + tagCode);
            }
        } else if ("prep".equals(partOfSpeech)) {
            tagLongCode |= parseCase(codes, 1);
            if (codes.length > 2) {
                tagLongCode |= parseVocality(codes, 2);
                assertTrue(codes.length == 3, "Incorrect extra tag data: " + tagCode);
            } else {
                assertTrue(codes.length == 2, "Incorrect extra tag data: " + tagCode);
            }
        } else if ("siebie".equals(partOfSpeech)) {
            tagLongCode |= parseCase(codes, 1);
        } else if ("subst".equals(partOfSpeech)
                || "depr".equals(partOfSpeech)
                || "xxs".equals(partOfSpeech)) {
            tagLongCode |= parseNumber(codes, 1);
            tagLongCode |= parseCase(codes, 2);
            tagLongCode |= parseGender(codes, 3);
            assertTrue(codes.length == 4, "Incorrect extra tag data: " + tagCode);
        } else if ("ger".equals(partOfSpeech)) {
            tagLongCode |= parseNumber(codes, 1);
            tagLongCode |= parseCase(codes, 2);
            tagLongCode |= parseGender(codes, 3);
            tagLongCode |= parseAspect(codes, 4);
            tagLongCode |= parseNegation(codes, 5);
            assertTrue(codes.length == 6, "Incorrect extra tag data: " + tagCode);
        } else if ("ppron12".equals(partOfSpeech)) {
            tagLongCode |= parseNumber(codes, 1);
            tagLongCode |= parseCase(codes, 2);
            tagLongCode |= parseGender(codes, 3);
            tagLongCode |= parsePerson(codes, 4);
            if (codes.length > 5) {
                tagLongCode |= parseAccentability(codes, 5);
                assertTrue(codes.length == 6, "Incorrect extra tag data: " + tagCode);
            } else {
                assertTrue(codes.length == 5, "Incorrect extra tag data: " + tagCode);
            }
        } else if ("ppron3".equals(partOfSpeech)) {
            tagLongCode |= parseNumber(codes, 1);
            tagLongCode |= parseCase(codes, 2);
            tagLongCode |= parseGender(codes, 3);
            tagLongCode |= parsePerson(codes, 4);
            int i = 5;
            if (codes.length > 5) {
                if (!codes[i].endsWith("p")) {
                    tagLongCode |= parseAccentability(codes, i);
                    i++;
                }
                if (i < codes.length) {
                    tagLongCode |= parsePostPrepositionality(codes, i);
                    i++;
                }
            }
            assertTrue(codes.length == i, "Incorrect extra tag data: " + tagCode);
        } else if ("num".equals(partOfSpeech)) {
            tagLongCode |= parseNumber(codes, 1);
            tagLongCode |= parseCase(codes, 2);
            tagLongCode |= parseGender(codes, 3);
            if (codes.length > 4) {
                tagLongCode |= parseAccommodability(codes, 4);
            }
        } else if ("adj".equals(partOfSpeech)) {
            tagLongCode |= parseNumber(codes, 1);
            tagLongCode |= parseCase(codes, 2);
            tagLongCode |= parseGender(codes, 3);
            tagLongCode |= parseDegree(codes, 4);
            assertTrue(codes.length == 5, "Incorrect extra tag data: " + tagCode);
        } else if ("pact".equals(partOfSpeech)
                || "ppas".equals(partOfSpeech)) {
            tagLongCode |= parseNumber(codes, 1);
            tagLongCode |= parseCase(codes, 2);
            tagLongCode |= parseGender(codes, 3);
            tagLongCode |= parseAspect(codes, 4);
            tagLongCode |= parseNegation(codes, 5);
            assertTrue(codes.length == 6, "Incorrect extra tag data: " + tagCode);
        } else if ("bedzie".equals(partOfSpeech)
                || "fin".equals(partOfSpeech)
                || "impt".equals(partOfSpeech)) {
            tagLongCode |= parseNumber(codes, 1);
            tagLongCode |= parsePerson(codes, 2);
            tagLongCode |= parseAspect(codes, 3);
            assertTrue(codes.length == 4, "Incorrect extra tag data: " + tagCode);
        } else if ("winien".equals(partOfSpeech)) {
            tagLongCode |= parseNumber(codes, 1);
            tagLongCode |= parseGender(codes, 2);
            tagLongCode |= parseAspect(codes, 3);
            assertTrue(codes.length == 4, "Incorrect extra tag data: " + tagCode);
        } else if ("praet".equals(partOfSpeech)) {
            tagLongCode |= parseNumber(codes, 1);
            tagLongCode |= parseGender(codes, 2);
            tagLongCode |= parseAspect(codes, 3);
            int i = 4;
            if (codes.length > 4) {
                tagLongCode |= parseAgglutination(codes, i);
                i++;
            }
            assertTrue(codes.length == i, "Incorrect extra tag data: " + tagCode);
        } else if ("aglt".equals(partOfSpeech)) {
            tagLongCode |= parseNumber(codes, 1);
            tagLongCode |= parsePerson(codes, 2);
            tagLongCode |= parseAspect(codes, 3);
            tagLongCode |= parseVocality(codes, 4);
            assertTrue(codes.length == 5, "Incorrect extra tag data: " + tagCode);
        } else {
            throw new TagParserException("Unknown POS tag: '" + tagCode + "'");
        }

        Long posCode = (Long) posCodes.get(partOfSpeech);
        tagLongCode = tagLongCode | posCode.longValue();
        this.tagCode = tagLongCode;
    }

    public long getCode() {
        return tagCode;
    }
    
    public static String toString(final long code) {
        StringBuffer buf = new StringBuffer();

        switch ((int) (code & MASK_POS)) {
            case (int)NOUN_SUBST   : buf.append("subst"); break;
            case (int)NOUN_DEPR    : buf.append("depr"); break;
            case (int)NOUN         : buf.append("NOUN"); break;

            case (int)ADJ          : buf.append("ADJ"); break;
            case (int)ADJ_         : buf.append("adj"); break;
            case (int)ADJ_A        : buf.append("adja"); break;
            case (int)ADJ_P        : buf.append("adjp"); break;

            case (int)ADV          : buf.append("adv"); break;
            case (int)NUM          : buf.append("num"); break;
            case (int)PPRON        : buf.append("ppron"); break;

            case (int)PPRON_12     : buf.append("ppron12"); break;
            case (int)PPRON_3      : buf.append("ppron3"); break;
            case (int)PPRON_SIEBIE : buf.append("siebie"); break;

            case (int)VERB         : buf.append("VERB"); break;
            case (int)VERB_FIN     : buf.append("fin"); break;
            case (int)VERB_BEDZIE  : buf.append("bedzie"); break;
            case (int)VERB_AGLT    : buf.append("aglt"); break;
            case (int)VERB_PRAET   : buf.append("praet"); break;
            case (int)VERB_IMPT    : buf.append("impt"); break;
            case (int)VERB_IMPS    : buf.append("imps"); break;
            case (int)VERB_INF     : buf.append("inf"); break;
            case (int)VERB_PCON    : buf.append("pcon"); break;
            case (int)VERB_PANT    : buf.append("pant"); break;
            case (int)VERB_GER     : buf.append("ger"); break;
            case (int)VERB_PACT    : buf.append("pact"); break;
            case (int)VERB_PPAS    : buf.append("ppas"); break;

            case (int)WINIEN       : buf.append("winien"); break;
            case (int)PRED         : buf.append("pred"); break;
            case (int)PREP         : buf.append("prep"); break;
            case (int)CONJ         : buf.append("conj"); break;
            case (int)QUB          : buf.append("qub"); break;
            case (int)XXS          : buf.append("xxs"); break;
            case (int)XXX          : buf.append("xxx"); break;
            case (int)INTERP       : buf.append("interp"); break;
            case (int)IGN          : buf.append("ign"); break;
            case (int)UNKNOWN      : buf.append("?"); break;
            default:
                throw new RuntimeException("Unreachable state.");
        }

        int emitChunks = 0;

        switch ((int) (code & MASK_POS)) {
            case (int) ADJ_A:
            case (int) ADJ_P:
            case (int) CONJ:
            case (int) INTERP:
            case (int) PRED:
            case (int) XXX:
            case (int) IGN:
                break;
            case (int) ADV:
                emitChunks |= EMIT_DEGREE;
                break;
            case (int) VERB_IMPS:
            case (int) VERB_INF:
            case (int) VERB_PANT:
            case (int) VERB_PCON:
                emitChunks |= EMIT_ASPECT;
                break;
            case (int) QUB:
                emitChunks |= EMIT_VOCALITY;
                break;
            case (int) PREP:
                emitChunks |= EMIT_CASE | EMIT_VOCALITY;
                break;
            case (int) PPRON:
                emitChunks |= EMIT_NUMBER | EMIT_CASE | EMIT_GENDER | EMIT_PERSON | EMIT_ACCENTABILITY | EMIT_POST_PREPOSITIONALITY;
                break;
            case (int) PPRON_SIEBIE:
                emitChunks |= EMIT_CASE;
                break;
            case (int) NOUN:
            case (int) NOUN_SUBST:
            case (int) NOUN_DEPR:
            case (int) XXS:
                emitChunks |= EMIT_NUMBER | EMIT_CASE | EMIT_GENDER;
                break;
            case (int) VERB_GER:
                emitChunks |= EMIT_NUMBER | EMIT_CASE | EMIT_GENDER | EMIT_ASPECT | EMIT_NEGATION;
                break;
            case (int) PPRON_12:
                emitChunks |= EMIT_NUMBER | EMIT_CASE | EMIT_GENDER | EMIT_PERSON | EMIT_ACCENTABILITY;
                break;
            case (int) PPRON_3:
                emitChunks |= EMIT_NUMBER | EMIT_CASE | EMIT_GENDER | EMIT_PERSON | EMIT_ACCENTABILITY | EMIT_POST_PREPOSITIONALITY;
                break;
            case (int) NUM:
                emitChunks |= EMIT_NUMBER | EMIT_CASE | EMIT_GENDER | EMIT_ACCOMMODABILITY;
                break;
            case (int) ADJ:
            case (int) ADJ_:
                emitChunks |= EMIT_NUMBER | EMIT_CASE | EMIT_GENDER | EMIT_DEGREE;
                break;
            case (int) VERB:
            case (int) VERB_PACT:
            case (int) VERB_PPAS:
                emitChunks |= EMIT_NUMBER | EMIT_CASE | EMIT_GENDER | EMIT_ASPECT | EMIT_NEGATION;
                break;
            case (int) VERB_BEDZIE:
            case (int) VERB_FIN:
            case (int) VERB_IMPT:
                emitChunks |= EMIT_NUMBER | EMIT_PERSON | EMIT_ASPECT;
                break;
            case (int) WINIEN:
                emitChunks |= EMIT_NUMBER | EMIT_GENDER | EMIT_ASPECT;
                break;
            case (int) VERB_PRAET:
                emitChunks |= EMIT_NUMBER | EMIT_GENDER | EMIT_ASPECT | EMIT_AGGLUTINATION;
                break;
            case (int) VERB_AGLT:
                emitChunks |= EMIT_NUMBER | EMIT_PERSON | EMIT_ASPECT | EMIT_VOCALITY;
                break;
            default:
                throw new RuntimeException("Unreachable state.");
        }
        
        if ((emitChunks & EMIT_NUMBER) != 0 && (code & MASK_NUMBER) != 0) {
            emit(buf, NUMBER_VALUES_ORDER, NUMBER_VALUES, code);
        }
        if ((emitChunks & EMIT_CASE) != 0) {
            emit(buf, CASE_VALUES_ORDER, CASE_VALUES, code);
        }
        if ((emitChunks & EMIT_GENDER) != 0 && (code & MASK_GENDER) != 0) {
            String [] codesOrder;
            if ((code & (GENDER_N1 | GENDER_N2)) == GENDER_N) {
                // There is NO specific gender code. Emit 'N' emission then.
                // IPI-WSTEPNY
                codesOrder = GENDER_VALUES_ORDER_IPI_WSTEPNY;
            } else {
                codesOrder = GENDER_VALUES_ORDER;
            }
            emit(buf, codesOrder, GENDER_VALUES, code & MASK_GENDER);
        }
        if ((emitChunks & EMIT_PERSON) != 0) {
            emit(buf, PERSON_VALUES_ORDER, PERSON_VALUES, code);
        }
        if ((emitChunks & EMIT_DEGREE) != 0) {
            emit(buf, DEGREE_VALUES_ORDER, DEGREE_VALUES, code);
        }
        if ((emitChunks & EMIT_ASPECT) != 0) {
            emit(buf, ASPECT_VALUES_ORDER, ASPECT_VALUES, code);
        }
        if ((emitChunks & EMIT_NEGATION) != 0) {
            emit(buf, NEGATION_VALUES_ORDER, NEGATION_VALUES, code);
        }
        if ((emitChunks & EMIT_ACCENTABILITY) != 0) {
            emit(buf, ACCENTABILITY_VALUES_ORDER, ACCENTABILITY_VALUES, code);
        }
        if ((emitChunks & EMIT_POST_PREPOSITIONALITY) != 0) {
            emit(buf, POST_PREPOSITIONALITY_VALUES_ORDER, POST_PREPOSITIONALITY_VALUES, code);
        }
        if ((emitChunks & EMIT_ACCOMMODABILITY) != 0) {
            emit(buf, ACCOMMODABILITY_VALUES_ORDER, ACCOMMODABILITY_VALUES, code);
        }
        if ((emitChunks & EMIT_AGGLUTINATION) != 0) {
            emit(buf, AGGLUTINATION_VALUES_ORDER, AGGLUTINATION_VALUES, code);
        }
        if ((emitChunks & EMIT_VOCALITY) != 0) {
            emit(buf, VOCALITY_VALUES_ORDER, VOCALITY_VALUES, code);
        }

        return buf.toString();
    }

    public String toString() {
        return toString(this.tagCode);
    }
    
    private static void emit(StringBuffer buf, String [] keys, HashMap values, long code) {
    	buf.append(':');
    	boolean emitted = false;
    	long allValues = 0;
    	for (int i = 0; i< keys.length; i++) {
    		final String key = keys[i];
    		long bitmask = ((Long) values.get(key)).longValue();
    		allValues |= bitmask;
    		if ((code & bitmask) == bitmask) {
    			if (emitted) {
    				buf.append('.');
    			} else {
					emitted = true;
				}
    			buf.append(key);
    		}
    	}
    	if (!emitted) {
    		buf.deleteCharAt(buf.length()-1);
    	} else {
    		if (EMIT_UNDERSCORE && allValues == code) {
    			buf.delete(buf.lastIndexOf(":") + 1, Integer.MAX_VALUE);
    			buf.append("_");
    		}
    	}
    }

    public static Tag[] create(final String tagAlternatives) {
        if (tagAlternatives.length() == 0) {
            return new Tag[0];
        }

        final String [] codes = tagAlternatives.split("\\|");
        final Tag[] tagList = new Tag[codes.length];
        for (int i=0; i<tagList.length; i++) {
            tagList[i] = new Tag(codes[i]);
        }

        return tagList;
    }
    
    public static boolean contained(final long widerTag, final long narrowTag) {
        if ((widerTag & Tag.MASK_POS) == (narrowTag & Tag.MASK_POS)) {
            final long masked = narrowTag & widerTag;

            if (((widerTag & MASK_ASPECT) != 0) && (masked & MASK_ASPECT) == 0)
                return false;
            if (((widerTag & MASK_CASE) != 0) && (masked & MASK_CASE) == 0)
                return false;
            if (((widerTag & MASK_DEGREE) != 0) && (masked & MASK_DEGREE) == 0)
                return false;
            if (((widerTag & MASK_GENDER) != 0) && (masked & MASK_GENDER) == 0)
                return false;
            if (((widerTag & MASK_NUMBER) != 0) && (masked & MASK_NUMBER) == 0)
                return false;
            if (((widerTag & MASK_PERSON) != 0) && (masked & MASK_PERSON) == 0)
                return false;

            return true;
        }
        return false;
    }
}
