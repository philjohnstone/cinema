package com.example.demo.showing;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.example.demo.film.Film;
import com.example.demo.screen.Screen;

import lombok.Data;

@Entity
@Data
public class Showing {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	long id;
	
	@ManyToOne
	@JoinColumn(name = "filmId")
	Film film;
	
	@ManyToOne
	@JoinColumn(name = "screenId")
	Screen screen;
	
	LocalDateTime startTime;
	
	public Showing(Film film, Screen screen, LocalDateTime startTime) {
		this.film = film;
		this.screen = screen;
		this.startTime = startTime;
	}

	@Override
	public String toString() {
		return String.format("film: %s, screen: %d, startTime: %s", film.getName(), screen.getScreenNumber(), startTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(film, screen, startTime);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Showing other = (Showing) obj;
		return Objects.equals(film, other.film) &&
			   Objects.equals(screen, other.screen) &&
			   Objects.equals(startTime, other.startTime);
	}
}
