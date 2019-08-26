package id.thoriq.dev.footballmatch.standings

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import id.thoriq.dev.footballmatch.R
import id.thoriq.dev.footballmatch.entity.Standing
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.standings_item.*
import org.jetbrains.anko.find

class StandingsAdapter(
    private val context: Context, private val standing: List<Standing>,
    private val listener: (Standing) -> Unit
) : RecyclerView.Adapter<StandingsAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(position, standing[position], listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.standings_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = standing.size

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        private val teamName = containerView.find(R.id.standingsName) as TextView
        private val played = containerView.find(R.id.standingsPlayed) as TextView
        private val goalFor = containerView.find(R.id.standingsGf) as TextView
        private val goalAgainst = containerView.find(R.id.standingsGa) as TextView
        private val win = containerView.find(R.id.standingsW) as TextView
        private val draw = containerView.find(R.id.standingsD) as TextView
        private val loss = containerView.find(R.id.standingsL) as TextView
        private val total = containerView.find(R.id.standingsT) as TextView


        fun bindItem(
            position: Int,
            standing: Standing,
            listener: (Standing) -> Unit
        ) {

            when {
                position == 0 -> {
                    standing_items.setBackgroundColor(Color.argb(220, 220, 220, 220))
                }
                position != 0 && position % 2 == 0 -> {
                    standing_items.setBackgroundColor(Color.argb(220, 220, 220, 220))
                }
                else -> {
                    standing_items.setBackgroundColor(Color.WHITE)
                }
            }

            teamName.text = standing.name
            played.text = standing.played.toString()
            goalFor.text = standing.goalsfor.toString()
            goalAgainst.text = standing.goalsagainst.toString()
            win.text = standing.win.toString()
            draw.text = standing.draw.toString()
            loss.text = standing.loss.toString()
            total.text = standing.total.toString()
            containerView.setOnClickListener {
                listener(standing)
            }
        }
    }
}