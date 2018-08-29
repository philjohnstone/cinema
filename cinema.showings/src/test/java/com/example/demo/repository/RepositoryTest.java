package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.demo.Application;
import com.example.demo.film.Film;
import com.example.demo.film.FilmRepository;
import com.example.demo.screen.Screen;
import com.example.demo.screen.ScreenRepository;
import com.example.demo.screen.Seat;
import com.example.demo.showing.Showing;
import com.example.demo.showing.ShowingRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
public class RepositoryTest {
	
	private static final Film FILM = new Film("The Shawshank Redemption", "https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_UX182_CR0,0,182,268_AL_.jpg", 142);
	private static final Screen SCREEN = new Screen(1, createSeats(3, 5));
	private static final Showing SHOWING = new Showing(FILM, SCREEN, LocalDateTime.now().plusHours(1));

	@Autowired
	FilmRepository filmRepository;
	
	@Autowired
	ScreenRepository screenRepository;
	
	@Autowired
	ShowingRepository showingRepository;
	
	@Test
	public void testFilmRepository() throws Exception {
		testRepository(Arrays.asList(new Film[]{ FILM }), filmRepository, "Film");
	}
	
	@Test
	public void testScreenRepository() throws Exception {
		testRepository(Arrays.asList(new Screen[] { SCREEN }), screenRepository, "Screen");
	}
	
	@Test
	public void testShowingRepository() throws Exception {
		screenRepository.save(SHOWING.getScreen());
		filmRepository.save(SHOWING.getFilm());
		testRepository(Arrays.asList(new Showing[] { SHOWING }), showingRepository, "Showing");
	}
	
	private static <E, ID, R extends JpaRepository<E, ID>> void testRepository(List<E> entities, R repository, String entityName) throws Exception {
		repository.saveAll(entities);
		List<E> persistedEntities = repository.findAll();
		Assert.assertEquals(String.format("Number of %s persisted", entityName), entities.size(), persistedEntities.size());
		for (int i=0; i<entities.size(); i++) {
			Assert.assertEquals(String.format("%s %d", entityName, i), entities.get(i), persistedEntities.get(i));
		}
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
