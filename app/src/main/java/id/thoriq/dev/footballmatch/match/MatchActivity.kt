package id.thoriq.dev.footballmatch.match


import android.database.sqlite.SQLiteConstraintException
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.squareup.picasso.Picasso
import id.thoriq.dev.footballmatch.R
import id.thoriq.dev.footballmatch.R.drawable.ic_add_to_favorites
import id.thoriq.dev.footballmatch.R.drawable.ic_added_to_favorites
import id.thoriq.dev.footballmatch.R.id.add_to_favorite
import id.thoriq.dev.footballmatch.R.id.home
import id.thoriq.dev.footballmatch.R.menu.match_detail_menu
import id.thoriq.dev.footballmatch.entity.Favorite
import id.thoriq.dev.footballmatch.entity.Match
import id.thoriq.dev.footballmatch.entity.Teams
import id.thoriq.dev.footballmatch.repository.MatchRepository
import id.thoriq.dev.footballmatch.repository.TeamRepository
import id.thoriq.dev.footballmatch.responses.MatchResponse
import id.thoriq.dev.footballmatch.responses.TeamsResponse
import id.thoriq.dev.footballmatch.sqlite.database
import id.thoriq.dev.footballmatch.team.AwayTeamPresenter
import id.thoriq.dev.footballmatch.team.AwayTeamView
import id.thoriq.dev.footballmatch.team.HomeTeamPresenter
import id.thoriq.dev.footballmatch.team.HomeTeamView
import id.thoriq.dev.footballmatch.utils.EspressoIdlingResource
import id.thoriq.dev.footballmatch.utils.invisible
import id.thoriq.dev.footballmatch.utils.visible
import kotlinx.android.synthetic.main.activity_league_detail.toolbar
import kotlinx.android.synthetic.main.activity_match.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

class MatchActivity : AppCompatActivity(), MatchDetailView, HomeTeamView, AwayTeamView {

