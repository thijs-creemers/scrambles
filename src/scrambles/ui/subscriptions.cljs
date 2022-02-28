(ns scrambles.ui.subscriptions
  (:require [re-frame.core :as re-frame]))

(defn scramble-results
  "Fetch scramble results"
  [db _]
  (get-in db [:data]))

(re-frame/reg-sub ::scramble-results scramble-results)