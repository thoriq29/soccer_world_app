package id.thoriq.dev.footballmatch.match

import id.thoriq.dev.footballmatch.repository.*
import id.thoriq.dev.footballmatch.responses.MatchResponse
import id.thoriq.dev.footballmatch.responses.SearchResponse

class PreviousMatchPresenter(private val view: LastMatchView, private val repository: MatchRepository) {
    fun getPreviousMatch(id: String) {
        view.showLoading()
        repository.getLastMatch(id, object : LastMatchCallback<MatchResponse?> {
            override fun onLastMatchDataLoaded(data: MatchResponse?) {
                if (data != null) {
                    view.onLastMatchDataLoaded(data)
                    view.hideLoading()
                } else {
                    view.emptyPastMatch()
                }
            }

            override fun onLastMatchDataError() {
                view.hideLoading()
                view.emptyPastMatch()
                view.onLastMatchDataError()
            }

        })
    }
}

class NextMatchPresenter(private val view: NextMatchView, private val repository: MatchRepository) {
    fun getNextMatch(id: String) {
        view.showLoading()
        repository.getNextMatch(id, object : NextMatchCallback<MatchResponse?> {
            override fun onNextMatchDataLoaded(data: MatchResponse?) {
                if (data != null) {
                    view.onNextMatchDataLoaded(data)
                    view.hideLoading()
                } else {
                    view.emptyNextMatch()
                }
            }

            override fun onNextMatchDataError() {
                view.hideLoading()
                view.onNextMatchDataError()
            }
        })

    }
}

class MatchPresenter(private val view: MatchDetailView, private val matchRepository: MatchRepository) {
    fun getMatchDetail(id: String) {
        view.showLoading()
        matchRepository.getMatchDetail(id, object : MatchRepositoryCallback<MatchResponse?> {
            override fun onDataLoaded(data: MatchResponse?) {
                view.onDataLoaded(data)
                view.hideLoading()
            }

            override fun onDataError() {
                view.hideLoading()
                view.onDataError()
            }

        })

    }
}

class MatchSearchPresenter(private val view: MatchSearchView, private val matchRepository: MatchRepository) {
    fun getMatchs(query: String) {
        view.showLoading()
        matchRepository.searchMatch(query, object : MatchSearchCallback<SearchResponse?> {
            override fun onSearchDataLoaded(data: SearchResponse?) {
                if (data?.event != null) {
                    view.onSearchDataLoaded(data)
                    view.hideLoading()
                } else {
                    view.onEmptyResult()
                    view.hideLoading()
                }
            }

            override fun onSearchError() {
                view.hideLoading()
                view.onSearchError()
            }

        })
    }
}
