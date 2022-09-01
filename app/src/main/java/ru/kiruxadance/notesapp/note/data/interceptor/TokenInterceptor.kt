package ru.kiruxadance.notesapp.note.data.interceptor

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ru.kiruxadance.notesapp.BuildConfig
import ru.kiruxadance.notesapp.note.data.data_source.AuthService
import ru.kiruxadance.notesapp.note.data.utils.TokenProvider
import ru.kiruxadance.notesapp.note.domain.model.RefreshToken
import ru.kiruxadance.notesapp.note.domain.model.Token
import ru.kiruxadance.notesapp.note.domain.repository.AuthRepository
import java.net.HttpURLConnection

class TokenInterceptor(
    private val tokenProvider: TokenProvider,
    private val authRepository: AuthRepository
) : Interceptor {
    private val accessToken = mutableStateOf("")
    private val refreshToken = mutableStateOf("")
    private val newTokens = mutableStateOf(Token(
        accessToken = "",
        refreshToken = ""
    ))

    override fun intercept(chain: Interceptor.Chain): Response {
        runBlocking {
            GlobalScope.launch {
                tokenProvider.getTokens().collectLatest {
                    accessToken.value = it.accessToken
                    refreshToken.value = it.refreshToken
                }
            }
        }
        println(accessToken.value)
        val originalRequest = chain.request()

        val initialResponse = chain.proceed(newRequestWithAccessToken(accessToken.value, originalRequest))

        if (initialResponse.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            runBlocking {
                var newTokensResponse = authRepository.refresh(RefreshToken(refreshToken.value))
                if (newTokensResponse != null) {
                    newTokens.value = newTokens.value.copy(
                        accessToken = newTokensResponse.accessToken,
                        refreshToken = newTokensResponse.refreshToken
                    )
                }
            }
            return chain.proceed(newRequestWithAccessToken(newTokens.value.accessToken, originalRequest))
        }
        return initialResponse
    }

    private fun newRequestWithAccessToken(accessToken: String?, request: Request): Request =
        request.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()
}