package id.thoriq.dev.footballmatch.team

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import id.thoriq.dev.footballmatch.R
import id.thoriq.dev.footballmatch.entity.Teams
import id.thoriq.dev.footballmatch.repository.TeamRepository
import id.thoriq.dev.footballmatch.responses.TeamsResponse
import id.thoriq.dev.footballmatch.utils.EspressoIdlingResource
import id.thoriq.dev.footballmatch.utils.invisible
import id.thoriq.dev.footballmatch.utils.visible
import kotlinx.android.synthetic.main.activity_search_team.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class SearchTeamActivity : AppCompatActivity(), TeamSearchView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_team)
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

    private fun searchQuery(view: TeamSearchView, searchView: SearchView) {
        var options: MutableMap<String, String> = mutableMapOf()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) {
                    rvTeamsSearch.invisible()
                    noResult.visible()
                    return false
                }

                options["query"] = query.toString()
                if (query != "") {
                    EspressoIdlingResource.increment()
                    SearchTeamPresenter(view, TeamRepository()).getSearchTeam(query.toString())
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != "") {
                    SearchTeamPresenter(view, TeamRepository()).getSearchTeam(newText.toString())
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

    override fun onTeamSearchLoaded(data: TeamsResponse?) {
        val teams = data?.teams as List<Teams>
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rvTeamsSearch.layoutManager = linearLayoutManager
        noResult.invisible()
        rvTeamsSearch.visible()
        rvTeamsSearch.adapter =
            TeamAdapter(this, teams) {
                if (it.strSport != "Soccer") {
                    toast("Please Select Soccer Team Only")
                } else {
                    startActivity<TeamDetailActivity>(
                        "id" to it.idTeam.toString()
                    )
                }
            }
    }

    override fun onTeamSearchError() {
        toast("No data found")
    }

    override fun onEmptyResult() {
        noResult.visible()
        rvTeamsSearch.invisible()
    }

}
