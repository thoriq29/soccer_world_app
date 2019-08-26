package id.thoriq.dev.footballmatch.repository

import id.thoriq.dev.footballmatch.responses.StandingsResponse
import id.thoriq.dev.footballmatch.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StandingsRepository {
    fun getLeagueStandings(id: String, callback: StandingsCallback<StandingsResponse?>) {
        ApiService.create()
            .getStandings(id)
            .enqueue(object : Callback<StandingsResponse> {
                override fun onFailure(call: Call<StandingsResponse?>, t: Throwable?) {
                    if (t != null) {
                        callback.onStandingsError()
                    }
                }

                override fun onResponse(call: Call<StandingsResponse>?, response: Response<StandingsResponse>?) {
                    response?.let {
                        if (it.isSuccessful) {
                            callback.onStandingsLoaded(it.body())
                        } else {
                            callback.onStandingsError()
                        }
                    }
                }
            })
    }
}