package id.thoriq.dev.footballmatch.match

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import id.thoriq.dev.footballmatch.R
import id.thoriq.dev.footballmatch.repository.MatchRepository
import id.thoriq.dev.footballmatch.responses.SearchResponse
import id.thoriq.dev.footballmatch.utils.EspressoIdlingResource
import id.thoriq.dev.footballmatch.utils.invisible
import id.thoriq.dev.footballmatch.utils.visible
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class MatchSearchActivity : AppCompatActivity(), MatchSearchView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.searchMenu)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                // Do whatever you need
                return true // KEEP IT TO TRUE OR IT DOESN'T OPEN !!
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                onBackPressed()
                return true // OR FALSE IF YOU DIDN'T WANT IT TO CLOSE!
            }
        })
        searchItem.expandActionView()
        searchQuery(this, searchView)
        return super.onCreateOptionsMenu(menu)

    }


    private fun searchQuery(view: MatchSearchView, searchView: SearchView) {
        var options: MutableMap<String, String> = mutableMapOf()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) {
                    rvEventsSearch.invisible()
                    noResult.visible()
                    return false
                }

                options["query"] = query.toString()
                if (query != "") {
                    EspressoIdlingResource.increment()
                    MatchSearchPresenter(view, MatchRepository()).getMatchs(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != "") {
                    MatchSearchPresenter(view, MatchRepository()).getMatchs(newText.toString())
                }
                return false
            }
        })
    }

    override fun showLoading() {
        loading.visible()
    }

    override fun hideLoading() {
        loading.invisible()
    }

    override fun onSearchDataLoaded(data: SearchResponse?) {
        val events = data?.event
        if (events.isNullOrEmpty()) {
            rvEventsSearch.invisible()
        } else {
            if (!EspressoIdlingResource.idlingresource.isIdleNow) {
                EspressoIdlingResource.decrement()
            }
            rvEventsSearch.visible()
            noResult.invisible()
            rvEventsSearch.layoutManager = LinearLayoutManager(this)
            rvEventsSearch.adapter = MatchSearchAdapter(this, events) {
                startActivity<MatchActivity>(
                    "id" to it.idEvent.toString(), "leagueId" to it.idLeague,
                    "awayTeamId" to it.idAwayTeam.toString(), "homeTeamId" to it.idHomeTeam.toString()
                )
            }
        }
    }

    override fun onSearchError() {
        toast("error ketika search")
    }

    override fun onEmptyResult() {
        toast("No Data")
        noResult.visible()
        rvEventsSearch.invisible()
    }
}
