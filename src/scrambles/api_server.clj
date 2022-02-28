(ns scrambles.api-server
  (:gen-class)
  (:require [compojure.core :refer [GET POST defroutes]]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [scrambles.core :refer [scramble?]]))

(defonce ^:private api-server (atom nil))

(defn process-scramble-request
  [req]
  (let [letters (-> req :body :letters)
        word (-> req :body :word)]
    (try
      {:status  201
       :body    {:status "success"
                 :result (scramble? letters word)
                 :params [letters word]}}
      (catch IllegalArgumentException exc
        {:status  400
         :body    {:status  "failed"
                   :message (.getMessage exc)
                   :params  [letters word]}}))))

(defroutes
  app-routes
  (GET "/" []
    {:status  200
     :headers {"Content-type" "text/html"}
     :body    "<h1>Scramble application task</h1>"})

  (POST "/api/v1/scramble" request
    (process-scramble-request request))

  (route/not-found "<h1>Page not found</h1>"))

(def app
  (-> app-routes
      wrap-json-response
      (wrap-json-body {:keywords? true})))

(defn create-server
  "A ring-based server listening to all http requests
  port is an Integer greater than 128"
  [port]
  (jetty/run-jetty app {:port port}))

(defn stop-server
  "Gracefully shutdown the server, waiting 100ms "
  []
  (when-not (nil? @api-server)
    ;; graceful shutdown: wait 100ms for existing requests to be finished
    ;; :timeout is optional, when no timeout, stop immediately
    (@api-server :timeout 100)
    (reset! api-server nil)))

(defn -main
  "Start a httpkit server with a specific port
  #' enables hot-reload of the handler function and anything that code calls"
  [& {:keys [ip port]
      :or   {ip   "0.0.0.0"
             port 8000}}]
  (println "INFO: Starting httpkit server - listening on: " (str "http://" ip ":" port))
  (reset! api-server (jetty/run-jetty #'app {:port port})))

(comment
  ;; curl -d '{"letters":"vueaeacelue", "word":"value"}' -H "Content-Type: application/json" -X POST http://localhost:8000/api/v1/scramble
  (-main)
  (stop-server)
  ...)