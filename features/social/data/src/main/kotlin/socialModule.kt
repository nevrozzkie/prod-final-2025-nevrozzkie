import org.koin.dsl.module
import room.RoomSocialLocalDataSource
import room.SocialRepositoryImpl

val socialModule = module {
    single<RoomSocialLocalDataSource> {
        RoomSocialLocalDataSource(get())
    }


    single<SocialRepository> {
        SocialRepositoryImpl(get())
    }
}