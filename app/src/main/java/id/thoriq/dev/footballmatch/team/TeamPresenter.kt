package id.thoriq.dev.footballmatch.team

import id.thoriq.dev.footballmatch.repository.*
import id.thoriq.dev.footballmatch.responses.TeamResponse
import id.thoriq.dev.footballmatch.responses.TeamsResponse


class TeamPresenter(private val teamView: TeamView, private val repository: TeamRepository) {
    fun getAllTeam(id: String) {
        teamView.showLoading()
        repository.getAllTeam(id, object : TeamCallback<TeamResponse?> {
            override fun onTeamDataLoaded(data: TeamResponse?) {
                teamView.onTeamDataLoaded(data)
            }

            override fun onTeamDataError() {
                teamView.onTeamDataError()
            }

        })
    }
}

class HomeTeamPresenter(private val teamView: HomeTeamView, private val repository: TeamRepository) {
    fun getHomeTeam(id: String) {
        teamView.showLoading()
        repository.getHomeTeam(id, object : HomeTeamCallback<TeamsResponse?> {
            override fun onHomeTeamDataLoaded(data: TeamsResponse?) {
                teamView.onHomeTeamDataLoaded(data)
            }

            override fun onHomeTeamDataError() {
                teamView.onHomeTeamDataError()
            }

        })
    }
}


class AwayTeamPresenter(private val teamView: AwayTeamView, private val repository: TeamRepository) {
    fun getAwayTeam(id: String) {
        teamView.showLoading()
        repository.getAwayTeam(id, object : AwayTeamCallback<TeamsResponse?> {
            override fun onAwayTeamDataLoaded(data: TeamsResponse?) {
                teamView.onAwayTeamDataLoaded(data)
            }

            override fun onAwayTeamDataError() {
                teamView.onAwayTeamDataError()
            }

        })
    }
}

class SearchTeamPresenter(private val teamView: TeamSearchView, private val repository: TeamRepository) {
    fun getSearchTeam(query: String) {
        teamView.showLoading()
        repository.getSearchTeam(query, object : TeamSearchCallback<TeamsResponse?> {
            override fun onTeamSearchLoaded(data: TeamsResponse?) {
                if (data?.teams != null) {
                    teamView.onTeamSearchLoaded(data)
                    teamView.hideLoading()
                } else {
                    teamView.onEmptyResult()
                    teamView.hideLoading()
                }

            }

            override fun onTeamSearchError() {
                teamView.onTeamSearchError()
                teamView.hideLoading()
            }


        })
    }
}