package id.thoriq.dev.footballmatch.match

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import id.thoriq.dev.footballmatch.R
import id.thoriq.dev.footballmatch.entity.Match
import kotlinx.android.extensions.LayoutContainer
import org.jetbrains.anko.find
import java.text.SimpleDateFormat
import java.util.*

class MatchSearchAdapter(
    private val context: Context, private val events: List<Match>,
    private val listener: (Match) -> Unit
) : RecyclerView.Adapter<MatchSearchAdapter.ViewHolder>() {

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        private val homeScore = containerView.find(R.id.HomeTeamScore) as TextView
        private val awayScore = containerView.find(R.id.AwayTeamScore) as TextView
        private val homeTeam = containerView.find(R.id.HomeTeamName) as TextView
        private val awayTeam = containerView.find(R.id.AwayTeamName) as TextView
        private val matchTime = containerView.find(R.id.matchTime) as TextView
        private val teamArea = containerView.find(R.id.teamArea) as LinearLayout
        private val scoreArea = containerView.find(R.id.scoreArea) as LinearLayout
        fun bindItem(position: Int, items: Match, listener: (Match) -> Unit) {
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

            if (items.strDate != "null") {
                matchTime.text = dateFormat.format(date).toString() + "\n" + timeFormat.format(calendar.time).toString()
            } else {
                matchTime.text = timeFormat.format(calendar.time).toString()
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

            // setting match score
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


            // setting match team
            homeTeam.text = items.strHomeTeam
            awayTeam.text = items.strAwayTeam
            containerView.setOnClickListener {
                listener(items)
            }
        }

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(position, events[position], listener)
    }
}