package nl.bluevoid.githubexplorer.di

import nl.bluevoid.githubexplorer.data.GitHubDataRemoteRepository
import nl.bluevoid.githubexplorer.data.GitHubDataRemoteRepositoryImpl
import nl.bluevoid.githubexplorer.domain.usecase.GetGithubDataUsecase
import nl.bluevoid.githubexplorer.presentation.ExplorerViewmodel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<GitHubDataRemoteRepository> { GitHubDataRemoteRepositoryImpl() }

    factory { GetGithubDataUsecase(get()) }

    viewModel { ExplorerViewmodel(get()) }
}