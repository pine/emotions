# emotions &nbsp;[![CircleCI](https://circleci.com/gh/pine/emotions/tree/master.svg?style=shield&circle-token=9aff416dfaefc86bc002a0b7630386b32079fd88)](https://circleci.com/gh/pine/emotions/tree/master) [![codecov](https://codecov.io/gh/pine/emotions/branch/master/graph/badge.svg)](https://codecov.io/gh/pine/emotions)
:smile: Change avatar's emotion everyday.
<br>
<br>


## Requirements

- JDK 11

## Libraries

- Spring Boot 2.x

## Getting started

```sh
$ cp app/src/main/resources/application-sample.yml app/src/main/resources/application-local.yml
$ vim app/src/main/resources/application-local.yml

$ ./gradlew :app:bootRun
```

## Supported services

- Gravatar
- Slack
- Twitter

## Deployment

```sh
$ ./gradlew build
$ heroku apps:create your-app
$ heroku plugins:install java
$ heroku config:set SPRING_PROFILES_ACTIVE=prod
$ heroku config:set TZ=Asia/Tokyo
$ heroku config:set 'JAVA_OPTS=-verbose:gc -Xlog:gc* -XX:+UseStringDeduplication'
$ heroku deploy:jar --jar app/build/libs/app.jar --jdk 11
```

## Development
### JDK
For macOS users.

```
$ brew tap adoptopenjdk/openjdk
$ brew cask install adoptopenjdk11
```

### Gravatar
You should set `gravatar.images` properties when the app runs.

Please try the following commands after set `GRAVATAR_EMAIL`, `GRAVATAR_PASSWORD` environment variables.

```
$ ./gradlew :tool-gravatar:bootRun
```

## License
MIT &copy; [Pine Mizune](https://profile.pine.moe/)
