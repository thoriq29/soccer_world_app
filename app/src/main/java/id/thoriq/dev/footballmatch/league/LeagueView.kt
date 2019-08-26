package id.thoriq.dev.footballmatch.league

import id.thoriq.dev.footballmatch.repository.LeagueDetailCallback
import id.thoriq.dev.footballmatch.responses.LeagueResponse

interface LeagueView : LeagueDetailCallback<LeagueResponse> {
    fun showLoading()
    fun hideLoading()
}
