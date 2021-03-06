(ns scrambles.api-server-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [scrambles.api-server :refer [app]]))

(deftest app-routes-test
  (testing "The root path"
    (is (= (app (mock/request :get "/"))
           {:status  200
            :headers {"Content-type" "text/html"}
            :body    "<h1>Scramble application task</h1>"})))

  (testing "Happy path for /api/v1/scramble, true returned"
    (is (= (app (-> (mock/request :post "/api/v1/scramble")
                    (mock/json-body {:letters "vueaeacelue" :word "value"})))
           {:status  201
            :headers {"Content-Type" "application/json; charset=utf-8"}
            :body    "{\"status\":\"success\",\"result\":true,\"message\":\"\",\"params\":[\"vueaeacelue\",\"value\"]}"})))

  (testing "Happy path for /api/v1/scramble, false returned"
    (is (= (app (-> (mock/request :post "/api/v1/scramble")
                    (mock/json-body {:letters "lvowrl" :word "world"})))
           {:status  201
            :headers {"Content-Type" "application/json; charset=utf-8"}
            :body    "{\"status\":\"success\",\"result\":false,\"message\":\"\",\"params\":[\"lvowrl\",\"world\"]}"})))

  (testing "Invalid user input"
    (is (= (app (-> (mock/request :post "/api/v1/scramble")
                    (mock/json-body {:letters "vueaeacelue" :word "val3ue"}))))

        {:body    "{\"status\":\"failed\",\"result\":false,\"message\":\"Both the letters and word strings must be in lowercase characters.\",\"params\":[\"vueaeacelue\",\"vaLUe\"]}"
         :headers {"Content-Type" "application/json; charset=utf-8"}
         :status  400})))