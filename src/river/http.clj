(ns river.http
  ^{
    :author "Roman Gonzalez"
    :doc "
  river.http offers a set of producers for accessing the web.

  The producers are divided in 2 categories, the ones that stream bytes
  which will always end in `-bytes` and the one that stream chars."}
  (:require [clj-http.client :as client])
  (:use [river.io
        :only (produce-input-stream-bytes produce-reader-chars)]))


(defn- gen-produce-http-request [producer-fn]
  "Generates a producer that performs an HTTP request and streams it's
  response body to it's consumer."
  (fn produce-http-request
    ([method url consumer]
      (produce-http-request method url {} consumer))
    ([method url req consumer]
      (let [response (client/request (merge req
                                            {:method method
                                             :url url
                                             :as :stream}))]
        (with-open [input-stream (:body response)]
          (producer-fn input-stream consumer))))))

(defn- gen-http-producer 
  "Generates a http-producer given a producer-fn and an HTTP method."
  [producer-fn method]
  (let [produce-http-request (gen-produce-http-request producer-fn)]
    (fn produce-http-method
      ([url consumer]
        (produce-http-request method url {} consumer))
      ([url req-params consumer]
        (produce-http-request method url req-params consumer)))))


(def ^{:arglists '([url consumer] [url req-params consumer])}
  produce-http-get
  "Executes a GET request on the given url and streams characters
  from the response body.

  In case the response is not success or redirect it throws
  an exception (see: http-clj.client/request)"
  (gen-http-producer produce-reader-chars :get))
             

(def ^{:arglists '([url consumer] [url req-params consumer])}
  produce-http-post
  "Executes a POST request on the given url and streams characters
  from the response body.

  In case the response is not success or redirect it throws
  an exception (see: http-clj.client/request)"
  (gen-http-producer produce-reader-chars :post))

(def ^{:arglists '([url consumer] [url req-params consumer])}
  produce-http-get-bytes
  "Executes a GET request on the given url and streams bytes
  from the response body.

  In case the response is not success or redirect it throws
  an exception (see: http-clj.client/request)"
  (gen-http-producer produce-input-stream-bytes :get))

(def ^{:arglists '([url consumer] [url req-params consumer])}
  produce-http-post-bytes
  "Executes a POST request on the given url and streams bytes
  from the response body.

  In case the response is not success or redirect it throws
  an exception (see: http-clj.client/request)"
  (gen-http-producer produce-input-stream-bytes :post))


