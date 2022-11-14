FROM eclipse-temurin:17 as builder
WORKDIR /project
COPY . ./
RUN ./gradlew :app:bootJar

FROM eclipse-temurin:17-jre-alpine
WORKDIR /project
COPY --from=builder /project/app/build/libs/app.jar ./
CMD ["java", "-XX:+ExitOnOutOfMemoryError", "-XX:+UseCompressedOops", "-XX:+UseStringDeduplication", "-XX:-OmitStackTraceInFastThrow", "-Djava.security.egd=file:/dev/./urandom", "-Dlog4j2.formatMsgNoLookups=true", "-jar", "/project/app.jar"]
