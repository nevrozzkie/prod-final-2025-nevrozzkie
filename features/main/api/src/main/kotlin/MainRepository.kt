import utils.Ticker

interface MainRepository {

    suspend fun fetchTickers(ids: List<String>) : List<Ticker>

}