package com.example.trazeapp.service.impl


import android.content.Context
import android.content.DialogInterface
import com.example.trazeapp.R
import com.example.trazeapp.service.RegisterDialogSource
import com.example.trazeapp.util.dialogstates.MessageBoxType
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import javax.inject.Inject


class DialogRegisterImpl @Inject constructor() : RegisterDialogSource {

    override fun showInformationDialog(
        informationContextDialog: Context,
        listenersInformationDialog: DialogInterface.OnClickListener,
    ) {
        showDialogs(
            context = informationContextDialog,

            MessageBoxType.CONTINUE,
            onClickListener = listenersInformationDialog,
            message = 0

        )
    }


    override fun showChoicesCameraDialog(
        choicesCameraContext: Context,
        choicesListener: DialogInterface.OnClickListener,
    ) {
        showDialogs(
            context = choicesCameraContext,
            messageBoxType = MessageBoxType.CAPTURE_AND_BROWSE,
            onClickListener = choicesListener,
            message = 0
        )


    }

    override fun showPhotoInformationEmptyDialog(
        photoInformationEmptyContext: Context,
        photoMessageInformation: Int,
        photoInformationListeners: DialogInterface.OnClickListener,
    ) {
        showDialogs(context = photoInformationEmptyContext,
            messageBoxType = MessageBoxType.CANCEL_AND_CONTINUE,
            onClickListener = photoInformationListeners,
            message = photoMessageInformation)
    }

    override fun showUnsuccessfulCropImageDialogMessage(
        unSuccessfulCropContext: Context,
        unSuccessCropMessage: Int,
        unSuccessfulCropListeners: DialogInterface.OnClickListener,
    ) {
        showDialogs(context = unSuccessfulCropContext,
            messageBoxType = MessageBoxType.CANCEL_AND_CONTINUE,
            onClickListener = unSuccessfulCropListeners,
            message = unSuccessCropMessage)
    }

    override fun showDialogs(
        context: Context,
        messageBoxType: MessageBoxType,
        onClickListener: DialogInterface.OnClickListener,
        message: Int?,
    ) {
        val builder = MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
        builder.setCancelable(false)

        with(builder) {
            when (messageBoxType) {
                MessageBoxType.CAPTURE_AND_BROWSE -> {
                    setTitle(R.string.label_choices_header)
                    setMessage(R.string.label_choices_description)

                }
                MessageBoxType.CONTINUE -> {
                    setTitle(R.string.label_header_profile_image)
                    setMessage(R.string.label_information_dialog_description)
                }
                MessageBoxType.CANCEL_AND_CONTINUE -> {
                    setTitle(R.string.label_header_profile_image)
                    message?.let { setMessage(it) }
                }
            }
        }
        processButtonDialog(messageBoxType = messageBoxType,
            onClickListener = onClickListener,
            builder)
    }


    override fun processButtonDialog(
        messageBoxType: MessageBoxType,
        onClickListener: DialogInterface.OnClickListener,
        builder: MaterialAlertDialogBuilder,
    ) {
        with(builder) {
            when (messageBoxType) {
                MessageBoxType.CAPTURE_AND_BROWSE -> {
                    setPositiveButton(R.string.label_capture_dialog,
                        onClickListener)
                    setNegativeButton(R.string.label_browse_dialog, onClickListener)
                }
                MessageBoxType.CONTINUE -> setPositiveButton(R.string.label_continue_dialog,
                    onClickListener)
                MessageBoxType.CANCEL_AND_CONTINUE -> {
                    setPositiveButton(R.string.label_continue, onClickListener)
                    setNegativeButton(R.string.label_cancel, onClickListener)
                }
            }
        }
        builder.create().show()
    }


}


//    override fun showChoicesDialog(context: Context) {
//
//    }
