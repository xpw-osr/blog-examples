FROM isahl/openjdk17 AS BUILDER
ARG GRADLE_CONFIG
ENV PROJECT_FOLDER=/root/springk8s
RUN mkdir $PROJECT_FOLDER
ADD ./gradle $PROJECT_FOLDER/gradle
ADD ./src $PROJECT_FOLDER/src
COPY ./build.gradle $PROJECT_FOLDER/build.gradle
COPY ./gradlew $PROJECT_FOLDER/gradlew
COPY ./settings.gradle $PROJECT_FOLDER/settings.gradle
RUN mkdir /root/.gradle
COPY ${GRADLE_CONFIG} /root/.gradle/
RUN ls /root/.gradle/
RUN cd $PROJECT_FOLDER && ./gradlew clean && ./gradlew build -x test

FROM isahl/openjdk17
ENV LIB_DIR=/usr/local/lib/springk8s
ENV CONFIG_DIR=/etc/springk8s/
ENV BIN_DIR=/usr/local/bin
RUN mkdir -p $LIB_DIR
RUN mkdir -p $CONFIG_DIR
COPY --from=BUILDER /root/springk8s/build/libs/springk8s-0.0.1-SNAPSHOT.jar $LIB_DIR/springk8s.jar
RUN echo "java -jar $LIB_DIR/springk8s.jar com.simplejourney.springk8s.Springk8sApplication --spring.config.location=$CONFIG_DIR" > $BIN_DIR/springk8s
RUN chmod 755 $BIN_DIR/springk8s
CMD $BIN_DIR/springk8s