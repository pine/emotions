# emotions

## Getting started

## Deployment

```sh
$ ./gradlew build
$ heroku apps:create your-app
$ heroku plugins:install java
$ heroku config:set SPRING_PROFILES_ACTIVE=prod
$ heroku config:set TZ=Asia/Tokyo
$ heroku deploy:jar --jar app/build/libs/app.jar
```

## Tools
### Gravatar
Please try the following commands after set `GRAVATAR_EMAIL`, `GRAVATAR_PASSWORD` environment variables.

```
$ ./gradlew :tool-gravatar:bootRun
```

## License
MIT &copy; [Pine Mizune](https://profile.pine.moe/)
