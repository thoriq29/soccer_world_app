package id.thoriq.dev.footballmatch.standings

import id.thoriq.dev.footballmatch.repository.StandingsCallback
import id.thoriq.dev.footballmatch.responses.StandingsResponse

interface StandingsView : StandingsCallback<StandingsResponse> {
    fun showLoading()
    fun hideLoading()
}