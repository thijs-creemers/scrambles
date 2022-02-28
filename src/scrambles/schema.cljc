(ns scrambles.schema)

(def LowercaseString
  [:and
   string?
   [:re
    {:error/message "String must be in lowercase characters."}
    #"^[a-z]+$"]])