(ns scrambles.ui.core
  (:require [re-frame.core :as re-frame]
            [reagent.dom :as rdom]
            [reagent.core :as reagent]
            [secretary.core :as secretary :refer-macros [defroute]]
            [scrambles.ui.events :as events]))

(defonce form-data
         (reagent/atom
           {:letters {:value "" :valid? true :visited? false}
            :word    {:value "" :valid? true :visited? false}}))

(defn reset-form-values
  [data]
  (re-frame/dispatch [::reset-query-result])
  (reset! data {:letters {:value "" :valid? true :visited? false}
                :word    {:value "" :valid? true :visited? false}}))

(defn- submit-form
  [event]
  (.preventDefault event)
  ;; todo add data for submission
  (re-frame/dispatch [::events/submit-data (get-new-query) {}]))

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
                              (assoc-in [:class] (if (:valid? (field-key @data)) "valid" "error"))
                              (assoc-in [:placeholder] (str "Enter " title " Address"))
                              (assoc-in [:value] (:value (field-key @data)))
                              (assoc-in [:on-change] #(field-change-handler field-key (-> % .-target .-value)))
                              (assoc-in [:on-blur] #(check-and-save! field-key (-> % .-target .-value) true)))]
      [:div.input-field
       [:label.label {:for (:id html-attributes)} title]
       [:input.input-field__input html-attributes]
       (let [{:keys [visited? valid?]} (field-key @data)]
         (when (and visited? (not valid?))
           [:div.field-error (get-in @data [field-key :message])]))])))

(defn scramble-form-view []
  [:div.ui.container
   [:form]]
  [:form {:id "scramble-form"}
   :on-submit submit-form
   :on-reset #(reset-form-values form-data)
   [:div.form-group
    [:div.field-group {:style {:margin "30px"}}

     [my-input-field "Letters" {:type "text" :id "letters-field"} form-data :letters]
     [my-input-field "Word" {:type "text" :id "word-field"} form-data :word]
     [:div.button-group
      [:button.button
       {:type     "submit"} "Scramble?"]
      [:button.button
       {:type     "reset"} "Reset"]]]]])


(defn routes
  "The routes in this app."
  []
  (defroute "/" [_] [scramble-form-view]))


;;; Bootstrap the CLJS frontend by rendering the app.
;;;
;;; Calls the rdom/render function to render the `app` function on the html element with id `app`.
(defn ^:dev/after-load start
  []
  (rdom/render [app] (.getElementById js/document "app")))

;;; Initialisation of the app.
;;;
;;; This method is called first, initialises the router and db. After router and db are initialised the rendering is
;;; started.
(defn ^:export init                                         ; ^:export ensures the function name is not truncated by the Closure compiler.
  []
  (re-frame/dispatch-sync [:initialise-db])                 ; `dispatch-sync` to ensure db is initialised before the app is started.
  (routes)
  (start))

