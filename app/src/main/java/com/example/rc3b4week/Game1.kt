package com.example.rc3b4week

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.media.MediaPlayer
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.room.Room
import java.lang.Exception
import java.util.*
import kotlin.properties.Delegates


class Game1(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    //MyLocation
    private var scrH by Delegates.notNull<Int>()
    private var scrW by Delegates.notNull<Int>()
    private var cx: Float = 0f
    private var cy: Float = 0f
    private var n: Int = 0
    private var count = 0
    private var start: Boolean = false
    private var duration_1selected: String = ""
    private var duration_2selected: String = ""
    // myLife
    private var myLifeCount = 3
    private var myLife = IntArray(3) { 1 }
    private var myLifeHeart = BooleanArray(3) { true }

    //background
    private var paint: Paint = Paint()
    private var textPrint:Paint = Paint()
    //Bullet
    private var bullet_count = 0
    private var shotting = BooleanArray(30) { false }
    private var myX = IntArray(30) { 0 }
    private var myY = IntArray(30) { 0 }
    private var enemy_bullet_count = 0

    //Thread
    private var td_bt: BulletThread? = null
    private var td_ey: EnemyThread? = null
    private var td: MyPlaneThread? = null
    private var td_ey2: EnemyThread2? = null

    //enemy
    private var eyX = IntArray(5) { 0 }
    private var eyY = IntArray(5) { 0 }
    private var eyCount = IntArray(5) { 0 }
    private var eyLife = IntArray(5) { 0 }
    private var eyDuration = Array<String>(5) { "" }
    var random: Random = Random()

    //enemy2
    private var eyX2 = 0
    private var eyY2 = 0
    private var eyCount2 = 0
    private var eyLife2 = 0
    private var eyDuration2 = ""
    private var enemy_shotting = BooleanArray(7) { false }
    private var enemy_myX = IntArray(7) { 0 }
    private var enemy_myY = IntArray(7) { 0 }

    val mp : MediaPlayer = MediaPlayer.create(context, R.raw.common_music)
    private val db : AppDatabase = Room.databaseBuilder(context!!,AppDatabase::class.java,"db_change").allowMainThreadQueries().build()
    var userList = db.toChangeDao().getAll()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.scrW = w
        this.scrH = h
        mp.start()
        mp.isLooping = true

        for (i in 0 until 5) {
            eyLife[i] = 2
        }
        eyLife2 = 20
        if (td == null && td_bt == null && td_ey == null) {
            td = MyPlaneThread()
            td!!.start()

            td_bt = BulletThread()
            td_bt!!.start()

            td_ey = EnemyThread()
            td_ey!!.start()

            td_ey2 = EnemyThread2()
            td_ey2!!.start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        td!!.run = false
        td_bt!!.run = false
        td_ey!!.run = false
        td_ey2!!.run = false
        mp.stop()
        mp.reset()
    }
    var gameClear: Boolean = false
    @SuppressLint("DrawAllocation")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = (0x00000000).toInt()
        textPrint.color = (0xFF000000).toInt()
        textPrint.textSize = (scrH / 15).toFloat()
        if(gameClear){
            canvas?.drawText("Stage Clear", (scrW/6).toFloat(), (scrH /2  - scrH/20).toFloat(), textPrint)
        }

        var pic = arrayOfNulls<Bitmap>(5)
        for (i in 0 until 2) {
                pic[i] = BitmapFactory.decodeResource(resources, userList[0].image)
            Bitmap.createScaledBitmap(pic[i]!!, scrW / 4, scrH / 8, true)
            if (i == n) { canvas?.drawBitmap(pic[i]!!, (scrW / 3).toFloat() + cx, ((scrH - scrH / 6) + cy).toFloat(), null)
            }
        }

        var myLifeShape = arrayOfNulls<Bitmap>(3)
        for (i in 0 until 3) {
            myLifeShape[i] = BitmapFactory.decodeResource(resources, R.drawable.heart)
            Bitmap.createScaledBitmap(myLifeShape[i]!!, scrW / 8, scrH / 16, true)
            if(myLifeHeart[i]) {
                when (i) {
                    0 -> {
                        canvas?.drawBitmap(myLifeShape[i]!!, (scrW - scrW / 8).toFloat(), (scrH - scrH / 16).toFloat(), null)
                    }
                    1 -> {
                        canvas?.drawBitmap(myLifeShape[i]!!, (scrW - scrW / 8).toFloat(), (scrH - scrH / 8).toFloat(), null)
                    }
                    2 -> {
                        canvas?.drawBitmap(myLifeShape[i]!!, (scrW - scrW / 8).toFloat(), (scrH - 3 * scrH / 16).toFloat(), null)
                    }
                }
            }
        }

        if(myLifeCount == 0){
            td!!.run = false
            td_bt!!.run = false
            td_ey!!.run = false
            td_ey2!!.run = false
            mp.stop()
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("GAME OVER")
            builder.setNegativeButton("종료", DialogInterface.OnClickListener { dialog, which ->
                (context as Game1Activity).finish()
            })
            builder.setPositiveButton("다시 시작", DialogInterface.OnClickListener { dialog, which ->
                mp.reset()
                mp.start()
                for (i in 0 until 5) {
                    eyLife[i] = 2
                }
                for (i in 0 until 3) {
                    myLifeHeart[i] = true
                }
                eyLife2 = 20
                eyY2 = 0
                myLifeCount = 3
                dialog.dismiss()

                td = MyPlaneThread()
                td!!.start()

                td_bt = BulletThread()
                td_bt!!.start()

                td_ey = EnemyThread()
                td_ey!!.start()

                td_ey2 = EnemyThread2()
                td_ey2!!.start()

            })
            builder.show()
            onDetachedFromWindow()
        }
        var bulletShape = arrayOfNulls<Bitmap>(30)
        for (i in 0 until 30) {
            bulletShape[i] = BitmapFactory.decodeResource(resources, userList[0].bulletImage)
            Bitmap.createScaledBitmap(bulletShape[i]!!, scrW / 30, scrH / 45, true)

            if (shotting[i]) {
                canvas?.drawBitmap(bulletShape[i]!!, myX[i].toFloat(), myY[i].toFloat(), null)

                for (j in 0 until 5) {
                    if (eyLife[j] > 0 && myX[i] > (scrW / 3) + eyX[j] && myX[i] < (scrW / 2 + scrW / 8) + eyX[j]
                        && myY[i] > (scrH - 3 * scrH / 4) + eyY[j] && myY[i] < (scrH - 5 * scrH / 6) + scrH / 8 + eyY[j]
                    ) {
                        eyLife[j] -= 1;
                        shotting[i] = false;
                    }
                }
                if (eyLife2 > 0 && myX[i] > (2*scrW / 5) + eyX2 && myX[i] < (scrW / 2 + scrW / 8) + eyX2
                    && myY[i] > 0 + eyY2 && myY[i] < scrH / 12 + eyY2) {
                    eyLife2 -= 1;
                    shotting[i] = false;
                }
            }
            if (myY[i] < 0 - scrH/12) {
                shotting[i] = false
            }

        }
        var enemyBulletShape = arrayOfNulls<Bitmap>(7)
        for (i in 0 until 7) {
            enemyBulletShape[i] = BitmapFactory.decodeResource(resources, R.drawable.enemy_bullet_default2)
            Bitmap.createScaledBitmap(enemyBulletShape[i]!!, scrW / 80, scrH / 100, true)

            if (enemy_shotting[i]) {
                canvas?.drawBitmap(enemyBulletShape[i]!!, enemy_myX[i].toFloat(), enemy_myY[i].toFloat(), null)
            }
            if (enemy_myY[i] > 7*scrH/9) {
                enemy_shotting[i] = false
            }
            if(myLifeCount == 3 && enemy_shotting[i] && enemy_myX[i] > scrW /3 + cx - scrW/4 && enemy_myX[i] < (scrW /3) + cx + scrW /4 - scrW/4
                && enemy_myY[i] > (scrH - scrH / 6) + cy - 2*scrH/9&& enemy_myY[i] < (scrH - scrH / 6) + cy  + (scrH /8) - (2*scrH/9)){
                myLifeCount--
                myLifeHeart[2] = false
                enemy_shotting[i] = false
            }
            if (myLifeCount == 2 && enemy_shotting[i] && enemy_myX[i] > scrW /3 + cx - scrW/4 && enemy_myX[i] < (scrW /3) + cx + scrW /4 - scrW/4
                && enemy_myY[i] > (scrH - scrH / 6) + cy - 2*scrH/9 && enemy_myY[i] < (scrH - scrH / 6) + cy  + (scrH /8) - (2*scrH/9)){
                myLifeCount--
                myLifeHeart[1] = false
                enemy_shotting[i] = false
            }
            if (myLifeCount == 1 && enemy_shotting[i] && enemy_myX[i] > scrW /3 + cx - scrW/4 && enemy_myX[i] < (scrW /3) + cx + scrW /4 - scrW/4
                && enemy_myY[i] > (scrH - scrH / 6) + cy - 2*scrH/9&& enemy_myY[i] < (scrH - scrH / 6) + cy  + (scrH /8) - (2*scrH/9)){
                myLifeCount--
                myLifeHeart[0] = false
                enemy_shotting[i] = false
            }
        }


        var enemyShape = arrayOfNulls<Bitmap>(5)
        for (k in 0 until 5) {
            enemyShape[k] = BitmapFactory.decodeResource(resources, R.drawable.plane2)
            Bitmap.createScaledBitmap(enemyShape[k]!!, scrW / 4, scrH / 8, true)
            if (eyLife[k] > 0) {
                canvas?.drawBitmap(
                    enemyShape[k]!!,
                    ((scrW / 2) + eyX[k]).toFloat(),
                    ((scrH - 3 * scrH / 4) + eyY[k]).toFloat(),
                    null
                )
            }
        }

        var enemyShape2 = arrayOfNulls<Bitmap>(1)
        enemyShape2[0] = BitmapFactory.decodeResource(resources, R.drawable.enemy_ship1)
        Bitmap.createScaledBitmap(enemyShape2[0]!!, scrW / 4, scrH / 12, true)
        if (eyLife2 > 0) {
            canvas?.drawBitmap(enemyShape2[0]!!, ((scrW / 2) + eyX2).toFloat(), (0 + eyY2).toFloat(), null)
        }
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event?.action == MotionEvent.ACTION_DOWN || event?.action == MotionEvent.ACTION_MOVE || event?.action == MotionEvent.ACTION_POINTER_DOWN){

            cx = event.x - (scrW / 3).toFloat()
            cy = event.y - (scrH - scrH / 6)

                if(bullet_count < 30){
                    shotting[bullet_count] = true
                    myX[bullet_count] = ((4*scrW/15) + cx).toInt()
                    myY[bullet_count] = ((scrH - 4*scrH/15) + cy).toInt()

                    if(bullet_count == 29){
                        bullet_count = 0
                    }
                    if(bullet_count < 30) {
                        bullet_count++
                    }

                    if(enemy_bullet_count < 7 && bullet_count % 6 == 1) {
                        enemy_shotting[enemy_bullet_count] = true
                        enemy_myX[enemy_bullet_count] = (scrW / 2) + eyX2
                        enemy_myY[enemy_bullet_count] = scrH / 12 + eyY2

                        if (enemy_bullet_count == 6) {
                            enemy_bullet_count = 0
                        }
                        if (enemy_bullet_count < 7) {
                            enemy_bullet_count++
                        }
                    }
                }
            if(gameClear){
                (context as Game1Activity).finish()
            }
        }

        if(event?.action == MotionEvent.ACTION_UP || event?.action == MotionEvent.ACTION_POINTER_UP){
            //left_button
            if((event.x).toInt()<scrW/6 && (event.x).toInt() > 0 && (event.y).toInt() > scrH - scrH/6 && (event.y).toInt() < scrH - scrH/12){
                start = false
            }
            //right_button
            else if((event.x).toInt() < scrW/2 && (event.x).toInt() > scrW/3 && (event.y).toInt() > scrH - scrH/6 && (event.y).toInt() < scrH - scrH/12){
                start = false
            }
            //up_button
            else if((event.x).toInt() < scrW/3 && (event.x).toInt() > scrW/6 && (event.y).toInt() < scrH - scrH/6 && (event.y).toInt() > scrH - scrH/4){
                start = false
            }
            //down_button
            else if((event.x).toInt() < scrW/3 && (event.x).toInt() > scrW/6 && (event.y).toInt() < scrH && (event.y).toInt() > scrH - scrH/12){
                start = false
            }
        }
        return true
    }

    inner class MyPlaneThread : Thread() {
        var run = true
        override fun run() {
            while (run){
                try{
                    postInvalidate()
                    handler.post {
                        if (count == 10) {
                            count = 0
                            duration_1selected = duration_2selected
                        }
                        if (count == 0 && start) {
                            count++
                        }

                        if (count < 10 && count > 0) {
                            count++
                        }
                        if (start && duration_1selected == "LEFT" && count != 10 || !start && duration_1selected == "LEFT" && count < 10 && count > 0) {
                            cx -= 5
                        } else if (start && duration_1selected == "RIGHT" && count != 10 || !start && duration_1selected == "RIGHT" && count < 10 && count > 0) {
                            cx += 5
                        } else if (start && duration_1selected == "UP" && count != 10 || !start && duration_1selected == "UP" && count < 10 && count > 0) {
                            cy -= 5
                        } else if (start && duration_1selected == "DOWN" && count != 10 || !start && duration_1selected == "DOWN" && count < 10 && count > 0) {
                            cy += 5
                        }

                    }
                    sleep(10)
                }
                catch (e: Exception){

                }
            }
            super.run()
        }

    }

    inner class BulletThread : Thread() {
        var run = true
        override fun run() {
            while (run) {
                try {
                    postInvalidate()
                    handler.post{
                        for(i in 0 until 30) {
                            if (shotting[i]) {
                                myY[i] -= 10
                            }
                        }
                        for(i in 0 until 7) {
                            if (enemy_shotting[i]) {
                                enemy_myY[i] += 10
                            }
                        }
                    }
                    sleep(10)

                }
                catch (e: Exception){

                }
            }
            super.run()
        }
    }
    inner class EnemyThread : Thread() {
        var run = true
        override fun run() {
            while (run) {
                try {
                    postInvalidate()
                    handler.post{
                        for(i in 0 until 5){
                            if(eyCount[i] == 20){
                                eyCount[i] = 0
                            }
                            if(eyCount[i] == 0){
                                var ed = random.nextInt(4)

                                when(ed){
                                    0 -> {
                                        eyDuration[i] = "LEFT"
                                    }
                                    1 -> {
                                        eyDuration[i] = "RIGHT"
                                    }
                                    2 -> {
                                        eyDuration[i] = "UP"
                                    }
                                    3 -> {
                                        eyDuration[i] = "DOWN"
                                    }
                                }
                            }
                            if(eyLife[i] > 0 && eyDuration[i] == "LEFT"){
                                if((scrW/2)+eyX[i] > 0){
                                    eyX[i] -= 10
                                }
                            }
                            if(eyLife[i] > 0 && eyDuration[i] == "RIGHT"){
                                if((scrW/2 + scrW/4)+eyX[i] < scrW){
                                    eyX[i] += 10
                                }

                            }
                            if(eyLife[i] > 0 && eyDuration[i] == "UP"){
                                if((scrH - 3*scrH/4) + eyY[i] > 0){
                                    eyY[i] -= 10
                                }
                            }
                            if(eyLife[i] > 0 && eyDuration[i] == "DOWN"){
                                if((scrH - 3*scrH/4) + scrH/8 + eyY[i] < scrH){
                                    eyY[i] += 10
                                }
                            }
                        }
                        for (i in 0 until 3) {
                            if (eyLife[i] > 0) {
                                eyCount[i] += 1
                            }
                        }
                    }
                    sleep(20)

                }
                catch (e: Exception){

                }
            }
            super.run()
        }
    }
    inner class EnemyThread2 : Thread() {
        var run = true
        override fun run() {
            while (run) {
                try {
                    postInvalidate()
                    handler.post{
                            if(eyCount2 == 20){
                                eyCount2 = 0
                            }
                            if(eyCount2 == 0){
                                var ed2 = random.nextInt(4)

                                when(ed2){
                                    0 -> {
                                        eyDuration2 = "LEFT"
                                    }
                                    1 -> {
                                        eyDuration2 = "RIGHT"
                                    }
                                    2 -> {
                                        eyDuration2 = "UP"
                                    }
                                    3 -> {
                                        eyDuration2 = "DOWN"
                                    }
                                }
                            }
                            if(eyLife2 > 0 && eyDuration2 == "LEFT"){
                                if((scrW/2)+eyX2 > 0){
                                    eyX2 -= 10
                                }
                            }
                            if(eyLife2 > 0 && eyDuration2 == "RIGHT"){
                                if((scrW/2 + scrW/4)+eyX2 < scrW){
                                    eyX2 += 10
                                }

                            }
                            if(eyLife2 > 0 && eyDuration2 == "UP"){
                                if(0 + eyY2 > 0){
                                    eyY2 -= 10
                                }
                            }
                            if(eyLife2 > 0 && eyDuration2 == "DOWN"){
                                if((scrH - 3*scrH/4) + scrH/8 + eyY2 < scrH){
                                    eyY2 += 10
                                }
                        }
                        if (eyLife2 > 0) {
                            eyCount2 += 1
                        }
                        if(eyLife2 == 0){
                            gameClear = true

                        }
                    }
                    sleep(20)

                }
                catch (e: Exception){

                }
            }
            super.run()
        }
    }
}
