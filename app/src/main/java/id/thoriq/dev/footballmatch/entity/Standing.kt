package id.thoriq.dev.footballmatch.entity

import com.google.gson.annotations.SerializedName

data class Standing(
    @SerializedName("name") val name: String,
    @SerializedName("teamid") val teamid: Int,
    @SerializedName("played") val played: Int,
    @SerializedName("goalsfor") val goalsfor: Int,
    @SerializedName("goalsagainst") val goalsagainst: Int,
    @SerializedName("goalsdifference") val goalsdifference: Int,
    @SerializedName("win") val win: Int,
    @SerializedName("draw") val draw: Int,
    @SerializedName("loss") val loss: Int,
    @SerializedName("total") val total: Int
)
