import ktor.ktorModule
import org.koin.dsl.module
import sharedPreferences.commonSharedPreferencesModule

val coreModule = module {
    includes(
        ktorModule,
        commonSharedPreferencesModule
    )
}