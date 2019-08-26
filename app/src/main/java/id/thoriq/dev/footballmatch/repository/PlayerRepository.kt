package id.thoriq.dev.footballmatch.repository

import id.thoriq.dev.footballmatch.responses.PlayerListResponse
import id.thoriq.dev.footballmatch.responses.PlayerResponse
import id.thoriq.dev.footballmatch.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlayerRepository {
    fun getAllPlayer(id: String, callback: PlayersCallback<PlayerListResponse?>) {
        ApiService.create()
            .getPlayer(id)
            .enqueue(object : Callback<PlayerListResponse> {
                override fun onFailure(call: Call<PlayerListResponse?>, t: Throwable?) {
                    if (t != null) {
                        callback.onPlayersError(t)
                    }
                }

                override fun onResponse(call: Call<PlayerListResponse>?, response: Response<PlayerListResponse>?) {
                    response?.let {
                        if (it.isSuccessful) {
                            callback.onPlayersLoaded(it.body())
                        }
                    }
                }
            })
    }

    fun getPlayerDetail(id: String, callback: PlayerDetailCallback<PlayerResponse?>) {
        ApiService.create()
            .getDetailPlayer(id)
            .enqueue(object : Callback<PlayerResponse> {
                override fun onFailure(call: Call<PlayerResponse>, t: Throwable) {
                    callback.onPlayerError()
                }

                override fun onResponse(call: Call<PlayerResponse>, response: Response<PlayerResponse>) {
                    if (response.isSuccessful) {
                        callback.onPlayerDetailLoaded(response.body())
                    }
                }


            })
    }
}