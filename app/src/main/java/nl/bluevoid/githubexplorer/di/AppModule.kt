package nl.bluevoid.githubexplorer.di

import androidx.room.Room
import nl.bluevoid.githubexplorer.data.GitHubRepository
import nl.bluevoid.githubexplorer.data.GithubRepositoryImpl
import nl.bluevoid.githubexplorer.data.local.RepositoryDatabase
import nl.bluevoid.githubexplorer.data.remote.GitHubDataRemoteRepository
import nl.bluevoid.githubexplorer.data.remote.GitHubDataRemoteRepositoryImpl
import nl.bluevoid.githubexplorer.domain.util.NetworkMonitor
import nl.bluevoid.githubexplorer.presentation.GitHubExplorerViewmodel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            RepositoryDatabase::class.java,
            "repositories_database"
        ).build()
    }

    single { get<RepositoryDatabase>().repositoryDao() }

    single<GitHubRepository> { GithubRepositoryImpl(get(), get()) }

    single { NetworkMonitor(androidContext()) }

    single<GitHubDataRemoteRepository> { GitHubDataRemoteRepositoryImpl() }

    viewModel { GitHubExplorerViewmodel(get(), get()) }
}