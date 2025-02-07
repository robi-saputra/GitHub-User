package robi.technicaltest.networks

sealed class NetworkState<out T> {
    class Success<out T>(val data: T) : NetworkState<T>() {
        override fun equals(other: Any?): Boolean {
            return other is Success<*> && other.data == data
        }

        override fun hashCode(): Int {
            return data?.hashCode() ?: 0
        }
    }

    class Error(val exception: Throwable) : NetworkState<Nothing>() {
        override fun equals(other: Any?): Boolean {
            return other is Error && other.exception.message == exception.message
        }

        override fun hashCode(): Int {
            return exception.message?.hashCode() ?: 0
        }
    }

    data object Loading : NetworkState<Nothing>()
}