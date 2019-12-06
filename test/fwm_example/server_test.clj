(ns fwm-example.server-test
  (:require [clojure.test :refer :all]
            [fwm-example.server :refer [multiply]]))

(deftest multiply-test
  (testing "The multiply function"
    (is (= 35 (multiply 5 7)))
    (is (not= -17 (multiply 3 11)))))

(deftest another-multiply-test
  (testing "The multifply function to death."
    (is (= 9409 (multiply 97 97)))))
