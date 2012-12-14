#include <stdio.h>
#include <stdlib.h>
#include "com_dawidweiss_morfeusz_Analyzer.h"
#include "morfeusz.h"

const static char *CVSID =
	"$Id$";

/*
 * Adjust these if you need longer tokens (although it is more reasonable
 * to split them before passing to the analyzer in such case...)
 */

/** Maximum length of a single token. */
#define MAX_TOKEN_LENGTH 4096

/** An internal buffer to copy the token to. */
static char token_buffer[ MAX_TOKEN_LENGTH ];


/**
 * A helper function to copy the contents of a string into
 * a Java array and a field that stores the length of the copied
 * string.
 */
inline int copyCharArray( JNIEnv *env, jobject object,
	jfieldID array_id, jfieldID array_length_id, char *src)
{
	if (src == NULL) {
		(*env)->SetIntField(env, object, array_length_id, (jint) 0);
		return 1;
	}

	jcharArray array =  (*env)->GetObjectField(env, object, array_id);
	char *array_contents = (*env)->GetByteArrayElements(env, array, NULL);

	jsize max_length = (*env)->GetArrayLength(env, array);

	int i = 0;
	char *tmpsrc = src;
	char *tmpdst = array_contents;
	while (tmpsrc[0] != 0) {
		if (i == max_length) {
			break;
		} else {
			*(tmpdst++) = *(tmpsrc++);
		}
		i++;
	}

	(*env)->ReleaseByteArrayElements(env, array, array_contents, 0);

	if (i == max_length) {
		char message[512];
		
		sprintf(message, "Buffer array length exceeded: [%li bytes needed]", strlen(src));

		jclass exception_class =
			(*env)->FindClass(env, "java/lang/RuntimeException");
		if (exception_class == NULL)
			return 0;
		(*env)->ThrowNew(env, exception_class, message);
		return 0;
	} else {
		(*env)->SetIntField(env, object, array_length_id, (jint) i);
		return 1;
	}
}


/** 
 * JNI method implementation.
 * @see com.dawidweiss.morfeusz.Analyzer#analyse()
 */
JNIEXPORT jint JNICALL Java_com_dawidweiss_morfeusz_Analyzer_morfeusz_1analyse
  (JNIEnv *env, jobject object, jbyteArray term_array)
{
	char *term_bytes = (*env)->GetByteArrayElements(env, term_array, NULL);
	jsize term_length = (*env)->GetArrayLength(env, term_array );
	if (term_length + 1 > MAX_TOKEN_LENGTH) {
		jclass exception_class =
			(*env)->FindClass(env, "java/lang/RuntimeException");
		if (exception_class == NULL)
			return 0;
		(*env)->ThrowNew(env, exception_class,
			"Token too long. Adjust MAX_TOKEN_LENGTH");
		(*env)->ReleaseByteArrayElements(env, term_array, term_bytes, JNI_ABORT);
		return 0;
	} else {
		strncpy(&token_buffer[0], term_bytes, term_length);
		token_buffer[ term_length ] = 0;
		(*env)->ReleaseByteArrayElements(env, term_array, term_bytes, JNI_ABORT);
	}
	
	// perform the analysis.
	InterpMorf *interp_morf = morfeusz_analyse(token_buffer);

	// get a handle to the array of morphological analysis blocks for tokens.
	jclass myClass = (*env)->GetObjectClass(env, object);
	
	jfieldID morphological_analysis_array_id = (*env)->GetFieldID(env, 
		myClass,
		"morphologicalAnalysis",
		"[Lcom/dawidweiss/morfeusz/InterpMorf;");
	if (morphological_analysis_array_id == NULL) return 0;
	
	jobjectArray morphological_analysis_array = 
		(jobjectArray) (*env)->GetObjectField(env, object,
			morphological_analysis_array_id);
	if (morphological_analysis_array == NULL) return 0;
	
	// get method/ field IDs
	jclass interp_morf_class = (*env)->FindClass(env,
		"com/dawidweiss/morfeusz/InterpMorf");
	if (interp_morf_class == NULL) return 0;

	jfieldID token_id = (*env)->GetFieldID(env, interp_morf_class, "token", "[B");
	if (token_id == NULL) return 0;
	jfieldID token_length_id = (*env)->GetFieldID(env, interp_morf_class, "tokenLength", "I");
	if (token_length_id == NULL) return 0;

	jfieldID lemma_id = (*env)->GetFieldID(env, interp_morf_class, "lemma", "[B");
	if (lemma_id == NULL) return 0;
	jfieldID lemma_length_id = (*env)->GetFieldID(env, interp_morf_class, "lemmaLength", "I");
	if (lemma_length_id == NULL) return 0;

	jfieldID tag_id = (*env)->GetFieldID(env, interp_morf_class, "tag", "[B");
	if (tag_id == NULL) return 0;
	jfieldID tag_length_id = (*env)->GetFieldID(env, interp_morf_class, "tagLength", "I");
	if (tag_length_id == NULL) return 0;

	jfieldID p_id = (*env)->GetFieldID(env, interp_morf_class, "p", "I");
	if (p_id == NULL) return 0;
	jfieldID k_id = (*env)->GetFieldID(env, interp_morf_class, "k", "I");
	if (k_id == NULL) return 0;


	// now go through the elements of interp_morf and copy them to
	// analyzer's morphological_analysis_array fields.
	
	jsize max_segment_number = (*env)->GetArrayLength(env, morphological_analysis_array);

	jsize segment_index = 0;
	while (interp_morf->p != -1) {

		if (segment_index >= max_segment_number) {
			jclass exception_class =
				(*env)->FindClass(env, "java/lang/RuntimeException");
			if (exception_class == NULL)
				return 0;
			(*env)->ThrowNew(env, exception_class,
				"Tokens array length exceeded. Recompile with greater Analyzer.MAX_GRAPH_NODES");
			return 0;
		}
		
		// any Java exceptions (synchronous or asynchronous)?
		if ((*env)->ExceptionOccurred(env) != NULL)
			return 0;

		jobject morph_segment = 
			(*env)->GetObjectArrayElement(env, 
				morphological_analysis_array, segment_index);

		// copy start node and end node.
		(*env)->SetIntField(env, morph_segment, p_id, (jint) interp_morf->p );
		(*env)->SetIntField(env, morph_segment, k_id, (jint) interp_morf->k );
		
		// copy the token, lemma and tag arrays
		
		if (copyCharArray(env, morph_segment, token_id, token_length_id, interp_morf->forma ) == 0)
			return 0;
		if (copyCharArray(env, morph_segment, lemma_id, lemma_length_id, interp_morf->haslo ) == 0)
			return 0;
		if (copyCharArray(env, morph_segment, tag_id,   tag_length_id,   interp_morf->interp) == 0)
			return 0;

		// advance to the next token.
		interp_morf++;
		segment_index++;
	}

	return (jint) segment_index;
}

