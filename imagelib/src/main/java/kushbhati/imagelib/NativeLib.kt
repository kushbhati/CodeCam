package kushbhati.imagelib

class NativeLib {

    /**
     * A native method that is implemented by the 'imagelib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'imagelib' library on application startup.
        init {
            System.loadLibrary("imagelib")
        }
    }
}