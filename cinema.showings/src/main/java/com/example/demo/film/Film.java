package com.example.demo.film;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Film {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;
	
	String name;
	String imageUrl;
	long durationMinutes;
	
	public Film(String name, String imageUrl, long durationMinutes) {
		this.name = name;
		this.imageUrl = imageUrl;
		this.durationMinutes = durationMinutes;
	}

	@Override
	public String toString() {
		return String.format("name %s, imageUrl %s, durationMinutes %d", name, imageUrl, durationMinutes);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, imageUrl, durationMinutes);
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
		Film other = (Film) obj;
		return Objects.equals(name, other.name) &&
			   Objects.equals(imageUrl, other.imageUrl) &&
			   Objects.equals(durationMinutes, other.durationMinutes);
	}
	
	
}
