package nl.bluevoid.githubexplorer.domain.util

sealed interface ResultState<out T> {
    data object Loading : ResultState<Nothing>
    data class Success<T>(val data: T) : ResultState<T>
    data class Failure(val exception: Throwable) : ResultState<Nothing>
}

fun <T> Result<T>.toResultState(): ResultState<T> {
    return fold(
        onSuccess = { ResultState.Success(it) },
        onFailure = { ResultState.Failure(it) }
    )
}