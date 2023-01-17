package fr.gilles.riceattend.services.modules;

import android.content.Context
import androidx.room.Entity
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.gilles.riceattend.app.RiceAttendApp
import fr.gilles.riceattend.models.*
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiService
import fr.gilles.riceattend.services.repositories.*
import fr.gilles.riceattend.services.repositories.retrofit.*
import fr.gilles.riceattend.services.repositories.room.RiceAttendDatabase
import fr.gilles.riceattend.services.storage.SessionManager
import fr.gilles.riceattend.utils.Dialog
import fr.gilles.riceattend.utils.DialogService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
 class RiceAttendModules {

    @Provides
    @Named("api_base_url")
    fun baseUrl(): String = "http://192.168.100.96:8080/"

    @Provides
    @Named("room_database")
    @Singleton
    fun roomDatabase(@ApplicationContext context: Context) : RiceAttendDatabase {
        return Room.databaseBuilder(
            context,
            RiceAttendDatabase::class.java,
            "riceattend.db"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    val entities = arrayOf(Activity::class, Worker::class, PaddyField::class, Resource::class)
                    for (entity in entities) {
                        val tableName = entity.java.getAnnotation(Entity::class.java)?.tableName
                        tableName?.let {
                            db.execSQL("CREATE TRIGGER insert_timestamp AFTER INSERT ON $tableName BEGIN UPDATE $tableName SET created_at = strftime('%s','now') WHERE id = new.id; END")
                            db.execSQL("CREATE TRIGGER update_timestamp AFTER UPDATE ON $tableName BEGIN UPDATE $tableName SET updated_at = strftime('%s','now') WHERE id = old.id; END")
                        }
                    }
                }
            })
            .build()
    }

    private val logger: HttpLoggingInterceptor = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        @Named("api_base_url") baseUrl:String,
        okHttpClient: OkHttpClient
    ):Retrofit{
        return   Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(ApiService.gson))
            .build()
    }

    private val headerInterceptor = Interceptor { chain ->
        chain.proceed(
            request = chain.request().newBuilder().addHeader(
                "Authorization",
                SessionManager.session.authorization
            ).build()
        )
    }


    @Provides
    @Singleton
    fun responseInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())
            if (response.code == 401) {
                DialogService.show(
                    Dialog(
                        title = "Session expirée",
                        message = "Votre session a expiré, veuillez vous reconnecter",
                        submitText = "Se reconnecter",
                        displayDismissButton = false,
                        onSuccess = {
                            SessionManager.clear()
                            RiceAttendApp.instance.startActivity(
                                RiceAttendApp.instance.packageManager.getLaunchIntentForPackage(
                                    RiceAttendApp.instance.packageName
                                )
                            )
                        }
                    )
                )
            }
            response
        }
    }


    @Provides
    @Singleton
    fun provideHttpClient():OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(logger)
            .addInterceptor(responseInterceptor())
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }


    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): RiceAttendApp {
        return app as RiceAttendApp
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        retrofit: Retrofit
    ): AuthRepository = retrofit.create(AuthRepository::class.java)

    @Provides
    @Singleton
    fun provideStatisticsRepository(
        retrofit: Retrofit
    ): StatisticsRepository = retrofit.create(StatisticsRepository::class.java)

    @Provides
    @Singleton
    fun providePaddyFieldRepository(
        retrofit: Retrofit
    ): PaddyFieldRepository = retrofit.create(PaddyFieldRepository::class.java)

    @Provides
    @Singleton
    fun provideActivityRepository(
        retrofit: Retrofit
    ): ActivityRepository = retrofit.create(ActivityRepository::class.java)


    @Provides
    @Singleton
    fun provideWorkerRepository(
        retrofit: Retrofit
    ): WorkerRepository = retrofit.create(WorkerRepository::class.java)


    @Provides
    @Singleton
    fun provideResourceRepository(
        retrofit: Retrofit
    ): ResourceRepository = retrofit.create(ResourceRepository::class.java)



    @Provides
    @Singleton
     fun provideApiEndpoint(
        authRepository: AuthRepository,
        workerRepository: WorkerRepository,
        resourceRepository: ResourceRepository,
        paddyFieldRepository: PaddyFieldRepository,
        activityRepository: ActivityRepository,
        statisticsRepository: StatisticsRepository
     ):ApiEndpoint = ApiEndpoint(
         authRepository = authRepository,
         workerRepository = workerRepository,
         statisticsRepository= statisticsRepository,
         resourceRepository =  resourceRepository,
         paddyFieldRepository =  paddyFieldRepository,
         activityRepository = activityRepository
     )








}
