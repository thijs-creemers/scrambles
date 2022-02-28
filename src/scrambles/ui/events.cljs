;;;; Database
;;;;
;;;; This module contains the `:initialise-db` event.

(ns scrambles.ui.events
  (:require [day8.re-frame.http-fx]
           [ajax.core :as ajax]
           [re-frame.core :as re-frame]))


;;; The server url for the API service to use.
(defonce api-url "http://localhost:8000/api/v1")

(re-frame/reg-event-db
  ::reset-query-result
  (fn [db _]
    (-> db
        (assoc-in [:data :result] nil)
        (assoc-in [:data :letters] "")
        (assoc-in [:data :word] "")
        (assoc-in [:data :message] ""))))


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
  ::submit-edit-form
  (fn
    [_ [_ data]]
    {:http-xhrio {:method          :post
                  :uri             (str api-url "/scramble")
                  :params          data
                  :timeout         5000
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success      [:submit-edit-form-success]
                  :on-failure      [:submit-edit-form-failed]}}))


;; Request handler for successful requests
(re-frame/reg-event-db
  :submit-edit-form-success
  (fn
    [db [_ data]]
    (prn data)
    (-> db
        (assoc-in [:data] data))))


;; Request handler for successful requests
(re-frame/reg-event-db
  :submit-edit-form-failed
  (fn
    [db [_ data]]
    (-> db
        (assoc-in [:data] (:response data)))))
