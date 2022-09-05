package ru.kiruxadance.notesapp.note.data.interceptor

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.HttpException
import ru.kiruxadance.notesapp.BuildConfig
import ru.kiruxadance.notesapp.note.data.data_source.AuthService
import ru.kiruxadance.notesapp.note.data.utils.TokenProvider
import ru.kiruxadance.notesapp.note.domain.model.RefreshToken
import ru.kiruxadance.notesapp.note.domain.model.Token
import ru.kiruxadance.notesapp.note.domain.repository.AuthRepository
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

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
    private val mutex = Mutex()

    override fun intercept(chain: Interceptor.Chain): Response {
            runBlocking {
                GlobalScope.launch() {
                    tokenProvider.getTokens().collectLatest {
                        accessToken.value = it.accessToken
                        refreshToken.value = it.refreshToken
                    }
                }
        }
            val originalRequest = chain.request()
            try {
                Thread.sleep(1000);
                val initialResponse =
                    chain.proceed(newRequestWithAccessToken(accessToken.value, originalRequest))

                if (initialResponse.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    runBlocking {
                        var newTokensResponse =
                            authRepository.refresh(RefreshToken(refreshToken.value))
                        if (newTokensResponse != null) {
                            newTokens.value = newTokens.value.copy(
                                accessToken = newTokensResponse.accessToken,
                                refreshToken = newTokensResponse.refreshToken
                            )
                            tokenProvider.saveTokens(newTokensResponse)
                        }
                    }
                    return chain.proceed(
                        newRequestWithAccessToken(
                            newTokens.value.accessToken,
                            originalRequest
                        )
                    )
                }
                return initialResponse

            } catch (e: Exception) {
                throw e
            }
    }

    private fun newRequestWithAccessToken(accessToken: String?, request: Request): Request =
        request.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()
}