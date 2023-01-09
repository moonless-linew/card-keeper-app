package com.example.cardapp.extensions

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.cardapp.R

fun NavController.navigateSafely(@IdRes actionId: Int) {
    currentDestination?.getAction(actionId)?.let { navigate(actionId) }
}

fun Fragment.activityNavController() =
    requireActivity().findNavController(R.id.activityNavigationHost)

fun String.toPhoneStandard() = this.replace("[()-]".toRegex(), "")

fun NavController.navigateSafelyBundle(@IdRes actionId: Int, args: Bundle) {
    currentDestination?.getAction(actionId)?.let { navigate(actionId, args) }
}


