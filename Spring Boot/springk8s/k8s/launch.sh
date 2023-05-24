# start cluster
kind create cluster

#####################################################
# deploy dashboard
# Github: https://github.com/kubernetes/dashboard
kubectl create -f ./dashboard/recommended.yml
kubectl apply -f ./dashboard/service-account.yml
kubectl apply -f ./dashboard/role-binding.yml

# start proxy
kubectl proxy -p 3000 > $HOME/.kube/proxy.log &
## maybe could use port-forwarding, but still not working with following command
# kubectl port-forward pods/$(kubectl get pods -n kubernetes-dashboard -o custom-columns=NAME:.metadata.name | grep kubernetes-dashboard) 8443:443 -n springk8s &

# waiting for ready
echo 'waiting for dashboard ready ...'
sleep 60

#####################################################
# deploy springk8s

# create namespace
kubectl apply -f ./springk8s/namespace.yml

#****************************************************
# deploy database - postgresql
export DB_STORAGE_FOLDER="$(pwd)/postgresql/data"
envsubst < ./springk8s/postgresql/pv.yml | kubectl apply -f -

kubectl apply -f ./springk8s/postgresql/pvc.yml
kubectl apply -f ./springk8s/postgresql/configmap.yml
kubectl apply -f ./springk8s/postgresql/deploy.yml
kubectl apply -f ./springk8s/postgresql/service.yml
kubectl apply -f ./springk8s/postgresql/service-account.yml

# waiting for ready
echo 'waiting for database ready ...'
sleep 60

#****************************************************
# deploy application

# configmap
kubectl create configmap springk8s-settings -n springk8s --from-file springk8s/app/application.yaml

# deployment
kind load docker-image springk8s:latest
kubectl apply -f ./springk8s/app/deploy.yml

# service
kubectl apply -f ./springk8s/app/service.yml
kubectl apply -f ./springk8s/app/service-account.yml

# waiting for ready
echo 'waiting for application ready ...'
sleep 120

# port-forward
kubectl port-forward pods/$(kubectl get pods -n springk8s -o custom-columns=NAME:.metadata.name | grep springk8s-backend) 8080:8080 -n springk8s &