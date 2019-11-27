package com.protect.love.bean
import com.google.gson.annotations.SerializedName


data class OneDayMsgBean(
    @SerializedName("caption")
    val caption: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("dateline")
    val dateline: String,
    @SerializedName("fenxiang_img")
    val fenxiangImg: String,
    @SerializedName("love")
    val love: String,
    @SerializedName("note")
    val note: String,
    @SerializedName("picture")
    val picture: String,
    @SerializedName("picture2")
    val picture2: String,
    @SerializedName("s_pv")
    val sPv: String,
    @SerializedName("sid")
    val sid: String,
    @SerializedName("sp_pv")
    val spPv: String,
    @SerializedName("tags")
    val tags: List<Tag>,
    @SerializedName("translation")
    val translation: String,
    @SerializedName("tts")
    val tts: String
)

data class Tag(
    @SerializedName("id")
    val id: Any,
    @SerializedName("name")
    val name: Any
)