package room

import ManagePostDTO
import Post
import SocialRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SocialRepositoryImpl(
    private val localDataSource: RoomSocialLocalDataSource
) : SocialRepository {
    override suspend fun insertPost(managePostDTO: ManagePostDTO) {
        localDataSource.insertManagePostDTO(managePostDTO)
    }

    override suspend fun fetchPosts(): Flow<List<Post>> = localDataSource.fetchPostsEntities().map { list ->
        println("Fetched posts with images: $list")
        list.map {

            it.toPost() }
    }

}