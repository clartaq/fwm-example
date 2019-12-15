(ns fwm-example.server.handler
  (:require
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [compojure.core :refer [defroutes GET]]
    [compojure.route :refer [not-found]]
    [fwm-example.server.util.periodic-tasks :as periodic]
    [fwm-example.server.util.date-time :as dt]
    [org.httpkit.server :refer [on-receive on-close send! with-channel]]
    [ring.middleware.resource :refer [wrap-resource]]
    [ring.middleware.reload :refer [wrap-reload]]))

(defonce ws-client (atom nil))
(defonce updates-sent (atom 0))
(def my-timer (atom nil))

(def starting-prefs {:body-margin    "2rem"
                     :h4-line-height "1.75rem"
                     :time-str       (dt/formatted-now)
                     :time-color     "green"})

(def colors ["cyan" "darkorange" "red" "green" "blue" "black" "brown" "orange"
             "darkorchid" "chartreuse" "lightblue" "darkpink" "plum" "indigo"])

(defn random-color []
  (get colors (rand-int (count colors))))

(defn send-updates []
  (when @ws-client
    (swap! updates-sent inc)
    (let [data (merge {:time-str (dt/formatted-now)}
                      (when (zero? (mod @updates-sent 3))
                        {:time-color (random-color)}))]
      (send! @ws-client (pr-str {:message {:command "hey-client/accept-this-update"
                                           :data    data}})))))

(defn mesg-received [msg]
  (let [message-map (edn/read-string msg)
        command (get-in message-map [:message :command])
        data (get-in message-map [:message :data])]

    (cond

      (= command "hey-server/send-preferences")
      (do
        (reset! my-timer (periodic/timer "fwm-example"))
        (periodic/run-task! send-updates :by @my-timer :period 1000)
        (send! @ws-client (pr-str {:message {:command "hey-client/accept-these-preferences"
                                             :data    starting-prefs}}))))))

(defn websocket-handler [req]
  (with-channel req channel
                (reset! ws-client channel)
                (on-receive channel #'mesg-received)
                (on-close channel (fn [status]
                                    (println (str channel ", closed, status" status)
                                    (reset! ws-client nil)
                                    (when @my-timer
                                      (periodic/cancel-task! @my-timer)
                                      (reset! my-timer nil)))))))

(defroutes routes
           (GET "/ws" [] websocket-handler)
           (GET "/" [] (io/resource "public/index.html"))
           (not-found "<p>Page not found.</p>"))

(def all-routes (wrap-reload (wrap-resource routes "public")))
