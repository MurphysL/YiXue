package cn.edu.nuc.androidlab.yixue.util

/**
 * Live Builder
 *
 * Created by MurphySL on 2017/7/12.
 */

val TEXT_LIVE_TABLE_NAME  : String = "Live" // 文字 Live 表名

val TEXT_LIVE_STAR : String = "star" // 文字 Live 评价星级属性名

val TEXT_LIVE_COMMIT : String = "commit" // 文字 Live 评价

val TEXT_LIVE_USER_ID : String = "userId" // Live 用户ID

val TEXT_LIVE_CONVERSATION_ID : String = "conversationId" // Live 会话ID

interface Element{
    fun render(builder: StringBuilder, indent : String)
}

open class TextElement(val name : String) : Element{
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("  $indent$name\n")
    }
}

open class Tag(name : String) : TextElement(name) {

    val children = arrayListOf<TextElement>()
    val attributes = hashMapOf<String, String>()

    fun <T : TextElement>initTag(t : T, init : T.()->Unit) : T{
        children.add(t)
        t.init()
        return t
    }

    override fun render(builder: StringBuilder, indent: String) {
        //builder.append("$indent<$name${renderAttr()}>\n")
        builder.append("$indent\"$name\" : \"${renderAttr()}\"\n")
        for (c in children){
            c.render(builder, indent+"  ")
        }
        //builder.append("$indent</$name>\n")
        builder.append(",\n")
    }

    fun renderAttr() : String?{
        val builder  = StringBuilder()
        for(a in attributes.keys){
            builder.append(" $a=\"${attributes[a]}\"")
        }
        return builder.toString()
    }

    override fun toString(): String {
        val builder = StringBuilder()

        render(builder,"")

        return builder.toString()
    }
}

open class TagWithText(name : String) : Tag(name){
    operator fun String.unaryPlus(){
        children.add(TextElement(this))
    }
}

class UserID : TagWithText(TEXT_LIVE_USER_ID)

class ConversationID : TagWithText(TEXT_LIVE_CONVERSATION_ID)

class STAR : TagWithText(TEXT_LIVE_STAR)

class COMMIT : TagWithText(TEXT_LIVE_COMMIT)

class Live : TagWithText("live"){
    fun userId(init : UserID.()->Unit) : UserID = initTag(UserID(), init)

    fun conversationId(init : ConversationID.()->Unit) : ConversationID = initTag(ConversationID(), init)

    fun star(init : STAR.()->Unit) : STAR = initTag(STAR(), init)

    fun commit(init : COMMIT.()->Unit) : COMMIT = initTag(COMMIT(), init)
}

fun live(init : Live.()->Unit) : Live = Live().apply (init)

fun main(args: Array<String>) {
    live {
        userId { + "123" }
        conversationId { +"123" }
    }
}

