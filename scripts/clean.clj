(require
  '[clojure.java.io :as io])

(def out-dir "resources/public/cljs-out")

;; Stolen from prod.clj in the full-stack-clj-example
;; https://github.com/oakes/full-stack-clj-example
(defn delete-children-recursively! [f]
      (when (.isDirectory f)
            (doseq [f2 (.listFiles f)]
                   (delete-children-recursively! f2)))
      (when (.exists f) (io/delete-file f)))

(delete-children-recursively! (io/file out-dir))
(System/exit 0)