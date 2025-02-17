data object HttpConstants {
    data object Core {
        data object News {
            const val CLIENT_NAME = "HttpClientNews"
            const val HOST = "api.nytimes.com"
            const val TOKEN_HEADER = "api-key"
            const val TOKEN = "SefpbOBF0C26dkV5vduYQWsebvOxNWoP"
        }

        data object Stock {
            const val CLIENT_NAME = "HttpClientStock"
            const val HOST = "finnhub.io"
            const val TOKEN_HEADER = "X-Finnhub-Token"
            const val TOKEN = "cum9nq9r01qsaphv45rgcum9nq9r01qsaphv45s0"
        }

        data object Exchange {
            const val CLIENT_NAME = "HttpClientExchange"
            const val HOST = "v6.exchangerate-api.com/v6"
            const val TOKEN = "bf0ad224573b24404c2e70a4"
        }
    }

    data object Paths {
        data object Stock {
            private const val PRE_PATH = "api/v1/"

            const val FETCH_PRICE = PRE_PATH + "quote"
            const val FETCH_INFO = PRE_PATH + "stock/profile2"
        }

        data object News {
            private const val PRE_PATH = "svc/news/v3/"
            const val FETCH_RECENT = PRE_PATH + "content/all/all.json"
        }
    }

    data object CacheSeconds {
        data object Stock {
            const val COMPANY_INFO = 60 * 60
            const val TICKER_PRICE = 0
        }
        data object Exchange {
            const val EXCHANGE_RATE = 60 * 30
        }
        data object News {
            // cuz we cache it in local
            const val RECENT_NEWS = 0
            const val RECENT_NEWS_IMAGE = 0
        }


        const val IMAGE = 60 * 60
    }
}