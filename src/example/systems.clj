(ns example.systems
  (:require 
   [system.core :refer [defsystem]]
   [com.stuartsierra.component :as component]
   [ring.adapter.jetty :refer [run-jetty]]
   (system.components 
    [h2 :refer [new-h2-database DEFAULT-MEM-SPEC DEFAULT-DB-SPEC]]
    [http-kit :refer [new-web-server]]
    [app :refer [new-app]])
   [example.handler :refer [routes app]]
   [example.db :refer [create-table!]]
   [environ.core :refer [env]]))

(defn dev-system []
  (component/system-map
   :db (new-h2-database DEFAULT-MEM-SPEC)
   :app (component/using
         (new-app routes #'app (fn [db] (create-table! (:db-spec db))))
         [:db])
   :web (component/using
         (new-web-server (Integer. (env :http-port)))
         {:handler :app})))

(defn prod-system []
  (component/system-map
   :db (new-h2-database DEFAULT-DB-SPEC)
   :app (component/using
         (new-app (routes #'app (fn [db] (create-table! (:db-spec db)))))
         [:db])
   :web (component/using
         (new-web-server (Integer. (env :http-port)))
         {:handler :app})))

