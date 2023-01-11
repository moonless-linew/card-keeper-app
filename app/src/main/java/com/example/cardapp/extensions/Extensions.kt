package com.example.cardapp.extensions

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




