package kushbhati.camcode

import android.app.Application

class CamCode : Application() {
    companion object {
        lateinit var application: Application
    }
    override fun onCreate() {
        super.onCreate()
        application = this
    }
}