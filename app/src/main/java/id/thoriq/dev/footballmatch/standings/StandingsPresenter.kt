package id.thoriq.dev.footballmatch.standings

import id.thoriq.dev.footballmatch.repository.StandingsCallback
import id.thoriq.dev.footballmatch.repository.StandingsRepository
import id.thoriq.dev.footballmatch.responses.StandingsResponse

class StandingsPresenter(private val standingsView: StandingsView, private val repository: StandingsRepository) {
    fun getStandings(id: String) {
        standingsView.showLoading()
        repository.getLeagueStandings(id, object : StandingsCallback<StandingsResponse?> {
            override fun onStandingsLoaded(data: StandingsResponse?) {
                standingsView.onStandingsLoaded(data)
            }

            override fun onStandingsError() {
                standingsView.onStandingsError()
            }
        })
    }
}