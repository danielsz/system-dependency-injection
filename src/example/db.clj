(ns example.db
  (:refer-clojure :exclude [list])
  (:require [yesql.core :refer [defqueries]]
            [system.components.jdbc])
  (:import [system.components.jdbc JDBCDatabase]))

(defqueries "sql/queries.sql")

(defprotocol DemoPersistence
  (list [this] "list all users")
  (save [this name] "save a user")
  (delete [this name] "get a video by id"))

(extend-type JDBCDatabase
  DemoPersistence
  (list [this]
    (directors {} {:connection (:db-spec this)}))
  (save [this name]
    (save-director<! {:name name} {:connection (:db-spec this)}))
  (delete [this name]
    (delete-director! {:name name} {:connection (:db-spec this)})))
