(ns example.systems
  (:require 
   [system.core :refer [defsystem]]
   [com.stuartsierra.component :as component]
   (system.components 
    [h2 :refer [new-h2-database DEFAULT-MEM-SPEC DEFAULT-DB-SPEC]]
    [http-kit :refer [new-web-server]]
    [endpoint :refer [new-endpoint]]
    [handler :refer [new-handler]])
   [example.handler :refer [app-routes]]
   [example.db :refer [create-table!]]
   [environ.core :refer [env]]))

(defn dev-system []
  (component/system-map
   :db (new-h2-database DEFAULT-MEM-SPEC create-table!)
   :routes (component/using
              (new-endpoint app-routes)
              [:db])
   :handler (component/using
             (new-handler)
             [:routes])
   :http (component/using
          (new-web-server (Integer. (env :http-port)))
          [:handler])))

(defn prod-system []
  (component/system-map
   :db (new-h2-database DEFAULT-DB-SPEC create-table!)
   :routes (component/using
              (new-endpoint app-routes)
              [:db])
   :handler (component/using
             (new-handler)
             [:routes])
   :http (component/using
         (new-web-server (Integer. (env :http-port)))
         [:handler])))
