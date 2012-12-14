#include <stdio.h>
#include <stdlib.h>
#include "com_dawidweiss_morfeusz_Morfeusz.h"
#include "morfeusz.h"

const static char *CVSID =
	"$Id$";


/**
 * Copies an array of bytes representing Morfeusz's 'about' text
 * to a Java byte array (encoded using iso-8859-2).
 *
 * @see com.dawidweiss.morfeusz.Morfeusz#about()
 */
JNIEXPORT jbyteArray JNICALL Java_com_dawidweiss_morfeusz_Morfeusz_aboutJniNative
  (JNIEnv *env, jobject object)
{
    char *about_text = morfeusz_about();

    jsize about_text_length = strlen(about_text);
    jbyteArray java_array = (*env)->NewByteArray( env, about_text_length );
    char *array_contents = (*env)->GetByteArrayElements(env, java_array, NULL);
    int i;
    for (i=0;i<about_text_length;i++) {
        array_contents[i] = about_text[i];
    }
    (*env)->ReleaseByteArrayElements(env, java_array, array_contents, 0); 
    return java_array;
}

/**
 * Class:     com_dawidweiss_morfeusz_Morfeusz
 * Method:    morfeusz_set_option
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_dawidweiss_morfeusz_Morfeusz_morfeusz_1set_1option
	(JNIEnv *env, jobject object, jint option, jint value) {
	return morfeusz_set_option(option, value);
}
