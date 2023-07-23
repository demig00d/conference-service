FROM amazoncorretto:17-alpine
ENV GRADLE_OPTS -Dorg.gradle.daemon=false
COPY . /build
WORKDIR /build
