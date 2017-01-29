(ns example.systems
  (:require 
   [system.core :refer [defsystem]]
   [com.stuartsierra.component :as component]
   [example.db :refer [create-table!]]
   (system.components 
    [h2 :refer [new-h2-database DEFAULT-MEM-SPEC DEFAULT-DB-SPEC]]
    [http-kit :refer [new-web-server]]
    [endpoint :refer [new-endpoint]]
    [middleware :refer [new-middleware]]
    [handler :refer [new-handler]])
   [example.handler :refer [app-routes]]
   [ring.middleware.format :refer [wrap-restful-format]]
   [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
   [environ.core :refer [env]]))

(defn select-database [env]
  (let [dbs {"default-mem-spec" DEFAULT-MEM-SPEC
             "default-db-spec" DEFAULT-DB-SPEC}]
    (get dbs (env :db) DEFAULT-MEM-SPEC)))

(defn base-system []
  (component/system-map
   :db (new-h2-database (select-database env) #(create-table! {} {:connection %}))
   :routes (component/using
              (new-endpoint app-routes)
              [:db])
   :middleware (new-middleware {:middleware [wrap-restful-format
                                             [wrap-defaults api-defaults]]})
   :handler (component/using
             (new-handler)
             [:routes :middleware])
   :http (component/using
          (new-web-server (Integer. (env :http-port)))
          [:handler])))
