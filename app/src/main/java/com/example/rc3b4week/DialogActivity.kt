package com.example.rc3b4week

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rc3b4week.databinding.ActivityDialogBinding

private lateinit var binding : ActivityDialogBinding
class DialogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

}