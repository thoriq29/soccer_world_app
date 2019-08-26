package id.thoriq.dev.footballmatch.services

import id.thoriq.dev.footballmatch.responses.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("json/1/lookupleague.php")
    fun getLeagueDetail(@Query("id") id: String): Call<LeagueResponse>

    @GET("json/1/lookup_all_teams.php")
    fun getAllLeagueTeam(@Query("id") id: String): Call<TeamResponse>

    @GET("json/1/eventspastleague.php")
    fun getPreviousMatch(@Query("id") id: String): Call<MatchResponse>

    @GET("json/1/eventsnextleague.php")
    fun getNextMatch(@Query("id") id: String): Call<MatchResponse>

    @GET("json/1/lookupevent.php")
    fun getMatchDetail(@Query("id") id: String): Call<MatchResponse>

    @GET("json/1/searchevents.php")
    fun getSearchMatch(@Query("e") e: String): Call<SearchResponse>

    @GET("json/1/lookupteam.php")
    fun getTeamDetail(@Query("id") id: String): Call<TeamsResponse>

    @GET("json/1/lookuptable.php")
    fun getStandings(@Query("l") id: String): Call<StandingsResponse>

    @GET("json/1/lookup_all_players.php")
    fun getPlayer(@Query("id") id: String): Call<PlayerListResponse>

    @GET("json/1/lookupplayer.php")
    fun getDetailPlayer(@Query("id") id: String): Call<PlayerResponse>

    @GET("json/1/searchteams.php")
    fun getSearchTeam(@Query("t") t: String): Call<TeamsResponse>

    companion object {
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://www.thesportsdb.com/api/v1/")
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}