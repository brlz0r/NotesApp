package ru.kiruxadance.notesapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.kiruxadance.notesapp.note.data.data_source.AuthService
import ru.kiruxadance.notesapp.note.data.data_source.NoteService
import ru.kiruxadance.notesapp.note.data.data_source.UserService
import ru.kiruxadance.notesapp.note.data.interceptor.TokenInterceptor
import ru.kiruxadance.notesapp.note.data.repository.AuthRepositoryImpl
import ru.kiruxadance.notesapp.note.data.repository.NoteRepositoryImpl
import ru.kiruxadance.notesapp.note.data.repository.UserRepositoryImpl
import ru.kiruxadance.notesapp.note.data.utils.TokenProvider
import ru.kiruxadance.notesapp.note.domain.repository.AuthRepository
import ru.kiruxadance.notesapp.note.domain.repository.NoteRepository
import ru.kiruxadance.notesapp.note.domain.repository.UserRepository
import ru.kiruxadance.notesapp.note.domain.use_case.auth.AuthUseCases
import ru.kiruxadance.notesapp.note.domain.use_case.auth.Login
import ru.kiruxadance.notesapp.note.domain.use_case.auth.Registration
import ru.kiruxadance.notesapp.note.domain.use_case.note.*
import ru.kiruxadance.notesapp.note.domain.use_case.user.GetUser
import ru.kiruxadance.notesapp.note.domain.use_case.user.UserUseCases
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun providesBaseUrl() : String = "http://10.0.2.2:5234/api/"

    @Provides
    @Singleton
    fun provideRetrofit(BASE_URL : String, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    @Named("auth")
    fun provideAuthRetrofit(BASE_URL : String): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClientBuilder = OkHttpClient().newBuilder()
        okHttpClientBuilder.addInterceptor(logging)
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClientBuilder.build())
            .build()
    }

    private const val READ_TIMEOUT = 30
    private const val WRITE_TIMEOUT = 30
    private const val CONNECTION_TIMEOUT = 10

    @Provides
    @Singleton
    fun provideOkHttpClient(
        tokenInterceptor: TokenInterceptor
    ): OkHttpClient {

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClientBuilder = OkHttpClient().newBuilder()
        okHttpClientBuilder.connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.addInterceptor(tokenInterceptor)
        okHttpClientBuilder.addInterceptor(logging)

        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideTokenInterceptor(tokenProvider: TokenProvider, authRepository: AuthRepository): TokenInterceptor {
        return TokenInterceptor(tokenProvider, authRepository)
    }

    @Provides
    @Singleton
    fun provideNoteRepository(noteService: NoteService): NoteRepository {
        return NoteRepositoryImpl(noteService)
    }

    @Provides
    @Singleton
    fun provideAuthService(@Named("auth") retrofit : Retrofit) : AuthService = retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideUserService(retrofit : Retrofit) : UserService = retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun provideNoteService(retrofit : Retrofit) : NoteService = retrofit.create(NoteService::class.java)

    @Provides
    @Singleton
    fun provideAuthRepository(authService: AuthService): AuthRepository {
        return AuthRepositoryImpl(authService)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userService: UserService): UserRepository {
        return UserRepositoryImpl(userService)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            getNote = GetNote(repository),
            updateNote = UpdateNote(repository)
        )
    }

    @Provides
    @Singleton
    fun provideAuthUseCases(repository: AuthRepository): AuthUseCases {
        return AuthUseCases(
            login = Login(repository),
            registration = Registration(repository)
        )
    }

    @Provides
    @Singleton
    fun provideUserUseCases(repository: UserRepository): UserUseCases {
        return UserUseCases(
            getUser = GetUser(repository),
        )
    }

    @Singleton
    @Provides
    fun provideTokenProvider(@ApplicationContext context: Context) = TokenProvider(context)
}