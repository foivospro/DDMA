import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappdemo.data.repositories.UserRepository
import com.example.gymappdemo.data.repositories.WorkoutRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeScreenViewModel(
    private val userRepository: UserRepository,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _username = MutableStateFlow("Guest")
    val username: StateFlow<String> get() = _username

    init {
        viewModelScope.launch {
            fetchUsername()
        }
    }

    private suspend fun fetchUsername() {
        // Switch to I/O dispatcher for database operations
        val email = withContext(Dispatchers.IO) {
            userRepository.getLoggedInUserEmail()
        }

        val user = if (email != "Guest") {
            withContext(Dispatchers.IO) {
                userRepository.getUserByEmail(email!!)
            }
        } else {
            null
        }

        // Update the state on the main thread
        _username.value = user?.name ?: "Guest"
    }

}
