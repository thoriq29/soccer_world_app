package id.thoriq.dev.footballmatch.player

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import id.thoriq.dev.footballmatch.R
import id.thoriq.dev.footballmatch.entity.Player
import kotlinx.android.extensions.LayoutContainer
import org.jetbrains.anko.find

class PlayerAdapter(
    private val context: Context, private val player: List<Player>,
    private val listener: (Player) -> Unit
) : RecyclerView.Adapter<PlayerAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(player[position], listener)
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

    override fun getItemCount(): Int = player.size

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        private val playerPict = containerView.find(R.id.logoBadge) as ImageView
        private val playerName = containerView.find(R.id.titleName) as TextView
        private val playerSubtitle = containerView.find(R.id.subtitleText) as TextView

        fun bindItem(
            player: Player,
            listener: (Player) -> Unit
        ) {
            Picasso.get().load(player.strCutout).into(playerPict)
            playerName.text = player.strPlayer
            playerSubtitle.text = player.strPosition
            containerView.setOnClickListener {
                listener(player)
            }
        }
    }
}