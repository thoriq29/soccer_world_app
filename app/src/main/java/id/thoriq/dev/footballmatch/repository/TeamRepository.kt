package id.thoriq.dev.footballmatch.repository

import id.thoriq.dev.footballmatch.responses.TeamResponse
import id.thoriq.dev.footballmatch.responses.TeamsResponse
import id.thoriq.dev.footballmatch.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeamRepository {
    fun getAllTeam(id: String, callback: TeamCallback<TeamResponse?>) {
        ApiService.create()
            .getAllLeagueTeam(id)
            .enqueue(object : Callback<TeamResponse> {
                override fun onFailure(call: Call<TeamResponse>?, t: Throwable?) {
                    if (t != null) {
                        callback.onTeamDataError()
                    }
                }

                override fun onResponse(call: Call<TeamResponse>?, response: Response<TeamResponse>?) {
                    response?.let {
                        if (it.isSuccessful) {
                            callback.onTeamDataLoaded(it.body())
                        } else {
                            callback.onTeamDataError()
                        }
                    }
                }
            })
    }

    fun getHomeTeam(id: String, callback: HomeTeamCallback<TeamsResponse?>) {
        ApiService.create()
            .getTeamDetail(id)
            .enqueue(object : Callback<TeamsResponse> {
                override fun onFailure(call: Call<TeamsResponse?>, t: Throwable?) {
                    callback.onHomeTeamDataError()
                }

                override fun onResponse(call: Call<TeamsResponse?>, response: Response<TeamsResponse?>) {
                    response.let {
                        if (it.isSuccessful) {
                            callback.onHomeTeamDataLoaded(it.body())
                        } else {
                            callback.onHomeTeamDataError()
                        }
                    }
                }

            })
    }

    fun getAwayTeam(id: String, callback: AwayTeamCallback<TeamsResponse?>) {
        ApiService.create()
            .getTeamDetail(id)
            .enqueue(object : Callback<TeamsResponse> {
                override fun onFailure(call: Call<TeamsResponse?>, t: Throwable?) {
                    callback.onAwayTeamDataError()
                }

                override fun onResponse(call: Call<TeamsResponse?>, response: Response<TeamsResponse?>) {
                    response.let {
                        if (it.isSuccessful) {
                            callback.onAwayTeamDataLoaded(it.body())
                        } else {
                            callback.onAwayTeamDataError()
                        }
                    }
                }
            })
    }

    fun getSearchTeam(query: String, callback: TeamSearchCallback<TeamsResponse?>) {
        ApiService.create()
            .getSearchTeam(query)
            .enqueue(object : Callback<TeamsResponse> {
                override fun onFailure(call: Call<TeamsResponse?>, t: Throwable?) {
                    callback.onTeamSearchError()
                }

                override fun onResponse(call: Call<TeamsResponse?>, response: Response<TeamsResponse?>) {
                    response.let {
                        if (it.isSuccessful) {
                            callback.onTeamSearchLoaded(it.body())
                        } else {
                            callback.onTeamSearchError()
                        }
                    }
                }

            })
    }
}