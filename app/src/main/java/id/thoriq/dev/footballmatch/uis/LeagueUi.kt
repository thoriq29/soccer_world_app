package id.thoriq.dev.footballmatch.uis

import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import id.thoriq.dev.footballmatch.R
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView

class LeagueItemUi : AnkoComponent<ViewGroup> {

    companion object {
        const val league_badge = 1
        const val league_name = 2
    }


    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        cardView {
            lparams(matchParent, wrapContent) {
                margin = dip(5)
                backgroundColor = Color.WHITE
            }

            linearLayout {
                orientation = LinearLayout.VERTICAL
                lparams(matchParent, wrapContent)
                imageView {
                    id = league_badge
                    imageResource = R.drawable.ic_launcher_background

                }.lparams(
                    dip(50), dip(50)
                ) {
                    margin = dip(16)
                    gravity = Gravity.CENTER_HORIZONTAL
                }

                textView {
                    id = league_name
                    text = "League Name"
                }.lparams(matchParent, wrapContent) {
                    margin = dip(16)
                    gravity = Gravity.CENTER_HORIZONTAL
                }
            }
        }

    }
}