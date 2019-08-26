package id.thoriq.dev.footballmatch.repository

interface MatchRepositoryCallback<T> {
    fun onDataLoaded(data: T?)
    fun onDataError()
}

interface NextMatchCallback<T> {
    fun onNextMatchDataLoaded(data: T?)
    fun onNextMatchDataError()
}

interface LastMatchCallback<T> {
    fun onLastMatchDataLoaded(data: T?)
    fun onLastMatchDataError()
}

interface LeagueDetailCallback<T> {
    fun onLeagueDetailDataLoaded(data: T?)
    fun onLeagueDetailDataError()
}

interface TeamCallback<T> {
    fun onTeamDataLoaded(data: T?)
    fun onTeamDataError()
}

interface HomeTeamCallback<T> {
    fun onHomeTeamDataLoaded(data: T?)
    fun onHomeTeamDataError()
}

interface AwayTeamCallback<T> {
    fun onAwayTeamDataLoaded(data: T?)
    fun onAwayTeamDataError()
}

interface MatchSearchCallback<T> {
    fun onSearchDataLoaded(data: T?)
    fun onSearchError()
}

interface StandingsCallback<T> {
    fun onStandingsLoaded(data: T?)
    fun onStandingsError()
}

interface PlayersCallback<T> {
    fun onPlayersLoaded(data: T?)
    fun onPlayersError(error: Throwable)
}

interface PlayerDetailCallback<T> {
    fun onPlayerDetailLoaded(data: T?)
    fun onPlayerError()
}

interface TeamSearchCallback<T> {
    fun onTeamSearchLoaded(data: T?)
    fun onTeamSearchError()
}
