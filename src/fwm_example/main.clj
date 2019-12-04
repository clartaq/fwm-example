(ns fwm-example.main
  (:require [fwm-example.server :as server]))

(defn start []
  (println "start")
  (server/start-server))

(defn stop []
  (println "stop"))

(defn dev-main []
  (println "dev-main")
  (start))

(defn -main []
  (println "main"))
