FROM bellsoft/liberica-openjdk-alpine:latest
RUN apk add -U tzdata
ENV TZ America/Lima
RUN ln -snf /usr/share/zoneinfo/${TZ} /etc/localtime
RUN echo "${TZ}" > /etc/timezone
VOLUME /tmp
EXPOSE 8001
ADD ./target/ms-producto-0.0.1-SNAPSHOT.jar ms-producto.jar
ENTRYPOINT ["java", "-jar", "/ms-producto.jar"]
