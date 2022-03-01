FROM clojure:openjdk-18-tools-deps-alpine

# Make a directory
RUN mkdir -p /app
WORKDIR /app

RUN apk add rlwrap

# Copy only the target jar over
COPY scrambles.jar .

# Open the port
EXPOSE 8000

# Run the JAR
CMD java -cp scrambles.jar clojure.main -m scrambles.api-server

# docker build --tag scrambles -f Dockerfile .
# docker run --name scrambles-container --env EV=true scrambles -p 8000:8000 -rm scrambles
