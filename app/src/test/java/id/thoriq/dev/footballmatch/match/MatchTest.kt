package id.thoriq.dev.footballmatch.match

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import id.thoriq.dev.footballmatch.repository.MatchRepository
import id.thoriq.dev.footballmatch.repository.MatchRepositoryCallback
import id.thoriq.dev.footballmatch.repository.MatchSearchCallback
import id.thoriq.dev.footballmatch.responses.MatchResponse
import id.thoriq.dev.footballmatch.responses.SearchResponse
import id.thoriq.dev.footballmatch.services.ApiService
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MatchTest {

    @Mock
    private lateinit var matchView: MatchDetailView

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var matchPresenter: MatchPresenter


    @Mock
    private lateinit var matchResponse: MatchResponse


    @Mock
    private lateinit var matchRepository: MatchRepository

    @Mock
    private lateinit var matchSearchView: MatchSearchView

    @Mock
    private lateinit var matchSearchPresenter: MatchSearchPresenter

    @Mock
    private lateinit var searchResponse: SearchResponse

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        apiService = ApiService.create()
        matchPresenter = MatchPresenter(matchView, matchRepository)
        matchSearchPresenter = MatchSearchPresenter(matchSearchView, matchRepository)
    }

    @Test
    fun testGetMatchData() {

        val id = "576849"
        matchPresenter.getMatchDetail(id)
        argumentCaptor<MatchRepositoryCallback<MatchResponse?>>().apply {

            Mockito.verify(matchRepository).getMatchDetail(eq(id), capture())
            firstValue.onDataLoaded(matchResponse)
        }
        Mockito.verify(matchView).showLoading()
        Mockito.verify(matchView).onDataLoaded(matchResponse)
        Mockito.verify(matchView).hideLoading()
    }

    @Test
    fun getMatchErrorTest() {

        matchPresenter.getMatchDetail("")

        argumentCaptor<MatchRepositoryCallback<MatchResponse?>>().apply {

            Mockito.verify(matchRepository).getMatchDetail(eq(""), capture())
            firstValue.onDataError()
        }

        Mockito.verify(matchView).showLoading()
        Mockito.verify(matchView).onDataError()
        Mockito.verify(matchView).hideLoading()
    }

    @Test
    fun testSearchMatch() {
        val query = "Arsenal"
        matchSearchPresenter.getMatchs(query)
        argumentCaptor<MatchSearchCallback<SearchResponse?>>().apply {

            Mockito.verify(matchRepository).searchMatch(eq(query), capture())
            firstValue.onSearchDataLoaded(searchResponse)
        }
        Mockito.verify(matchSearchView).showLoading()
        Mockito.verify(matchSearchView).onSearchDataLoaded(searchResponse)
        Mockito.verify(matchSearchView).hideLoading()
    }

    @Test
    fun testSearchMatchError() {
        val query = "aaaaaaaa"
        matchSearchPresenter.getMatchs(query)
        argumentCaptor<MatchSearchCallback<SearchResponse?>>().apply {

            Mockito.verify(matchRepository).searchMatch(eq(query), capture())
            firstValue.onSearchError()
        }
        Mockito.verify(matchSearchView).showLoading()
        Mockito.verify(matchSearchView).onSearchError()
        Mockito.verify(matchSearchView).hideLoading()
    }
}