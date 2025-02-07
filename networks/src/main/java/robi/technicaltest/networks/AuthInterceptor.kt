package robi.technicaltest.networks

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Accept", "application/vnd.github.v3+json")
            .build()
        return chain.proceed(request)
    }
}