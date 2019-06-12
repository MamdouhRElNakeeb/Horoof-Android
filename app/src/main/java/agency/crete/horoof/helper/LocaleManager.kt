package agency.crete.horoof.helper

import android.os.Build
import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.preference.PreferenceManager
import java.util.*


class LocaleManager {

    companion object {
        private const val LANGUAGE_ARABIC = "ar"
        private const val LANGUAGE_KEY = "display_lang"

        fun setNewLocale(c: Context, language: String): Context {
            persistLanguage(c, language)
            return updateResources(c, language)
        }

        private fun persistLanguage(c: Context, language: String) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(c)
            prefs.edit().putString(LANGUAGE_KEY, language).apply()
        }

        private fun updateResources(context: Context, language: String): Context {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val config = Configuration(context.resources.configuration)
            config.setLocale(locale)
            return context.createConfigurationContext(config)
        }

        fun setLocale(c: Context): Context {
            return updateResources(c, getLanguage(c))
        }

        fun getLanguage(c: Context): String {
            val prefs = PreferenceManager.getDefaultSharedPreferences(c)
            return prefs.getString(LANGUAGE_KEY, LANGUAGE_ARABIC)
        }
    }

}