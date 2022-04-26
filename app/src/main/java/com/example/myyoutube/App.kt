package com.example.myyoutube

import android.app.Application
import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.localization.ContentCountry
import org.schabi.newpipe.extractor.localization.Localization
import java.util.*

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        NewPipe.init(
            DownloaderImpl.init(null),
            Localization.fromLocale(Locale.getDefault()),
            ContentCountry(Locale.getDefault().country)
        )
    }
}