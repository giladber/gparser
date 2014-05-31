package com.gparser.java8.examples;

import java.time.*;

/**
 * java.time examples
 * Created by Gilad Ber on 5/31/2014.
 */
public class Dates
{
	public static void main(String... args)
	{
		ex1();
	}

	private static void ex1()
	{
		LocalTime localTime = LocalTime.of(11, 0, 0); //11:00
		LocalDate localDate = LocalDate.of(2014, 6, 2);
		LocalDateTime dateTime = localTime.atDate(localDate);
		ZonedDateTime zoned = dateTime.atZone(ZoneId.of("GMT+3"));
		ZonedDateTime other = ZonedDateTime.now();
		System.out.println(zoned);
		System.out.println("now zoned: " + other);

		ZoneId id = ZoneId.systemDefault();
		System.out.println("Default zone id is " + id);
	}
}
