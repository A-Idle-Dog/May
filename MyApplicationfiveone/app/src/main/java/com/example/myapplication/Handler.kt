package com.example.myapplication


abstract class Handler {
    //关联looper与消息列表
    private val mLooper: Looper = Looper.myLooper() ?: throw RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.")
    private val mQueue = mLooper.mQueue
    //发送即时消息
    fun sendMessage(msg: Message) {
        sendMessageDelayed(msg, 0)
    }
    //发送延迟消息
    fun sendMessageDelayed(msg: Message, delayMillis: Long) {
        msg.whenTime = System.currentTimeMillis() + delayMillis
        msg.target = this
        mQueue.enqueueMessage(msg)
    }
    //发送异步消息
    fun sendSyncBarrier() {
        val msg = Message.obtain()
        msg.isAsync = true
        mQueue.enqueueMessage(msg)
    }

    abstract fun handleMessage(msg: Message)
    //分发处理消息
    fun dispatchMessage(msg: Message) {
        handleMessage(msg)
    }
}