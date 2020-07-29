(ns ^:figwheel-hooks fwm-example.client.core
  (:require [cljs.core.async :refer [chan close! <! >!]]
            [clojure.edn :as edn]
            [fwm-example.client.layout :as layout]
            [fwm-example.client.ws :refer [start-ws!]]
            [goog.dom :as gdom]
            [reagent.core :as r :refer [atom]]
            [reagent.dom :as rdom])
  (:require-macros [cljs.core.async.macros :refer [go]]))

;; Just used to demonstrate testing.
(defn square [a] (* a a))

(println "This text is printed from src/fwm_example/core.cljs. Go ahead and edit it and see reloading in action.")

;; Define your app data so that it doesn't get over-written on reload.
(defonce app-state (r/atom {:update-num 0}))

;; A channel used to retrieve the preferences asynchronously when it becomes
;; available from the websocket.
(def ^{:private true} got-prefs-channel (chan))

(defn increment-update-num []
  (swap! app-state #(update % :update-num inc)))

(defn ws-message-handler
  "Handle messages received from the server."
  [e]
  (let [message-map (edn/read-string (.-data e))
        message-command (get-in message-map [:message :command])
        message-data (get-in message-map [:message :data])]

    (cond

      (= message-command "hey-client/accept-these-preferences")
      (do
        ;; Upon loading the initial render function is waiting on the
        ;; preferences before it lays out the page since the layout
        ;; is dependent on items stored in the preferences.
        (go (>! got-prefs-channel message-data))
        (increment-update-num))

      (= message-command "hey-client/accept-this-update")
      (do
        (swap! app-state merge @app-state message-data)
        (increment-update-num)))))

(defn on-ws-open
  "Handle the notification that the WebSocket has been opened."
  []
  ((:send-message-fn @app-state)
   (pr-str {:message {:command "hey-server/send-preferences"
                      :data    ""}})))

(defn reload [el]
  (let [ws-functions (start-ws! {:on-open-fn    on-ws-open
                                 :on-error-fn   nil
                                 :on-message-fn ws-message-handler})]
    (swap! app-state merge ws-functions)

    ;; Wait for the preferences to be obtained from the server since they will
    ;; affect the rendering.
    (go
      (let [prefs (<! got-prefs-channel)]
        (swap! app-state merge @app-state prefs)
        (close! got-prefs-channel)
        (rdom/render [layout/home app-state] el)))))

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
