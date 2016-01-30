(ns example.handler
  (:require
   [compojure.route :as route]
   [compojure.core :refer [routes GET POST ANY]]
   [ring.util.response :refer [response content-type charset]]
   [example.db :refer [save-director<! delete-director! directors]]
   [example.api :refer [get-director-of-movie]]))

(defn app-routes [{{db-spec :db-spec} :db}]
  (routes
   (GET "/" [] "Welcome. Feed a movie title, and get the director back. Info https://github.com/danielsz/system-advanced-example")
   (POST "/movie" req (fn [{params :params :as req}]
                        (-> (pr-str {:director (get-director-of-movie (:movie params))})
                            response
                            (content-type "application/edn")
                            (charset "UTF-8"))))
    (GET "/directors" req (-> (pr-str (map :name (directors {} {:connection db-spec})))
                             response
                             (content-type "application/edn")
                             (charset "UTF-8")))
   (ANY "/director" req (fn [{params :params :as req}]
                          (->
                           (case (:request-method req)
                             :put (save-director<! {:name (:director params)} {:connection db-spec})
                             :delete (delete-director! {:name (:director params)} {:connection db-spec}))
                           response)))
   (route/not-found "404")))
