package robi.technicaltest.networks

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer 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")
            .addHeader("Accept", "application/vnd.github.v3+json")
            .build()
        return chain.proceed(request)
    }
}