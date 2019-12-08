(ns fwm-example.server
  (:gen-class)
  (:require [fwm-example.handler :refer [all-routes]]
            [org.httpkit.server :as http-kit]))

;;; Just used to demonstrate testing.
(defn multiply [a b] (* a b))

(def default-port 3000)

(defonce ^{:private true} web-server_ (atom nil))

(defn stop-server! []
  (println "Stopping web server.")
  (when-let [stop-fn @web-server_]
    (println "stop-fn: " stop-fn)
    (stop-fn)))

(defn start-server! [& [port]]
  (stop-server!)
  (println "Starting web server.")
  (let [http-port (or port default-port)
        wrapped-routes all-routes
        [port-used stop-fn] (let [stop-fn (http-kit/run-server
                                            wrapped-routes
                                            {:port http-port})]
                              [(:local-port (meta stop-fn))
                               (fn [] (stop-fn :timeout 100))])
        uri (format "http://localhost:%s/" port-used)]

    (println "http-port: " http-port)
    (println "Web server is running at: " uri)
    (reset! web-server_ stop-fn)))


