package id.thoriq.dev.footballmatch.league

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import id.thoriq.dev.footballmatch.entity.League
import id.thoriq.dev.footballmatch.uis.LeagueItemUi
import id.thoriq.dev.footballmatch.uis.LeagueItemUi.Companion.league_badge
import id.thoriq.dev.footballmatch.uis.LeagueItemUi.Companion.league_name
import kotlinx.android.extensions.LayoutContainer
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.find

class LeagueAdapter(private val items: List<League>, private val listener: (League) -> Unit) :
    RecyclerView.Adapter<LeagueAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position], listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LeagueItemUi().createView(
                AnkoContext.create(
                    parent.context,
                    parent
                )
            )
        )
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        private val leagueName: TextView = itemView.find(league_name)
        private val leagueBadge: ImageView = itemView.find(league_badge)
        fun bindItem(items: League, listener: (League) -> Unit) {
            leagueName.text = items.name
            items.badge?.let { Picasso.get().load(it).into(leagueBadge) }
            containerView.setOnClickListener {
                listener(items)
            }
        }
    }

}