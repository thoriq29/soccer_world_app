package id.thoriq.dev.footballmatch.repository

import id.thoriq.dev.footballmatch.responses.LeagueResponse
import id.thoriq.dev.footballmatch.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LeagueRepository {
    fun getLeagueDetail(id: String, callback: LeagueDetailCallback<LeagueResponse>) {
        ApiService.create()
            .getLeagueDetail(id)
            .enqueue(object : Callback<LeagueResponse> {
                override fun onFailure(call: Call<LeagueResponse>, t: Throwable) {
                    callback.onLeagueDetailDataError()
                }

                override fun onResponse(call: Call<LeagueResponse>, response: Response<LeagueResponse>?) {
                    response?.let {
                        if (it.isSuccessful) {
                            callback.onLeagueDetailDataLoaded(it.body())
                        } else {
                            callback.onLeagueDetailDataError()
                        }
                    }
                }

            })
    }
}