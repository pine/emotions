# emotions

## Getting started

## Deployment

```sh
$ ./gradlew build 
$ heroku apps:create your-app
$ heroku plugins:install java
$ heroku config:set SPRING_PROFILES_ACTIVE=prod
$ heroku deploy:jar --jar app/build/libs/app.jar
```

## License
MIT &copy; [Pine Mizune](https://profile.pine.moe/)
