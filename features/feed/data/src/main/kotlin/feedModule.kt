import ktor.HttpConstants
import org.koin.core.qualifier.named
import org.koin.dsl.module

val feedModule = module {
    single<KtorFeedRemoteDataSource> {
        KtorFeedRemoteDataSource(
            hcStock = get(named(HttpConstants.Stock.CLIENT_NAME)),
            hcExchange = get(named(HttpConstants.Exchange.CLIENT_NAME)),
            hcNews = get(named(HttpConstants.News.CLIENT_NAME))
        )
    }

    single<FeedRepository> {
        FeedRepositoryImpl(get())
    }
}