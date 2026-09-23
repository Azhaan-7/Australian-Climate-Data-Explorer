# Australian Climate Data Explorer

A Java web application for exploring historical Australian climate data through filtering, aggregation, comparison, and similarity analysis.

The application was developed as a team project at RMIT University using Bureau of Meteorology climate data covering 1970–2020.

## Live Demo

> This project is deployed on Render's free tier. The service may experience cold-start delays after periods of inactivity, and data-intensive queries may run more slowly than they do locally.

**Live site:** [Link](https://australian-climate-data-explorer.onrender.com)

## Features

- Explore climate data by Australian weather station
- Filter weather stations by location and climate metric
- Analyse climate measurements across configurable date ranges
- Explore data quality information
- Compare weather stations using percentage change between time periods
- Compare climate metrics using similarity analysis
- Analyse relationships between climate metrics
- Export query results as CSV files
- View dataset and project information through a web interface

## Tech Stack

### Backend

- Java 17
- Javalin
- JDBC
- SQLite
- Maven

### Frontend

- Thymeleaf
- HTML
- CSS

### Deployment

- Docker
- Render

## Architecture

```text
Browser
   |
   v
Javalin Web Server
   |
   v
Application / Query Logic
   |
   v
JDBC
   |
   v
SQLite Climate Database