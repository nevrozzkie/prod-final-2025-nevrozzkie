import kotlinx.coroutines.flow.Flow

interface SocialRepository {

    suspend fun insertPost(managePostDTO: ManagePostDTO)

    suspend fun fetchPosts() : Flow<List<Post>>
}