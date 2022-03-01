# Use https://hub.docker.com/_/oracle-serverjre-8
FROM openjdk:11

# Make a directory
RUN mkdir -p /app
WORKDIR /app

# Copy only the target jar over
COPY scrambles.jar .

# Open the port
EXPOSE 8000

# Run the JAR
CMD /usr/local/openjdk-11/bin/java -cp srcambles.jar clojure.main -m scrambles.api-server


# docker build --tag scrambles -f Dockerfile .
# docker run --name scrambles-container --env EV=true scrambles -p 8000:8000 -rm scrambles
