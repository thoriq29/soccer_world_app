package id.thoriq.dev.footballmatch.player

import id.thoriq.dev.footballmatch.repository.PlayerDetailCallback
import id.thoriq.dev.footballmatch.repository.PlayerRepository
import id.thoriq.dev.footballmatch.repository.PlayersCallback
import id.thoriq.dev.footballmatch.responses.PlayerListResponse
import id.thoriq.dev.footballmatch.responses.PlayerResponse

class PlayerPresenter(private val playerView: PlayerView, private val repository: PlayerRepository) {

    fun getAllPlayer(id: String) {
        playerView.showLoading()
        repository.getAllPlayer(id, object : PlayersCallback<PlayerListResponse?> {
            override fun onPlayersError(error: Throwable) {
                playerView.onPlayersError(error)
                playerView.hideLoading()
            }

            override fun onPlayersLoaded(data: PlayerListResponse?) {
                if (data?.player != null) {
                    playerView.onPlayersLoaded(data)
                    playerView.hideLoading()
                } else {
                    playerView.hideLoading()
                }
            }
        })
    }

}

class PlayerDetailPresenter(private val view: PlayerDetailView, private val repository: PlayerRepository) {
    fun getDetailPlayer(id: String) {
        view.showLoading()
        repository.getPlayerDetail(id, object : PlayerDetailCallback<PlayerResponse?> {
            override fun onPlayerError() {
                view.onPlayerError()
                view.hideLoading()
            }

            override fun onPlayerDetailLoaded(data: PlayerResponse?) {
                view.onPlayerDetailLoaded(data)
                view.hideLoading()
            }
        })
    }
}