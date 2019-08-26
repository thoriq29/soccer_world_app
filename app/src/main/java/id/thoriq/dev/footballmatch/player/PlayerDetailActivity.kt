package id.thoriq.dev.footballmatch.player

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.squareup.picasso.Picasso
import id.thoriq.dev.footballmatch.R
import id.thoriq.dev.footballmatch.entity.Player
import id.thoriq.dev.footballmatch.repository.PlayerRepository
import id.thoriq.dev.footballmatch.responses.PlayerResponse
import id.thoriq.dev.footballmatch.utils.invisible
import id.thoriq.dev.footballmatch.utils.visible
import kotlinx.android.synthetic.main.activity_player_detail.*
import org.jetbrains.anko.toast


class PlayerDetailActivity : AppCompatActivity(), PlayerDetailView {

    private var id: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_detail)
        initialize()
    }

    private fun initialize() {
        val intent = intent
        id = intent.getStringExtra("id")
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(Color.TRANSPARENT)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_white)
        toolbar?.setNavigationOnClickListener {
            super.onBackPressed()
        }

        if (id != "") {
            PlayerDetailPresenter(this, PlayerRepository()).getDetailPlayer(id)
        }
    }

    override fun showLoading() {
        loading.visible()
    }

    override fun hideLoading() {
        loading.invisible()
    }

    override fun onPlayerDetailLoaded(data: PlayerResponse?) {
        for (player in data?.players as List<Player>) {
            Picasso.get().load(player.strFanart1).into(background)
            Picasso.get().load(player.strCutout).into(avatar)
            playerName.text = player.strPlayer
            playerInfoName.text = player.strPlayer
            playerInfoAlternate.text = player.strPosition
            playerInfoDesc.text = player.strDescriptionEN
            toolbar.title = player.strPlayer
            toolbar.setTitleTextColor(Color.WHITE)
        }
    }

    override fun onPlayerError() {
        toast("error")
    }
}
