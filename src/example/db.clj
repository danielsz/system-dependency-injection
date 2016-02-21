(ns example.db
  (:refer-clojure :exclude [list])
  (:require [yesql.core :refer [defqueries]]
            [system.components.jdbc])
  (:import [system.components.jdbc JDBCDatabase]))

(defqueries "sql/queries.sql")

(defprotocol DemoPersistence
  (list [this] "list all directors")
  (save [this name] "save a director")
  (delete [this name] "delete a director"))

(extend-type JDBCDatabase
  DemoPersistence
  (list [this]
    (directors {} {:connection (:db-spec this)}))
  (save [this name]
    (save-director<! {:name name} {:connection (:db-spec this)}))
  (delete [this name]
    (delete-director! {:name name} {:connection (:db-spec this)})))
