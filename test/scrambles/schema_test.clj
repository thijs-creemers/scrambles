(ns scrambles.schema-test
  (:require [clojure.test :refer :all]
            [scrambles.schema :as schema]
            [malli.core :as m]))


(deftest LowercaseString
  (testing "check that all lowercase strings are valid"
    (is (m/validate schema/LowercaseString "ab")))

  (testing "check that numbers, uppercase chars and punctuation are not allowed"
    (is (not (m/validate schema/LowercaseString "hello16")))
    (is (not (m/validate schema/LowercaseString "hello dude")))
    (is (not (m/validate schema/LowercaseString "CamelCase")))
    (is (not (m/validate schema/LowercaseString "all-lowercase=>with<=punctuation."))))

  (testing "check that numbers are not allowed"
    (is (not (m/validate schema/LowercaseString "hallo16")))))

(comment
  (run-tests)
  ...)