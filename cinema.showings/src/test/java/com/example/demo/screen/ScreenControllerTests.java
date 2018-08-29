package com.example.demo.screen;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.example.demo.film.FilmRepository;
import com.example.demo.showing.ShowingRepository;

@RunWith(SpringRunner.class)
@WebMvcTest({ScreenController.class})
@Import(ScreenResourceAssembler.class)
public class ScreenControllerTests {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private FilmRepository filmRepository;
	
	@MockBean
	private ShowingRepository showingRepository;
	
	@MockBean
	private ScreenRepository screenRepository;
	
	@Test
	public void testAllScreens() throws Exception {
		List<Screen> screens = Arrays.asList(
			new Screen(1, createSeats(1, 1)),
			new Screen(2, createSeats(2, 2)));
		
		// ID would automatically increment with JPA
		for (int i=0; i<screens.size(); i++) {
			screens.get(i).setId(i + 1);
		}
		
		given(screenRepository.findAll()).willReturn(screens);
				
		mvc.perform(get("/screens").accept(MediaTypes.HAL_JSON_VALUE))
			//.andDo(print())
			.andExpect(status().isOk())
			.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
			.andExpect(screenNumberJsonPath(screens, 0))
			.andExpect(seatRowJsonPath(screens, 0, 0))
			.andExpect(seatColumnJsonPath(screens, 0, 0))
			.andExpect(screenNumberJsonPath(screens, 1))
			.andExpect(seatRowJsonPath(screens, 1, 0))
			.andExpect(seatColumnJsonPath(screens, 1, 0))
			.andExpect(seatRowJsonPath(screens, 1, 1))
			.andExpect(seatColumnJsonPath(screens, 1, 1))
			.andExpect(seatRowJsonPath(screens, 1, 2))
			.andExpect(seatColumnJsonPath(screens, 1, 2))
			.andExpect(seatRowJsonPath(screens, 1, 3))
			.andExpect(seatColumnJsonPath(screens, 1, 3))
			.andExpect(jsonPath("$._links.self.href", is("http://localhost/screens")))
			.andReturn();
	}
	
	@Test
	public void testSingleScreen() throws Exception {
		List<Screen> screens = Arrays.asList(
			new Screen(1, createSeats(1, 1)),
			new Screen(2, createSeats(1, 2)),
			new Screen(3, createSeats(1, 3)));
		
		for (int i=0; i<screens.size(); i++) {
			// ID would automatically increment with JPA
			screens.get(i).setId(i + 1);
			given(screenRepository.findById(screens.get(i).getId())).willReturn(Optional.of(screens.get(i)));
		}
		
		mvc.perform(get("/screens/" + screens.get(0).getId()).accept(MediaTypes.HAL_JSON_VALUE))
			//.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.screenNumber", is((int) screens.get(0).getId())))
			.andExpect(jsonPath("$.seats[0].row", is((int) screens.get(0).getSeats().get(0).getRow())))
			.andExpect(jsonPath("$.seats[0].column", is(String.valueOf(screens.get(0).getSeats().get(0).getColumn()))))
			.andExpect(jsonPath("$._links.screens.href", is("http://localhost/screens")))
			.andReturn();
	}
	
	@Test
	public void testInvalidScreen() throws Exception {
		long id = 1;
		given(screenRepository.findById(id)).willReturn(Optional.empty());
		mvc.perform(get("/screens/" + id).accept(MediaTypes.HAL_JSON_VALUE))
			.andExpect(status().isNotFound())
			.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
			.andExpect(content().string("Could not find screen with id " + id))
			.andReturn();
	}
	
	private ResultMatcher screenNumberJsonPath(List<Screen> screens, int index) {
		return jsonPath("$._embedded.screens[" + index + "].screenNumber", is((int) screens.get(index).getScreenNumber()));
	}
	
	private ResultMatcher seatRowJsonPath(List<Screen> screens, int screenIndex, int seatIndex) {
		return jsonPath("$._embedded.screens[" + screenIndex + "].seats[" + seatIndex + "].row", is((int) screens.get(screenIndex).getSeats().get(seatIndex).getRow()));
	}
	
	private ResultMatcher seatColumnJsonPath(List<Screen> screens, int screenIndex, int seatIndex) {
		return jsonPath("$._embedded.screens[" + screenIndex + "].seats[" + seatIndex + "].column", is(String.valueOf(screens.get(screenIndex).getSeats().get(seatIndex).getColumn())));
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
