(ns scrambles.core-test
  (:require [clojure.test :refer [deftest is testing run-tests]]
            [scrambles.core :as scrambles]))
;; [malli.generator :as mg]


(deftest scramble-test
  (testing "Happy path: Check that word can be formed from letters."
    (is (scrambles/scramble? "ashxxlloh" "hallo"))
    (is (scrambles/scramble? "rekqodlw" "world"))
    (is (scrambles/scramble? "cedewaraaossoqqyt" "codewars")))

  (testing "Happy path: Scrables that don't fit (return false)"
    (is (not  (scrambles/scramble? "katas" "steak")))
    (is (not  (scrambles/scramble? "grwtuoswzs" "wootw"))))

  (testing "Testing handling of incorrect input"
    (is (thrown? IllegalArgumentException (scrambles/scramble? "grwtuoswzs" "Groot")))
    (is (thrown? IllegalArgumentException (scrambles/scramble? "roewhrkrj2enlrkfdn2eheeeeeeeeeeeeeeee" "wereld")))))

(comment
  (run-tests)
  ...)