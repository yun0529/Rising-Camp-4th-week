package com.example.rc3b4week

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rc3b4week.databinding.ActivityGame1Binding
import java.util.*
import kotlin.concurrent.timer


private lateinit var binding : ActivityGame1Binding
class Game1Activity : AppCompatActivity(){
    val dialog = CustomDialog(this)
    //private var dialog2 = CustomDialog2(this)
    var time = 6000
    private var timerTask : Timer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGame1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val handler = Handler(Looper.getMainLooper())

        startTimer()

        binding.btTest.setOnClickListener {
            stopTimer()
            dialog.setDialogListener(object : MyDialogListener {
                override fun onPositiveClicked(num: String?) {
                    dialog.dismiss()
                    startTimer()
                }
                override fun onNegativeClicked(num: String?) {
                    dialog.dismiss()
                    if (num != null) {
                        setResult(num)
                    }
                }
            })
            dialog.show(supportFragmentManager, "CustomDialog")
        }


    }
    private fun setResult(name: String) {
        if(name == "종료"){
            finish()
        }
        else if( name == "계속하기"){
        }
    }
    private fun startTimer(){
        timerTask = timer(period = 10){
            time--
            val sec = time/100
            runOnUiThread{
                binding.tvTimer.text = sec.toString()
                if(sec == 0){
                    val intent = Intent()
                    intent.putExtra("my_data","전달함")
                    setResult(RESULT_OK,intent)
                    finish()
                }

            }
        }
    }
    private fun stopTimer(){
        timerTask?.cancel()
    }
    private fun resetTimer(){
        timerTask?.cancel()
        time = 6000
        binding.tvTimer.text = 60.toString()
    }

    override fun onPause() {
        stopTimer()
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        resetTimer()
    }


    /*private fun showDialog2(){
        dialog2?.show()

        var noBt = dialog2.findViewById(R.id.bt_start2);
        noBt.setOnClickListener{
                dialog2?.dismiss() // 다이얼로그 닫기
        }
        // 네 버튼
        var yesBt = dialog2.bt_end2
        yesBt.setOnClickListener{
            dialog2?.dismiss()
            finish()
        }

    }*/
}