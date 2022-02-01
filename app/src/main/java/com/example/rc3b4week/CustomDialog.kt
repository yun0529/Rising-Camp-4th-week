package com.example.rc3b4week

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.rc3b4week.databinding.CustomdialogIngameBinding

private lateinit var binding :CustomdialogIngameBinding

class CustomDialog(game1Activity: Game1Activity) : DialogFragment(){

    private var dialogListener: MyDialogListener? = null
    private var context: Context? = null

    fun CustomDialog(context: Context?) {
        this.context = context
    }

    fun setDialogListener(dialogListener: MyDialogListener?) {
        this.dialogListener = dialogListener
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = CustomdialogIngameBinding.inflate(inflater, container, false)
        val view = binding.root
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        binding.btEnd.setOnClickListener {
            dialogListener?.onPositiveClicked("계속하기")
            dismiss()
        }
        binding.btStart.setOnClickListener {
            dialogListener?.onNegativeClicked("종료")
            dismiss()
        }
        return view
    }
}