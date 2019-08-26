package id.thoriq.dev.footballmatch.match

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
import id.thoriq.dev.footballmatch.entity.Match
import id.thoriq.dev.footballmatch.entity.Teams
import kotlinx.android.extensions.LayoutContainer
import org.jetbrains.anko.find
import java.text.SimpleDateFormat
import java.util.*

class MatchAdapter(
    private val state: String, private val context: Context, private val events: List<Match>,
    private val teams: List<Teams>, private val listener: (Match) -> Unit
) : RecyclerView.Adapter<MatchAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(state, position, events[position], teams, listener)
    }

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

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        private val teamData = mutableMapOf<String, String>()
        private val homeTeamBadge = containerView.find(R.id.HomeTeamBadge) as ImageView
        private val awayTeamBadge = containerView.find(R.id.AwayTeamBadge) as ImageView
        private val homeScore = containerView.find(R.id.HomeTeamScore) as TextView
        private val awayScore = containerView.find(R.id.AwayTeamScore) as TextView
        private val homeTeam = containerView.find(R.id.HomeTeamName) as TextView
        private val awayTeam = containerView.find(R.id.AwayTeamName) as TextView
        private val matchTime = containerView.find(R.id.matchTime) as TextView
        private val teamArea = containerView.find(R.id.teamArea) as LinearLayout
        private val scoreArea = containerView.find(R.id.scoreArea) as LinearLayout

        fun bindItem(
            state: String,
            position: Int,
            items: Match,
            teams: List<Teams>,
            listener: (Match) -> Unit
        ) {
            val myTimeString = items.strTime
            val myDateString = items.strDate
            val timeParser = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val dateParser = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
            val dateFormat = SimpleDateFormat("dd MMMM", Locale.getDefault())
            val date = try {
                dateParser.parse(myDateString)
            } catch (e: Exception) {
                val date = Date()
                val formatter = SimpleDateFormat("dd/mm/yy")
                val dateString: String = formatter.format(date)
                dateParser.parse(dateString)
            }
            val time = try {
                timeParser.parse(myTimeString)
            } catch (e: Exception) {
                val dateTime = Date()
                val formatter = SimpleDateFormat("HH:ss:mm")
                val timeString: String = formatter.format(dateTime)
                timeParser.parse(timeString)
            }

            val calendar = Calendar.getInstance()
            calendar.time = time
            calendar.add(GregorianCalendar.HOUR, 7)

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

            // setting match date time
            if (items.strDate != "null") {
                matchTime.text = dateFormat.format(date).toString() + "\n" + timeFormat.format(calendar.time).toString()
            } else {
                matchTime.text = timeFormat.format(calendar.time).toString()
            }

            // setting match team badge
            for (team in teams) {
                teamData[team.strTeam] = team.strTeamBadge
            }

            if (teamData[items.strHomeTeam] != null) {
                Picasso.get().load(teamData[items.strHomeTeam]).into(homeTeamBadge)
            }

            if (teamData[items.strAwayTeam] != null) {
                Picasso.get().load(teamData[items.strAwayTeam]).into(awayTeamBadge)
            }

            // setting match score
            if (state == "Next Match") {
                homeScore.text = "-"
                awayScore.text = "-"
            } else {
                when {
                    items.intHomeScore == items.intAwayScore -> {
                        homeScore.setTextColor(Color.GREEN)
                        awayScore.setTextColor(Color.GREEN)
                    }
                    items.intHomeScore > items.intAwayScore -> {
                        homeScore.setTextColor(Color.GREEN)
                        awayScore.setTextColor(Color.RED)
                    }
                    else -> {
                        homeScore.setTextColor(Color.RED)
                        awayScore.setTextColor(Color.GREEN)
                    }
                }
                homeScore.text = items.intHomeScore.toString()
                awayScore.text = items.intAwayScore.toString()
            }

            // setting match team
            homeTeam.text = items.strHomeTeam
            awayTeam.text = items.strAwayTeam
            containerView.setOnClickListener {
                listener(items)
            }
        }
    }
}