# GitHub-User

GitHub-User is an Android application designed to interact with GitHub's API, displaying user profiles, repositories, and other relevant details. This project is built using modern Android development practices and follows clean architecture principles.

## Features
- Fetch and display GitHub user profiles.
- View repositories and followers of a user.
- Responsive UI using Material Design components.
- Dependency injection with Hilt.
- Efficient networking with Retrofit and OkHttp.
- Image loading with Glide.
- Shimmer effect for loading states.
- Navigation Component for seamless screen transitions.

## Tech Stack
- **Language:** Kotlin
- **Architecture:** MVVM (Model-View-ViewModel)
- **Dependency Injection:** Hilt
- **Networking:** Retrofit, OkHttp
- **UI Components:** Material Design, ConstraintLayout, Flexbox
- **Testing:** JUnit, Mockito, Espresso

## Project Structure
```
app
│── manifests
│── kotlin+java
│   └── robi.technicaltest.githubuser
│       ├── di
│       ├── ui
│       │   ├── GitHubApplication
│       │   ├── MainActivity
│       ├── (androidTest)
│       ├── (test)
│── res

BaseApplication
│── manifests
│── kotlin+java
│   └── robi.technicaltest.baseapplication
│       ├── utils
│       │   ├── BaseActivity
│       │   ├── BaseFragment
│       │   ├── BaseRecyclerViewAdapter
│       ├── (androidTest)
│       ├── (test)

networks
│── manifests
│── kotlin+java
│   └── robi.technicaltest.networks
│       ├── data
│       ├── di
│       ├── repository
│       ├── usecase
│       │   ├── ApiServices
│       │   ├── AuthInterceptor
│       │   ├── NetworkState
│       ├── (androidTest)
│       ├── (test)
```

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


