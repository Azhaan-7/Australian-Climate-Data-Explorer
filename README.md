# Australian Climate Data Explorer

Explore Australian weather records from 1970 to 2020. Search by weather station,
state, date, or weather measurement. Compare stations and measurements, check
data quality, and download results as CSV files.

This project was built at RMIT University using Bureau of Meteorology data.

## Team

- Azhaanul Haque Farooqui
- Kieran Collins
- Sam Virgona

## Try it

[Open the climate explorer](https://australian-climate-data-explorer.onrender.com).
The site runs on Render's free plan, so it may take a little while to wake up.

## Run it on your computer

You need Java 17 or newer and Maven.

1. Extract `database/climate.db.zip` into the `database` folder.
2. Run `mvn package` from the project folder.
3. Run `java -jar target/studio-project-eqv-sep23-1.0-SNAPSHOT.jar`.
4. Open [localhost:7001](http://localhost:7001) in your browser.

The first run adds database indexes to speed up searches. It takes a few extra
seconds and uses about 67 MB more disk space. Later runs reuse those indexes.
The weather records stay the same.

## Check the code

Run `mvn test`. The tests use a separate temporary database.

The app uses Java, Javalin, SQLite, HTML, and CSS. Docker is used to run it on Render.
