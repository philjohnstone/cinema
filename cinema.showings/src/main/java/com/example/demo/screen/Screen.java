package com.example.demo.screen;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import lombok.Data;

@Entity
@Data
public class Screen {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	long id;
	
	long screenNumber;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "seats")
	@OrderColumn(name = "seatId")
	List<Seat> seats;
	
	public Screen(long screenNumber, List<Seat> seats) {
		this.screenNumber = screenNumber;
		this.seats = seats;
	}

	@Override
	public String toString() {
		return String.format("Screen %d has %d seats from %s to %s", screenNumber, seats.size(), seats.get(0), seats.get(seats.size() - 1));
	}

	@Override
	public int hashCode() {
		return Objects.hash(screenNumber, seats);
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
		Screen other = (Screen) obj;
		return Objects.equals(screenNumber, other.screenNumber) &&
			   Objects.equals(seats, other.seats);
	}
}
