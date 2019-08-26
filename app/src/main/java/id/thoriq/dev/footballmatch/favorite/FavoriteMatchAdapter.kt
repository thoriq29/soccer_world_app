package id.thoriq.dev.footballmatch.favorite

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import id.thoriq.dev.footballmatch.R
import id.thoriq.dev.footballmatch.entity.Favorite
import kotlinx.android.extensions.LayoutContainer
import org.jetbrains.anko.find
import java.text.SimpleDateFormat
import java.util.*

class FavoriteMatchAdapter(
    private val context: Context, private val events: List<Favorite>,
    private val listener: (Favorite) -> Unit
) : RecyclerView.Adapter<FavoriteMatchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.match_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(position, events[position], listener)
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        private val homeScore = containerView.find(R.id.HomeTeamScore) as TextView
        private val awayScore = containerView.find(R.id.AwayTeamScore) as TextView
        private val homeTeam = containerView.find(R.id.HomeTeamName) as TextView
        private val awayTeam = containerView.find(R.id.AwayTeamName) as TextView
        private val matchTime = containerView.find(R.id.matchTime) as TextView
        private val teamArea = containerView.find(R.id.teamArea) as LinearLayout
        private val scoreArea = containerView.find(R.id.scoreArea) as LinearLayout
        private val homeBadge = containerView.find(R.id.HomeTeamBadge) as ImageView
        private val awayBadge = containerView.find(R.id.AwayTeamBadge) as ImageView
        fun bindItem(position: Int, items: Favorite, listener: (Favorite) -> Unit) {
            val myTimeString = items.hour
            val timeParser = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            var times: Date
            if (myTimeString != null) {
                times = timeParser.parse(myTimeString)
                val calendar = Calendar.getInstance()
                calendar.time = times
                calendar.add(GregorianCalendar.HOUR, 7)
                if (items.date != "null") {
                    matchTime.text = timeFormat.format(calendar.time).toString()
                } else {
                    matchTime.text = timeFormat.format(calendar.time).toString()
                }
            }



            when {
                position == 0 -> {
                    scoreArea.setBackgroundColor(Color.argb(220, 220, 220, 220))
                    teamArea.setBackgroundColor(Color.WHITE)
                }
                position != 0 && position % 2 == 0 -> {
                    scoreArea.setBackgroundColor(Color.argb(220, 220, 220, 220))
                    teamArea.setBackgroundColor(Color.WHITE)
                }
                else -> {
                    scoreArea.setBackgroundColor(Color.WHITE)
                    teamArea.setBackgroundColor(Color.argb(220, 220, 220, 220))
                }
            }

            // setting team badge
            Picasso.get().load(items.homeBadge).into(homeBadge)
            Picasso.get().load(items.awayBadge).into(awayBadge)

            // setting match score
            if (items.matchStatus == "Previous Match") {
                when {
                    items.homeScore?.toInt() as Int == items.awayScore?.toInt() -> {
                        homeScore.setTextColor(Color.GREEN)
                        awayScore.setTextColor(Color.GREEN)
                    }
                    items.homeScore.toInt() > items.awayScore?.toInt() as Int -> {
                        homeScore.setTextColor(Color.GREEN)
                        awayScore.setTextColor(Color.RED)
                    }
                    else -> {
                        homeScore.setTextColor(Color.RED)
                        awayScore.setTextColor(Color.GREEN)
                    }
                }

                homeScore.text = items.homeScore.toString()
                awayScore.text = items.awayScore.toString()
            } else {
                homeScore.text = "-"
                awayScore.text = "-"
            }


            // setting match team
            homeTeam.text = items.homeTeam
            awayTeam.text = items.awayTeam
            containerView.setOnClickListener {
                listener(items)
            }
        }
    }

}