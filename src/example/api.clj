(ns example.api
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [environ.core :refer [env]]))

(defn search-movie [title]
  (-> 
   (client/get "http://api.themoviedb.org/3/search/movie" {:query-params {"api_key" (:imdb-key env)  "query" title}})
   :body
   json/read-str
   clojure.walk/keywordize-keys))

(defn get-movie-id [title]
  (let [hits (search-movie title)]
    (when (= 1 (:total_results hits))
      (:id (first (:results hits))))))

(defn get-movie-director [id]
  (->> (client/get (str "http://api.themoviedb.org/3/movie/" id "/casts") {:query-params {"api_key" (:imdb-key env)}})
      :body
      json/read-str
      clojure.walk/keywordize-keys
      :crew
      (filter #(= (:job %) "Director"))
      (map :name)))

(defn get-director-of-movie [title]
  (if-let [movie-id (get-movie-id title)]
    (get-movie-director movie-id)
    "Please try again."))
