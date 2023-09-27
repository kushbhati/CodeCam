#include <string>

#include <jni.h>

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_kushbhati_imagelib_ImageProcessorHelper_clamp(
        JNIEnv *env,
        jobject thiz,
        jbyteArray _receiver
) {
    jbyte *arr = env->GetByteArrayElements(_receiver, nullptr);
    if (arr == nullptr) return nullptr;
    int size = env->GetArrayLength(_receiver);
    jbyteArray _output = env->NewByteArray(size);
    jbyte *new_arr = env->GetByteArrayElements(_output, nullptr);
    for (int i = 0; i < size; i++) {
        if (uint8_t(arr[i]) > 192) new_arr[i] = -1;//jbyte(uint8_t(uint8_t(arr[i]) * 4));
        else new_arr[i] = jbyte(uint8_t(arr[i]) / 4);
    }
    env->ReleaseByteArrayElements(_receiver, arr, JNI_ABORT);

    return _output;
}


// simple separated box blur implementation
extern "C"
JNIEXPORT jbyteArray JNICALL
Java_kushbhati_imagelib_ImageProcessorHelper_blur(
        JNIEnv *env,
        jobject thiz,
        jbyteArray _receiver,
        jint row_stride,
        jint row_count
) {
    jbyte *arr = env->GetByteArrayElements(_receiver, nullptr);
    if (arr == nullptr) return nullptr;
    int size = env->GetArrayLength(_receiver);

    int radius = 10;

    jbyteArray _output = env->NewByteArray(size);
    jbyte *new_arr = env->GetByteArrayElements(_output, nullptr);

    for (int i = radius; i < row_count - radius; i++) {
        for (int j = radius; j < row_stride - radius; j++) {
            int sum = 0;
            for (int k = i - radius; k < i + radius; k++)
                sum += reinterpret_cast<uint8_t&>((arr + row_stride * k)[j]);
            (new_arr + row_stride * i)[j] = jbyte(uint_t(sum / radius / 2));
        }
    }

    for (int i = radius; i < row_count - radius; i++) {
        for (int j = radius; j < row_stride - radius; j++) {
            int sum = 0;
            for (int l = j - radius; l < j + radius; l++)
                sum += reinterpret_cast<uint8_t&>((new_arr + row_stride * i)[l]);
            (new_arr + row_stride * i)[j] = jbyte(uint_t(sum / radius / 2));
        }
    }

    env->ReleaseByteArrayElements(_receiver, arr, JNI_ABORT);
    return _output;
}