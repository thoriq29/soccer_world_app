package id.thoriq.dev.footballmatch.league

import id.thoriq.dev.footballmatch.repository.LeagueDetailCallback
import id.thoriq.dev.footballmatch.repository.LeagueRepository
import id.thoriq.dev.footballmatch.responses.LeagueResponse

class LeaguePresenter(private val leagueView: LeagueView, private val repository: LeagueRepository) {
    fun getDataLeague(id: String) {
        leagueView.showLoading()
        repository.getLeagueDetail(id, object : LeagueDetailCallback<LeagueResponse> {
            override fun onLeagueDetailDataLoaded(data: LeagueResponse?) {
                leagueView.onLeagueDetailDataLoaded(data)
            }

            override fun onLeagueDetailDataError() {
                leagueView.onLeagueDetailDataError()
                leagueView.hideLoading()
            }
        })
    }
}


