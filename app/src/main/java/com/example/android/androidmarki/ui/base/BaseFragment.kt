package com.example.android.androidmarki.ui.base

import android.content.DialogInterface
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.android.androidmarki.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment() {
    private val builder: MaterialAlertDialogBuilder by lazy {
        MaterialAlertDialogBuilder(
            requireContext(),
            R.style.ThemeOverlay_App_MaterialAlertDialog
        )
    }
    protected lateinit var snackView: View
    protected lateinit var navController: NavController

    // fun isUserLoggedIn(): Boolean = StorageUtil(context!!).currentUser != null

    fun showShortToast(@StringRes message: Int) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun showSnackBar(@StringRes error: Int) {
        Snackbar.make(snackView, error, Snackbar.LENGTH_SHORT).show()
    }

    fun showSnackBar(error: String, actionTitle: String, listener: View.OnClickListener) {
        Snackbar.make(snackView, error, Snackbar.LENGTH_LONG)
            .setAction(actionTitle, listener)
            .show()
    }

    fun showDialog(
        title: String,
        message: String? = null,
        positiveText: String = requireContext().getString(R.string.action_ok),
        negativeText: String = requireContext().getString(R.string.action_cancel),
        neutralText: String = "",
        negativeAction: (dialog: DialogInterface, id: Int) -> Unit = { dialog, _ -> dialog.dismiss() },
        neutralAction: ((dialog: DialogInterface, id: Int) -> Unit)? = null,
        positiveAction: (dialog: DialogInterface, id: Int) -> Unit
    ) {
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveText) { dialog, id ->
                positiveAction(dialog, id)
            }
            .setNegativeButton(negativeText) { dialog, id ->
                negativeAction(dialog, id)
            }

        if (neutralAction != null) {
            builder.setNeutralButton(neutralText) { dialog, which ->
                neutralAction(dialog, which)
            }
        }

        val dialog = builder.create()
        dialog.show()
    }


}