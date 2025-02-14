import ktor.HttpConstants
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainModule = module {
    single<KtorMainRemoteDataSource> {
        KtorMainRemoteDataSource(
            hcStock = get(named(HttpConstants.Stock.CLIENT_NAME)),
            hcExchange = get(named(HttpConstants.Exchange.CLIENT_NAME)),
            hcNews = get(named(HttpConstants.News.CLIENT_NAME)),
            hcDefault = get(named(HttpConstants.Default.CLIENT_NAME))
        )
    }

    single<MainRepository> {
        MainRepositoryImpl(get())
    }
}