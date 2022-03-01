# Scramblies challenge

## Prerequisites
```bash
brew install clojure yarn  # installs the clojure and yarn packages
yarn install               # installs all npm dependencies
```
## Install environment

```bash
git clone git@github.com:thijs-creemers/scrambles.git

cd scrambles
yarn clean
```
This installs a clean set of node npm modules and compiles the css.

## Run server unit-tests
```bash
clj -X:test
```
-or-

```bash
yarn server-test
```

## Start the api-server for development
- Start and connect to the REPL.
- load the namespace scrambles.api-server.
- start the server with (-main)
- The server can be stopped wit (stop-server)


## Start the api-server through clojure cli
```bash
clj -M:server
```

## Build an uberjar for the sever

```bash
clojure -X:uberjar :jar scrambles.jar
```
-or-

```bash
yarn build-uberjar
```

## Run the server
```bash
java -cp srcambles.jar clojure.main -m scrambles.api-server
```
-or-
```bash
yarn api-server
```

## Compile and run the dev server for the frontend
```bash
yarn dev
```
Wait a few moments for the compilation to complete and go the your browser and open
the app with the url: 'http://127.0.0.1:8101'.

## Create a optimized runtime version of the frontend app 
```bash
yarn release
```

## To be done
- create and test build env for the api-server
- more tests
