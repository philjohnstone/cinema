package com.example.demo.screen;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Seat {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	long id;
	
	long row;
	char column;
	
	public Seat() {
		
	}
	
	public Seat(long row, char column) {
		this.row = row;
		this.column = column;
	}

	public long getRow() {
		return row;
	}

	public void setRow(long row) {
		this.row = row;
	}

	public char getColumn() {
		return column;
	}

	public void setColumn(char column) {
		this.column = column;
	}

	@Override
	public String toString() {
		return String.format("%s%d", column, row);
	}

	@Override
	public int hashCode() {
		return Objects.hash(row, column);
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
		Seat other = (Seat) obj;
		return Objects.equals(row, other.row) &&
			   Objects.equals(column, other.column);
	}
	
}
