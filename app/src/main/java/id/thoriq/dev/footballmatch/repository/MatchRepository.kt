package id.thoriq.dev.footballmatch.repository

import id.thoriq.dev.footballmatch.responses.MatchResponse
import id.thoriq.dev.footballmatch.responses.SearchResponse
import id.thoriq.dev.footballmatch.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchRepository {
    fun getMatchDetail(id: String, callback: MatchRepositoryCallback<MatchResponse?>) {
        ApiService.create()
            .getMatchDetail(id)
            .enqueue(object : Callback<MatchResponse> {
                override fun onFailure(call: Call<MatchResponse>?, t: Throwable?) {
                    if (t != null) {
                        callback.onDataError()
                    }
                }

                override fun onResponse(call: Call<MatchResponse>?, response: Response<MatchResponse>?) {
                    response?.let {
                        if (it.isSuccessful) {
                            callback.onDataLoaded(it.body())
                        } else {
                            callback.onDataError()
                        }
                    }
                }
            })
    }


    fun getNextMatch(id: String, callback: NextMatchCallback<MatchResponse?>) {
        ApiService.create()
            .getNextMatch(id)
            .enqueue(object : Callback<MatchResponse> {
                override fun onFailure(call: Call<MatchResponse>, t: Throwable) {
                    callback.onNextMatchDataError()
                }

                override fun onResponse(call: Call<MatchResponse>, response: Response<MatchResponse>?) {
                    response?.let {
                        if (it.isSuccessful) {
                            callback.onNextMatchDataLoaded(it.body())
                        } else {
                            callback.onNextMatchDataError()
                        }
                    }
                }
            })
    }

    fun getLastMatch(id: String, callback: LastMatchCallback<MatchResponse?>) {
        ApiService.create()
            .getPreviousMatch(id)
            .enqueue(object : Callback<MatchResponse> {
                override fun onFailure(call: Call<MatchResponse>, t: Throwable?) {
                    if (t != null) {
                        callback.onLastMatchDataError()
                    }
                }

                override fun onResponse(call: Call<MatchResponse>, response: Response<MatchResponse>?) {
                    response?.let {
                        if (it.isSuccessful) {
                            callback.onLastMatchDataLoaded(it.body())
                        } else {
                            callback.onLastMatchDataError()
                        }
                    }
                }
            })
    }

    fun searchMatch(query: String, callback: MatchSearchCallback<SearchResponse?>) {
        ApiService.create()
            .getSearchMatch(query)
            .enqueue(object : Callback<SearchResponse> {
                override fun onFailure(call: Call<SearchResponse>, t: Throwable?) {
                    if (t != null) {
                        callback.onSearchError()
                    }
                }

                override fun onResponse(call: Call<SearchResponse?>, response: Response<SearchResponse?>) {
                    response.let {
                        if (it.isSuccessful) {
                            callback.onSearchDataLoaded(it.body())
                        } else {
                            callback.onSearchError()
                        }
                    }
                }
            })
    }
}