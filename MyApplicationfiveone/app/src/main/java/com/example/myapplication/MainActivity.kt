package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var  mbtnSendImmediate:Button
    private lateinit var mbtnSendDelayed:Button
    private lateinit var mtvMessage: TextView
    private lateinit var handler: Handler
    private lateinit var mbtnCount:Button
    private var countSecond=10

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initEvent()


    }
    private fun initView(){
        mtvMessage = findViewById(R.id.tv_message_log)
        mbtnSendImmediate = findViewById(R.id.btn_send_immediate)
        mbtnSendDelayed = findViewById(R.id.btn_send_delayed)
        mbtnCount=findViewById(R.id.btn_count)
    }
    private fun initEvent(){


        mbtnCount.setOnClickListener{
            val msg= Message.obtain()
            countSecond=10
            msg.what=2
            handler.sendMessageDelayed(msg,1000)
        }
        mbtnSendImmediate.setOnClickListener {
            val msg = Message.obtain()
            msg.what = 1
            msg.obj="即时消息"
            handler.sendMessage(msg)
        }


        mbtnSendDelayed.setOnClickListener {
            val msg = Message.obtain()
            msg.what = 1
            msg.obj="延时消息"
            handler.sendMessageDelayed(msg, 1000)
        }
        startRecive()

    }
    private fun startRecive(){
        Thread {
            Looper.prepare()

            handler = object : Handler() {
                override fun handleMessage(msg: Message) {
                    val newMsg=Message()
                    newMsg.what=2
                    val what=msg.what
                    val obj=msg.obj
                    when(what){
                        1->runOnUiThread {
                            mtvMessage.append("收到消息: $what$obj\n")
                        }
                        2-> if (countSecond>0){
                            runOnUiThread {
                                mtvMessage.append("倒计时:$countSecond\n")
                            }
                            countSecond--
                            handler.sendMessageDelayed(newMsg,1000)

                        }else{
                           runOnUiThread {
                               mtvMessage.append("倒计时结束\n")
                           }

                        }
                    }

                }
            }
            Looper.loop()
        }.start()
    }
}