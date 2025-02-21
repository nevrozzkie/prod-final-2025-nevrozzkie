import kotlinx.serialization.Serializable


data class Post(
    val id: Long,
    val images: List<ByteArray>,
    val tags: List<Int>,
    val text: String,
    val creationDate: String,
    val creationTime: String,
    val edited: Boolean,
    val newsData: PostNewsData?,
    val isFavourite: Boolean
)

@Serializable
data class PostNewsData(
    val id: String,
    val url: String,
    val icon: ByteArray?,
    val title: String
)


@Serializable
data class ManagePostDTO(
    val id: Long?,
    val images: List<ByteArray>,
    val tags: List<Int>,
    val text: String,
    val creationDate: String?,
    val creationTime: String?,
    val newsData: PostNewsData?
) {
}