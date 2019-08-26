package id.thoriq.dev.footballmatch.match

import id.thoriq.dev.footballmatch.repository.LastMatchCallback
import id.thoriq.dev.footballmatch.repository.MatchRepositoryCallback
import id.thoriq.dev.footballmatch.repository.MatchSearchCallback
import id.thoriq.dev.footballmatch.repository.NextMatchCallback
import id.thoriq.dev.footballmatch.responses.MatchResponse
import id.thoriq.dev.footballmatch.responses.SearchResponse

interface NextMatchView : NextMatchCallback<MatchResponse> {
    fun showLoading()
    fun hideLoading()
    fun emptyNextMatch()

}

interface LastMatchView : LastMatchCallback<MatchResponse> {
    fun showLoading()
    fun hideLoading()
    fun emptyPastMatch()
}

interface MatchDetailView : MatchRepositoryCallback<MatchResponse> {
    fun showLoading()
    fun hideLoading()
}

interface MatchSearchView : MatchSearchCallback<SearchResponse> {
    fun showLoading()
    fun hideLoading()
    fun onEmptyResult()
}
