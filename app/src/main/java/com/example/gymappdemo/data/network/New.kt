import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val status: String?,
    val totalResults: Int?,
    val articles: List<Article>
)

@Serializable
data class Article(
    val source: Source?,
    val author: String? = null,
    val title: String,
    val description: String? = null,
    val url: String? = null,
    val urlToImage: String? = null,
    val publishedAt: String,
    val content: String? = null
)

@Serializable
data class Source(
    val id: String? = null,
    val name: String?
)
