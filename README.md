# emotions &nbsp;[![CircleCI](https://circleci.com/gh/pine/emotions/tree/main.svg?style=shield&circle-token=9aff416dfaefc86bc002a0b7630386b32079fd88)](https://circleci.com/gh/pine/emotions/tree/main) [![codecov](https://codecov.io/gh/pine/emotions/branch/master/graph/badge.svg)](https://codecov.io/gh/pine/emotions)
:smile: Change avatar's emotion everyday.

![](images/resized.jpg)<br>
<sup><sup>&copy; shihina/123RF.COM</sup></sup>
<br>
<br>

## Requirements

- JDK 17 or later

## Libraries

- Spring Boot 2
- Spring Batch

## Supported services

- [Bookmeter](https://bookmeter.com/)
- [Gravatar](https://gravatar.com/)
  - [GitHub](https://github.com/)
  - [Qiita](https://qiita.com/)
- [Twitter](https://twitter.com)

## Development
### JDK
For macOS users.

```
$ brew install --cask temurin17
```

### Gravatar
You should set `gravatar.images` properties when the app runs.

Please try the following commands after set `GRAVATAR_EMAIL`, `GRAVATAR_PASSWORD` environment variables.

```
$ ./gradlew :tool-gravatar:bootRun
```

### Encrypt credentials with Jasypt
To encrypt plain text with Jasypt, please use the command below.

```sh
$ bin/encrypt.sh \
    algorithm=PBEWITHHMACSHA512ANDAES_256 \
    ivGeneratorClassName=org.jasypt.iv.RandomIvGenerator \
    password=<password> \
    input=<input>
```

### Run Spring Batch's job
`<job_name>` is defined in [BatchConfiguration.java](app/src/main/java/moe/pine/emotions/app/config/BatchConfiguration.java).

#### Using Gradle

```bash
$ ./gradlew :app:bootRun --args='--spring.batch.job.names=<job_name>'
 ```

#### Using .jar file

```bash
$ ./gradlew :app:bootJar
$ java -jar -jar app/build/libs/app.jar \
    --jasypt.encryptor.password=<password> \
    --spring.batch.job.names=<job_name>
```

## License
MIT &copy; [Pine Mizune](https://profile.pine.moe/)
