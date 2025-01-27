package com.pmdk.gymapp.ui.viewmodels

import Article
import NewsResponse
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmdk.gymapp.data.network.NewsApiService
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class NewsViewModel(private val service: NewsApiService) : ViewModel() {

    var newsUiState: NewsUiState by mutableStateOf(NewsUiState.Loading)
        private set

    init {
        getNews()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNews() {
        viewModelScope.launch {
            newsUiState = try {
                val response: NewsResponse = service.getNews(
                    query = "Sports",
                    fromDate = getDates(),
                    sortBy = "publishedAt",
                    apiKey = "e77cba1ddfbf40618da7d163d4d33946",
                    pageSize = 10,
                    page = 1,
                    language = "en"
                )
                NewsUiState.Success(response.articles)
            } catch (e: Exception) {
                NewsUiState.Error(e.message ?: "An unexpected error occurred!")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDates(): String {
    // Get today's date
    val today = LocalDate.now()

    // Calculate the date 5 days ago
    val fromDate = today.minusDays(5)

    // Format the dates as "yyyy-MM-dd"
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val fromDateFormatted = fromDate.format(formatter)

    return fromDateFormatted
}


// Define UI state sealed class
sealed class NewsUiState {
    object Loading : NewsUiState()
    data class Success(val news: List<Article>) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}
