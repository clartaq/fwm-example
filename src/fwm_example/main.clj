(ns fwm-example.main
  (:require [fwm-example.server :as server])
  (:gen-class))

(defn start []
  (println "start")
  (server/start-server))

(defn stop []
  (println "stop"))

(defn dev-main []
  (println "dev-main")
  (start))

(defn -main []
  (println "main")
  (start))
