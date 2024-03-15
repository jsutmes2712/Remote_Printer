Rails.application.routes.draw do
  # Define your application routes per the DSL in https://guides.rubyonrails.org/routing.html

  # Reveal health status on /up that returns 200 if the app boots with no exceptions, otherwise 500.
  # Can be used by load balancers and uptime monitors to verify that the app is live.
  get "up" => "rails/health#show", as: :rails_health_check

  # Defines the root path route ("/")
  # root "posts#index"
  root to: "main#home"

  # Defines the path of the home page (root)
  get "home", to: "main#home"

  # Defines the path of the sendPdf page
  get "uploadPdf", to: "main#uploadPdf", as: :uploadPdf_page

  # Page that makes the post
  post "postPdf", to: "main#postPdf", as: :postPdf_page

end
