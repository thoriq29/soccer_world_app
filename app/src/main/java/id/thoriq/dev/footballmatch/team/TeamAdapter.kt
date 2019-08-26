package id.thoriq.dev.footballmatch.team

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import id.thoriq.dev.footballmatch.R
import id.thoriq.dev.footballmatch.entity.Teams
import kotlinx.android.extensions.LayoutContainer
import org.jetbrains.anko.find

class TeamAdapter(
    private val context: Context, private val teams: List<Teams>,
    private val listener: (Teams) -> Unit
) : RecyclerView.Adapter<TeamAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(teams[position], listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.team_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = teams.size

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        private val teamBadge = containerView.find(R.id.logoBadge) as ImageView
        private val teamName = containerView.find(R.id.titleName) as TextView
        private val teamAlternate = containerView.find(R.id.subtitleText) as TextView

        fun bindItem(
            teams: Teams,
            listener: (Teams) -> Unit
        ) {
            Picasso.get().load(teams.strTeamBadge).into(teamBadge)
            teamName.text = teams.strTeam
            teamAlternate.text = teams.strSport
            containerView.setOnClickListener {
                listener(teams)
            }
        }
    }
}