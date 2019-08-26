package id.thoriq.dev.footballmatch.league

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.squareup.picasso.Picasso
import id.thoriq.dev.footballmatch.R
import id.thoriq.dev.footballmatch.R.id.list_favorite_in_league
import id.thoriq.dev.footballmatch.R.id.searchMenuTeam
import id.thoriq.dev.footballmatch.R.menu.league_detail_menu
import id.thoriq.dev.footballmatch.entity.Leagues
import id.thoriq.dev.footballmatch.entity.Match
import id.thoriq.dev.footballmatch.entity.Standing
import id.thoriq.dev.footballmatch.entity.Teams
import id.thoriq.dev.footballmatch.favorite.FavoriteActivity
import id.thoriq.dev.footballmatch.fragments.NextMatchFragment
import id.thoriq.dev.footballmatch.fragments.PreviousMatchFragment
import id.thoriq.dev.footballmatch.fragments.StandingsFragment
import id.thoriq.dev.footballmatch.fragments.TeamFragment
import id.thoriq.dev.footballmatch.match.*
import id.thoriq.dev.footballmatch.repository.LeagueRepository
import id.thoriq.dev.footballmatch.repository.MatchRepository
import id.thoriq.dev.footballmatch.repository.StandingsRepository
import id.thoriq.dev.footballmatch.repository.TeamRepository
import id.thoriq.dev.footballmatch.responses.LeagueResponse
import id.thoriq.dev.footballmatch.responses.MatchResponse
import id.thoriq.dev.footballmatch.responses.StandingsResponse
import id.thoriq.dev.footballmatch.responses.TeamResponse
import id.thoriq.dev.footballmatch.standings.StandingsAdapter
import id.thoriq.dev.footballmatch.standings.StandingsPresenter
import id.thoriq.dev.footballmatch.standings.StandingsView
import id.thoriq.dev.footballmatch.team.*
import id.thoriq.dev.footballmatch.utils.invisible
import id.thoriq.dev.footballmatch.utils.visible
import kotlinx.android.synthetic.main.activity_league_detail.*
import kotlinx.android.synthetic.main.last_match.*
import kotlinx.android.synthetic.main.next_match.*
import kotlinx.android.synthetic.main.standings.*
import kotlinx.android.synthetic.main.team.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class LeagueDetailActivity : AppCompatActivity(), LeagueView, NextMatchView, LastMatchView,
    TeamView, StandingsView {


    private var id: String? = null
    private lateinit var teamData: List<Teams>
    private var menuItem: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_league_detail)
        initialize()
    }

    private fun initialize() {
        val intent = intent
        id = intent.getStringExtra("id")
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(Color.TRANSPARENT)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_white)
        toolbar?.setNavigationOnClickListener {
            super.onBackPressed()
        }

        configureTabLayout()
        id?.let { getAllData(it) }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(league_detail_menu, menu)
        menuItem = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            list_favorite_in_league -> {
                startActivity<FavoriteActivity>(
                    "leagueId" to id.toString()
                )
                true
            }
            searchMenuTeam -> {
                startActivity<SearchTeamActivity>()
                true
            }


            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getAllData(id: String) {
        StandingsPresenter(this, StandingsRepository()).getStandings(id)
        LeaguePresenter(this, LeagueRepository()).getDataLeague(id)
        TeamPresenter(this, TeamRepository()).getAllTeam(id)
    }

    override fun onTeamDataLoaded(data: TeamResponse?) {
        PreviousMatchPresenter(this, MatchRepository()).getPreviousMatch(id.toString())
        NextMatchPresenter(this, MatchRepository()).getNextMatch(id.toString())
        val teams = data?.teams as List<Teams>
        teamData = teams
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerTeams.layoutManager = linearLayoutManager
        recyclerTeams.adapter =
            TeamAdapter(this, teams) {
                startActivity<TeamDetailActivity>(
                    "id" to it.idTeam.toString()
                )
            }
    }

    override fun onTeamDataError() {
        toast("error fetching teams data")
    }

    override fun onLastMatchDataLoaded(data: MatchResponse?) {
        noPastResult.invisible()
        recyclerPreviousMatch.visible()
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerPreviousMatch.layoutManager = linearLayoutManager

        recyclerPreviousMatch.adapter =
            MatchAdapter("Last Match", this, data?.events as List<Match>, teamData) {
                startActivity<MatchActivity>(
                    "id" to it.idEvent.toString(), "leagueId" to id.toString(),
                    "awayTeamId" to it.idAwayTeam.toString(), "homeTeamId" to it.idHomeTeam.toString(),
                    "status" to "Previous Match"
                )
            }
    }

    override fun onLastMatchDataError() {
        toast("last match data error")
    }


    override fun onNextMatchDataError() {
        toast("Next match error")
    }

    override fun onNextMatchDataLoaded(data: MatchResponse?) {
        noNextResult.invisible()
        recyclerMatch.visible()
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerMatch.layoutManager = linearLayoutManager
        recyclerMatch.adapter =
            MatchAdapter("Next Match", this, data?.events as List<Match>, teamData) {
                startActivity<MatchActivity>(
                    "id" to it.idEvent.toString(), "leagueId" to id.toString(),
                    "awayTeamId" to it.idAwayTeam.toString(), "homeTeamId" to it.idHomeTeam.toString(),
                    "status" to "Next Match"
                )
            }
    }

    override fun onLeagueDetailDataLoaded(data: LeagueResponse?) {
        for (league in data?.leagues as List<Leagues>) {
            // Background Image
            Picasso.get().load(league.strFanart3).into(background)
            // Avatar Team Badge
            Picasso.get().load(league.strBadge).into(avatar)
            // League Name
            leagueName.text = league.strLeague
            // Toolbar title initialize
            toolbar.title = league.strLeague
            toolbar.setTitleTextColor(Color.WHITE)
        }
    }

    override fun onStandingsLoaded(data: StandingsResponse?) {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerStandings.layoutManager = linearLayoutManager
        recyclerStandings.adapter = StandingsAdapter(this, data?.table as List<Standing>) {

        }
    }

    override fun onStandingsError() {
        toast("error fetching standings")
    }

    override fun onLeagueDetailDataError() {
        toast("error fetching detail league data")
    }

    override fun showLoading() {
        loading.visible()
    }

    override fun hideLoading() {
        loading.invisible()
    }

    override fun emptyPastMatch() {
        noPastResult.visible()
        recyclerPreviousMatch.invisible()
    }

    override fun emptyNextMatch() {
        noNextResult.visible()
        recyclerMatch.invisible()
    }

    private fun configureTabLayout() {
        tabs.addTab(tabs.newTab().setText("Last Match"))
        tabs.addTab(tabs.newTab().setText("Next Match"))
        tabs.addTab(tabs.newTab().setText("Standings"))
        tabs.addTab(tabs.newTab().setText("Teams"))

        val adapter = TabPagerAdapter(
            supportFragmentManager,
            tabs.tabCount
        )
        viewpager.adapter = adapter
        viewpager.offscreenPageLimit = 3

        viewpager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(tabs)
        )
        tabs.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewpager.currentItem = tab.position
                recyclerMatch
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {


            }

        })
    }

    internal inner class TabPagerAdapter(fm: FragmentManager, private var tabCount: Int) :
        FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {

            return when (position) {
                0 -> PreviousMatchFragment()
                1 -> NextMatchFragment()
                2 -> StandingsFragment()
                3 -> TeamFragment()
                else -> null
            }
        }

        override fun getCount(): Int {
            return tabCount
        }
    }
}
