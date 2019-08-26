package id.thoriq.dev.footballmatch.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import id.thoriq.dev.footballmatch.entity.Favorite
import id.thoriq.dev.footballmatch.entity.TeamFavorite
import org.jetbrains.anko.db.*

class SqliteDatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "soccer.db", null, 1) {
    companion object {
        private var instance: SqliteDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): SqliteDatabaseOpenHelper {
            if (instance == null) {
                instance = SqliteDatabaseOpenHelper(ctx.applicationContext)
            }
            return instance as SqliteDatabaseOpenHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // create tables
        db.createTable(
            Favorite.TABLE_MATCH_FAVORITE, true,
            Favorite.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            Favorite.MATCH_ID to TEXT + UNIQUE,
            Favorite.MATCH_NAME to TEXT,
            Favorite.HOME_TEAM to TEXT,
            Favorite.HOME_ID to TEXT,
            Favorite.HOME_SCORE to TEXT,
            Favorite.HOME_BADGE to TEXT,
            Favorite.AWAY_TEAM to TEXT,
            Favorite.AWAY_ID to TEXT,
            Favorite.AWAY_SCORE to TEXT,
            Favorite.AWAY_BADGE to TEXT,
            Favorite.LEAGUE_ID to TEXT,
            Favorite.MATCH_DATE to TEXT,
            Favorite.MATCH_TIME to TEXT,
            Favorite.MATCH_STATUS to TEXT
        )
        db.createTable(
            TeamFavorite.TABLE_TEAM_FAVORITE, true,
            TeamFavorite.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            TeamFavorite.TEAM_ID to TEXT,
            TeamFavorite.TEAM_NAME to TEXT,
            TeamFavorite.TEAM_DESC to TEXT,
            TeamFavorite.TEAM_BADGE to TEXT,
            TeamFavorite.LEAGUE_ID to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db.dropTable(Favorite.TABLE_MATCH_FAVORITE, true)
        db.dropTable(TeamFavorite.TABLE_TEAM_FAVORITE, true)
    }
}

val Context.database: SqliteDatabaseOpenHelper
    get() = SqliteDatabaseOpenHelper.getInstance(applicationContext)