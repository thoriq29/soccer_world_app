package id.thoriq.dev.footballmatch.team

import android.database.sqlite.SQLiteConstraintException
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.squareup.picasso.Picasso
import id.thoriq.dev.footballmatch.R
import id.thoriq.dev.footballmatch.R.id.add_to_favorite
import id.thoriq.dev.footballmatch.R.id.home
import id.thoriq.dev.footballmatch.R.menu.match_detail_menu
import id.thoriq.dev.footballmatch.entity.Player
import id.thoriq.dev.footballmatch.entity.TeamFavorite
import id.thoriq.dev.footballmatch.entity.Teams
import id.thoriq.dev.footballmatch.fragments.PlayerFragment
import id.thoriq.dev.footballmatch.fragments.TeamInfoFragment
import id.thoriq.dev.footballmatch.player.PlayerAdapter
import id.thoriq.dev.footballmatch.player.PlayerDetailActivity
import id.thoriq.dev.footballmatch.player.PlayerPresenter
import id.thoriq.dev.footballmatch.player.PlayerView
import id.thoriq.dev.footballmatch.repository.PlayerRepository
import id.thoriq.dev.footballmatch.repository.TeamRepository
import id.thoriq.dev.footballmatch.responses.PlayerListResponse
import id.thoriq.dev.footballmatch.responses.TeamsResponse
import id.thoriq.dev.footballmatch.sqlite.database
import id.thoriq.dev.footballmatch.utils.EspressoIdlingResource
import id.thoriq.dev.footballmatch.utils.invisible
import id.thoriq.dev.footballmatch.utils.visible
import kotlinx.android.synthetic.main.activity_team_detail.*
import kotlinx.android.synthetic.main.player.*
import kotlinx.android.synthetic.main.team_info.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class TeamDetailActivity : AppCompatActivity(), HomeTeamView, PlayerView {

    private var id: String = ""
    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private var isLoading: Boolean = true
    private lateinit var teamData: Teams
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)
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
        if (id != "") {
            HomeTeamPresenter(this, TeamRepository()).getHomeTeam(id)
            PlayerPresenter(this, PlayerRepository()).getAllPlayer(id)
            favoriteState()
        }
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
                    TeamFavorite.TABLE_TEAM_FAVORITE,
                    TeamFavorite.TEAM_ID to teamData.idTeam.toString(),
                    TeamFavorite.TEAM_NAME to teamData.strTeam,
                    TeamFavorite.TEAM_DESC to teamData.strLeague,
                    TeamFavorite.TEAM_BADGE to teamData.strTeamBadge,
                    TeamFavorite.LEAGUE_ID to teamData.idLeague.toString()
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
                delete(TeamFavorite.TABLE_TEAM_FAVORITE, "(TEAM_ID = {id})", "id" to teamData.idTeam.toString() as Any)
            }

            val snack = Snackbar.make(root_layout, "Removed from favorite", Snackbar.LENGTH_LONG)
            snack.show()

        } catch (e: SQLiteConstraintException) {
            toast(e.localizedMessage)
        }

    }

    private fun favoriteState() {
        database.use {
            val result = select(TeamFavorite.TABLE_TEAM_FAVORITE)
                .whereArgs(
                    "(TEAM_ID = {id})",
                    "id" to id as Any
                )
            val favorite = result.parseList(classParser<TeamFavorite>())
            if (favorite.isNotEmpty()) {
                isFavorite = true
            }
        }
    }

    private fun setFavorite() {

        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(
                this,
                R.drawable.ic_added_to_favorites
            )
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(
                this,
                R.drawable.ic_add_to_favorites
            )
    }


    private fun configureTabLayout() {
        team_tabs.addTab(team_tabs.newTab().setText("Team Info"))
        team_tabs.addTab(team_tabs.newTab().setText("Players"))

        val adapter = TabPagerAdapter(
            supportFragmentManager,
            team_tabs.tabCount
        )
        viewpager.adapter = adapter
        viewpager.offscreenPageLimit = 1

        viewpager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(team_tabs)
        )
        team_tabs.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewpager.currentItem = tab.position
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
                0 -> TeamInfoFragment()
                1 -> PlayerFragment()
                else -> null
            }
        }

        override fun getCount(): Int {
            return tabCount
        }
    }

    override fun showLoading() {
        loading.visible()
    }

    override fun hideLoading() {
        loading.invisible()
    }

    override fun onPlayersLoaded(data: PlayerListResponse?) {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerPlayers.layoutManager = linearLayoutManager
        recyclerPlayers.adapter = PlayerAdapter(this, data?.player as List<Player>) {
            startActivity<PlayerDetailActivity>(
                "id" to it.idPlayer.toString()
            )
        }

    }


    override fun onHomeTeamDataLoaded(data: TeamsResponse?) {
        isLoading = false
        for (team in data?.teams as List<Teams>) {
            teamData = team
            teamDetailName.text = team.strTeam
            teamInfoName.text = team.strTeam
            teamInfoAlternate.text = team.strSport
            teamInfoStadiumName.text = team.strStadium + " Stadium"
            teamInfoStadiumDesc.text = team.strStadiumDescription
            Picasso.get().load(team.strTeamFanart1).into(background)
            Picasso.get().load(team.strTeamBadge).into(teamDetailAvatar)
            Picasso.get().load(team.strStadiumThumb).into(teamInfoStadium)
            toolbar.title = team.strTeam
            toolbar.setTitleTextColor(Color.WHITE)
        }
    }

    override fun onPlayersError(error: Throwable) {
        toast(error.localizedMessage)
    }

    override fun onHomeTeamDataError() {
        toast("error")
    }
}
