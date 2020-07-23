# EPAM Test Task

Copyright &copy; 2020, Alexander Valdez

This is a REST/JSON service using MongoDB and WebFlux with the following functions:

* Submit a blog post with their name (unique nickhandle), title and content with the publish date
* Update the blog post
* View content posted by other users by their nicks ordered by creation time (newest first)
* Comment on any post in a flat structure. 

## API

The service understands the following APIs:

| URI                      | Method | Description                                             |
|------------------------- | ------ | ------------------------------------------------------- | 
| /blogPosts               | POST   | submits a new blog post                                 |
| /blogPosts/{id}          | GET    | returns the post with a particular ID                   |
| /blogPosts/byUser/{user} | GET    | returns all posts by the user sorted on descending date |
| /blogPosts/update/{id}   | POST   | finds an existing post and update the title and content |
| /blogPosts/comment/{id}  | POST   | adds a comment to an existing post                      |
| /blogPosts               | GET    | retrieves all posts in the database [^note]             |
| /actuator/health         | GET    | rudimentary health check                                |

[^note]: This shouldn't be in production code but was useful for debugging 

## TODOs

* Add security
* Sort comments by date
* Add some validation e.g. title and content can't be null, etc.
