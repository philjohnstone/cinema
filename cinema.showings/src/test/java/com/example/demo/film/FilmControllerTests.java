package com.example.demo.film;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
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

import com.example.demo.screen.ScreenRepository;
import com.example.demo.showing.ShowingRepository;

@RunWith(SpringRunner.class)
@WebMvcTest({FilmController.class})
@Import(FilmResourceAssembler.class)
public class FilmControllerTests {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private FilmRepository filmRepository;
	
	@MockBean
	private ShowingRepository showingRepository;
	
	@MockBean
	private ScreenRepository screenRepository;
	
	@Test
	public void testAllFilms() throws Exception {
		List<Film> films = Arrays.asList(
			new Film("The Shawshank Redemption", "https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_UX182_CR0,0,182,268_AL_.jpg", 142),
			new Film("The Godfather", "https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_UY268_CR3,0,182,268_AL_.jpg", 175));
		
		// ID would automatically increment with JPA
		for (int i=0; i<films.size(); i++) {
			films.get(i).setId(i + 1);
		}
		
		given(filmRepository.findAll()).willReturn(films);
		
		mvc.perform(get("/films").accept(MediaTypes.HAL_JSON_VALUE))
			//.andDo(print())
			.andExpect(status().isOk())
			.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
			.andExpect(filmNameJsonPath(films, 0))
			.andExpect(filmImageUrlJsonPath(films, 0))
			.andExpect(filmDurationMinutesJsonPath(films, 0))
			.andExpect(filmSelfLinkJsonPath(films, 0))
			.andExpect(filmCollectionLinkJsonPath(films, 0))
			.andExpect(filmNameJsonPath(films, 1))
			.andExpect(filmImageUrlJsonPath(films, 1))
			.andExpect(filmDurationMinutesJsonPath(films, 1))
			.andExpect(filmSelfLinkJsonPath(films, 1))
			.andExpect(filmCollectionLinkJsonPath(films, 1))
			.andExpect(jsonPath("$._links.self.href", is("http://localhost/films")))
			.andReturn();
	}
	
	@Test
	public void testOneFilm() throws Exception {
		Film film = new Film("The Shawshank Redemption", "https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_UX182_CR0,0,182,268_AL_.jpg", 142);
		film.setId(1);
		
		given(filmRepository.findById(film.getId())).willReturn(Optional.of(film));
		
		mvc.perform(get("/films/" + film.getId()).accept(MediaTypes.HAL_JSON_VALUE))
			//.andDo(print())
			.andExpect(status().isOk())
			.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
			.andExpect(jsonPath("$.name", is(film.getName())))
			.andExpect(jsonPath("$.imageUrl", is(film.getImageUrl())))
			.andExpect(jsonPath("$.durationMinutes", is((int) film.getDurationMinutes())))
			.andExpect(jsonPath("$._links.self.href", is("http://localhost/films/" + film.getId())))
			.andExpect(jsonPath("$._links.films.href", is("http://localhost/films")))
			.andReturn();
	}
	
	@Test
	public void testInvalidFilm() throws Exception {
		long id = 1;
		given(filmRepository.findById(id)).willReturn(Optional.empty());
		
		mvc.perform(get("/films/" + id).accept(MediaTypes.HAL_JSON_VALUE))
			//.andDo(print())
			.andExpect(status().isNotFound())
			.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
			.andExpect(content().string("Could not find film with id " + id))
			.andReturn();
	}
	
	@Test
	public void testInvalidPostToCollection() throws Exception {
		mvc.perform(post("http://localhost/films")).andExpect(status().isMethodNotAllowed());
	}
	
	private ResultMatcher filmNameJsonPath(List<Film> films, int index) {
		return jsonPath("$._embedded.films[" + index + "].name", is(films.get(index).getName()));
	}
	
	private ResultMatcher filmImageUrlJsonPath(List<Film> films, int index) {
		return jsonPath("$._embedded.films[" + index + "].imageUrl", is(films.get(index).getImageUrl()));
	}
	
	private ResultMatcher filmDurationMinutesJsonPath(List<Film> films, int index) {
		return jsonPath("$._embedded.films[" + index + "].durationMinutes", is((int) films.get(index).getDurationMinutes()));
	}
	
	private ResultMatcher filmSelfLinkJsonPath(List<Film> films, int index) {
		return jsonPath("$._embedded.films[" + index + "]._links.self.href", is("http://localhost/films/" + films.get(index).getId()));
	}
	
	private ResultMatcher filmCollectionLinkJsonPath(List<Film> films, int index) {
		return jsonPath("$._embedded.films[" + index + "]._links.films.href", is("http://localhost/films"));
	}
}
