package id.thoriq.dev.footballmatch.league


import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import id.thoriq.dev.footballmatch.entity.Leagues
import id.thoriq.dev.footballmatch.match.LastMatchView
import id.thoriq.dev.footballmatch.match.NextMatchPresenter
import id.thoriq.dev.footballmatch.match.NextMatchView
import id.thoriq.dev.footballmatch.match.PreviousMatchPresenter
import id.thoriq.dev.footballmatch.repository.*
import id.thoriq.dev.footballmatch.responses.LeagueResponse
import id.thoriq.dev.footballmatch.responses.MatchResponse
import id.thoriq.dev.footballmatch.services.ApiService
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class LeagueDetailActivityTest {

    @Mock
    private lateinit var view: LeagueView

    @Mock
    private lateinit var nextMatchView: NextMatchView

    @Mock
    private lateinit var previousMatchView: LastMatchView

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var presenter: LeaguePresenter

    @Mock
    private lateinit var nextMatchPresenter: NextMatchPresenter

    @Mock
    private lateinit var lastMatchPresenter: PreviousMatchPresenter

    @Mock
    private lateinit var matchRepository: MatchRepository

    @Mock
    private lateinit var leagueRepository: LeagueRepository

    @Mock
    private lateinit var leagueResponse: LeagueResponse

    @Mock
    private lateinit var matchResponse: MatchResponse


    private val league = Leagues(
        4328,
        1,
        "Soccer",
        "English Premier League",
        "Premier League",
        0,
        0,
        1992,
        "2000-08-19",
        "Male",
        "England",
        "www.premierleague.com",
        "en-gb.facebook.com/premierleague/",
        "twitter.com/premierleague",
        "www.youtube.com/channel/UCxr_0bvLiacGBhtyAb6BKug",
        "http://feeds.feedburner.com/PremierLeagueFootballNews",
        "The Premier League (often referred to as the English Premier League (EPL) outside England), is the top level of the English football league system. Contested by 20 clubs, it operates on a system of promotion and relegation with the English Football League (EFL).\\r\\n\\r\\nThe Premier League is a corporation in which the member clubs act as shareholders. Seasons run from August to May with each team playing 38 matches (playing each other home and away). Most games are played on Saturday and Sunday afternoons. The Premier League has featured 47 English and two Welsh clubs since its inception, making it a cross-border league.\\r\\n\\r\\nThe competition was formed as the FA Premier League on 20 February 1992 following the decision of clubs in the Football League First Division to break away from the Football League, founded in 1888, and take advantage of a lucrative television rights deal. The deal was worth £1 billion a year domestically as of 2013–14, with BSkyB and BT Group securing the domestic rights to broadcast 116 and 38 games respectively. The league generates €2.2 billion per year in domestic and international television rights. In 2014–15, teams were apportioned revenues of £1.6 billion, rising sharply to £2.4 billion in 2016–17.\\r\\n\\r\\nThe Premier League is the most-watched sports league in the world, broadcast in 212 territories to 643 million homes and a potential TV audience of 4.7 billion people. In the 2014–15 season, the average Premier League match attendance exceeded 36,000, second highest of any professional football league behind the Bundesliga's 43,500. Most stadium occupancies are near capacity. The Premier League ranks second in the UEFA coefficients of leagues based on performances in European competitions over the past five seasons, as of 2018.\\r\\n\\r\\nForty-nine clubs have competed since the inception of the Premier League in 1992. Six of them have won the title: Manchester United (13), Chelsea (5), Arsenal (3), Manchester City (3), Blackburn Rovers (1), and Leicester City (1). Following the 2003–04 season, Arsenal acquired the nickname \\\"The Invincibles\\\" as they became, and still remain, the only club to complete a Premier League campaign without losing a single game. The record of most points in a season is 100 by Manchester City in 2017–18.",
        "https://www.thesportsdb.com/images/media/league/fanart/o9c14r1547554186.jpg",
        "https://www.thesportsdb.com/images/media/league/fanart/917urb1547554025.jpg",
        "https://www.thesportsdb.com/images/media/league/fanart/3tjmas1547554302.jpg",
        "https://www.thesportsdb.com/images/media/league/fanart/qvuxyp1444339642.jpg",
        "https://www.thesportsdb.com/images/media/league/banner/4m3g4s1520767740.jpg",
        "https://www.thesportsdb.com/images/media/league/badge/i6o0kh1549879062.png",
        "https://www.thesportsdb.com/images/media/league/logo/4c377s1535214890.png",
        "https://www.thesportsdb.com/images/media/league/poster/v8d7nf1535455996.jpg",
        "https://www.thesportsdb.com/images/media/league/trophy/yrywtr1422246014.png",
        "{strHomeTeam} vs {strAwayTeam}",
        "yes",
        "unlocked"

    )

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        view = mock()
        apiService = ApiService.create()
        presenter = LeaguePresenter(view, leagueRepository)
        nextMatchPresenter = NextMatchPresenter(nextMatchView, matchRepository)
        lastMatchPresenter = PreviousMatchPresenter(previousMatchView, matchRepository)
    }

    @Test
    fun testGetNextMatch() {

        // test if league id is valid
        val leagueId: String = league.idLeague.toString()

        nextMatchPresenter.getNextMatch(leagueId)
        argumentCaptor<NextMatchCallback<MatchResponse?>>().apply {

            Mockito.verify(matchRepository).getNextMatch(eq(leagueId), capture())
            firstValue.onNextMatchDataLoaded(matchResponse)
        }

        Mockito.verify(nextMatchView).showLoading()
        Mockito.verify(nextMatchView).onNextMatchDataLoaded(matchResponse)
        Mockito.verify(nextMatchView).hideLoading()
    }

    @Test
    fun testGetNextMatchError() {
        // if league id is empty string
        val leagueId = ""
        nextMatchPresenter.getNextMatch(leagueId)
        argumentCaptor<NextMatchCallback<MatchResponse?>>().apply {

            Mockito.verify(matchRepository).getNextMatch(eq(leagueId), capture())
            firstValue.onNextMatchDataError()
        }

        Mockito.verify(nextMatchView).showLoading()
        Mockito.verify(nextMatchView).onNextMatchDataError()
        Mockito.verify(nextMatchView).hideLoading()
    }

    @Test
    fun testGetLastMatch() {
        var leagueId: String = league.idLeague.toString()

        // if leagueId is Valid
        lastMatchPresenter.getPreviousMatch(leagueId)
        argumentCaptor<LastMatchCallback<MatchResponse?>>().apply {

            Mockito.verify(matchRepository).getLastMatch(eq(leagueId), capture())
            firstValue.onLastMatchDataLoaded(matchResponse)
        }

        Mockito.verify(previousMatchView).showLoading()
        Mockito.verify(previousMatchView).onLastMatchDataLoaded(matchResponse)
        Mockito.verify(previousMatchView).hideLoading()
    }

    @Test
    fun testGetLastMatchError() {
        var leagueId: String = league.idLeague.toString()

        // if leagueId is Valid
        lastMatchPresenter.getPreviousMatch(leagueId)
        argumentCaptor<LastMatchCallback<MatchResponse?>>().apply {

            Mockito.verify(matchRepository).getLastMatch(eq(leagueId), capture())
            firstValue.onLastMatchDataError()
        }

        Mockito.verify(previousMatchView).showLoading()
        Mockito.verify(previousMatchView).onLastMatchDataError()
        Mockito.verify(previousMatchView).hideLoading()
    }

    @Test
    fun testGetLeagueData() {
        val id = league.idLeague.toString()
        presenter.getDataLeague(id)
        argumentCaptor<LeagueDetailCallback<LeagueResponse>>().apply {
            Mockito.verify(leagueRepository).getLeagueDetail(eq(id), capture())
            firstValue.onLeagueDetailDataLoaded(leagueResponse)
        }
        Mockito.verify(view).showLoading()
        Mockito.verify(view).onLeagueDetailDataLoaded(leagueResponse)
    }

    @Test
    fun testGetLeagueDataError() {
        val id = ""
        presenter.getDataLeague(id)
        argumentCaptor<LeagueDetailCallback<LeagueResponse>>().apply {
            Mockito.verify(leagueRepository).getLeagueDetail(eq(id), capture())
            firstValue.onLeagueDetailDataError()
        }
        Mockito.verify(view).showLoading()
        Mockito.verify(view).onLeagueDetailDataError()
        Mockito.verify(view).hideLoading()
    }
}
