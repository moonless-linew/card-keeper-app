package com.example.cardapp.di

import com.example.cardapp.utils.ApiUtils
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Singleton
    @Provides
    @Named("users")
    fun provideUsersRef(): CollectionReference =
        Firebase.firestore.collection(ApiUtils.API_USERS_COLLECTION)

    @Provides
    @Singleton
    @Named("cards")
    fun provideCardsRef(): CollectionReference =
        Firebase.firestore.collection(ApiUtils.API_CARDS_COLLECTION)

    @Provides
    @Singleton
    @Named("markets")
    fun provideShopsRef(): CollectionReference =
        Firebase.firestore.collection(ApiUtils.API_MARKETS_COLLECTION)
}
