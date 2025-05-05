package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var  btnSendImmediate:Button
    private lateinit var btnSendDelayed:Button
    private lateinit var mtvMessage: TextView
    private lateinit var handler: Handler

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initEvent()


    }
    fun initView(){
        mtvMessage = findViewById(R.id.tv_message_log)
        btnSendImmediate = findViewById<Button>(R.id.btn_send_immediate)
        btnSendDelayed = findViewById<Button>(R.id.btn_send_delayed)
    }
    fun initEvent(){
        startRecive()
        btnSendImmediate.setOnClickListener {
            val msg = Message.obtain()
            msg.what = 1
            msg.obj="即时消息"
            handler.sendMessage(msg)
        }


        btnSendDelayed.setOnClickListener {
            val msg = Message.obtain()
            msg.what = 2
            msg.obj="延时消息"
            handler.sendMessageDelayed(msg, 1000)
        }

    }
    fun startRecive(){
        Thread {
            Looper.prepare()

            handler = object : Handler() {
                override fun handleMessage(msg: Message) {
                    val what=msg.what
                    val obj=msg.obj
                    runOnUiThread {
                        mtvMessage.append("收到消息: $what$obj\n")
                    }
                }
            }
            Looper.loop()
        }.start()
    }
}