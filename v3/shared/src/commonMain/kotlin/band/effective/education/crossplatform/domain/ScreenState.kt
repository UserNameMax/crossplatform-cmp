package band.effective.education.crossplatform.domain

sealed interface ScreenState<out T> {
    data object Loading : ScreenState<Nothing>
    data class Content<T>(val value: T) : ScreenState<T>
    data object Empty : ScreenState<Nothing>
    data class Error(val message: String) : ScreenState<Nothing>
}
