package id.thoriq.dev.footballmatch.team

import id.thoriq.dev.footballmatch.repository.AwayTeamCallback
import id.thoriq.dev.footballmatch.repository.HomeTeamCallback
import id.thoriq.dev.footballmatch.repository.TeamCallback
import id.thoriq.dev.footballmatch.repository.TeamSearchCallback
import id.thoriq.dev.footballmatch.responses.TeamResponse
import id.thoriq.dev.footballmatch.responses.TeamsResponse

interface TeamView : TeamCallback<TeamResponse> {
    fun showLoading()
    fun hideLoading()
}

interface HomeTeamView : HomeTeamCallback<TeamsResponse> {
    fun showLoading()
    fun hideLoading()
}

interface AwayTeamView : AwayTeamCallback<TeamsResponse> {
    fun showLoading()
    fun hideLoading()
}

interface TeamSearchView : TeamSearchCallback<TeamsResponse> {
    fun showLoading()
    fun hideLoading()
    fun onEmptyResult()
}
