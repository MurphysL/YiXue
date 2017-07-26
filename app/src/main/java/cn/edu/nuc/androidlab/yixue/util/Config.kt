package cn.edu.nuc.androidlab.yixue.util

/**
 * 常量
 *
 * Created by MurphySL on 2017/7/12.
 */
class Config{
    companion object {
        @JvmStatic
        val USER_TABLE : String= "_User" // 用户信息表
        @JvmStatic
        val CONVERSATION_TABLE : String = "_Conversation" // 会话信息表

        @JvmStatic
        val TEXT_LIVE_TABLE_NAME  : String = "Live" // 文字 Live 表名
        @JvmStatic
        val TEXT_LIVE_USER_ID : String = "userId" // 文字 Live 用户ID
        @JvmStatic
        val TEXT_LIVE_CONVERSATION_ID : String = "conversationId" // 文字 Live 会话ID
        @JvmStatic
        val TEXT_LIVE_LIVE_NAME : String = "name" // 文字 Live 主题
        @JvmStatic
        val TEXT_LIVE_STAR : String = "star" // 文字 Live 评价星级
        @JvmStatic
        val TEXT_LIVE_SUMMARY : String = "summary" // 文字 Live 简介
        @JvmStatic
        val TEXT_LIVE_PRICE : String = "price" // 文字 Live 价格
        @JvmStatic
        val TEXT_LIVE_TYPE : String = "type" // 文字 Live 类型
        @JvmStatic
        val TEXT_LIVE_PIC : String = "pic" // 文字 Live 封面
        @JvmStatic
        val TEXT_LIVE_START_TIME : String = "startAt" // 文字 Live 开始时间


    }
}
