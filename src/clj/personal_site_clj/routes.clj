(ns personal-site-clj.routes
  (:require [clojure.java.io :as io]
            [compojure.core :refer [ANY GET PUT POST DELETE routes]]
            [compojure.route :refer [resources]]
						[ring.util.response :refer [response redirect]]
						[ring.middleware.multipart-params :refer [multipart-params-request]]
						[environ.core :refer [env]]
						[personal-site-clj.mail :as mail]))

(defn home-routes [endpoint]
  (routes
		(GET "/" req
			(-> "public/index.html"
					io/resource
					io/input-stream
					response
					(assoc :headers { "Content-Type" "text/html; charset=utf-8" })))
		(POST "/contact-form" req
			(try
				(let [params ((multipart-params-request req) :params)]
					(mail/contact-submission
						(params "name") (params "email") (params "message"))
					(str "Message sent successfully!"))
				(catch Exception e
					(println e)
					(str "An error occurred while trying to send your message. Feel free to retry later, or email me at nathanclonts@gmail.com."))))
		(resources "/")
		; @TODO: add 404 page.
		(ANY "/*" [path]
			(redirect "/"))))
