package id.thoriq.dev.footballmatch.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import id.thoriq.dev.footballmatch.R
import id.thoriq.dev.footballmatch.entity.TeamFavorite
import id.thoriq.dev.footballmatch.favorite.FavoriteTeamAdapter
import id.thoriq.dev.footballmatch.sqlite.database
import id.thoriq.dev.footballmatch.team.TeamDetailActivity
import id.thoriq.dev.footballmatch.utils.invisible
import id.thoriq.dev.footballmatch.utils.visible
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.support.v4.startActivity

class FavoriteTeamFragment : Fragment() {

    private var favoriteTeams: MutableList<TeamFavorite> = mutableListOf()
    private var leagueId: String? = null
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var adapterTeam: FavoriteTeamAdapter
    private lateinit var noTeamsResult: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.team, container, false)
        mRecyclerView = view.findViewById(R.id.recyclerTeams) as RecyclerView
        noTeamsResult = view.findViewById(R.id.noTeamsResult) as TextView
        val activity = activity
        leagueId = activity?.intent?.getStringExtra("leagueId")
        adapterTeam = FavoriteTeamAdapter(context as Context, favoriteTeams) {
            startActivity<TeamDetailActivity>(
                "id" to it.teamId.toString()
            )
        }
        showFavoriteTeam()
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView.layoutManager = linearLayoutManager
        mRecyclerView.adapter = adapterTeam
        return view
    }

    override fun onResume() {
        super.onResume()
        showFavoriteTeam()
    }

    private fun showFavoriteTeam() {
        favoriteTeams.clear()
        context?.database?.use {
            val result = select(TeamFavorite.TABLE_TEAM_FAVORITE).whereArgs(
                "(LEAGUE_ID = {id})",
                "id" to leagueId as Any
            )
            val favorite = result.parseList(classParser<TeamFavorite>())
            favoriteTeams.addAll(favorite)
            adapterTeam.notifyDataSetChanged()
            if (favoriteTeams.isEmpty()) {
                mRecyclerView.invisible()
                noTeamsResult.visible()
            } else {
                noTeamsResult.invisible()
                mRecyclerView.visible()
            }
        }
    }
}