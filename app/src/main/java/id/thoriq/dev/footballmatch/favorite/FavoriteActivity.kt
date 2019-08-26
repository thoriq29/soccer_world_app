package id.thoriq.dev.footballmatch.favorite

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import id.thoriq.dev.footballmatch.R
import id.thoriq.dev.footballmatch.entity.Favorite
import id.thoriq.dev.footballmatch.entity.TeamFavorite
import id.thoriq.dev.footballmatch.fragments.FavoriteNextMatchFragment
import id.thoriq.dev.footballmatch.fragments.FavoritePreviousMatchFragment
import id.thoriq.dev.footballmatch.fragments.FavoriteTeamFragment
import id.thoriq.dev.footballmatch.match.MatchActivity
import id.thoriq.dev.footballmatch.team.TeamDetailActivity
import kotlinx.android.synthetic.main.activity_favorite.*
import org.jetbrains.anko.startActivity


class FavoriteActivity : AppCompatActivity() {

    private var favoriteLastMatch: MutableList<Favorite> = mutableListOf()
    private var favoriteTeams: MutableList<TeamFavorite> = mutableListOf()

    private lateinit var adapterLastMatch: FavoriteMatchAdapter
    private lateinit var adapterTeam: FavoriteTeamAdapter
    private var leagueId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        leagueId = intent.getStringExtra("leagueId")

        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.title = "Favorite"
        toolbar.setNavigationIcon(R.drawable.ic_arrow_white)
        toolbar?.setNavigationOnClickListener {
            super.onBackPressed()
        }

        adapterLastMatch = FavoriteMatchAdapter(this, favoriteLastMatch) {
            startActivity<MatchActivity>(
                "id" to it.matchId.toString(), "leagueId" to it.leagueId.toString(),
                "awayTeamId" to it.awayId.toString(), "homeTeamId" to it.homeId.toString()
            )
        }

        adapterTeam = FavoriteTeamAdapter(this, favoriteTeams) {
            startActivity<TeamDetailActivity>(
                "id" to it.teamId.toString()
            )
        }

        configureTabLayout()
    }

    private fun configureTabLayout() {
        favorite_tabs.addTab(favorite_tabs.newTab().setText("Last Match"))
        favorite_tabs.addTab(favorite_tabs.newTab().setText("Next Match"))
        favorite_tabs.addTab(favorite_tabs.newTab().setText("Teams"))

        val adapter = TabPagerAdapter(
            supportFragmentManager,
            favorite_tabs.tabCount
        )
        viewpager.adapter = adapter
        viewpager.offscreenPageLimit = 2

        viewpager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(favorite_tabs)
        )
        favorite_tabs.addOnTabSelectedListener(object :
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
                0 -> FavoritePreviousMatchFragment()
                1 -> FavoriteNextMatchFragment()
                2 -> FavoriteTeamFragment()
                else -> null
            }
        }

        override fun getCount(): Int {
            return tabCount
        }
    }
}
