package id.thoriq.dev.footballmatch.responses

import com.google.gson.annotations.SerializedName
import id.thoriq.dev.footballmatch.entity.*

data class LeagueResponse(
    @SerializedName("leagues") val leagues: List<Leagues>
)

data class TeamResponse(
    @SerializedName("teams") val teams: List<Teams>
)

data class MatchResponse(
    @SerializedName("events") val events: List<Match>
)

data class SearchResponse(
    @SerializedName("event") val event: List<Match>
)

data class TeamsResponse(
    @SerializedName("teams") val teams: List<Teams>
)

data class StandingsResponse(
    @SerializedName("table") val table: List<Standing>
)

data class PlayerListResponse(
    @SerializedName("player") val player: List<Player>
)

data class PlayerResponse(
    @SerializedName("players") val players: List<Player>
)