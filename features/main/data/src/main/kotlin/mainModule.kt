
import consts.HttpConstants
import ktor.KtorMainRemoteDataSource
import org.koin.core.qualifier.named
import org.koin.dsl.module
import room.RoomMainLocalDataSource

val mainModule = module {
    single<KtorMainRemoteDataSource> {
        KtorMainRemoteDataSource(
            hcStock = get(named(HttpConstants.Core.Stock.CLIENT_NAME)),
            hcExchange = get(named(HttpConstants.Core.Exchange.CLIENT_NAME)),
            hcNews = get(named(HttpConstants.Core.News.CLIENT_NAME))
        )
    }

    single<RoomMainLocalDataSource> {
        RoomMainLocalDataSource(get())
    }

    single<MainRepository> {
        MainRepositoryImpl(get(), get())
    }
}