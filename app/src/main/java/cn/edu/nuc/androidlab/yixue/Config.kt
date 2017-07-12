package cn.edu.nuc.androidlab.yixue

/**
 * 常量
 *
 * Created by MurphySL on 2017/7/12.
 */
class Config{
    companion object {
        @JvmStatic
        val TEXT_LIVE_TABLE_NAME  : String = "Live" // 文字 Live 表名
        @JvmStatic
        val TEXT_LIVE_STAR : String = "star" // 文字 Live 评价星级属性名
        @JvmStatic
        val TEXT_LIVE_COMMIT : String = "commit" // 文字 Live 评价
        @JvmStatic
        val TEXT_LIVE_USER_ID : String = "userId" // Live 用户ID
        @JvmStatic
        val TEXT_LIVE_CONVERSATION_ID : String = "conversationId" // Live 会话ID
        @JvmStatic
        val USER_TABLE : String= "_User" // 用户信息表
        @JvmStatic
        val CONVERSATION_TABLE : String = "_Conversation" // 会话信息表
    }
}
