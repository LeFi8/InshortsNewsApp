# InshortsNewsApp
This News App is an Android application that allows users to read short news articles. 
It provides a convenient way to stay updated with latest news. 

## Features
- User Authentication
  - Users can create accounts and log in to access the app's features
  - It is also possible to use a <b>Google Account</b> for authentication
- Category-based News Articles, sorted by popularity
  - General
  - Technology
  - Sports
- Articles already read by a user is marked as <b>seen</b>
- Clicking on the article redirects you to its source for detailed reading
- Clean User Interface provides a visually appealing and user-friendly interface for easy navigation and reading

## Used Tools and Technologies
- [News API](https://newsapi.org/docs): This API is used to fetch the latest news articles from various sources
- Jetpack Compose: Login functionality is implemented using Jetpack Compose
- Firebase: The app integrates Firebase Authentication and Firebase Realtime Database for user authentication and storing user data, such as read articles
- Lottie: Lottie is used to display animation on the login screen
- Glide: Glide is used for efficient image loading and caching
- OkHttp: OkHttp is used as the HTTP client for making network requests
- Kotlin and Android studio
- ViewBindings, Coroutines

|Splash screen|Login|Register|
|---|---|---|
|<img src="/sample-images/splash.png">|<img src="/sample-images/login.gif">|<img src="/sample-images/register.png">|

|News|Article|Article marked as seen|
|---|---|---|
|<img src="/sample-images/news_panel.png">|<img src="/sample-images/article.png">|<img src="/sample-images/seen.png">|

## Disclaimer
This app is created for <b>educational purposes</b> and does not guarantee the accuracy or reliability of news articles presented. Users are encouraged to verify information from reliable sources.

