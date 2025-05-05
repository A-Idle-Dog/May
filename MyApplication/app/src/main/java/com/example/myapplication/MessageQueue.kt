package com.example.myapplication


class MessageQueue {
    private var mMessages: Message? = null

    fun enqueueMessage(msg: Message) {
        val whenTime = msg.whenTime
        var p = mMessages
        var prev: Message? = null
        while (p != null && p.whenTime <= whenTime) {
            prev = p
            p = p.next
        }
        if (prev == null) {
            msg.next = mMessages
            mMessages = msg
        } else {
            msg.next = p
            prev.next = msg
        }
    }

    fun next(): Message? {
        var prevMsg: Message? = null
        var msg = mMessages
        while (msg != null) {
            val now = System.currentTimeMillis()
            if (msg.whenTime <= now) {
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