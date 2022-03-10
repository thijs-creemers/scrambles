(ns scrambles.ui.core
  (:require [clojure.string :as s]
            [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [reagent.dom :as rdom]
            [scrambles.ui.events :as events]
            [scrambles.ui.subscriptions :as subscriptions]
            [secretary.core :refer-macros [defroute]]))


(defonce form-data
         (reagent/atom
           {:letters {:value "" :valid? true :visited? false}
            :word    {:value "" :valid? true :visited? false}}))


(defn reset-form-values
  [data]
  (re-frame/dispatch [::events/reset-query-result])
  (reset! data {:letters {:value "" :valid? true :visited? false}
                :word    {:value "" :valid? true :visited? false}}))


(defn- submit-form
  [event]
  (prn (str "FORM-DATA" @form-data))
  (.preventDefault event)
  (re-frame/dispatch [::events/submit-edit-form {:letters (get-in @form-data [:letters :value])
                                                 :word    (get-in @form-data [:word :value])}]))


(defn check-and-save!
  [field-key value set-visited?]
  (if set-visited?
    (swap! form-data update-in [field-key] assoc :message nil :valid? true :visited? true :value (s/trim value))
    (swap! form-data update-in [field-key] assoc :message nil :valid? true :value (s/trim value))))


(defn field-change-handler
  [field-key value]
  (swap! form-data assoc-in [field-key :value] value))

(defn my-input-field
  "Render an input field"
  [title form-spec data field-key]
  (fn []
    (let [html-attributes (-> form-spec
                              (assoc-in [:style] {:margin-left "20px"})
                              (assoc-in [:class] (if (:valid? (field-key @data)) "valid" "error"))
                              (assoc-in [:placeholder] (str "Enter " title))
                              (assoc-in [:value] (:value (field-key @data)))
                              (assoc-in [:on-change] #(field-change-handler field-key (-> % .-target .-value)))
                              (assoc-in [:on-blur] #(check-and-save! field-key (-> % .-target .-value) true)))]
      [:div.ui.ribbon.label
       [:label {:for (:id html-attributes)} title]
       [:div.ui.big.input
        [:input html-attributes
         (let [{:keys [visited? valid?]} (field-key @data)]
           (when (and visited? (not valid?))
             [:div.field-error (get-in @data [field-key :message])]))]]])))


(defn scramble-form-view []
  [:div.ui.container
   [:div.ui.horizontal.divider]
   [:div.ui.header "Scrambles"]
   [:div.ui.horizontal.divider]
   [:form {:id        "scramble-form"
           :on-submit submit-form
           :on-reset  #(reset-form-values form-data)}
    [:div.content
     [my-input-field "Letters" {:type "text" :id "letters-field"} form-data :letters]
     [my-input-field "Word" {:type "text" :id "word-field"} form-data :word]]

    [:div.ui.horizontal.divider]

    [:div.content
     [:button.ui.primary.button {:type "submit"} "Scramble?"]
     [:button.ui.button {:type "reset"} "Reset"]]]
   [:div.ui.horizontal.divider]
   [:div.content
    [:div.ui.header "Scramble result"]
    (let [results @(re-frame/subscribe [::subscriptions/scramble-results])]
      [:table
       [:tbody
        [:tr [:td "Status"] [:td (:status results)]]
        [:tr [:td "Result"] [:td (if (:result results) "true" "false")]]
        [:tr [:td "Message"] [:td (:message results)]]]])]])


(defn routes
  "The routes in this app."
  []
  (defroute "/" [_] [scramble-form-view]))


;;; Bootstrap the CLJS frontend by rendering the app.
;;;
;;; Calls the rdom/render function to render the `app` function on the html element with id `app`.
(defn ^:dev/after-load start
  []
  (rdom/render [scramble-form-view] (.getElementById js/document "app")))

;;; Initialisation of the app.
;;;
;;; This method is called first, initialises the router and db. After router and db are initialised the rendering is
;;; started.
(defn ^:export init                                         ; ^:export ensures the function name is not truncated by the Closure compiler.
  []
  (re-frame/dispatch-sync [:initialise-db])                 ; `dispatch-sync` to ensure db is initialised before the app is started.
  (routes)
  (start))

