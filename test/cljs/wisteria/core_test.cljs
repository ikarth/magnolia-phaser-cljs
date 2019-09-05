(ns wisteria.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [wisteria.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
