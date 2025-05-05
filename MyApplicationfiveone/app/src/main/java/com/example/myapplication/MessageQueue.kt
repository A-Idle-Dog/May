package com.example.myapplication


class MessageQueue {
    //头消息
    private var mMessages: Message? = null
    //将消息按时间顺序放入消息列表
    fun enqueueMessage(msg: Message) {
        val whenTime = msg.whenTime
        var p = mMessages
        var prev: Message? = null
        //遍历消息链，寻找合适的位置(小-->大)
        while (p != null && p.whenTime <= whenTime) {
            prev = p
            p = p.next
        }
        //prev没被赋值，上述循环未进行，即两条件有一未成立
        if (prev == null) {
            msg.next = mMessages
            mMessages = msg
            //插入在中间
        } else {
            msg.next = p
            prev.next = msg
        }
    }
    //取出消息
    fun next(): Message? {
        var prevMsg: Message? = null
        var msg = mMessages
        while (msg != null) {
            val now = System.currentTimeMillis()
            //msg到了处理时间
            if (msg.whenTime <= now) {
                if (prevMsg != null) {
                    //不是头消息，将msg去除
                    prevMsg.next = msg.next
                } else {
                    //是头消息，去除
                    mMessages = msg.next
                }
                return msg
            }
            //未到处理时间，则向前遍历
            prevMsg = msg
            msg = msg.next
        }
        return null
    }
    //是否存在同步屏障
    fun hasSyncBarrier(): Boolean {
        var p = mMessages
        while (p != null) {
            if (p.isAsync) {
                return true
            }
            p = p.next
        }
        return false
    }
    //取出异步消息
    fun getNextAsyncMessage(): Message? {
        var prevMsg: Message? = null
        var msg = mMessages
        while (msg != null) {
            if (msg.isAsync) {
                if (prevMsg != null) {
                    prevMsg.next = msg.next
                } else {
                    mMessages = msg.next
                }
                return msg
            }
            prevMsg = msg
            msg = msg.next
        }
        return null
    }
}