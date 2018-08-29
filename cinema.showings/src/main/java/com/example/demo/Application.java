package com.example.demo;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.film.Film;
import com.example.demo.film.FilmRepository;
import com.example.demo.screen.Screen;
import com.example.demo.screen.ScreenRepository;
import com.example.demo.screen.Seat;
import com.example.demo.showing.Showing;
import com.example.demo.showing.ShowingRepository;

@SpringBootApplication
public class Application implements CommandLineRunner {
	
	@Autowired
	FilmRepository filmRepository;
	
	@Autowired
	ScreenRepository screenRepository;
	
	@Autowired
	ShowingRepository showingRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Film shawshank = new Film("The Shawshank Redemption", "https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_UX182_CR0,0,182,268_AL_.jpg", 142);
		Film godfather = new Film("The Godfather", "https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_UY268_CR3,0,182,268_AL_.jpg", 175);
		Film godfather2 = new Film("The Godfather: Part II", "https://m.media-amazon.com/images/M/MV5BMWMwMGQzZTItY2JlNC00OWZiLWIyMDctNDk2ZDQ2YjRjMWQ0XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_UY268_CR3,0,182,268_AL_.jpg", 202);
		Film[] films = { shawshank, godfather, godfather2 };
		filmRepository.saveAll(Arrays.asList(films));
				
		Screen screenOne = new Screen(1, createSeats(3, 5));
		Screen screenTwo = new Screen(2, createSeats(10, 10));
		Screen screenThree = new Screen(3, createSeats(15, 10));
		Screen[] screens = { screenOne, screenTwo, screenThree };
		screenRepository.saveAll(Arrays.asList(screens));
				
		int minutesBetweenShowings = 15;
		Showing showingOne = new Showing(shawshank, screenOne, LocalDateTime.now().plusHours(1));
		Showing showingTwo = new Showing(shawshank, screenOne, LocalDateTime.now().plusHours(1).plusMinutes(shawshank.getDurationMinutes()).plusMinutes(minutesBetweenShowings));
		Showing[] showings = { showingOne, showingTwo };
		showingRepository.saveAll(Arrays.asList(showings));
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
