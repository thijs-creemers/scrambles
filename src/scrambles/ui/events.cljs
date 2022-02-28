;;;; Database
;;;;
;;;; This module contains the `:initialise-db` event.

(ns scrambles.ui.events
  (:require [day8.re-frame.http-fx]
           [ajax.core :as ajax]
           [re-frame.core :as re-frame]))


;;; The server url for the API service to use.
(defonce api-url "https://localhost:8000/api/v1/scrambles")

;;; Initialisation of the app-db with default data.
(re-frame/reg-event-db
  :initialise-db
  (fn
    [_ _]
    {:data {:status "success",
            :message "",
            :params [:letters "" :word ""]}}))

;;; Fetch the data from the API path and store it in [:data path] in our state DB.
(re-frame/reg-event-fx
  :submit-data
  (fn
    [_ [_ data]]
    {:http-xhrio {:method          :post
                  :uri             (str api-url path)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success      [:submit-data-success id-field key]
                  :on-failure      [:submit-data-failed key]}}))

;; Request handler for successful requests
(re-frame/reg-event-db
  :submit-data-success
  (fn
    [db [_ id-field key data]]
    (-> db
        (assoc-in [:data key] (into {} (map (fn [row]
                                              [(id-field row) row])) data)))))

;; Request handler for successful requests
(re-frame/reg-event-db
  :submit-data-failed
  (fn
    [db [_ key _]]
    (assoc-in db [:data key "0"] {:id "0" :name (str "Could not load " (name key))})))
