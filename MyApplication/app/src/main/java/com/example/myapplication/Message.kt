package com.example.myapplication

class Message {
    var what: Int = 0
    var obj: Any? = null
    var whenTime: Long = 0
    var isAsync: Boolean = false
    var next: Message? = null
    var target: Handler? = null

    companion object {
        private var sPool: Message? = null
        private var sPoolSize = 0
        private const val MAX_POOL_SIZE = 50

        fun obtain(): Message {
            sPool?.let {
                val m = it
                sPool = m.next
                m.next = null
                sPoolSize--
                return m
            }
            return Message()
        }
    }
    fun recycle() {
        what = 0
        obj = null
        whenTime = 0
        isAsync = false
        next = null
        target = null

        if (sPoolSize < MAX_POOL_SIZE) {
            next = sPool
            sPool = this
            sPoolSize++
        }
    }
}