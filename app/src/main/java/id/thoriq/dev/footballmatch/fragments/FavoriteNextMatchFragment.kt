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
import id.thoriq.dev.footballmatch.entity.Favorite
import id.thoriq.dev.footballmatch.favorite.FavoriteMatchAdapter
import id.thoriq.dev.footballmatch.match.MatchActivity
import id.thoriq.dev.footballmatch.sqlite.database
import id.thoriq.dev.footballmatch.utils.invisible
import id.thoriq.dev.footballmatch.utils.visible
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.support.v4.startActivity


class FavoriteNextMatchFragment : Fragment() {

    private var favoriteNextMatch: MutableList<Favorite> = mutableListOf()
    private var leagueId: String? = null
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var adapterNextMatch: FavoriteMatchAdapter
    private lateinit var noMatchResult: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.favorite_match, container, false)
        mRecyclerView = view.findViewById(R.id.recyclerMatch) as RecyclerView
        noMatchResult = view.findViewById(R.id.noMatchResult)
        leagueId = activity?.intent?.getStringExtra("leagueId")
        adapterNextMatch = FavoriteMatchAdapter(context as Context, favoriteNextMatch) {
            startActivity<MatchActivity>(
                "id" to it.matchId.toString(), "leagueId" to it.leagueId.toString(),
                "awayTeamId" to it.awayId.toString(), "homeTeamId" to it.homeId.toString()
            )
        }
        showFavoriteNextMatch()
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView.layoutManager = linearLayoutManager
        mRecyclerView.adapter = adapterNextMatch
        return view
    }

    override fun onResume() {
        super.onResume()
        showFavoriteNextMatch()
    }

    private fun showFavoriteNextMatch() {
        favoriteNextMatch.clear()
        context?.database?.use {
            val result = select(Favorite.TABLE_MATCH_FAVORITE).whereArgs(
                "(LEAGUE_ID = {id}) and (MATCH_STATUS = {status})",
                "id" to leagueId.toString() as Any,
                "status" to "Next Match" as Any
            )
            val favorite = result.parseList(classParser<Favorite>())
            favoriteNextMatch.addAll(favorite)

            adapterNextMatch.notifyDataSetChanged()
            if (favoriteNextMatch.isEmpty()) {
                mRecyclerView.invisible()
                noMatchResult.visible()
            } else {
                noMatchResult.invisible()
                mRecyclerView.visible()
            }
        }
    }
}