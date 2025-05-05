package com.example.myapplication

class Message {
    val lock:Any=Any()
    var what: Int = 0
    var obj: Any? = null
    var whenTime: Long = 0
    //判断是否为异步消息
    var isAsync: Boolean = false
    var next: Message? = null
    //所在handler
    var target: Handler? = null

    companion object {
        //消息池，以便复用消息对象
        private var sPool: Message? = null
        private var sPoolSize = 0
        private const val MAX_POOL_SIZE = 50
        //在消息池中获取消息对象，为空则创建一个消息对象
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
    //回收消息对象，将其重置并放入消息池
    fun recycle() {
        synchronized(lock){
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
}