package room

import ManagePostDTO
import Post
import SocialRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import room.postFavourites.encryption.EncryptionUtils

class SocialRepositoryImpl(
    private val localDataSource: RoomSocialLocalDataSource
) : SocialRepository {
    override suspend fun insertPost(managePostDTO: ManagePostDTO) {
        localDataSource.insertManagePostDTO(managePostDTO)
    }

    override suspend fun fetchPosts(): Flow<List<Post>> =
        localDataSource.fetchPostsEntities()
            .combine(localDataSource.fetchPostsFavourites()) { posts, favourites ->
                val decryptedFavourites = favourites.map { EncryptionUtils.decrypt(it.postId).toLong() }
                posts.map { post ->
                    post.toPost(isFavourite = post.post.id in decryptedFavourites)
                }
            }


    override suspend fun saveFavourite(id: Long) {
        localDataSource.insertFavouritePost(id)
    }

    override suspend fun deleteFavourite(id: Long) {
        localDataSource.deleteFromFavourites(id)
    }


}