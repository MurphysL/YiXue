package cn.edu.nuc.androidlab.yixue.bean

import cn.edu.nuc.androidlab.yixue.util.Config
import com.avos.avoscloud.AVClassName
import com.avos.avoscloud.AVObject

/**
 * LU
 *
 * Created by MurphySL on 2017/7/27.
 */
@AVClassName("LU")
class LU : AVObject(){
    companion object {
        val CREATOR : AVObjectCreator = AVObjectCreator.instance
    }

    var star : Int
        get() = getInt(Config.LU_STAR)
        set(value) = put(Config.LU_STAR, value)

    var comment : String?
        get() = getString(Config.LU_COMMENT)
        set(value) = put(Config.LU_COMMENT, value)

    var liveId : String
        get() {
            return getAVObject<AVObject>(Config.LU_LIVE_ID).objectId
        }
        set(value) = put(Config.LU_LIVE_ID, AVObject.createWithoutData(Config.LIVE_TABLE, value))

    var userId : String
        get() {
            return getAVObject<AVObject>(Config.LU_USER_ID).objectId
        }
        set(value) = put(Config.LU_USER_ID, AVObject.createWithoutData(Config.USER_TABLE, value))

    override fun toString(): String {
        return """
            {
              objectId : $objectId ,
              liveId : $liveId ,
              userId : $userId ,
              name : $comment ,
              star : $star
            }
        """
    }
}