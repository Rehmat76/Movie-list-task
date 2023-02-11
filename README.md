
# The MVVM Design Pattern With Solid Principle, ViewBinding and Koin




## Contents

- Introduction
- MVVM
    - What is MVVM?
    - How it Works
    - Room Database
    - Repositories
- Fullscreen mode
- View Binding
- Koin
- Orientation Behaviour


## Introduction

This project is to provide a simple example app for Android Developers to reference when starting a new app. The architecture pattern shown here and to be used in apps, whenever possible, is Model-View-ViewModel, or MVVM with View Binding and Koin as a Dependency Injection.

### App Features
- Save movie list in sharedpreferences when internet not available then show the cache list
- Stores Favourite movie in Room Database in app
- Favourite movie shown even there is no internet
- Displays favourite movies list from database
- Allows searching of movies 
- Select a movie and show details of the movies
- Detail movie screen has orientation controls and also change the view in landscape and potrate mode

## Language and IDE
This project was written using Kotlin 1.4.30 in Android Studio. All the application is written in Kotlin

## Prerequisites
This project assumes a base knowledge of Kotlin and Android, such as Activities, Fragments, RecyclerViews, and the Manifest.

# MVVM with ViewBinding and Koin
MVVM is a flexible guide and set of libraries used to standardize an app's architecture. What this means is that your code is split into distinct components that hold specific parts of code in specific areas.These parts then interact with each other in a set order. These parts are:

### Model
The Model consists of three parts
- Database Class
- DAO Interfaces
- Data Model Classes
In this app, our local database is Room. In other apps, Firebase may be the database. The flexibility of MVVM allows for these differences, and does not specify what to use, just where to put the code.
### View
The View is the UI presentation logic. It consists of
- Activities
- Fragments
- Adapters
- XML
The View should only be responsible for displaying values and state. Another way to say that is you should not make database requests and/or network requests in the View. These will be done elsewhere. This means the Views don't care where the data comes from or how it gets there, it just shows whatever data there is. This way, if a change needs to be made to the database, the View should not need to be changed at all.
### View Model
The View Model is responsible for interacting with the Database and coordinating between any remote sources. It is then responsible for representing the state of the data to the View. This coordination creates what is known as a single source of truth and exposes it to the Views. This way, you can be as sure as you can be, that what is being shown to the user is accurate, and where it came from.

ViewModels provided by the Jetpack components are also Lifecycle aware, and can survive configuration changes. This helps:
- Prevent memory leaks
- Prevent errant network calls
- Prevent null pointer exceptions due to UI changes
- Solves the issue of what happens when you rotate an app

## How it works
As mentioned before MVVM breaks the app into components and they interact in a certain way. This interaction follows the pattern:

1. The View subscribes to a LiveData from a ViewModel
2. The ViewModel connects to a repository
3. The Repository connects to databases, both remote and local, if there are both
4. The Repository returns the requested data to the the ViewModel
5. The ViewModel formats the data and exposes it through a LiveData
6. The View's subscriptions are notified of any changes through the LiveData, and updates the UI to match

### LiveData
LiveData was mentioned a few times, so what is it? LiveData is a wrapper class for data objects. It is observable from a View. Being observable is a way to automatically call update UI logic, anytime the data is changed. There's more to it than all that, but those are the very basics. If they don't make sense now, they will after you use them.

## Room Database
This app uses the Room Database library, provided by Google as part of Android Jetpack. Room is a wrapper for Android's SQLite DB. Essentially, it operates like Retrofit, but for database calls. It relies heavily on annotations to generate code for you. Room also natively supports LiveData.

### Basic Room Example
Room consist of three main parts

- The Database Class
- Data Access Object Interfaces
- Annotated Data Classes

## Repositories
Repositories, while not required and aren't an official component of MVVM, they are considered a good practice. Especially when you have data being stored locally and data available on a remote API. The repository provides what is known as a single source of truth. More simply put, when data is coming possibly coming from multiple locations, the repository decides what is valid, what to show, where to call, and when to make those calls.

### Final Thoughts on MVVM
That's it. This app is ready to be released or expanded on. You could add more endpoints, add new database operations, add new screens, add new data classes, add whole new features.

## View Binding
View binding is a feature that allows you to more easily write code that interacts with views. Once view binding is enabled in a module, it generates a binding class for each XML layout file present in that module.

## Koin
A framework to help you build any kind of Kotlin application, from Android mobile to backend Ktor server applications
Koin is developed by Kotzilla and open-source contributors.

Koin is a smart Kotlin dependency injection library to keep you focused on your app, not on your tools.

Koin gives you simple tools and API to let you build, assemble Kotlin related technologies into your application and let you scale your business with easyness.

## Orientation Behaviour
In the movie detail fragment we are handle the orientation and show two differnet view for user. 
