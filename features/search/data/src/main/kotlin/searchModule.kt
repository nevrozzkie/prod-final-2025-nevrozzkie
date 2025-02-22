import consts.HttpConstants
import ktor.KtorSearchRemoteDataSource
import org.koin.core.qualifier.named
import org.koin.dsl.module

val searchModule = module {
    single<KtorSearchRemoteDataSource> {
        KtorSearchRemoteDataSource(
            hcStock = get(named(HttpConstants.Core.Stock.CLIENT_NAME)),
            hcNews = get(named(HttpConstants.Core.News.CLIENT_NAME)),

        )
    }

    single<SearchRepository> {
        SearchRepositoryImpl(get())
    }
}