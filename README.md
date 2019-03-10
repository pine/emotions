# emotions &nbsp;[![CircleCI](https://circleci.com/gh/pine/emotions/tree/master.svg?style=shield&circle-token=9aff416dfaefc86bc002a0b7630386b32079fd88)](https://circleci.com/gh/pine/emotions/tree/master)
:smile: Change avatar's emotion everyday.
<br>
<br>

## Getting started

```sh
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
