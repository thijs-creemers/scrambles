(ns scrambles.core
  (:require [clojure.string :as str]
            [malli.core :as m]
            [scrambles.schema :refer [LowercaseString]]))

(defn scramble?
  "Task 1
   Complete the function (scramble str1 str2) that 
   returns true if a portion of str1 characters can be 
   rearranged to match str2, otherwise returns false

   Notes:
   Only lower case letters will be used (a-z). No punctuation or digits will be included.
   Performance needs to be considered"
  [letters word]
  (if (and (m/validate LowercaseString letters) (m/validate LowercaseString word))

    (let [letter-frequencies (frequencies (str/split letters #""))
          word-frequencies (frequencies (str/split word #""))]
      (every? true?
              (map (fn [[letter freq]]
                     (>= (get letter-frequencies letter 0) freq))
                   word-frequencies)))
    (throw (IllegalArgumentException.
             "Both the letters and word strings must be in lowercase characters."))))

(comment
  (scramble? "roewhrkrj2enlrkfdn2eheeeeeeeeeeeeeeee" "wereld") ;; => throws exception
  (scramble? "rekqodlw" "world")                            ;; => true
  (scramble? "cedewaraaossoqqyt" "codewars")                ;; => true
  (scramble? "katas" "steak")                               ;; => false
  (scramble? "grwtuoswzs" "Groot")                          ;; => throws exception
  (scramble? "grwtuoswzs" "wootw")                          ;; => false

  (m/validate LowercaseString "ab")
  (m/validate LowercaseString "hallo16")
  (m/explain LowercaseString "Hallo")


  ...)