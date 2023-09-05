# My Favorite Movie App

Welcome to the "My Favorite Movie" Android application repository! This native Android app allows you to search among a database of movies, save your favorite movies, and store information about them, including length, rating, and plot summary. The app is designed to run on Android devices and is written in Kotlin using Android Studio.

## Features

### 1. Search
- The Search page allows users to browse and add movies to their saved collection.
- Users can search for movies by genre or by the movie's title.
- By default, the page displays the most popular movies according to TMDB (The Movie Database).

### 2. Saved Movies
- The Saved Movies page displays the user's saved movie collection.
- Users can "like" their favorite movies for easy access.
- Tapping on a movie allows users to view detailed information about it.
- Long tapping on a movie opens a page where the user can add notes about the movie.

### 3. Information
- The Information page provides additional details about a selected movie.
- Users can access information such as plot summaries, movie length, and more.

## Technologies Used

This app leverages several technologies to provide a seamless movie browsing and saving experience:

- **TMDB API**: The app uses the TMDB API as the primary source for movie data.

- **Retrofit**: Retrofit is used to connect with the TMDB API and fetch movie information.

- **Android Room**: Android Room is utilized for local data storage, allowing users to save their favorite movies and associated information.

- **Hilt**: Hilt is used for dependency injection, making it easier to manage dependencies and maintain clean code.

- **Glide**: Glide is used for efficient image loading and caching.
