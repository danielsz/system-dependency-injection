(ns example.db
  (:require [yesql.core :refer [defqueries]]))

(defqueries "sql/queries.sql")


