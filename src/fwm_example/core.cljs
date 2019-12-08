(ns ^:figwheel-hooks fwm-example.core
  (:require [cljs.core.async :refer [chan close! <! >!]]
            [fwm-example.layout :as layout]
            [fwm-example.ws :refer [start-ws!]]
            [goog.dom :as gdom]
            [reagent.core :as r :refer [atom]]
            [clojure.edn :as edn])
  (:require-macros [cljs.core.async.macros :refer [go]]))

;; Just used to demonstrate testing.
(defn multiply [a b] (* a b))

(println "This text is printed from src/fwm_example/core.cljs. Go ahead and edit it and see reloading in action.")

;; Define your app data so that it doesn't get over-written on reload.
(defonce app-state (atom {:time-str   "00:00:00am"
                          :time-color "blue"
                          :update-num 0}))

;; A channel used to retrieve the preferences asynchronously when it becomes
;; available from the websocket.
(def ^{:private true} got-prefs-channel (chan))

(defn increment-update-num []
  (swap! app-state #(update % :update-num inc)))

(defn ws-message-handler
  "Handle messages received from the server."
  [e]
  (let [message-map (edn/read-string (.-data e)) ;(du/event-data e))
        message-command (get-in message-map [:message :command])
        message-data (get-in message-map [:message :data])]

    (cond

      (= message-command "hey-client/accept-these-preferences")
      (do
        (println "Client has received the preferences")
        ;; On reload, the initial render function is waiting on the
        ;; preferences before it lays out the page since the layout
        ;; is dependent on items stored in the preferences.
        (go (>! got-prefs-channel message-data))
        (increment-update-num))

      (= message-command "hey-client/accept-this-update")
      (do
        (println "Client has received an update")
        ;(debug "Saw message-command \"hey-client/accept-this-outline\"")
        ;(debugf "(:outline message-data): %s" (:outline message-data))
        (swap! app-state merge @app-state message-data) ; :current-outline (:outline message-data))
       ; (swap! app-state update-in [:update-num] inc)
       ; (swap! app-state #(update % :update-num inc))
        (increment-update-num)

        ;(swap! m1 #(-> %
        ;               (update :counter2 inc)
                       ;(debugf "@(state-ratom): %s" @(state-ratom)))
      ))))

(defn on-ws-open
  "Handle the notification that the WebSocket has been opened."
  []
  (println "on-ws-open: about to send request for preferences")
  ((:send-message-fn @app-state)
   (pr-str {:message {:command "hey-server/send-preferences"
                      :data    ""}})))

(defn reload [el]
  (println "app-state: " app-state)
  (let [ws-functions (start-ws! {:on-open-fn    on-ws-open
                                 :on-error-fn   nil
                                 :on-message-fn ws-message-handler})]
    (swap! app-state merge ws-functions)
    (println "ws-functions: " ws-functions)

    ;; Wait for the preferences to be obtained from the server since they will
    ;; affect the rendering.
    (println "About to go for blocking on prefs")
    (go
      (let [prefs (<! got-prefs-channel)]
        (println "got prefs: " prefs)
        (swap! app-state merge @app-state prefs)
        ;(swap! app-state assoc :preferences prefs)
        (close! got-prefs-channel)
        (println "app-state: " app-state)
        (r/render [layout/home app-state] el)))))

(defn mount-app-element []
  (when-let [el (gdom/getElement "app")]
    (reload el)))

;; Conditionally start your application based on the presence of an "app"
;; element. This is particularly helpful for testing this ns without launching
;; the app.
(mount-app-element)

;; Specify reload hook with ^;after-load metadata.
(defn ^:after-load on-reload []
  (mount-app-element)
  ;; Optionally touch your app-state to force re-rendering depending on
  ;; your application.
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )
