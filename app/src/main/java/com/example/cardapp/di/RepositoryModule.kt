package com.example.cardapp.di

import com.example.cardapp.data.repository.AuthRepository
import com.example.cardapp.data.repository.CardRepository
import com.example.cardapp.data.repository.MarketRepository
import com.example.cardapp.domain.repository.IAuthRepository
import com.example.cardapp.domain.repository.ICardRepository
import com.example.cardapp.domain.repository.IMarketRepository
import com.google.firebase.firestore.CollectionReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    @ViewModelScoped
    fun provideAuthRepository(@Named("users") usersRef: CollectionReference): IAuthRepository =
        AuthRepository(usersRef)

    @Provides
    @ViewModelScoped
    fun provideCardRepository(@Named("users") usersRef: CollectionReference): ICardRepository =
        CardRepository(usersRef)

    @Provides
    @ViewModelScoped
    fun provideMarketRepository(@Named("markets") marketsRef: CollectionReference): IMarketRepository =
        MarketRepository(marketsRef)
}
