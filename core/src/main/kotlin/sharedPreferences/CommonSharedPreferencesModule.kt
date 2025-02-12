package sharedPreferences
import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal val commonSharedPreferencesModule = module {
    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            "commonSharedPreference",
            Context.MODE_PRIVATE
        )
    }
}