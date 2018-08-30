package com.example.demo.showing;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import com.example.demo.film.Film;
import com.example.demo.film.FilmRepository;
import com.example.demo.screen.Screen;
import com.example.demo.screen.ScreenRepository;
import com.example.demo.screen.Seat;

@RunWith(SpringRunner.class)
@WebMvcTest({ShowingController.class})
@Import(ShowingResourceAssembler.class)
public class ShowingControllerTests {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private FilmRepository filmRepository;
	
	@MockBean
	private ShowingRepository showingRepository;
	
	@MockBean
	private ScreenRepository screenRepository;

	@Test
	public void testAllShowings() throws Exception {
		Film shawshank = new Film("The Shawshank Redemption", "https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_UX182_CR0,0,182,268_AL_.jpg", 142);
		Screen screenOne = new Screen(1, createSeats(1, 1));
		List<Showing> showings = Arrays.asList(
			new Showing(shawshank, screenOne, LocalDateTime.now().plusHours(1)),
			new Showing(shawshank, screenOne, LocalDateTime.now().plusHours(1).plusMinutes(shawshank.getDurationMinutes()).plusMinutes(15)));
		
		// ID would automatically increment with JPA
		shawshank.setId(1);
		screenOne.setId(1);
		for (int i=0; i<showings.size(); i++) {
			showings.get(i).setId(i + 1);
		}
		
		given(showingRepository.findAll()).willReturn(showings);

		mvc.perform(get("/showings").accept(MediaTypes.HAL_JSON_VALUE))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
			.andExpect(showingFilmNameJsonPath(showings, 0))
			.andExpect(showingFilmImageUrlJsonPath(showings, 0))
			.andExpect(showingFilmDurationMinutesJsonPath(showings, 0))
			.andExpect(showingScreenNumberJsonPath(showings, 0))
			.andExpect(showingScreenSeatsRowJsonPath(showings, 0, 0))
			.andExpect(showingScreenSeatsColumnJsonPath(showings, 0, 0))
			.andExpect(showingStartTimeJsonPath(showings, 0))
			.andExpect(jsonPath("$._links.self.href", is("http://localhost/showings")))
			.andReturn();
	}
	
	@Test
	public void testSingleShowing() throws Exception {
		Showing showing = new Showing(new Film("The Shawshank Redemption", "https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_UX182_CR0,0,182,268_AL_.jpg", 142), 
				new Screen(1, createSeats(1, 1)), LocalDateTime.now().plusHours(1));
		
		// ID would automatically increment with JPA
		showing.getFilm().setId(1);
		showing.getScreen().setId(1);
		showing.setId(1);
		
		given(showingRepository.findById(showing.getId())).willReturn(Optional.of(showing));
		
		mvc.perform(get("/showings/" + showing.getId()).accept(MediaTypes.HAL_JSON_VALUE))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
			.andExpect(jsonPath("film.name", is(showing.getFilm().getName())))
			.andExpect(jsonPath("film.imageUrl", is(showing.getFilm().getImageUrl())))
			.andExpect(jsonPath("film.durationMinutes", is((int) showing.getFilm().getDurationMinutes())))
			.andExpect(jsonPath("screen.screenNumber", is((int) showing.getScreen().getScreenNumber())))
			.andExpect(jsonPath("screen.seats[0].row", is((int) showing.getScreen().getSeats().get(0).getRow())))
			.andExpect(jsonPath("screen.seats[0].column", is(String.valueOf(showing.getScreen().getSeats().get(0).getColumn()))))
			.andExpect(jsonPath("startTime", is(showing.getStartTime().toString())))
			.andExpect(jsonPath("$._links.self.href", is("http://localhost/showings/" + showing.getId())))
			.andExpect(jsonPath("$._links.showings.href", is("http://localhost/showings")))
			.andReturn();
	}
	
	@Test
	public void testInvalidShowing() throws Exception {
		long id = 1;
		given(showingRepository.findById(id)).willReturn(Optional.empty());
		mvc.perform(get("/showings/" + id).accept(MediaTypes.HAL_JSON_VALUE))
			.andExpect(status().isNotFound())
			.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
			.andExpect(content().string("Could not find showing with id " + id))
			.andReturn();
	}
	
	@Test
	public void testInvalidPostToCollection() throws Exception {
		mvc.perform(post("http://localhost/showings")).andExpect(status().isMethodNotAllowed());
	}
	
	private ResultMatcher showingFilmNameJsonPath(List<Showing> showings, int index) {
		return jsonPath("$._embedded.showings[" + index + "].film.name", is(showings.get(index).getFilm().getName()));
	}
	
	private ResultMatcher showingFilmImageUrlJsonPath(List<Showing> showings, int index) {
		return jsonPath("$._embedded.showings[" + index + "].film.imageUrl", is(showings.get(index).getFilm().getImageUrl()));
	}

	private ResultMatcher showingFilmDurationMinutesJsonPath(List<Showing> showings, int index) {
		return jsonPath("$._embedded.showings[" + index + "].film.durationMinutes", is((int) showings.get(index).getFilm().getDurationMinutes()));
	}

	private ResultMatcher showingScreenNumberJsonPath(List<Showing> showings, int index) {
		return jsonPath("$._embedded.showings[" + index + "].screen.screenNumber", is((int) showings.get(index).getScreen().getScreenNumber()));
	}

	private ResultMatcher showingScreenSeatsRowJsonPath(List<Showing> showings, int showingIndex, int seatIndex) {
		return jsonPath("$._embedded.showings[" + showingIndex + "].screen.seats[" + seatIndex + "].row", is((int) showings.get(showingIndex).getScreen().getSeats().get(seatIndex).getRow()));
	}

	private ResultMatcher showingScreenSeatsColumnJsonPath(List<Showing> showings, int showingIndex, int seatIndex) {
		return jsonPath("$._embedded.showings[" + showingIndex + "].screen.seats[" + seatIndex + "].column", is(String.valueOf(showings.get(showingIndex).getScreen().getSeats().get(seatIndex).getColumn())));
	}

	private ResultMatcher showingStartTimeJsonPath(List<Showing> showings, int index) {
		return jsonPath("$._embedded.showings[" + index + "].startTime", is(showings.get(index).getStartTime().toString()));
	}
	
	private static List<Seat> createSeats(int numberOfRows, int numberOfColumns) {
		List<Seat> seats = new LinkedList<>();
		int asciiA = 65;
		for (int column=asciiA; column<asciiA + numberOfColumns; column++) {
			for (int row=1; row<=numberOfRows; row++) {
				seats.add(new Seat(row, (char) column));
			}
		}
		return seats;
	}
}
