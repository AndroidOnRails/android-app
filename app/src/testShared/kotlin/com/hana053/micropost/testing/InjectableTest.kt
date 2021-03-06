package com.hana053.micropost.testing

import android.preference.PreferenceManager
import com.github.salomonbrys.kodein.Kodein
import com.hana053.micropost.BaseApplication
import com.hana053.micropost.appModule
import org.junit.Before


interface InjectableTest {

    val app: BaseApplication

    @Before
    fun resetSharedPreferences() {
        PreferenceManager.getDefaultSharedPreferences(app).edit()
            .clear()
            .apply()

    }

    fun overrideAppBindings(init: Kodein.Builder.() -> Unit) {
        app.setKodein(Kodein {
            import(appModule(), allowOverride = true)
            import(blockedInteractorModule(), allowOverride = true)
            init()
        })
    }

}