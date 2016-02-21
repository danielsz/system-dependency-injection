(ns example.handler
  (:refer-clojure :exclude [list])
  (:require
   [compojure.route :as route]
   [compojure.core :refer [routes GET POST ANY]]
   [ring.util.response :refer [response content-type charset]]
   [example.db :refer [save delete list]]
   [example.api :refer [find-director]]))

(defn app-routes [{db :db}]
  (routes
   (GET "/" [] "Welcome. Feed a movie title, and get the director back. Info https://github.com/danielsz/system-advanced-example")
   (POST "/movie" req (fn [{params :params :as req}]
                        (-> (pr-str {:director (find-director (:movie params))})
                            response
                            (content-type "application/edn")
                            (charset "UTF-8"))))
    (GET "/directors" req (-> (pr-str (map :name (list db)))
                             response
                             (content-type "application/edn")
                             (charset "UTF-8")))
   (ANY "/director" req (fn [{params :params :as req}]
                          (->
                           (case (:request-method req)
                             :put (save db (:director params))
                             :delete (delete db (:director params)))
                           response)))
   (route/not-found "404")))
