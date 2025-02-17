import org.koin.dsl.module
import room.RoomFinanceLocalDataSource

val financeModule = module {
    single<RoomFinanceLocalDataSource> {
        RoomFinanceLocalDataSource(get())
    }

    single<FinanceRepository> {
        FinanceRepositoryImpl(get())
    }
}