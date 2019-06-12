package agency.crete.horoof.helper

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import java.util.*


class Utils {

    companion object {
        fun updateLanguage(context: Context, selectedLanguage: String) {
            if (!selectedLanguage.isEmpty()) {
                val locale = Locale(selectedLanguage)
                Locale.setDefault(locale)
                val res = context.resources
                val config = Configuration()
                val dm = res.displayMetrics
                config.setLocale(locale)
//                context.resources.updateConfiguration(config, dm)
                Resources.getSystem().updateConfiguration(config, dm)

            }
        }
    }

}