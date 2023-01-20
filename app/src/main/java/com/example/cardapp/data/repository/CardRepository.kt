package com.example.cardapp.data.repository

import com.example.cardapp.domain.repository.ICardRepository
import com.example.cardapp.domain.model.Card
import com.example.cardapp.utils.ApiUtils
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CardRepository @Inject constructor(
    @Named("users")
    private val usersRef: CollectionReference,
)  : ICardRepository {
    override suspend fun uploadCard(
        uid: String,
        card: Card,
    ) = suspendCoroutine { continuation ->
        usersRef
            .document(uid)
            .collection(ApiUtils.API_CARDS_COLLECTION)
            .add(card)
            .addOnSuccessListener { continuation.resume(Unit)}
            .addOnFailureListener { continuation.resumeWithException(it) }
    }

    override suspend fun deleteCard(
        uid: String,
        card: Card
    ) = suspendCoroutine { continuation ->
        val batch = FirebaseFirestore.getInstance().batch()
        usersRef
            .document(uid)
            .collection(ApiUtils.API_CARDS_COLLECTION)
            .whereEqualTo(ApiUtils.API_ID_FIELD, card.id.toString())
            .get()
            .addOnSuccessListener { cardList ->
                val foundCard = cardList.first()
                batch.apply {
                    delete(foundCard.reference)
                    commit().addOnSuccessListener {
                        continuation.resume(Unit)
                    }.addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
                }
            }
    }

    override suspend fun getUserCards(
        uid: String
    ): List<Card> = suspendCoroutine { continuation ->
        usersRef
            .document(uid)
            .collection(ApiUtils.API_CARDS_COLLECTION)
            .get()
            .addOnSuccessListener { documents ->
                val cards = documents.toObjects(Card::class.java)
                continuation.resume(cards)
            }
            .addOnFailureListener {
                continuation.resumeWithException(it)
            }
    }
}
