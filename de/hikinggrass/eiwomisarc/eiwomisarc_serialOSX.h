/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class de_hikinggrass_eiwomisarc_Serial */

#ifndef _Included_de_hikinggrass_eiwomisarc_Serial
#define _Included_de_hikinggrass_eiwomisarc_Serial
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     de_hikinggrass_eiwomisarc_Serial
 * Method:    externFuntion
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_de_hikinggrass_eiwomisarc_Serial_externFuntion
  (JNIEnv *, jobject, jstring);

/*
 * Class:     de_hikinggrass_eiwomisarc_Serial
 * Method:    externTest
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_de_hikinggrass_eiwomisarc_Serial_externTest
  (JNIEnv *, jobject, jint);

/*
 * Class:     de_hikinggrass_eiwomisarc_Serial
 * Method:    externOpenPort
 * Signature: (Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_de_hikinggrass_eiwomisarc_Serial_externOpenPort
  (JNIEnv *, jobject, jstring, jint);

/*
 * Class:     de_hikinggrass_eiwomisarc_Serial
 * Method:    externClosePort
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_de_hikinggrass_eiwomisarc_Serial_externClosePort
  (JNIEnv *, jobject);

/*
 * Class:     de_hikinggrass_eiwomisarc_Serial
 * Method:    externWrite
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_de_hikinggrass_eiwomisarc_Serial_externWrite
  (JNIEnv *, jobject, jbyteArray);

#ifdef __cplusplus
}
#endif
#endif
