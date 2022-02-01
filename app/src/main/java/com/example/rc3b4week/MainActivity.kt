package com.example.rc3b4week

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.room.Room
import com.example.rc3b4week.databinding.ActivityMainBinding
import java.util.*

private lateinit var binding : ActivityMainBinding

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //val handler = Handler(Looper.getMainLooper())
        setContentView(binding.root)
        binding.btStart.setOnClickListener {
            val intent = Intent(this, Game1Activity ::class.java)
            startActivityForResult(intent,1234)
        }

        var db : AppDatabase = Room.databaseBuilder(this,AppDatabase::class.java,"db_change").allowMainThreadQueries().build()
        var userList = db.toChangeDao().getAll()
        db.toChangeDao().insert(ToChange(R.drawable.plane1, R.drawable.bullet_default))


        binding.btChange.setOnClickListener {
            var toChange = ToChange(R.drawable.enemy_ship_boss, R.drawable.bullet_default2)
            toChange.setId(1)
            toChange.setImage(R.drawable.enemy_ship_boss)
            toChange.setBulletImage(R.drawable.bullet_default2)
            db.toChangeDao().update(toChange)
            binding.ivP1.setImageResource(R.drawable.enemy_ship_boss)
        }
        binding.btChange2.setOnClickListener {
            var toChange = ToChange(R.drawable.plane1, R.drawable.bullet_default)
            toChange.setId(1)
            toChange.setImage(R.drawable.plane1)
            toChange.setBulletImage(R.drawable.bullet_default)
            db.toChangeDao().update(toChange)
            binding.ivP1.setImageResource(R.drawable.plane1)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            Toast.makeText(this, "GAME OVER", Toast.LENGTH_LONG).show()
        }
    }
}

