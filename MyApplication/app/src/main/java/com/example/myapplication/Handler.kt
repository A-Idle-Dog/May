package com.example.myapplication


abstract class Handler { private val mLooper: Looper = Looper.myLooper() ?: throw RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.")
    private val mQueue = mLooper.mQueue

    fun sendMessage(msg: Message) {
        sendMessageDelayed(msg, 0)
    }

    fun sendMessageDelayed(msg: Message, delayMillis: Long) {
        msg.whenTime = System.currentTimeMillis() + delayMillis
        msg.target = this
        mQueue.enqueueMessage(msg)
    }

    fun sendSyncBarrier() {
        val msg = Message.obtain()
        msg.isAsync = true
        mQueue.enqueueMessage(msg)
    }

    abstract fun handleMessage(msg: Message)

    fun dispatchMessage(msg: Message) {
        handleMessage(msg)
    }
}