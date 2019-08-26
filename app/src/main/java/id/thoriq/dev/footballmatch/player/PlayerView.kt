package id.thoriq.dev.footballmatch.player

import id.thoriq.dev.footballmatch.repository.PlayerDetailCallback
import id.thoriq.dev.footballmatch.repository.PlayersCallback
import id.thoriq.dev.footballmatch.responses.PlayerListResponse
import id.thoriq.dev.footballmatch.responses.PlayerResponse

interface PlayerView : PlayersCallback<PlayerListResponse> {
    fun showLoading()
    fun hideLoading()
}

interface PlayerDetailView : PlayerDetailCallback<PlayerResponse> {
    fun showLoading()
    fun hideLoading()
}
