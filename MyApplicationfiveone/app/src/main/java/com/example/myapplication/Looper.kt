package com.example.myapplication

import android.util.Log


class Looper private constructor() {
    val mQueue = MessageQueue()

    companion object {
        //线程局部变量，存储每个线程的Looper实例
        private val sThreadLocal = ThreadLocal<Looper>()
        //为当前线程准备Looper实例，同时确保每个线程只有一个Looper实例
        fun prepare() {
            if (sThreadLocal.get() != null) {
                throw RuntimeException("Only one Looper may be created per thread")
            }
            sThreadLocal.set(Looper())
        }
        //获取当前线程的Looper实例
        fun myLooper(): Looper? {
            return sThreadLocal.get()
        }
        //启动Looper
        fun loop() {
            val me = myLooper()
            if (me == null) {
                throw RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.")
            }
            val queue = me.mQueue
            synchronized(queue){
                while (true) {
                    var msg = queue.next()
                    if (msg == null) {
                    continue
                }
                    if (queue.hasSyncBarrier()) {
                    msg = queue.getNextAsyncMessage()
                }
                    try {
                        Log.d("0", "loop: ${msg?.what} ")
                    //分发消息进行处理
                    msg?.target?.dispatchMessage(msg)
                        Log.d("3", "loop: ${msg?.what} \")")
                }
                    finally {
                    msg?.recycle()
                }
            }
            }
        }
    }
}