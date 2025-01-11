package com.proyecto.Fer.exception;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

//La notacion data es ceunta d eos getter y setter
	@Data
	//para crear constructor vacio
	@NoArgsConstructor

	//ES UN DTO EN SI POR QUE DEVUELVE ALGO
	public class Error {
		private String message;
		private String error;
		private int status;
		private Date date;


	}


