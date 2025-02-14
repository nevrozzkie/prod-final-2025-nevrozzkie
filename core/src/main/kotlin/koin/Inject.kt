package koin


import org.koin.core.qualifier.named
import org.koin.mp.KoinPlatformTools

object Inject {
    inline fun <reified T : Any> instance(): T {
        return KoinPlatformTools.defaultContext().get().get<T>()
    }

    inline fun <reified T : Any> instance(name: String): T {
        return KoinPlatformTools.defaultContext().get().get<T>(named(name))
    }
}