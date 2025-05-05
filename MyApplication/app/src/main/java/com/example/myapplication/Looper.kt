package com.example.myapplication


class Looper private constructor() {
    val mQueue = MessageQueue()

    companion object {
        private val sThreadLocal = ThreadLocal<Looper>()

        fun prepare() {
            if (sThreadLocal.get() != null) {
                throw RuntimeException("Only one Looper may be created per thread")
            }
            sThreadLocal.set(Looper())
        }

        fun myLooper(): Looper? {
            return sThreadLocal.get()
        }

        fun loop() {
            val me = myLooper()
            if (me == null) {
                throw RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.")
            }
            val queue = me.mQueue
            while (true) {
                var msg = queue.next()
                if (msg == null) {
                    continue
                }
                if (queue.hasSyncBarrier()) {
                    msg = queue.getNextAsyncMessage()
                }
                try {
                    msg?.target?.dispatchMessage(msg)
                } finally {
                    msg?.recycle()
                }
            }
        }
    }
}