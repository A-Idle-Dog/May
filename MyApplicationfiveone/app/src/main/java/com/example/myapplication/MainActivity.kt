package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var tvMessageLog: TextView
    private lateinit var handler: Handler

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvMessageLog = findViewById(R.id.tv_message_log)

        Thread {
            Looper.prepare()
            handler = object : Handler() {
                override fun handleMessage(msg: Message) {
                    runOnUiThread {
                        tvMessageLog.append("收到消息: ${msg.what}\n")
                    }
                }
            }
            Looper.loop()
        }.start()

        val btnSendImmediate = findViewById<Button>(R.id.btn_send_immediate)
        btnSendImmediate.setOnClickListener {
            val msg = Message.obtain()
            msg.what = 1
            handler.sendMessage(msg)
        }

        val btnSendDelayed = findViewById<Button>(R.id.btn_send_delayed)
        btnSendDelayed.setOnClickListener {
            val msg = Message.obtain()
            msg.what = 2
            handler.sendMessageDelayed(msg, TimeUnit.SECONDS.toMillis(2))
        }
    }
}