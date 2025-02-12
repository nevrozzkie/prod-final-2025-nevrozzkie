package koin


import org.koin.mp.KoinPlatformTools

object Inject {
    inline fun <reified T : Any> instance(): T {
        return KoinPlatformTools.defaultContext().get().get<T>()
    }
}