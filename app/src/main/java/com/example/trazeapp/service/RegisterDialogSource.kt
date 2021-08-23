package com.example.trazeapp.service


import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import com.example.trazeapp.util.DialogChoices
import com.example.trazeapp.util.dialogstates.MessageBoxType
import com.example.trazeapp.view.fragments.RegisterFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

interface RegisterDialogSource {

    fun showInformationDialog(
        informationContextDialog: Context,
        listenersInformationDialog: DialogInterface.OnClickListener,
    )

    fun showChoicesCameraDialog(
        choicesCameraContext: Context,
        choicesListener: DialogInterface.OnClickListener,
    )

    fun showPhotoInformationEmptyDialog(
        photoInformationEmptyContext: Context,
        photoMessageInformation: Int,
        photoInformationListeners: DialogInterface.OnClickListener,
    )



    fun showUnsuccessfulCropImageDialogMessage(
        unSuccessfulCropContext: Context,
        unSuccessCropMessage: Int,
        unSuccessfulCropListeners: DialogInterface.OnClickListener,
    )

    fun showDialogs(
        context: Context,
        messageBoxType: MessageBoxType,
        onClickListener: DialogInterface.OnClickListener,
        message: Int?,

        )

    fun processButtonDialog(
        messageBoxType: MessageBoxType,
        onClickListener: DialogInterface.OnClickListener,
        builder: MaterialAlertDialogBuilder,
    )


}