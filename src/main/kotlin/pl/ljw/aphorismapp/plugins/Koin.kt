package pl.ljw.aphorismapp.plugins

import io.ktor.application.*
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.koin.logger.SLF4JLogger
import pl.ljw.aphorismapp.repositories.*
import pl.ljw.aphorismapp.services.*

fun Application.configureKoinDependencyInjection() {

    val appModule = module {
        single<UserRepository> { UserRepositoryImpl() }
        single<UserService> { UserServiceImpl(get()) }
        single<AphorismRepository> { AphorismRepositoryImpl() }
        single<AphorismService> { AphorismServiceImpl(get()) }
        single<RatingRepository> { RatingRepositoryImpl() }
        single<RatingService> { RatingServiceImpl(get()) }
    }

    install(Koin) {
        SLF4JLogger()
        modules(appModule)
    }
}
