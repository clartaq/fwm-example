(ns fwm-example.layout
  (:require [reagent.core :as r]))

(defn home [state]
  (fn [state]
    [:div {:style {:margin (:body-margin @state)}}
     [:h4 {:style {:line-height (:h4-line-height @state)}}
      "The server says the time is:"
      [:br]
      [:span {:style {:color (:time-color @state)}} (str (:time-str @state))]]
     [:p "This is update: " (:update-num @state)]]))
