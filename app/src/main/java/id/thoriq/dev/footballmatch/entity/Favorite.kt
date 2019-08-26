package id.thoriq.dev.footballmatch.entity

data class Favorite(
    val id: Long?, val matchId: String?, val matchName: String?, val homeTeam: String?,
    val homeId: String?, val homeScore: String?, val homeBadge: String?, val awayTeam: String?,
    val awayId: String?, val awayScore: String?, val awayBadge: String?, val leagueId: String?,
    val date: String?, val hour: String?, val matchStatus: String?
) {

    companion object {
        const val TABLE_MATCH_FAVORITE: String = "TABLE_MATCH_FAVORITE"
        const val ID: String = "ID_"
        const val MATCH_ID: String = "MATCH_ID"
        const val MATCH_NAME: String = "MATCH_NAME"
        const val HOME_ID: String = "HOME_ID"
        const val HOME_TEAM: String = "HOME_TEAM"
        const val HOME_SCORE: String = "HOME_SCORE"
        const val HOME_BADGE: String = "HOME_BADGE"
        const val AWAY_ID: String = "AWAY_ID"
        const val AWAY_TEAM: String = "AWAY_TEAM"
        const val AWAY_SCORE: String = "AWAY_SCORE"
        const val AWAY_BADGE: String = "AWAY_BADGE"
        const val LEAGUE_ID: String = "LEAGUE_ID"
        const val MATCH_DATE: String = "MATCH_DATE"
        const val MATCH_TIME: String = "MATCH_TIME"
        const val MATCH_STATUS: String = "MATCH_STATUS"
    }
}

data class TeamFavorite(
    val id: Long?, val teamId: String?, val teamName: String?, val teamDesc: String?,
    val teamBadge: String?, val leagueId: String?
) {
    companion object {
        const val TABLE_TEAM_FAVORITE: String = "TABLE_TEAM_FAVORITE"
        const val ID: String = "ID_"
        const val TEAM_ID: String = "TEAM_ID"
        const val TEAM_NAME: String = "TEAM_NAME"
        const val TEAM_DESC: String = "TEAM_DESC"
        const val TEAM_BADGE: String = "TEAM_BADGE"
        const val LEAGUE_ID: String = "LEAGUE_ID"
    }
}