    private var id: String? = null
    private var leagueId: String? = null
    private var homeTeamId: String? = null
    private var status: String? = null
    private var awayTeamId: String? = null
    private var awayBadge: String? = null
    private var homeBadge: String? = null
    private lateinit var teamHomeData: List<Teams>
    private lateinit var teamAwayData: List<Teams>
    private lateinit var match: Match
    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private var isLoading: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)
        initialize()

    }

    private fun initialize() {
        val intent = intent
        id = intent.getStringExtra("id")
        status = intent.getStringExtra("status")
        leagueId = intent.getStringExtra("leagueId")
        homeTeamId = intent.getStringExtra("homeTeamId")
        awayTeamId = intent.getStringExtra("awayTeamId")
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_white)
        toolbar?.setNavigationOnClickListener {
            super.onBackPressed()
        }
        if (homeTeamId != null && awayTeamId != null) {
            HomeTeamPresenter(this, TeamRepository()).getHomeTeam(homeTeamId.toString())
        }
        favoriteState()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(match_detail_menu, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            home -> {
                finish()
                true
            }
            add_to_favorite -> {
                if (!isLoading) {
                    EspressoIdlingResource.increment()
                    if (isFavorite) removeFromFavorite() else addToFavorite()
                    isFavorite = !isFavorite
                    setFavorite()
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addToFavorite() {
        try {
            database.use {
                insert(
                    Favorite.TABLE_MATCH_FAVORITE,
                    Favorite.MATCH_ID to match.idEvent,
                    Favorite.MATCH_NAME to match.strEvent,
                    Favorite.HOME_ID to match.idHomeTeam,
                    Favorite.HOME_TEAM to match.strHomeTeam,
                    Favorite.HOME_SCORE to match.intHomeScore.toString(),
                    Favorite.HOME_BADGE to homeBadge,
                    Favorite.AWAY_ID to match.idAwayTeam,
                    Favorite.AWAY_TEAM to match.strAwayTeam,
                    Favorite.AWAY_SCORE to match.intAwayScore,
                    Favorite.AWAY_BADGE to awayBadge,
                    Favorite.LEAGUE_ID to leagueId,
                    Favorite.MATCH_DATE to match.strDate,
                    Favorite.MATCH_TIME to match.strTime,
                    Favorite.MATCH_STATUS to status
                )

            }
            val snack = Snackbar.make(root_layout, "Added to favorite", Snackbar.LENGTH_LONG)
            snack.show()


        } catch (e: SQLiteConstraintException) {
            toast(e.localizedMessage)
        }

    }

    private fun removeFromFavorite() {
        try {
            database.use {
                delete(Favorite.TABLE_MATCH_FAVORITE, "(MATCH_ID = {id})", "id" to id as Any)
            }
            val snack = Snackbar.make(root_layout, "Removed from favorite", Snackbar.LENGTH_LONG)
            snack.show()

        } catch (e: SQLiteConstraintException) {
            toast(e.localizedMessage)
        }

    }

    private fun favoriteState() {
        database.use {
            val result = select(Favorite.TABLE_MATCH_FAVORITE)
                .whereArgs(
                    "(MATCH_ID = {id})",
                    "id" to id as Any
                )
            val favorite = result.parseList(classParser<Favorite>())
            if (favorite.isNotEmpty()) isFavorite = true
        }
    }

    private fun setFavorite() {

        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, ic_added_to_favorites)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, ic_add_to_favorites)
    }

    override fun onHomeTeamDataLoaded(data: TeamsResponse?) {
        val teams = data?.teams as List<Teams>
        teamHomeData = teams
        homeBadge = teams[0].strTeamBadge
        AwayTeamPresenter(this, TeamRepository()).getAwayTeam(awayTeamId.toString())
    }

    override fun onHomeTeamDataError() {
        toast("error fetching home team")
    }

    override fun onAwayTeamDataLoaded(data: TeamsResponse?) {
        val teams = data?.teams as List<Teams>
        awayBadge = teams[0].strTeamBadge
        teamAwayData = teams
        MatchPresenter(this, MatchRepository()).getMatchDetail(id.toString())
    }

    override fun onAwayTeamDataError() {
        toast("error fetching Away teams")
    }

    override fun onDataLoaded(data: MatchResponse?) {
        isLoading = false
        val events = data?.events
        for (event in events as List<Match>) {
            match = event
            if (homeTeamId != null && awayTeamId != null) {
                for (team in teamHomeData) {
                    Picasso.get().load(team.strTeamBadge).into(matchHomeBadge)
                }
                for (team in teamAwayData) {
                    Picasso.get().load(team.strTeamBadge).into(matchAwayBadge)
                }
            }

            if (event.strHomeRedCards != null) {
                val awayYellow = (event.strAwayYellowCards?.split(";")?.size?.div(2)).toString()
                awayMatchYellow.text = if (event.strAwayYellowCards != "") awayYellow else "-"
            }
            if (event.strHomeYellowCards != null) {
                val homeYellow: String = (event.strHomeYellowCards.split(";").size / 2).toString()
                homeMatchYellow.text = if (event.strHomeYellowCards != "") homeYellow else "-"
            }
            if (event.strAwayRedCards != null) {
                val awayRed: String = (event.strAwayRedCards.split(";").size / 2).toString()
                awayMatchRed.text = if (event.strAwayRedCards != "") awayRed else "-"
            }
            if (event.strHomeRedCards != null) {
                val homeRed: String = (event.strHomeRedCards.split(";").size / 2).toString()
                homeMatchRed.text = if (event.strHomeRedCards != "") homeRed else "-"
            }

            // formatting date and time
            val myTimeString = event.strTime
            val timeParser = SimpleDateFormat("HH:mm:ss")
            val timeFormat = SimpleDateFormat("HH:mm")
            var times: Date
            times = timeParser.parse(myTimeString)
            val calendar = Calendar.getInstance()
            calendar.time = times
            calendar.add(GregorianCalendar.HOUR, 7)
            if (event.strDate != "null") {
                matchDateTime.text = timeFormat.format(calendar.time).toString()
            } else {
                matchDateTime.text = timeFormat.format(calendar.time).toString()
            }

            homeMatchShots.text = event.intHomeShots.toString()
            awayMatchShots.text = event.intAwayShots.toString()
            homeMatchScore.text = event.intHomeScore.toString()
            homeMatchScores.text = event.intHomeScore.toString()
            awayMatchScore.text = event.intAwayScore.toString()
            matchLeague.text = event.strLeague
            homeMatchFormation.text = event.strHomeFormation
            awayMatchFormation.text = event.strAwayFormation
            awayMatchScores.text = event.intAwayScore.toString()
            toolbar.title = event.strEvent

        }
    }

    override fun onDataError() {
        toast("Error")
    }


    override fun showLoading() {
        loading.visible()
    }

    override fun hideLoading() {
        loading.invisible()
    }
}

