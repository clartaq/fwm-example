(ns fwm-example.handler
  (:require
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [clojure.pprint :as pprint]
    [compojure.core :refer [defroutes GET]]
    [compojure.route :refer [not-found]]
    [org.httpkit.server :refer [on-receive on-close send! with-channel]]
    [ring.middleware.resource :refer [wrap-resource]]
    [ring.middleware.reload :refer [wrap-reload]]))

(def ws-client (atom nil))

(defn mesg-received [msg]
  (println "mesg-received: msg: " msg)
  (let [message-map (edn/read-string msg)
        command (get-in message-map [:message :command])
        data (get-in message-map [:message :data])]
    (println "command: " command)
    (println "data: " data)

    (cond

      (= command "hey-server/send-preferences")
      (send! @ws-client (pr-str {:message {:command "hey-client/accept-these-preferences"
                                           :data    {:body-margin "2rem"
                                                     :h4-line-height "1.75rem"
                                                     :time-str "11:11:11pm"
                                                     :time-color "green"}}})))))

(defn websocket-handler [req]
  (with-channel req channel
                (reset! ws-client channel)
                (on-receive channel #'mesg-received)
                (on-close channel (fn [status]
                                    (reset! ws-client nil)
                                    (println (str channel "closed, status" status))))))

(defroutes routes
           (GET "/ws" [] websocket-handler)
           (GET "/" [] (io/resource "public/index.html"))
           (not-found "<p>Page not found.</p>"))

(def all-routes (wrap-reload (wrap-resource routes "public")))
