### [Demo](https://cool-school-19352.web.app/)&nbsp;&nbsp;&nbsp;&nbsp;[Cool School](https://github.com/Gin-n-Tonicc/Cool-School)

This codebase was created to show a full application built with React with TypeScript that uses Java with Spring Boot as backend server with CRUD operations, authentication, routing, filtering and more.

# Making requests to the backend API

For convenience, the API server is in the project files so everyone can host it on their own computer. In order to run the server, open the project in IntelliJ or any IDE of your choice and run it from there. Make sure you have Java installed!

If you want to change the API URL, simply edit `client/.env` and change `REACT_APP_API_URL` to the server's URL (i.e. `localhost:3000/api`)

# Getting started

I use [npm](https://www.npmjs.com/) to manage the dependencies. It should be automatically installed with node. Open a terminal in the client folder and run `npm install` or `npm i` to resolve all dependencies.
Also run the backend server using an IDE, it should start on `http://localhost:8080`. Make sure to change the application.yaml file to your own needs before running the backend server locally.

Run `npm start` in the client folder for a dev server. Navigate to `http://localhost:3000/`. The app will automatically reload if you change any of the source files.

### Building the project

Run `npm run build` to build the project. The build will be stored in the `dist/` directory.

## Functionality overview

The project application is an educational application called "Cool School". It uses a custom API for all requests, including authentication.

**General functionality:**

- Authenticate users via JWT (login/register pages + logout button)
- CRUD Users
- CRUD Courses
  - CRUD Subsections
  - CR\*\* Reviews
  - CRUD Resources
- CRUD Blogs
  - CR\*\* Likes
  - CR\*\* Comments 
- CRUD Categories
  
- GET and display paginated lists of courses, subsections, resources, blogs, comments, users, categories
- GET and display filtered list of blogs by category and/or title.

**The general page breakdown looks like this:**

- Home page ( URL: / )
  - Welcome page with general site info
- Not Found page ( URL: /not-found )
  - Navigated to automatically when you try to access a page that doesn't exist
- Contact page ( URL: /contact )
  - Page where you can email us about anything going on with the website.
- Login/Register pages ( URL: /login, /register )
  - Uses JWT (stores the token in localStorage)
- Courses page ( URL: /courses )
  - Pagination the courses sorted by newest first
  - Single Course page ( URL: /courses/:id )
     - Show detailed course info
     - Enroll the course which gives access to subsections and resources of the course
     - Rate the course based on 2 criteria (quality and punctuality)
  - Create Course page ( URL: /courses/create )
     - Page which allows you to create a course by fulfilling a form which is validated
- Blog page ( URL: /blog )
  - Pagination and filtering of the blogs by title and/or category
  - Three most recent blogs
  - Single Blog page ( URL: /blog/:id )
     - Show detailed blog info
     - Comment on blog
     - Like blog
  - Create Blog page ( URL: /blog/create )
     - Page which allows you to create a blog by fulfilling a form which is validated
- Admin Panel ( URL: /admin)
  - There are 6 different pages which can be navigated to from the navigation bar. There are forms containing all of the results of the pages which can be deleted/updated.
- Post details page ( URL: /#/catalog/post-id-here )
  - Add item to cart (unable to add if is owner of post or post already in cart)
- Cart page ( URL: /#/cart )
  - Display the cart items (stored in local storage)
  - Manage state and change detection manually
