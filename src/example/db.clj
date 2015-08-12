(ns example.db
  (:require [yesql.core :refer [defquery defqueries]]))

(defqueries "sql/queries.sql")

